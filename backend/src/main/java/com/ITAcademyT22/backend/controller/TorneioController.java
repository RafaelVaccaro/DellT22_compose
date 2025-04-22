package com.ITAcademyT22.backend.controller;

import com.ITAcademyT22.backend.domain.dto.TorneioStatusDTO;
import com.ITAcademyT22.backend.infrastructure.entity.Batalha;
import com.ITAcademyT22.backend.domain.service.TorneioService;
import com.ITAcademyT22.backend.infrastructure.entity.Startup;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/torneio")
@AllArgsConstructor
public class TorneioController {

    private final TorneioService torneioService;

    // Endpoint para iniciar o torneio
    @PostMapping("/iniciar")
    @ResponseStatus(HttpStatus.CREATED)
    public void iniciarTorneio() {
        torneioService.sortearBatalhas();
    }

    // Endpoint para verificar o status das batalhas (pendentes ou finalizadas)
    @GetMapping("/status")
    public ResponseEntity<TorneioStatusDTO> status() {
        return ResponseEntity.ok(torneioService.obterStatus());
    }

    // Endpoint para avançar a rodada
    @PostMapping("/avancar")
    public ResponseEntity<Void> avancarRodada() {
        torneioService.avancarRodada();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/ranking")
    public ResponseEntity<List<Startup>> ranking() {
        List<Startup> startups = torneioService.listarTodasStartups();
        startups.sort(Comparator.comparingInt(Startup::getPontuacao).reversed());
        return ResponseEntity.ok(startups);
    }

    @DeleteMapping("/resetar")
    public ResponseEntity<Void> resetarTorneio() {
        torneioService.resetarTorneio();
        return ResponseEntity.noContent().build();
    }





}
