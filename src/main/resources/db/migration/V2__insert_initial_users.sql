-- PagaTú - Sesión 1 | Datos semilla Usuario/Cuenta
-- Estudiante: Cuadros Flores Jhosef Giampiere | 202410808 | Grupo 1

INSERT INTO roles (id, name, description) VALUES
    (1, 'CLIENTE', 'Titular de cuenta PagaTú'),
    (2, 'ADMIN', 'Administrador del servicio base');

INSERT INTO users (
    full_name,
    email,
    password,
    phone,
    account_number,
    account_type,
    balance,
    status,
    role_id,
    created_at
) VALUES
    (
        'Cuadros Flores Jhosef Giampiere',
        'jhosef.cuadros@pagatu.edu.pe',
        '$2a$10$semilla.hash.local.lab.pagatu.user01',
        '999111222',
        '001-0000001',
        'AHORROS',
        1500.50,
        'ACTIVO',
        1,
        CURRENT_TIMESTAMP
    ),
    (
        'Ana María Torres Vega',
        'ana.torres@pagatu.edu.pe',
        '$2a$10$semilla.hash.local.lab.pagatu.user02',
        '988333444',
        '001-0000002',
        'CORRIENTE',
        8200.00,
        'ACTIVO',
        1,
        CURRENT_TIMESTAMP
    ),
    (
        'Luis Alberto Rivas Soto',
        'luis.rivas@pagatu.edu.pe',
        '$2a$10$semilla.hash.local.lab.pagatu.user03',
        '977555666',
        '001-0000003',
        'CTS',
        430.75,
        'ACTIVO',
        2,
        CURRENT_TIMESTAMP
    );

SELECT setval('roles_id_seq', (SELECT MAX(id) FROM roles));
SELECT setval('users_id_seq', (SELECT MAX(id) FROM users));
