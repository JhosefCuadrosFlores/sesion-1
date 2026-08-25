# Defensa — Sección 4.5

**Laboratorio:** PagaTú — Construcción del Servicio Base (Sesión 1)  
**Estudiante:** Cuadros Flores Jhosef Giampiere  
**Código Universitario:** 202410808  
**Grupo:** Grupo 1  
**Entorno:** VS Code + Docker Desktop + PostgreSQL  
**Entidad principal:** Usuario / Cuenta  

Este documento responde de forma argumentada las preguntas de la Sección 4.5 de la guía oficial.

---

## 1. Diferencias y justificación de uso entre Entidades JPA y DTOs

### Qué es una Entidad JPA

La clase `User` (`entity/User.java`) es el **modelo de persistencia**. Está anotada con `@Entity` y `@Table`, refleja columnas de PostgreSQL (`users`) y es administrada por el `EntityManager` / Hibernate. Incluye preocupaciones de base de datos: clave primaria, unicidad, timestamps (`@PrePersist` / `@PreUpdate`) y el campo `password`.

### Qué es un DTO

Los records `UserRequestDTO` y `UserResponseDTO` son **objetos de transferencia** para la API HTTP. No tienen anotaciones JPA. El request concentra validaciones de entrada (`@NotBlank`, `@Email`, `@Pattern`). El response expone solo lo que el cliente debe ver (nunca la contraseña).

### Diferencias clave

| Aspecto | Entidad JPA | DTO |
| --- | --- | --- |
| Ciclo de vida | Persistido, managed/detached | Inmutable (record), sin ciclo JPA |
| Acoplamiento | Esquema SQL / Hibernate | Contrato REST / OpenAPI |
| Validación | Restricciones de BD (NOT NULL, UNIQUE, CHECK) | Bean Validation en el borde HTTP |
| Seguridad | Puede contener secretos (`password`) | El response los omite |
| Evolución | Cambiarla implica migración Flyway | Puede versionarse sin romper la tabla |

### Justificación de uso en PagaTú

1. **Aislamiento de capas:** el controlador no recibe ni devuelve `User`. El servicio traduce con `UserMapper`. Así Hibernate no “escapa” a la capa web (se evita el antipatrón de exponer entidades y problemas de lazy loading / `open-in-view`).
2. **Contrato estable:** el JSON de Postman/Swagger puede cambiar nombres o omitir campos internos (`createdAt` se genera en servidor) sin alterar el DDL.
3. **Seguridad y validación temprana:** `@Valid` en `UserController` rechaza payloads inválidos con `400` **antes** de tocar el repositorio. La unicidad de email se valida además en servicio (409) y en BD (UNIQUE).
4. **Mapeo explícito:** `UserMapper` hace el puente Entity ↔ DTO campo a campo, sin Magia de MapStruct en esta sesión, lo que facilita explicar el flujo en la defensa oral.

En resumen: **la entidad es de la base de datos; el DTO es de la API**. Mezclarlos acopla el modelo relacional al cliente y complica versionado, seguridad y pruebas.

---

## 2. Funcionamiento de Flyway y la tabla `flyway_schema_history`

### Qué es Flyway

Flyway es un motor de **migración de esquema versionado**. En este proyecto está habilitado en `application.yml`:

```yaml
spring:
  flyway:
    enabled: true
    locations: classpath:db/migration
```

Los scripts viven en `src/main/resources/db/migration/` y siguen la convención:

- `V1__create_users_table.sql` — DDL (roles, users, PKs, FKs, UNIQUE, índices)
- `V2__insert_initial_users.sql` — DML semilla (3 usuarios)

El prefijo `V{versión}__` es obligatorio. Flyway aplica las versiones **en orden estricto y una sola vez**.

### Arranque de la aplicación

1. Spring Boot inicia y configura el `DataSource` hacia `db_pagatu_base`.
2. Flyway se ejecuta **antes** de que Hibernate valide el esquema (`ddl-auto: validate`).
3. Si la base está vacía, aplica V1 y luego V2.
4. Hibernate comprueba que las entidades coinciden con las tablas ya creadas. No se usa `update`/`create` de Hibernate para no pelear con las migraciones.

### La tabla `flyway_schema_history`

Flyway crea (si no existe) la tabla de control `flyway_schema_history` en el mismo schema. Cada fila es una migración aplicada. Columnas típicas:

| Columna | Significado |
| --- | --- |
| `installed_rank` | Orden de instalación |
| `version` | `1`, `2`, … |
| `description` | `create users table`, `insert initial users` |
| `type` | `SQL` |
| `script` | Nombre del archivo |
| `checksum` | Huella del contenido del script |
| `installed_by` | Usuario de BD (`postgres`) |
| `installed_on` | Fecha/hora de aplicación |
| `execution_time` | Duración en ms |
| `success` | `true` si terminó bien |

### Por qué importa el checksum

Si alguien **edita** un script ya aplicado, Flyway detecta que el checksum cambió y **falla el arranque**. Eso protege entornos compartidos: el historial es inmutable. Los cambios posteriores se hacen con `V3__...`, nunca reescribiendo V1/V2 en producción.

Consulta de verificación:

