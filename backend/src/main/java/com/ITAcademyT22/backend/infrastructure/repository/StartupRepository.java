package com.ITAcademyT22.backend.infrastructure.repository;

import com.ITAcademyT22.backend.infrastructure.entity.Startup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StartupRepository extends JpaRepository<Startup, Long> {
    List<Startup> findByEliminadaFalse();
}

