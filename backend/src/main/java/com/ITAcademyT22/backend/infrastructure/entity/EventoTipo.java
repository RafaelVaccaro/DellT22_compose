package com.ITAcademyT22.backend.infrastructure.entity;

import lombok.Getter;

@Getter
public enum EventoTipo {
    PITCH_CONVINCENTE(6),
    PRODUTO_COM_BUGS(-4),
    BOA_TRACAO_USUARIOS(3),
    INVESTIDOR_IRRITADO(-6),
    FAKE_NEWS(-8);

    private final int impacto;

    EventoTipo(int impacto) {
        this.impacto = impacto;
    }

}

