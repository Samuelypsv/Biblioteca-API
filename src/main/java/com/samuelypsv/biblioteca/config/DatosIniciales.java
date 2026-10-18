package com.samuelypsv.biblioteca.config;

import com.samuelypsv.biblioteca.model.Autor;
import com.samuelypsv.biblioteca.model.Libro;
import com.samuelypsv.biblioteca.model.Socio;
import com.samuelypsv.biblioteca.repository.AutorRepository;
import com.samuelypsv.biblioteca.repository.LibroRepository;
import com.samuelypsv.biblioteca.repository.SocioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/** Carga datos de ejemplo al arrancar en el perfil dev. */
@Component
@Profile("dev")
public class DatosIniciales implements CommandLineRunner {

    private final AutorRepository autores;
    private final LibroRepository libros;
    private final SocioRepository socios;

    public DatosIniciales(AutorRepository autores, LibroRepository libros, SocioRepository socios) {
        this.autores = autores;
        this.libros = libros;
        this.socios = socios;
    }

    @Override
    public void run(String... args) {
        if (autores.count() > 0) {
            return;
        }
        Autor cervantes = autores.save(new Autor("Miguel de Cervantes", "Española"));
        Autor garcia = autores.save(new Autor("Gabriel García Márquez", "Colombiana"));
        Autor orwell = autores.save(new Autor("George Orwell", "Británica"));
        Autor zafon = autores.save(new Autor("Carlos Ruiz Zafón", "Española"));

        libros.save(new Libro("9788420412146", "Don Quijote de la Mancha", 1605, "Novela", cervantes));
        libros.save(new Libro("9780307474728", "Cien años de soledad", 1967, "Realismo mágico", garcia));
        libros.save(new Libro("9788497592208", "El amor en los tiempos del cólera", 1985, "Novela", garcia));
        libros.save(new Libro("9780451524935", "1984", 1949, "Distopía", orwell));
        libros.save(new Libro("9780451526342", "Rebelión en la granja", 1945, "Sátira", orwell));
        libros.save(new Libro("9788408163435", "La sombra del viento", 2001, "Misterio", zafon));

        socios.save(new Socio("Ana López", "ana@example.com"));
        socios.save(new Socio("Carlos Pérez", "carlos@example.com"));
    }
}
