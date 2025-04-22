package com.ITAcademyT22.backend.domain.service;

import com.ITAcademyT22.backend.domain.dto.StartupRequestDTO;
import com.ITAcademyT22.backend.domain.dto.StartupResponseDTO;
import com.ITAcademyT22.backend.infrastructure.entity.Startup;
import com.ITAcademyT22.backend.infrastructure.repository.BatalhaRepository;
import com.ITAcademyT22.backend.infrastructure.repository.StartupRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StartupService {

    private final StartupRepository startupRepository;
    private final BatalhaRepository batalhaRepository;

    // Registra uma nova startup, mas só se o número de startups for menor que 8
    public Startup registrarStartup(StartupRequestDTO dto) {
        long totalStartups = startupRepository.count();

        // Limita o numero maximo de startups
        if (totalStartups >= 8) {
            throw new RuntimeException("Número máximo de startups 8 atingido.");
        }

        // Cria e salva a startup com os dados do DTO
        Startup startup = new Startup(dto);
        return startupRepository.save(startup);
    }

    // Lista todas as startups criadas
    public List<StartupResponseDTO> listarStartups() {
        return startupRepository.findAll()
                .stream()
                .map(StartupResponseDTO::new) // Converte cada startup para DTO de resposta
                .toList();
    }

    // Deleta uma startup pelo ID, se existir
    public Startup deleteStartup(Long id) {
        Startup startup = startupRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Startup com ID " + id + " não encontrada"));

        startupRepository.delete(startup);
        return startup;
    }

    // Ativa o "Investidor Secreto" para uma startup, se ainda não foi usado
    public void ativarInvestidorSecreto(Long id) {
        Startup startup = startupRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Startup não encontrada"));

        // Verifica se o "Investidor Secreto" já foi usado
        if (startup.isInvestidorSecretoUsado()) {
            throw new IllegalStateException("Investidor Secreto já foi usado.");
        }

        // Adiciona 5 pontos na pontuação da startup e marca como usado
        startup.setPontuacao(startup.getPontuacao() + 5);
        startup.setInvestidorSecretoUsado(true);

        startupRepository.save(startup);
    }
}
