package com.ITAcademyT22.backend.domain.service;

import com.ITAcademyT22.backend.domain.dto.TorneioStatusDTO;
import com.ITAcademyT22.backend.infrastructure.entity.Batalha;
import com.ITAcademyT22.backend.infrastructure.entity.Startup;
import com.ITAcademyT22.backend.infrastructure.repository.BatalhaRepository;
import com.ITAcademyT22.backend.infrastructure.repository.StartupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TorneioService {

    private final StartupRepository startupRepository;
    private final BatalhaRepository batalhaRepository;

    public void sortearBatalhas() {
        List<Startup> startups = startupRepository.findAll();

        if (startups.size() != 4 && startups.size() != 8) {
            throw new IllegalStateException("É necessário ter exatamente 4 ou 8 startups para iniciar o torneio.");
        }

        if (batalhaRepository.existsByFinalizadaFalse()) {
            throw new IllegalStateException("Ainda existem batalhas pendentes da rodada anterior.");
        }

        int proximaRodada = descobrirRodadaAtual() + 1;

        Collections.shuffle(startups);
        criarBatalhas(startups, proximaRodada);
    }

    public List<Batalha> listarBatalhasDaRodadaAtual() {
        int rodadaAtual = descobrirRodadaAtual();
        return batalhaRepository.findByRodada(rodadaAtual);
    }

    public void avancarRodada() {
        int rodadaAtual = descobrirRodadaAtual();

        List<Batalha> batalhasRodada = batalhaRepository.findByRodada(rodadaAtual);

        if (!batalhasRodada.stream().allMatch(Batalha::isFinalizada)) {
            throw new IllegalStateException("Ainda existem batalhas não finalizadas nesta rodada.");
        }

        List<Startup> vencedoras = new ArrayList<>(
                batalhasRodada.stream()
                        .map(this::extrairVencedora)
                        .toList()
        );

        batalhasRodada.forEach(batalha -> {
            Startup perdedora = extrairPerdedora(batalha);
            perdedora.setEliminada(true);
            startupRepository.save(perdedora);
        });

        if (vencedoras.size() == 1) {
            Startup campea = vencedoras.getFirst();
            System.out.println("🏆 Torneio encerrado! Campeã: " + campea.getNome());
            return;
        }

        Collections.shuffle(vencedoras);
        criarBatalhas(vencedoras, rodadaAtual + 1);
    }

    private int descobrirRodadaAtual() {
        return batalhaRepository.findTopByOrderByRodadaDesc()
                .map(Batalha::getRodada)
                .orElse(0);
    }

    private void criarBatalhas(List<Startup> startups, int rodada) {
        for (int i = 0; i < startups.size(); i += 2) {
            Startup a = startups.get(i);
            Startup b = startups.get(i + 1);

            Batalha batalha = new Batalha();
            batalha.setStartupA(a);
            batalha.setStartupB(b);
            batalha.setRodada(rodada);
            batalha.setFinalizada(false);
            batalha.setPontuacaoA(0);
            batalha.setPontuacaoB(0);
            batalhaRepository.save(batalha);
        }
    }

    private Startup extrairVencedora(Batalha batalha) {
        int pontosA = batalha.getPontuacaoA();
        int pontosB = batalha.getPontuacaoB();

        return (pontosA >= pontosB) ? batalha.getStartupA() : batalha.getStartupB();
    }

    private Startup extrairPerdedora(Batalha batalha) {
        return (batalha.getPontuacaoA() >= batalha.getPontuacaoB())
                ? batalha.getStartupB()
                : batalha.getStartupA();
    }

    public TorneioStatusDTO obterStatus() {
        int rodadaAtual = descobrirRodadaAtual();

        List<Batalha> todasDaRodada = batalhaRepository.findByRodada(rodadaAtual);

        List<Batalha> finalizadas = todasDaRodada.stream()
                .filter(Batalha::isFinalizada)
                .toList();

        List<Batalha> pendentes = todasDaRodada.stream()
                .filter(b -> !b.isFinalizada())
                .toList();

        List<Startup> ativas = startupRepository.findByEliminadaFalse();

        Startup campea = (ativas.size() == 1) ? ativas.getFirst() : null;

        return new TorneioStatusDTO(
                rodadaAtual,
                finalizadas,
                pendentes,
                ativas,
                campea
        );
    }

    public List<Startup> listarTodasStartups() {
        return startupRepository.findAll();
    }

    public void resetarTorneio() {
        batalhaRepository.deleteAll();
        startupRepository.deleteAll();
    }

}
