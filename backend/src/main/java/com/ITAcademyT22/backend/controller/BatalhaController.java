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

    // GET /batalhas → lista batalhas da rodada atual
    @GetMapping
    public ResponseEntity<List<Batalha>> listarTodas() {
        List<Batalha> batalhas = batalhaRepository.findAll(); // opcional: filtrar por rodada atual
        return ResponseEntity.ok(batalhas);
    }

    // GET /batalhas/{id} → detalhes da batalha
    @GetMapping("/{id}")
    public ResponseEntity<Batalha> buscarPorId(@PathVariable Long id) {
        Batalha batalha = batalhaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Batalha não encontrada"));
        return ResponseEntity.ok(batalha);
    }

    // POST /batalhas/{id}/eventos → aplicar evento
    @PostMapping("/{id}/eventos")
    public ResponseEntity<Void> aplicarEvento(
            @PathVariable Long id,
            @RequestBody EventoRequestDTO evento
    ) {
        batalhaService.aplicarEvento(id, evento);
        return ResponseEntity.ok().build();
    }

    // POST /batalhas/{id}/finalizar → finaliza a batalha e calcula vencedor
    @PostMapping("/{id}/finalizar")
    public ResponseEntity<Void> finalizarBatalha(@PathVariable Long id) {
        batalhaService.finalizarBatalha(id);
        return ResponseEntity.ok().build();
    }
}
