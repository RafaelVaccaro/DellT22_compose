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

    // Quando o torneio começa, as batalhas são sorteadas
    @PostMapping("/iniciar")
    @ResponseStatus(HttpStatus.CREATED)
    public void iniciarTorneio() {
        torneioService.sortearBatalhas();
    }

    // Esse método mostra como está o andamento das batalhas, se estão pendentes ou já finalizadas
    @GetMapping("/status")
    public ResponseEntity<TorneioStatusDTO> status() {
        return ResponseEntity.ok(torneioService.obterStatus());
    }

    // Torneio avança para a próxima rodada
    @PostMapping("/avancar")
    public ResponseEntity<Void> avancarRodada() {
        torneioService.avancarRodada();
        return ResponseEntity.ok().build();
    }

    // Exibe o ranking das startups, ordenando pela pontuação
    @GetMapping("/ranking")
    public ResponseEntity<List<Startup>> ranking() {
        List<Startup> startups = torneioService.listarTodasStartups();
        startups.sort(Comparator.comparingInt(Startup::getPontuacao).reversed()); // As startups são ordenadas pela pontuação, da maior para a menor
        return ResponseEntity.ok(startups);
    }

    // Reseta o torneio, apagando todos os dados e começando do zero
    @DeleteMapping("/resetar")
    public ResponseEntity<Void> resetarTorneio() {
        torneioService.resetarTorneio();
        return ResponseEntity.noContent().build();
    }
}
