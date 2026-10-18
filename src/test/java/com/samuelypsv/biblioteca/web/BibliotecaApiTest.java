package com.samuelypsv.biblioteca.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/** Prueba de extremo a extremo de la API contra H2 en memoria. */
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BibliotecaApiTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper json;

    private long crear(String url, String cuerpo) throws Exception {
        String respuesta = mvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(cuerpo))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        JsonNode nodo = json.readTree(respuesta);
        return nodo.get("id").asLong();
    }

    @Test
    void cicloCompletoDePrestamoYDevolucion() throws Exception {
        long autor = crear("/api/autores", """
                {"nombre":"Ursula K. Le Guin","nacionalidad":"Estadounidense"}""");
        long libro = crear("/api/libros", """
                {"isbn":"9780441478125","titulo":"La mano izquierda de la oscuridad","anioPublicacion":1969,"autorId":%d}"""
                .formatted(autor));
        long socio = crear("/api/socios", """
                {"nombre":"Lucía","email":"lucia@example.com"}""");

        long prestamo = crear("/api/prestamos", """
                {"libroId":%d,"socioId":%d}""".formatted(libro, socio));

        mvc.perform(get("/api/libros/{id}", libro))
                .andExpect(jsonPath("$.disponible").value(false));

        mvc.perform(post("/api/prestamos").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"libroId\":%d,\"socioId\":%d}".formatted(libro, socio)))
                .andExpect(status().isConflict());

        mvc.perform(post("/api/prestamos/{id}/devolucion", prestamo))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activo").value(false));

        mvc.perform(get("/api/libros").param("q", "oscuridad").param("disponible", "true"))
                .andExpect(jsonPath("$.totalElementos").value(1))
                .andExpect(jsonPath("$.contenido[0].autor.nombre").value("Ursula K. Le Guin"));
    }

    @Test
    void validaLosDatosDeEntrada() throws Exception {
        mvc.perform(post("/api/libros").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"isbn\":\"abc\",\"titulo\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errores.isbn").exists())
                .andExpect(jsonPath("$.errores.titulo").exists())
                .andExpect(jsonPath("$.errores.autorId").exists());
    }

    @Test
    void devuelve404SiNoExiste() throws Exception {
        mvc.perform(get("/api/libros/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Libro con id 999 no existe"));
    }

    @Test
    void cargaDatosDeEjemploEnDev() throws Exception {
        mvc.perform(get("/api/libros").param("q", "orwell"))
                .andExpect(jsonPath("$.totalElementos").value(2));
    }
}
