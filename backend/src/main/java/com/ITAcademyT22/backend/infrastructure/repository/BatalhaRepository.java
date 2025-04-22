package com.ITAcademyT22.backend.infrastructure.repository;

import com.ITAcademyT22.backend.infrastructure.entity.Batalha;
import com.ITAcademyT22.backend.infrastructure.entity.Startup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BatalhaRepository extends JpaRepository<Batalha, Long> {

    boolean existsByFinalizadaFalse();

    Optional<Batalha> findTopByOrderByRodadaDesc();

    List<Batalha> findByRodada(int rodada);
}

