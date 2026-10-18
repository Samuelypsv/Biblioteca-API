package com.samuelypsv.biblioteca.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "prestamos")
public class Prestamo {

    public static final int DIAS_PRESTAMO = 15;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "libro_id")
    private Libro libro;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "socio_id")
    private Socio socio;

    @Column(nullable = false)
    private LocalDate fechaInicio;

    @Column(nullable = false)
    private LocalDate fechaLimite;

    private LocalDate fechaDevolucion;

    protected Prestamo() {
    }

    public Prestamo(Libro libro, Socio socio, LocalDate fechaInicio) {
        this.libro = libro;
        this.socio = socio;
        this.fechaInicio = fechaInicio;
        this.fechaLimite = fechaInicio.plusDays(DIAS_PRESTAMO);
    }

    public boolean isActivo() {
        return fechaDevolucion == null;
    }

    public boolean isRetrasado(LocalDate hoy) {
        LocalDate referencia = fechaDevolucion != null ? fechaDevolucion : hoy;
        return referencia.isAfter(fechaLimite);
    }

    public void devolver(LocalDate fecha) {
        this.fechaDevolucion = fecha;
        this.libro.setDisponible(true);
    }

    public Long getId() { return id; }
    public Libro getLibro() { return libro; }
    public Socio getSocio() { return socio; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public LocalDate getFechaLimite() { return fechaLimite; }
    public LocalDate getFechaDevolucion() { return fechaDevolucion; }
}
