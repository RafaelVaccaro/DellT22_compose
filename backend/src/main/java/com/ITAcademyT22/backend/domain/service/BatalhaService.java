package com.ITAcademyT22.backend.domain.service;

import com.ITAcademyT22.backend.domain.dto.EventoRequestDTO;
import com.ITAcademyT22.backend.domain.dto.StartupRequestDTO;
import com.ITAcademyT22.backend.domain.dto.StartupResponseDTO;
import com.ITAcademyT22.backend.infrastructure.entity.Batalha;
import com.ITAcademyT22.backend.infrastructure.entity.EventoTipo;
import com.ITAcademyT22.backend.infrastructure.entity.Startup;
import com.ITAcademyT22.backend.infrastructure.repository.BatalhaRepository;
import com.ITAcademyT22.backend.infrastructure.repository.StartupRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BatalhaService {

    private final BatalhaRepository batalhaRepository;
    private final StartupRepository startupRepository;

    private final Map<Long, Set<EventoTipo>> eventosA = new HashMap<>();
    private final Map<Long, Set<EventoTipo>> eventosB = new HashMap<>();

    public void aplicarEvento(Long idBatalha, EventoRequestDTO eventoDTO) {
        Batalha batalha = batalhaRepository.findById(idBatalha)
                .orElseThrow(() -> new RuntimeException("Batalha não encontrada"));

        if (batalha.isFinalizada()) {
            throw new IllegalStateException("Batalha já foi finalizada.");
        }

        Startup startup = getStartupDaBatalha(batalha, eventoDTO.idStartup());
        boolean isA = batalha.getStartupA().getId().equals(startup.getId());

        EventoTipo tipo = eventoDTO.tipoEvento();
        Set<EventoTipo> eventosAplicados = isA ?
                eventosA.computeIfAbsent(batalha.getId(), k -> new HashSet<>()) :
                eventosB.computeIfAbsent(batalha.getId(), k -> new HashSet<>());

        if (eventosAplicados.contains(tipo)) {
            throw new IllegalArgumentException("Este evento já foi aplicado para essa startup nesta batalha.");
        }

        eventosAplicados.add(tipo);

        int impacto = tipo.getImpacto();
        startup.setPontuacao(startup.getPontuacao() + impacto);
        atualizarEstatistica(startup, tipo);

        if (isA) {
            batalha.setPontuacaoA(batalha.getPontuacaoA() + impacto);
        } else {
            batalha.setPontuacaoB(batalha.getPontuacaoB() + impacto);
        }

        startupRepository.save(startup);
        batalhaRepository.save(batalha);
    }

    public void finalizarBatalha(Long idBatalha) {
        Batalha batalha = batalhaRepository.findById(idBatalha)
                .orElseThrow(() -> new RuntimeException("Batalha não encontrada"));

        if (batalha.isFinalizada()) return;

        Startup a = batalha.getStartupA();
        Startup b = batalha.getStartupB();

        int pontosA = batalha.getPontuacaoA();
        int pontosB = batalha.getPontuacaoB();

        if (pontosA == pontosB) {
            if (Math.random() < 0.5) {
                pontosA += 2;
                batalha.setPontuacaoA(pontosA);
                System.out.println("A equipe " + batalha.getStartupA().getNome() + " ganhou o Shark Fight");
            } else {
                pontosB += 2;
                batalha.setPontuacaoB(pontosB);
                System.out.println("A equipe " + batalha.getStartupB().getNome() + " ganhou o Shark Fight");
            }
        }

        Startup vencedora;
        String resultado;
        if (pontosA > pontosB) {
            vencedora = a;
            a.setPontuacao(a.getPontuacao() + 30);
            resultado = a.getNome() + " venceu";
        } else {
            vencedora = b;
            b.setPontuacao(b.getPontuacao() + 30);
            resultado = b.getNome() + " venceu";
        }

        batalha.setFinalizada(true);
        batalha.setResultado(resultado);

        startupRepository.save(a);
        startupRepository.save(b);
        batalhaRepository.save(batalha);
    }

    private Startup getStartupDaBatalha(Batalha batalha, Long idStartup) {
        if (batalha.getStartupA().getId().equals(idStartup)) return batalha.getStartupA();
        if (batalha.getStartupB().getId().equals(idStartup)) return batalha.getStartupB();
        throw new IllegalArgumentException("Startup não pertence a essa batalha.");
    }

    private void atualizarEstatistica(Startup startup, EventoTipo tipo) {
        switch (tipo) {
            case PITCH_CONVINCENTE -> startup.setPitchCount(startup.getPitchCount() + 1);
            case PRODUTO_COM_BUGS -> startup.setBugsCount(startup.getBugsCount() + 1);
            case BOA_TRACAO_USUARIOS -> startup.setTracaoCount(startup.getTracaoCount() + 1);
            case INVESTIDOR_IRRITADO -> startup.setInvestidorIrritadoCount(startup.getInvestidorIrritadoCount() + 1);
            case FAKE_NEWS -> startup.setFakeNewsCount(startup.getFakeNewsCount() + 1);
        }
    }
}

