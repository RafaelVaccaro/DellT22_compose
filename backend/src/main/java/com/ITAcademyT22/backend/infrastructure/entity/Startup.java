package com.ITAcademyT22.backend.infrastructure.entity;

import com.ITAcademyT22.backend.domain.dto.StartupRequestDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Startup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nome;

    @NotBlank
    private String slogan;

    @NotBlank
    private String anoFundacao;

    private int pontuacao = 70;

    private int pitchCount = 0;
    private int bugsCount = 0;
    private int tracaoCount = 0;
    private int investidorIrritadoCount = 0;
    private int fakeNewsCount = 0;

    private boolean eliminada = false;

    private boolean investidorSecretoUsado = false;

    public Startup(StartupRequestDTO startupDTO) {
        this.nome = startupDTO.nome();
        this.slogan = startupDTO.slogan();
        this.anoFundacao = startupDTO.anoFundacao();
    }
}


