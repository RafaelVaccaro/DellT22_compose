package com.ITAcademyT22.backend.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Batalha {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Startup startupA;

    @ManyToOne
    private Startup startupB;

    private int rodada = 0;

    private boolean finalizada = false;

    private int pontuacaoA = 0;
    private int pontuacaoB = 0;

    private String resultado;

}

