package com.samuelypsv.biblioteca.service;

/** Petición válida en forma pero que viola una regla de la biblioteca (409 Conflict). */
public class ReglaNegocioException extends RuntimeException {

    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
