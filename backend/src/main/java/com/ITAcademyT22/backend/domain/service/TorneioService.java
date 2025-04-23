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

    // Inicia uma nova rodada embaralhando as startups e criando as batalhas
    public void sortearBatalhas() {
        List<Startup> startups = startupRepository.findAll();

        int proximaRodada = descobrirRodadaAtual() + 1;

        Collections.shuffle(startups); // Mistura a ordem pra não ficar previsível
        criarBatalhas(startups, proximaRodada); // Monta os confrontos da rodada
    }

    // Retorna todas as batalhas da rodada mais recente
    public List<Batalha> listarBatalhasDaRodadaAtual() {
        int rodadaAtual = descobrirRodadaAtual();
        return batalhaRepository.findByRodada(rodadaAtual);
    }

    // Fecha a rodada atual e monta a próxima com os vencedores
    public void avancarRodada() {
        int rodadaAtual = descobrirRodadaAtual();

        List<Batalha> batalhasRodada = batalhaRepository.findByRodada(rodadaAtual);

        // Pega só as startups vencedoras de cada confronto
        List<Startup> vencedoras = new ArrayList<>(
                batalhasRodada.stream()
                        .map(this::extrairVencedora)
                        .toList()
        );

        // Marca as perdedoras como eliminadas
        batalhasRodada.forEach(batalha -> {
            Startup perdedora = extrairPerdedora(batalha);
            perdedora.setEliminada(true);
            startupRepository.save(perdedora);
        });

        // Se sobrou só uma, ela é a campeã! Nada mais a fazer aqui
        if (vencedoras.size() == 1) {
            Startup campea = vencedoras.getFirst();
            return;
        }

        // Se ainda tem confronto, bora embaralhar e montar a próxima rodada
        Collections.shuffle(vencedoras);
        criarBatalhas(vencedoras, rodadaAtual + 1);
    }

    // Retorna o número da rodada mais recente (ou 0 se ainda não começou)
    private int descobrirRodadaAtual() {
        return batalhaRepository.findTopByOrderByRodadaDesc()
                .map(Batalha::getRodada)
                .orElse(0);
    }

    // Cria batalhas a partir da lista de startups e define a rodada atual
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

    // Decide quem ganhou com base na pontuação
    private Startup extrairVencedora(Batalha batalha) {
        int pontosA = batalha.getPontuacaoA();
        int pontosB = batalha.getPontuacaoB();

        return (pontosA >= pontosB) ? batalha.getStartupA() : batalha.getStartupB();
    }

    // E aqui, quem perdeu (o oposto da de cima)
    private Startup extrairPerdedora(Batalha batalha) {
        return (batalha.getPontuacaoA() >= batalha.getPontuacaoB())
                ? batalha.getStartupB()
                : batalha.getStartupA();
    }

    // Monta um "resumo" do estado atual do torneio: rodada, lutas finalizadas, pendentes etc.
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

        // Se só restou uma startup ativa, temos uma campeã
        Startup campea = (ativas.size() == 1) ? ativas.getFirst() : null;

        return new TorneioStatusDTO(
                rodadaAtual,
                finalizadas,
                pendentes,
                ativas,
                campea
        );
    }

    // Retorna todas as startups do sistema (inclusive as eliminadas)
    public List<Startup> listarTodasStartups() {
        return startupRepository.findAll();
    }

    // Limpa tudo e reinicia o torneio do zero
    public void resetarTorneio() {
        batalhaRepository.deleteAll();
        startupRepository.deleteAll();
    }
}
