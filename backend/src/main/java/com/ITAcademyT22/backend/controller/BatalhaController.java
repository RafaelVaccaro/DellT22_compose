package com.ITAcademyT22.backend.controller;

import com.ITAcademyT22.backend.domain.dto.EventoRequestDTO;
import com.ITAcademyT22.backend.infrastructure.entity.Batalha;
import com.ITAcademyT22.backend.domain.service.BatalhaService;
import com.ITAcademyT22.backend.infrastructure.repository.BatalhaRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/batalha")
@RequiredArgsConstructor
public class BatalhaController {

    private final BatalhaService batalhaService;
    private final BatalhaRepository batalhaRepository;

    // Lista todas as batalhas
    @GetMapping
    public ResponseEntity<List<Batalha>> listarTodas() {
        List<Batalha> batalhas = batalhaRepository.findAll();
        return ResponseEntity.ok(batalhas);
    }

    // Retorna os detalhes de uma batalha específica pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<Batalha> buscarPorId(@PathVariable Long id) {
        Batalha batalha = batalhaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Batalha não encontrada"));
        return ResponseEntity.ok(batalha);
    }

    // Aplica um evento à batalha com o ID fornecido
    @PostMapping("/{id}/eventos")
    public ResponseEntity<Void> aplicarEvento(
            @PathVariable Long id,
            @RequestBody EventoRequestDTO evento
    ) {
        batalhaService.aplicarEvento(id, evento);
        return ResponseEntity.ok().build();
    }

    // Finaliza a batalha e calcula o vencedor
    @PostMapping("/{id}/finalizar")
    public ResponseEntity<Void> finalizarBatalha(@PathVariable Long id) {
        batalhaService.finalizarBatalha(id);
        return ResponseEntity.ok().build();
    }
}
