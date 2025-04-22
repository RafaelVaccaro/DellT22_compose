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

    public Startup registrarStartup(StartupRequestDTO dto) {
        long totalStartups = startupRepository.count();

        if (totalStartups >= 8) {
            throw new RuntimeException("Número máximo de startups 8 atingido.");
        }

        Startup startup = new Startup(dto);
        return startupRepository.save(startup);
    }

    public List<StartupResponseDTO> listarStartups() {
        return startupRepository.findAll()
                .stream()
                .map(StartupResponseDTO::new)
                .toList();
    }

    public Startup deleteStartup(Long id) {
        Startup startup = startupRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Startup com ID " + id + " não encontrada"));

        startupRepository.delete(startup);
        return startup;
    }

    public void ativarInvestidorSecreto(Long id) {
        Startup startup = startupRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Startup não encontrada"));

        if (startup.isInvestidorSecretoUsado()) {
            throw new IllegalStateException("Investidor Secreto já foi usado.");
        }

        startup.setPontuacao(startup.getPontuacao() + 5);
        startup.setInvestidorSecretoUsado(true);

        startupRepository.save(startup);
    }


}
