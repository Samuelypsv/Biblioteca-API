package com.samuelypsv.biblioteca.web;

import com.samuelypsv.biblioteca.service.PrestamoService;
import com.samuelypsv.biblioteca.service.SocioService;
import com.samuelypsv.biblioteca.web.dto.PrestamoDto;
import com.samuelypsv.biblioteca.web.dto.SocioDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/socios")
@Tag(name = "Socios")
public class SocioController {

    private final SocioService service;
    private final PrestamoService prestamos;

    public SocioController(SocioService service, PrestamoService prestamos) {
        this.service = service;
        this.prestamos = prestamos;
    }

    @GetMapping
    public List<SocioDto.Response> listar() {
        return service.listar().stream().map(SocioDto.Response::of).toList();
    }

    @GetMapping("/{id}")
    public SocioDto.Response obtener(@PathVariable Long id) {
        return SocioDto.Response.of(service.obtener(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SocioDto.Response crear(@Valid @RequestBody SocioDto.Request req) {
        return SocioDto.Response.of(service.crear(req));
    }

    @GetMapping("/{id}/prestamos")
    public List<PrestamoDto.Response> prestamos(@PathVariable Long id) {
        var hoy = prestamos.hoy();
        return prestamos.deSocio(id).stream().map(p -> PrestamoDto.Response.of(p, hoy)).toList();
    }
}
