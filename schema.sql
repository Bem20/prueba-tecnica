-- Crear base de datos
CREATE DATABASE prueba_tecnica;

-- Conectar a la base de datos
\c prueba_tecnica;

-- Crear tabla usuarios
CREATE TABLE usuarios (
  id BIGSERIAL PRIMARY KEY,
  rut INTEGER NOT NULL,
  dv VARCHAR(1) NOT NULL,
  nombre VARCHAR(200) NOT NULL,
  email VARCHAR(200) NOT NULL UNIQUE,
  activo BOOLEAN NOT NULL DEFAULT TRUE,
  fecha_creacion TIMESTAMP NOT NULL DEFAULT NOW(),
  UNIQUE(rut, dv)
);

-- Insertar datos de prueba
INSERT INTO usuarios (rut, dv, nombre, email, activo) VALUES
(12345678, '5', 'Juan Perez', 'juan.perez@email.com', true),
(23456789, '6', 'Maria Gonzalez', 'maria.gonzalez@email.com', false),
(34567890, '1', 'Carlos Lopez', 'carlos.lopez@email.com', true);
