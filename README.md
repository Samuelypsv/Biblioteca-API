# 📚 Biblioteca API

API REST para gestionar una biblioteca: autores, libros, socios y préstamos. Hecha con **Spring Boot 3** y **Java 21** como proyecto de 2º de DAM.

Es el backend de [biblioteca-web](https://github.com/Samuelypsv/biblioteca-web).

## Tecnologías

- Java 21, Spring Boot 3.5 (Web, Data JPA, Validation)
- H2 en memoria (desarrollo) y PostgreSQL (producción)
- OpenAPI / Swagger UI con springdoc
- JUnit 5, Mockito y MockMvc
- Docker y Docker Compose
- GitHub Actions (CI)

## Funcionalidades

- CRUD de autores, libros y socios
- Búsqueda paginada de libros por título o autor, filtrando por disponibilidad
- Préstamos con reglas de negocio:
  - un libro prestado no se puede volver a prestar
  - cada socio puede tener como máximo **3 préstamos activos**
  - el plazo es de **15 días** y la API marca los préstamos retrasados
- Errores en formato estándar RFC 7807 (`application/problem+json`) con detalle de validación por campo

## Arquitectura

```
web (controladores + DTOs)  →  service (reglas de negocio)  →  repository (Spring Data JPA)  →  BD
```

Las entidades nunca salen de la API: los controladores devuelven `record`s DTO. El reloj (`Clock`) se inyecta para poder probar los plazos de préstamo con fechas fijas.

## Cómo ejecutarlo

### Opción 1: en local con H2 (no necesitas instalar nada más que Java 21)

```bash
./mvnw spring-boot:run
```

Arranca en el perfil `dev` con datos de ejemplo.

- API: http://localhost:8080/api/libros
- Swagger UI: http://localhost:8080/swagger-ui.html
- Consola H2: http://localhost:8080/h2-console (JDBC URL `jdbc:h2:mem:biblioteca`)

### Opción 2: con Docker y PostgreSQL

```bash
docker compose up --build
```

## Tests

```bash
./mvnw test
```

Incluye tests unitarios del dominio y del servicio de préstamos (con Mockito) y tests de integración de la API completa con MockMvc.

## Endpoints principales

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/libros?q=&disponible=&page=&size=` | Buscar libros (paginado) |
| POST | `/api/libros` | Crear libro |
| PUT / DELETE | `/api/libros/{id}` | Editar / borrar libro |
| GET / POST | `/api/autores` | Listar / crear autores |
| GET / POST | `/api/socios` | Listar / crear socios |
| GET | `/api/socios/{id}/prestamos` | Historial de un socio |
| POST | `/api/prestamos` | Prestar un libro `{libroId, socioId}` |
| POST | `/api/prestamos/{id}/devolucion` | Devolver un libro |
| GET | `/api/prestamos/activos` | Préstamos pendientes, ordenados por fecha límite |

### Ejemplo

```bash
curl -X POST localhost:8080/api/prestamos \
  -H "Content-Type: application/json" \
  -d '{"libroId": 1, "socioId": 1}'
```

## Autor

Samuel ([@Samuelypsv](https://github.com/Samuelypsv)), estudiante de Desarrollo de Aplicaciones Multiplataforma.
