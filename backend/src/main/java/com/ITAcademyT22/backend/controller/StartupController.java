package com.ITAcademyT22.backend.controller;

import com.ITAcademyT22.backend.domain.dto.MensagemResponseDTO;
import com.ITAcademyT22.backend.domain.dto.StartupRequestDTO;
import com.ITAcademyT22.backend.domain.dto.StartupResponseDTO;
import com.ITAcademyT22.backend.domain.service.StartupService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/startup")
@AllArgsConstructor
public class StartupController {

    private final StartupService startupService;

    @PostMapping
    @Transactional
    public ResponseEntity<?> registrarStartup(@RequestBody StartupRequestDTO dto) {
        try {
            var startup = startupService.registrarStartup(dto);
            return ResponseEntity.ok(new StartupResponseDTO(startup));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new MensagemResponseDTO(e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<StartupResponseDTO>> listarStartups() {
        var startups = startupService.listarStartups();
        return ResponseEntity.ok(startups);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<StartupResponseDTO> eliminarStartup(@PathVariable Long id) {
        var startup = startupService.deleteStartup(id);
        return ResponseEntity.ok(new StartupResponseDTO(startup));
    }

    @PatchMapping("/{id}/investidor-secreto")
    public ResponseEntity<?> ativarInvestidorSecreto(@PathVariable Long id) {
        try {
            startupService.ativarInvestidorSecreto(id);
            return ResponseEntity.ok().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(new MensagemResponseDTO(e.getMessage()));
        }
    }

}
