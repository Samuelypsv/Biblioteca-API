package com.samuelypsv.biblioteca.web;

import com.samuelypsv.biblioteca.service.PrestamoService;
import com.samuelypsv.biblioteca.web.dto.PrestamoDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prestamos")
@Tag(name = "Préstamos")
public class PrestamoController {

    private final PrestamoService service;

    public PrestamoController(PrestamoService service) {
        this.service = service;
    }

    @GetMapping("/activos")
    public List<PrestamoDto.Response> activos() {
        var hoy = service.hoy();
        return service.activos().stream().map(p -> PrestamoDto.Response.of(p, hoy)).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PrestamoDto.Response prestar(@Valid @RequestBody PrestamoDto.Request req) {
        return PrestamoDto.Response.of(service.prestar(req.libroId(), req.socioId()), service.hoy());
    }

    @PostMapping("/{id}/devolucion")
    public PrestamoDto.Response devolver(@PathVariable Long id) {
        return PrestamoDto.Response.of(service.devolver(id), service.hoy());
    }
}
