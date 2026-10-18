package com.samuelypsv.biblioteca.web.dto;

import com.samuelypsv.biblioteca.model.Prestamo;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public final class PrestamoDto {

    private PrestamoDto() {
    }

    public record Request(@NotNull Long libroId, @NotNull Long socioId) {
    }

    public record Response(Long id, Long libroId, String tituloLibro, Long socioId, String nombreSocio,
                           LocalDate fechaInicio, LocalDate fechaLimite, LocalDate fechaDevolucion,
                           boolean activo, boolean retrasado) {
        public static Response of(Prestamo p, LocalDate hoy) {
            return new Response(p.getId(), p.getLibro().getId(), p.getLibro().getTitulo(),
                    p.getSocio().getId(), p.getSocio().getNombre(),
                    p.getFechaInicio(), p.getFechaLimite(), p.getFechaDevolucion(),
                    p.isActivo(), p.isRetrasado(hoy));
        }
    }
}
