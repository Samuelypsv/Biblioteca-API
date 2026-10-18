package com.samuelypsv.biblioteca.web;

import org.springframework.data.domain.Page;

import java.util.List;

/** Respuesta paginada estable (Spring desaconseja serializar Page directamente). */
public record PaginaDto<T>(List<T> contenido, int pagina, int tamanio, long totalElementos, int totalPaginas) {

    public static <T> PaginaDto<T> of(Page<T> page) {
        return new PaginaDto<>(page.getContent(), page.getNumber(), page.getSize(),
                page.getTotalElements(), page.getTotalPages());
    }
}
