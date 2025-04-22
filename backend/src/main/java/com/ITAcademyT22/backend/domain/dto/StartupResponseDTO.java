package com.ITAcademyT22.backend.domain.dto;

import com.ITAcademyT22.backend.infrastructure.entity.Startup;

public record StartupResponseDTO(Long id, String nome, String slogan, String anoFundacao, int pontuacao, boolean eliminada) {
    public StartupResponseDTO(Startup startup) {
        this(
                startup.getId(),
                startup.getNome(),
                startup.getSlogan(),
                startup.getAnoFundacao(),
                startup.getPontuacao(),
                startup.isEliminada()
        );
    }
}


