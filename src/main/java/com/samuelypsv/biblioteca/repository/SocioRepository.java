package com.samuelypsv.biblioteca.repository;

import com.samuelypsv.biblioteca.model.Socio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SocioRepository extends JpaRepository<Socio, Long> {

    boolean existsByEmail(String email);
}
