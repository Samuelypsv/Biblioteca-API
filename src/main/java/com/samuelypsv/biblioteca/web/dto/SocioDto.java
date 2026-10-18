package com.samuelypsv.biblioteca.web.dto;

import com.samuelypsv.biblioteca.model.Socio;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public final class SocioDto {

    private SocioDto() {
    }

    public record Request(
            @NotBlank @Size(max = 120) String nombre,
            @NotBlank @Email @Size(max = 150) String email) {
    }

    public record Response(Long id, String nombre, String email) {
        public static Response of(Socio socio) {
            return new Response(socio.getId(), socio.getNombre(), socio.getEmail());
        }
    }
}
