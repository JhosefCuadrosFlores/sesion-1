-- PagaTú - Sesión 1 | DDL Usuario/Cuenta
-- Estudiante: Cuadros Flores Jhosef Giampiere | 202410808 | Grupo 1

CREATE TABLE roles (
    id          BIGSERIAL       PRIMARY KEY,
    name        VARCHAR(50)     NOT NULL UNIQUE,
    description VARCHAR(255)
);

CREATE TABLE users (
    id              BIGSERIAL           PRIMARY KEY,
    full_name       VARCHAR(150)        NOT NULL,
    email           VARCHAR(150)        NOT NULL UNIQUE,
    password        VARCHAR(255)        NOT NULL,
    phone           VARCHAR(20),
    account_number  VARCHAR(20)         NOT NULL UNIQUE,
    account_type    VARCHAR(30)         NOT NULL,
    balance         NUMERIC(15, 2)      NOT NULL DEFAULT 0.00,
    status          VARCHAR(20)         NOT NULL,
    role_id         BIGINT              NOT NULL,
    created_at      TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP,
    CONSTRAINT fk_users_role
        FOREIGN KEY (role_id) REFERENCES roles (id),
    CONSTRAINT chk_users_balance
        CHECK (balance >= 0),
    CONSTRAINT chk_users_account_type
        CHECK (account_type IN ('AHORROS', 'CORRIENTE', 'CTS')),
    CONSTRAINT chk_users_status
        CHECK (status IN ('ACTIVO', 'INACTIVO', 'BLOQUEADO'))
);

CREATE UNIQUE INDEX uk_users_email ON users (email);
CREATE UNIQUE INDEX uk_users_account_number ON users (account_number);
CREATE INDEX idx_users_role_id ON users (role_id);
CREATE INDEX idx_users_status ON users (status);
CREATE INDEX idx_users_full_name ON users (full_name);
