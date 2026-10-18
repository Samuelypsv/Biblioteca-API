package com.samuelypsv.biblioteca.repository;

import com.samuelypsv.biblioteca.model.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    long countBySocioIdAndFechaDevolucionIsNull(Long socioId);

    List<Prestamo> findBySocioIdOrderByFechaInicioDesc(Long socioId);

    List<Prestamo> findByFechaDevolucionIsNullOrderByFechaLimiteAsc();
}
