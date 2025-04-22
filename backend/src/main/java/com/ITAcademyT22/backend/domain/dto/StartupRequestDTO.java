package com.ITAcademyT22.backend.domain.dto;

import com.ITAcademyT22.backend.infrastructure.entity.Startup;

public record StartupRequestDTO(String nome, String slogan, String anoFundacao) {
}

