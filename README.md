# PagaTú — Sesión 1: Construcción del Servicio Base

## Carátula académica

| Campo | Dato |
| --- | --- |
| **Curso / Laboratorio** | PagaTú — Construcción del Servicio Base |
| **Sesión** | Sesión 1 |
| **Estudiante** | Cuadros Flores Jhosef Giampiere |
| **Código Universitario** | 202410808 |
| **Grupo** | Grupo 1 |
| **Entidad principal** | Usuario / Cuenta |
| **Entorno de desarrollo** | VS Code + Docker Desktop + PostgreSQL |
| **Stack** | Java 21 · Spring Boot 3.4.2 · Spring Data JPA · Flyway · Springdoc OpenAPI |

---

## 1. Descripción

Servicio REST base de PagaTú que gestiona la entidad **Usuario/Cuenta**. Incluye persistencia en PostgreSQL 16, versionado de esquema con Flyway, validación Jakarta, documentación OpenAPI (Swagger UI), observabilidad con Actuator y preparación para Spring Cloud Config Client.

## 2. Requisitos previos

- JDK 21
- Maven 3.9+ (o Maven Wrapper incluido)
- Docker Desktop en ejecución

## 3. Ejecución

### 3.1. Levantar PostgreSQL con Docker

```bash
docker compose up -d
```

Verificar el contenedor:

```bash
docker compose ps
docker compose logs postgres
```

### 3.2. Ejecutar la aplicación

En Linux / macOS:

```bash
./mvnw spring-boot:run
```

En Windows (PowerShell o CMD):

```bash
mvnw.cmd spring-boot:run
```

Variables de entorno opcionales (tienen valor por defecto en `application.yml`):

| Variable | Valor por defecto |
| --- | --- |
| `DB_HOST` | `localhost` |
| `DB_PORT` | `5432` |
| `DB_NAME` | `db_pagatu_base` |
| `DB_USERNAME` | `postgres` |
| `DB_PASSWORD` | `postgres` |
| `CONFIG_SERVER_URI` | `http://localhost:8888` |
| `SERVER_PORT` | `8080` |

## 4. URLs de acceso

| Recurso | URL |
| --- | --- |
| API Usuarios | http://localhost:8080/api/v1/users |
| **Swagger UI** | http://localhost:8080/swagger-ui.html |
| OpenAPI JSON | http://localhost:8080/v3/api-docs |
| **Actuator Health** | http://localhost:8080/actuator/health |
| Actuator Info | http://localhost:8080/actuator/info |

## 5. Colección de peticiones HTTP (cURL)

### 5.1. Listar usuarios

```bash
curl -X GET "http://localhost:8080/api/v1/users" -H "Accept: application/json"
```

### 5.2. Obtener usuario por ID

```bash
curl -X GET "http://localhost:8080/api/v1/users/1" -H "Accept: application/json"
```

### 5.3. Buscar por email (método derivado)

```bash
curl -X GET "http://localhost:8080/api/v1/users/search?email=jhosef.cuadros@pagatu.edu.pe" -H "Accept: application/json"
```

### 5.4. Crear usuario / cuenta

```bash
curl -X POST "http://localhost:8080/api/v1/users" ^
  -H "Content-Type: application/json" ^
  -d "{\"fullName\":\"María Elena Quispe Rojas\",\"email\":\"maria.quispe@pagatu.edu.pe\",\"password\":\"ClaveSegura123\",\"phone\":\"966777888\",\"accountNumber\":\"001-0000004\",\"accountType\":\"AHORROS\",\"balance\":250.00,\"status\":\"ACTIVO\",\"roleId\":1}"
```

Linux / macOS:

```bash
curl -X POST "http://localhost:8080/api/v1/users" \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "María Elena Quispe Rojas",
    "email": "maria.quispe@pagatu.edu.pe",
    "password": "ClaveSegura123",
    "phone": "966777888",
    "accountNumber": "001-0000004",
    "accountType": "AHORROS",
    "balance": 250.00,
    "status": "ACTIVO",
    "roleId": 1
  }'
```

### 5.5. Actualizar usuario

```bash
curl -X PUT "http://localhost:8080/api/v1/users/4" \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "María Elena Quispe Rojas",
    "email": "maria.quispe@pagatu.edu.pe",
    "password": "ClaveSegura123",
    "phone": "966777999",
    "accountNumber": "001-0000004",
    "accountType": "CORRIENTE",
    "balance": 1250.00,
    "status": "ACTIVO",
    "roleId": 1
  }'
```

### 5.6. Eliminar usuario

```bash
curl -X DELETE "http://localhost:8080/api/v1/users/4"
```

### 5.7. Health de Actuator

```bash
curl -X GET "http://localhost:8080/actuator/health"
```

## 6. JSON de prueba para Postman

Importar el archivo `postman/PagaTu_Sesion1_Users.postman_collection.json` o copiar los cuerpos siguientes.

**POST / PUT — Body (raw JSON):**

```json
{
  "fullName": "María Elena Quispe Rojas",
  "email": "maria.quispe@pagatu.edu.pe",
  "password": "ClaveSegura123",
  "phone": "966777888",
  "accountNumber": "001-0000004",
  "accountType": "AHORROS",
  "balance": 250.00,
  "status": "ACTIVO",
  "roleId": 1
}
```

**Valores permitidos**

- `accountType`: `AHORROS` | `CORRIENTE` | `CTS`
- `status`: `ACTIVO` | `INACTIVO` | `BLOQUEADO`
- `roleId`: `1` (CLIENTE) | `2` (ADMIN)

**Ejemplo de error estandarizado (404 / 400 / 409):**

```json
{
  "timestamp": "2026-08-24T18:40:00",
  "status": 404,
  "error": "Not Found",
  "message": "No existe un usuario con el id: 99",
  "path": "/api/v1/users/99",
  "details": []
}
```

## 7. Estructura del proyecto

```
sesion 1/
├── pom.xml
├── docker-compose.yml
├── README.md
├── DEFENSA_SECCION_4.md
├── postman/PagaTu_Sesion1_Users.postman_collection.json
└── src/main/
    ├── java/com/pagatu/base/
    │   ├── PagatuBaseApplication.java
    │   ├── config/OpenApiConfig.java
    │   ├── controller/UserController.java
    │   ├── dto/UserRequestDTO.java
    │   ├── dto/UserResponseDTO.java
    │   ├── dto/ErrorResponseDTO.java
    │   ├── entity/User.java
    │   ├── exception/ResourceNotFoundException.java
    │   ├── exception/DuplicateResourceException.java
    │   ├── exception/GlobalExceptionHandler.java
    │   ├── mapper/UserMapper.java
    │   ├── repository/UserRepository.java
    │   ├── service/UserService.java
    │   └── service/impl/UserServiceImpl.java
    └── resources/
        ├── application.yml
        └── db/migration/
            ├── V1__create_users_table.sql
            └── V2__insert_initial_users.sql
```

## 8. Datos semilla

Al arrancar, Flyway crea el esquema y carga 3 usuarios:

1. `jhosef.cuadros@pagatu.edu.pe` — cuenta `001-0000001` (AHORROS)
2. `ana.torres@pagatu.edu.pe` — cuenta `001-0000002` (CORRIENTE)
3. `luis.rivas@pagatu.edu.pe` — cuenta `001-0000003` (CTS)
