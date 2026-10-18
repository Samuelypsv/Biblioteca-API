package com.samuelypsv.biblioteca.repository;

import com.samuelypsv.biblioteca.model.Libro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LibroRepository extends JpaRepository<Libro, Long> {

    boolean existsByIsbn(String isbn);

    boolean existsByAutorId(Long autorId);

    @Query("""
            select l from Libro l join fetch l.autor a
            where (:texto is null
                   or lower(l.titulo) like lower(concat('%', cast(:texto as string), '%'))
                   or lower(a.nombre) like lower(concat('%', cast(:texto as string), '%')))
              and (:disponible is null or l.disponible = :disponible)
            """)
    Page<Libro> buscar(@Param("texto") String texto,
                       @Param("disponible") Boolean disponible,
                       Pageable pageable);
}
