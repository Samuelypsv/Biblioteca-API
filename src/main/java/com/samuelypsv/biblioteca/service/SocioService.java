package com.samuelypsv.biblioteca.service;

import com.samuelypsv.biblioteca.model.Socio;
import com.samuelypsv.biblioteca.repository.SocioRepository;
import com.samuelypsv.biblioteca.web.dto.SocioDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SocioService {

    private final SocioRepository socios;

    public SocioService(SocioRepository socios) {
        this.socios = socios;
    }

    @Transactional(readOnly = true)
    public List<Socio> listar() {
        return socios.findAll();
    }

    @Transactional(readOnly = true)
    public Socio obtener(Long id) {
        return socios.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Socio", id));
    }

    public Socio crear(SocioDto.Request req) {
        if (socios.existsByEmail(req.email())) {
            throw new ReglaNegocioException("Ya existe un socio con email " + req.email());
        }
        return socios.save(new Socio(req.nombre(), req.email()));
    }
}
