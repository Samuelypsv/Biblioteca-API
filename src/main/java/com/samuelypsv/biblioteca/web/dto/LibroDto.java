package com.samuelypsv.biblioteca.web.dto;

import com.samuelypsv.biblioteca.model.Libro;
import jakarta.validation.constraints.*;

public final class LibroDto {

    private LibroDto() {
    }

    public record Request(
            @NotBlank @Pattern(regexp = "^(97[89])?\\d{9}[\\dX]$", message = "ISBN-10 o ISBN-13 sin guiones")
            String isbn,
            @NotBlank @Size(max = 200) String titulo,
            @Min(1450) @Max(2100) Integer anioPublicacion,
            @Size(max = 60) String genero,
            @NotNull Long autorId) {
    }

    public record Response(Long id, String isbn, String titulo, Integer anioPublicacion,
                           String genero, AutorDto.Response autor, boolean disponible) {
        public static Response of(Libro libro) {
            return new Response(libro.getId(), libro.getIsbn(), libro.getTitulo(),
                    libro.getAnioPublicacion(), libro.getGenero(),
                    AutorDto.Response.of(libro.getAutor()), libro.isDisponible());
        }
    }
}
