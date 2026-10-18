package com.samuelypsv.biblioteca.service;

import com.samuelypsv.biblioteca.model.Autor;
import com.samuelypsv.biblioteca.model.Libro;
import com.samuelypsv.biblioteca.model.Prestamo;
import com.samuelypsv.biblioteca.model.Socio;
import com.samuelypsv.biblioteca.repository.PrestamoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PrestamoServiceTest {

    @Mock PrestamoRepository prestamos;
    @Mock LibroService libros;
    @Mock SocioService socios;

    PrestamoService service;
    Libro libro;
    Socio socio;

    @BeforeEach
    void setUp() {
        Clock fijo = Clock.fixed(Instant.parse("2026-03-01T10:00:00Z"), ZoneOffset.UTC);
        service = new PrestamoService(prestamos, libros, socios, fijo);
        libro = new Libro("9780451524935", "1984", 1949, "Distopía", new Autor("George Orwell", null));
        socio = new Socio("Ana", "ana@example.com");
        lenient().when(libros.obtener(1L)).thenReturn(libro);
        lenient().when(socios.obtener(2L)).thenReturn(socio);
    }

    @Test
    void prestarMarcaElLibroComoNoDisponible() {
        when(prestamos.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Prestamo p = service.prestar(1L, 2L);

        assertThat(libro.isDisponible()).isFalse();
        assertThat(p.getFechaInicio()).isEqualTo(LocalDate.of(2026, 3, 1));
        assertThat(p.getFechaLimite()).isEqualTo(LocalDate.of(2026, 3, 16));
    }

    @Test
    void noSePuedePrestarUnLibroYaPrestado() {
        libro.setDisponible(false);

        assertThatThrownBy(() -> service.prestar(1L, 2L))
                .isInstanceOf(ReglaNegocioException.class)
                .hasMessageContaining("ya está prestado");
        verify(prestamos, never()).save(any());
    }

    @Test
    void unSocioNoPuedeTenerMasDeTresPrestamos() {
        when(prestamos.countBySocioIdAndFechaDevolucionIsNull(2L)).thenReturn(3L);

        assertThatThrownBy(() -> service.prestar(1L, 2L))
                .isInstanceOf(ReglaNegocioException.class)
                .hasMessageContaining("3 préstamos");
        assertThat(libro.isDisponible()).isTrue();
    }
}
