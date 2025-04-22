package com.ITAcademyT22.backend.domain.dto;

import com.ITAcademyT22.backend.infrastructure.entity.EventoTipo;

public record EventoRequestDTO(
        Long idStartup,
        EventoTipo tipoEvento
) {}
