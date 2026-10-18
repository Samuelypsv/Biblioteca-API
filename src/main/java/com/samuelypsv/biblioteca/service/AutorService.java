package com.samuelypsv.biblioteca.service;

import com.samuelypsv.biblioteca.model.Autor;
import com.samuelypsv.biblioteca.repository.AutorRepository;
import com.samuelypsv.biblioteca.repository.LibroRepository;
import com.samuelypsv.biblioteca.web.dto.AutorDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AutorService {

    private final AutorRepository autores;
    private final LibroRepository libros;

    public AutorService(AutorRepository autores, LibroRepository libros) {
        this.autores = autores;
        this.libros = libros;
    }

    @Transactional(readOnly = true)
    public List<Autor> listar() {
        return autores.findAll();
    }

    @Transactional(readOnly = true)
    public Autor obtener(Long id) {
        return autores.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Autor", id));
    }

    public Autor crear(AutorDto.Request req) {
        return autores.save(new Autor(req.nombre(), req.nacionalidad()));
    }

    public Autor actualizar(Long id, AutorDto.Request req) {
        Autor autor = obtener(id);
        autor.setNombre(req.nombre());
        autor.setNacionalidad(req.nacionalidad());
        return autor;
    }

    public void eliminar(Long id) {
        Autor autor = obtener(id);
        if (libros.existsByAutorId(id)) {
            throw new ReglaNegocioException("No se puede borrar un autor que tiene libros");
        }
        autores.delete(autor);
    }
}
