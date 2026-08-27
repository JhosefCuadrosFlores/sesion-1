-- orden-ms | Flyway V1
-- S02 Equipo 1 | Cuadros Flores Jhosef Giampiere

CREATE TABLE ordenes (
    id              BIGSERIAL           PRIMARY KEY,
    codigo          VARCHAR(30)         NOT NULL UNIQUE,
    cliente_email   VARCHAR(150)        NOT NULL,
    total           NUMERIC(12, 2)      NOT NULL,
    estado          VARCHAR(20)         NOT NULL,
    created_at      TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO ordenes (codigo, cliente_email, total, estado) VALUES
    ('ORD-2026-0001', 'jhosef.cuadros@pagatu.edu.pe', 20.00, 'PAGADA'),
    ('ORD-2026-0002', 'ana.torres@pagatu.edu.pe', 50.00, 'PENDIENTE');