```sql
SELECT version, description, script, success, installed_on
FROM flyway_schema_history
ORDER BY installed_rank;
```

En Docker, el volumen `pagatu_pg_data` conserva tanto las tablas de negocio como `flyway_schema_history` entre reinicios.

---

## 3. Flujo de datos en Arquitectura en Capas (Controller → Service → Repository)

El servicio base sigue una arquitectura en capas clásica, con un mapper como adaptador entre el mundo HTTP y el mundo JPA.

```
Cliente HTTP (cURL / Postman / Swagger)
        │  JSON UserRequestDTO
        ▼
UserController          ← validación @Valid, códigos HTTP, OpenAPI
        │  DTO
        ▼
UserService / UserServiceImpl  ← reglas de negocio, transacciones, unicidad
        │  Entity (vía UserMapper)
        ▼
UserRepository          ← Spring Data JPA (CRUD + findByEmail)
        │  SQL generado
        ▼
PostgreSQL 16 (tabla users + FK a roles)
        │
        ▼  Entity
UserMapper.toResponse()
        │
        ▼  UserResponseDTO JSON
Cliente HTTP
```

### Responsabilidad de cada capa

**Controller (`UserController`)**  
- Expone `/api/v1/users` con GET, POST, PUT, DELETE.  
- No contiene SQL ni reglas de unicidad.  
- Traduce resultados a `200`, `201`, `204`.  
- Las excepciones suben al `GlobalExceptionHandler`.

**Service (`UserServiceImpl`)**  
- Orquesta el caso de uso: buscar o lanzar `ResourceNotFoundException`.  
- Valida email y número de cuenta duplicados (`DuplicateResourceException`).  
- Usa `@Transactional` en escrituras y `readOnly = true` en lecturas.  
- **Nunca** retorna `User` al controlador: siempre DTO.

**Mapper (`UserMapper`)**  
- Convierte `UserRequestDTO` → `User` y `User` → `UserResponseDTO`.  
- En update copia campos sobre la entidad managed, preservando el `id`.

**Repository (`UserRepository`)**  
- Extiende `JpaRepository<User, Long>`.  
- El método derivado `findByEmail(String email)` genera `SELECT ... WHERE email = ?` por convención de nombres, sin `@Query` manual.

**Exception (`GlobalExceptionHandler`)**  
- `@RestControllerAdvice` unifica 404, 400, 409 y 500 en `ErrorResponseDTO` (`timestamp`, `status`, `error`, `message`, `path`, `details`).

Este flujo cumple separación de responsabilidades: la web no conoce JPA, JPA no conoce HTTP, y la regla de negocio (cuenta única, usuario existente) vive en el servicio.

---

## 4. Función del Config Server en una arquitectura distribuida

### Problema que resuelve

En un sistema distribuido (varios microservicios: servicio base, pagos, notificaciones, API gateway) cada instancia tendría su propio `application.yml`. Cambiar el host de PostgreSQL, un feature flag o la URI de un broker implicaría **reconstruir y redesplegar N artefactos**. Eso no escala y genera deriva de configuración.

### Qué es Spring Cloud Config Server

El **Config Server** es un servicio centralizado que sirve propiedades (YAML/properties) desde un backend, habitualmente un repositorio Git. Los microservicios actúan como **Config Client**: al arrancar (y opcionalmente al refrescar) piden su configuración según `spring.application.name` y el perfil (`dev`, `prod`).

En este laboratorio el cliente queda preparado en `application.yml`:

```yaml
spring:
  application:
    name: pagatu-base
  config:
    import: optional:configserver:${CONFIG_SERVER_URI:http://localhost:8888}
```

`optional:` es deliberado: si el Config Server aún no está levantado (Sesión 1 solo tiene el servicio base), la aplicación **no falla**; usa los valores locales por defecto (`DB_HOST=localhost`, etc.). Cuando exista el servidor en `http://localhost:8888`, esas propiedades podrán sobreescribirse sin tocar el JAR.

### Función en una arquitectura distribuida

1. **Fuente única de verdad:** credenciales, URLs y umbrales viven en Git, con historial y revisión.
2. **Desacople despliegue vs. configuración:** se cambia un YAML en el repo y se refresca el cliente (`/actuator/refresh` cuando se habilite), sin nuevo build.
3. **Perfiles por ambiente:** el mismo artefacto `pagatu-base` obtiene JDBC de `dev` o `prod` según el perfil activo.
4. **Consistencia entre instancias:** las réplicas del servicio base leen la misma versión de configuración, evitando que una instancia apunte a otra base.
5. **Seguridad operativa:** secretos pueden externalizarse (Vault, cifrado `cipher`) en lugar de hardcodearlos. En Sesión 1 se usan variables de entorno + defaults académicos (`postgres`/`postgres`), coherentes con Docker Compose.

### Relación con PagaTú

El servicio base de Usuario/Cuenta es el primer nodo. Más adelante, pagos o transferencias necesitarán el mismo `datasource` o la URL de este API. El Config Server evita copiar `application.yml` en cada repo y permite gobernar el ecosistema PagaTú como una plataforma, no como islas de configuración.

---

**Fin de la defensa — Sección 4.5**  
Cuadros Flores Jhosef Giampiere · 202410808 · Grupo 1
