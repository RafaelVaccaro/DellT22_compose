package com.ITAcademyT22.backend.domain.dto;

import com.ITAcademyT22.backend.infrastructure.entity.Batalha;
import com.ITAcademyT22.backend.infrastructure.entity.Startup;

import java.util.List;

public record TorneioStatusDTO(
        int rodadaAtual,
        List<Batalha> batalhasFinalizadas,
        List<Batalha> batalhasPendentes,
        List<Startup> startupsAtivas,
        Startup campea
) {}
