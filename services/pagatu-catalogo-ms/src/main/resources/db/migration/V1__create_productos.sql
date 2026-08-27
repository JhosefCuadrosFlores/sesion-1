-- pagatu-catalogo-ms | Flyway V1
-- S02 Equipo 1 | Cuadros Flores Jhosef Giampiere

CREATE TABLE productos (
    id          BIGSERIAL       PRIMARY KEY,
    nombre      VARCHAR(120)    NOT NULL,
    categoria   VARCHAR(60)     NOT NULL,
    precio      NUMERIC(12, 2)  NOT NULL,
    stock       INTEGER         NOT NULL DEFAULT 0,
    activo      BOOLEAN         NOT NULL DEFAULT TRUE
);

INSERT INTO productos (nombre, categoria, precio, stock, activo) VALUES
    ('Recarga PagaTú 20', 'RECARGA', 20.00, 100, TRUE),
    ('Recarga PagaTú 50', 'RECARGA', 50.00, 80, TRUE);
