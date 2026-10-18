package com.samuelypsv.biblioteca.service;

import com.samuelypsv.biblioteca.model.Libro;
import com.samuelypsv.biblioteca.model.Prestamo;
import com.samuelypsv.biblioteca.model.Socio;
import com.samuelypsv.biblioteca.repository.PrestamoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class PrestamoService {

    public static final int MAX_PRESTAMOS_ACTIVOS = 3;

    private final PrestamoRepository prestamos;
    private final LibroService libros;
    private final SocioService socios;
    private final Clock clock;

    public PrestamoService(PrestamoRepository prestamos, LibroService libros, SocioService socios, Clock clock) {
        this.prestamos = prestamos;
        this.libros = libros;
        this.socios = socios;
        this.clock = clock;
    }

    public LocalDate hoy() {
        return LocalDate.now(clock);
    }

    public Prestamo prestar(Long libroId, Long socioId) {
        Libro libro = libros.obtener(libroId);
        Socio socio = socios.obtener(socioId);

        if (!libro.isDisponible()) {
            throw new ReglaNegocioException("El libro '" + libro.getTitulo() + "' ya está prestado");
        }
        if (prestamos.countBySocioIdAndFechaDevolucionIsNull(socioId) >= MAX_PRESTAMOS_ACTIVOS) {
            throw new ReglaNegocioException("El socio ya tiene " + MAX_PRESTAMOS_ACTIVOS + " préstamos activos");
        }

        libro.setDisponible(false);
        return prestamos.save(new Prestamo(libro, socio, hoy()));
    }

    public Prestamo devolver(Long prestamoId) {
        Prestamo prestamo = prestamos.findById(prestamoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Préstamo", prestamoId));
        if (!prestamo.isActivo()) {
            throw new ReglaNegocioException("El préstamo ya fue devuelto");
        }
        prestamo.devolver(hoy());
        return prestamo;
    }

    @Transactional(readOnly = true)
    public List<Prestamo> activos() {
        return prestamos.findByFechaDevolucionIsNullOrderByFechaLimiteAsc();
    }

    @Transactional(readOnly = true)
    public List<Prestamo> deSocio(Long socioId) {
        socios.obtener(socioId);
        return prestamos.findBySocioIdOrderByFechaInicioDesc(socioId);
    }
}
