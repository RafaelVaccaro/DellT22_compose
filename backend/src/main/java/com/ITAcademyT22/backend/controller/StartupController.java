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

    // Registra uma nova startup e retorna os dados da startup registrada
    @PostMapping
    @Transactional
    public ResponseEntity<?> registrarStartup(@RequestBody StartupRequestDTO dto) {
        try {
            var startup = startupService.registrarStartup(dto);
            return ResponseEntity.ok(new StartupResponseDTO(startup));
        } catch (RuntimeException e) {
            // Retorna uma mensagem de erro se ocorrer uma exceção
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new MensagemResponseDTO(e.getMessage()));
        }
    }

    // Lista todas as startups registradas
    @GetMapping
    public ResponseEntity<List<StartupResponseDTO>> listarStartups() {
        var startups = startupService.listarStartups();
        return ResponseEntity.ok(startups);
    }

    // Elimina uma startup pelo ID
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<StartupResponseDTO> eliminarStartup(@PathVariable Long id) {
        var startup = startupService.deleteStartup(id);
        return ResponseEntity.ok(new StartupResponseDTO(startup));
    }

    // Ativa o investidor secreto para a startup especificada
    @PatchMapping("/{id}/investidor-secreto")
    public ResponseEntity<?> ativarInvestidorSecreto(@PathVariable Long id) {
        try {
            startupService.ativarInvestidorSecreto(id);
            return ResponseEntity.ok().build();
        } catch (IllegalStateException e) {
            // Retorna erro se houver problema ao ativar o investidor secreto
            return ResponseEntity.badRequest().body(new MensagemResponseDTO(e.getMessage()));
        }
    }

}
