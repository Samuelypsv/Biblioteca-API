package com.samuelypsv.biblioteca.web;

import com.samuelypsv.biblioteca.service.LibroService;
import com.samuelypsv.biblioteca.web.dto.LibroDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/libros")
@Tag(name = "Libros")
public class LibroController {

    private final LibroService service;

    public LibroController(LibroService service) {
        this.service = service;
    }

    @GetMapping
    public PaginaDto<LibroDto.Response> buscar(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Boolean disponible,
            @PageableDefault(size = 10, sort = "titulo", direction = Sort.Direction.ASC) Pageable pageable) {
        return PaginaDto.of(service.buscar(q, disponible, pageable).map(LibroDto.Response::of));
    }

    @GetMapping("/{id}")
    public LibroDto.Response obtener(@PathVariable Long id) {
        return LibroDto.Response.of(service.obtener(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LibroDto.Response crear(@Valid @RequestBody LibroDto.Request req) {
        return LibroDto.Response.of(service.crear(req));
    }

    @PutMapping("/{id}")
    public LibroDto.Response actualizar(@PathVariable Long id, @Valid @RequestBody LibroDto.Request req) {
        return LibroDto.Response.of(service.actualizar(id, req));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}
