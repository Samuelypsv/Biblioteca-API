package com.samuelypsv.biblioteca.service;

import com.samuelypsv.biblioteca.model.Libro;
import com.samuelypsv.biblioteca.repository.LibroRepository;
import com.samuelypsv.biblioteca.web.dto.LibroDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LibroService {

    private final LibroRepository libros;
    private final AutorService autores;

    public LibroService(LibroRepository libros, AutorService autores) {
        this.libros = libros;
        this.autores = autores;
    }

    @Transactional(readOnly = true)
    public Page<Libro> buscar(String texto, Boolean disponible, Pageable pageable) {
        String filtro = (texto == null || texto.isBlank()) ? null : texto.trim();
        return libros.buscar(filtro, disponible, pageable);
    }

    @Transactional(readOnly = true)
    public Libro obtener(Long id) {
        return libros.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Libro", id));
    }

    public Libro crear(LibroDto.Request req) {
        if (libros.existsByIsbn(req.isbn())) {
            throw new ReglaNegocioException("Ya existe un libro con ISBN " + req.isbn());
        }
        return libros.save(new Libro(req.isbn(), req.titulo(), req.anioPublicacion(), req.genero(),
                autores.obtener(req.autorId())));
    }

    public Libro actualizar(Long id, LibroDto.Request req) {
        Libro libro = obtener(id);
        if (!libro.getIsbn().equals(req.isbn()) && libros.existsByIsbn(req.isbn())) {
            throw new ReglaNegocioException("Ya existe un libro con ISBN " + req.isbn());
        }
        libro.setIsbn(req.isbn());
        libro.setTitulo(req.titulo());
        libro.setAnioPublicacion(req.anioPublicacion());
        libro.setGenero(req.genero());
        libro.setAutor(autores.obtener(req.autorId()));
        return libro;
    }

    public void eliminar(Long id) {
        Libro libro = obtener(id);
        if (!libro.isDisponible()) {
            throw new ReglaNegocioException("No se puede borrar un libro prestado");
        }
        libros.delete(libro);
    }
}
