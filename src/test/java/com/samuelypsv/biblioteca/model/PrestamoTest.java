package com.samuelypsv.biblioteca.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class PrestamoTest {

    private final Autor autor = new Autor("Autor", null);
    private final LocalDate inicio = LocalDate.of(2026, 1, 1);

    @Test
    void laFechaLimiteEsQuinceDiasDespues() {
        Prestamo p = new Prestamo(new Libro("1234567890", "T", null, null, autor), new Socio("S", "s@x.com"), inicio);
        assertThat(p.getFechaLimite()).isEqualTo(LocalDate.of(2026, 1, 16));
    }

    @Test
    void estaRetrasadoSoloTrasLaFechaLimite() {
        Prestamo p = new Prestamo(new Libro("1234567890", "T", null, null, autor), new Socio("S", "s@x.com"), inicio);
        assertThat(p.isRetrasado(LocalDate.of(2026, 1, 16))).isFalse();
        assertThat(p.isRetrasado(LocalDate.of(2026, 1, 17))).isTrue();
    }

    @Test
    void devolverLiberaElLibro() {
        Libro libro = new Libro("1234567890", "T", null, null, autor);
        libro.setDisponible(false);
        Prestamo p = new Prestamo(libro, new Socio("S", "s@x.com"), inicio);

        p.devolver(LocalDate.of(2026, 1, 10));

        assertThat(p.isActivo()).isFalse();
        assertThat(libro.isDisponible()).isTrue();
        assertThat(p.isRetrasado(LocalDate.of(2026, 3, 1))).isFalse();
    }
}
