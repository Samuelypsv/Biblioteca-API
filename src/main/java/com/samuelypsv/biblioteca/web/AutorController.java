package com.samuelypsv.biblioteca.web;

import com.samuelypsv.biblioteca.service.AutorService;
import com.samuelypsv.biblioteca.web.dto.AutorDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/autores")
@Tag(name = "Autores")
public class AutorController {

    private final AutorService service;

    public AutorController(AutorService service) {
        this.service = service;
    }

    @GetMapping
    public List<AutorDto.Response> listar() {
        return service.listar().stream().map(AutorDto.Response::of).toList();
    }

    @GetMapping("/{id}")
    public AutorDto.Response obtener(@PathVariable Long id) {
        return AutorDto.Response.of(service.obtener(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AutorDto.Response crear(@Valid @RequestBody AutorDto.Request req) {
        return AutorDto.Response.of(service.crear(req));
    }

    @PutMapping("/{id}")
    public AutorDto.Response actualizar(@PathVariable Long id, @Valid @RequestBody AutorDto.Request req) {
        return AutorDto.Response.of(service.actualizar(id, req));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}
