package com.samuelypsv.biblioteca.web.dto;

import com.samuelypsv.biblioteca.model.Autor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public final class AutorDto {

    private AutorDto() {
    }

    public record Request(
            @NotBlank @Size(max = 120) String nombre,
            @Size(max = 80) String nacionalidad) {
    }

    public record Response(Long id, String nombre, String nacionalidad) {
        public static Response of(Autor autor) {
            return new Response(autor.getId(), autor.getNombre(), autor.getNacionalidad());
        }
    }
}
