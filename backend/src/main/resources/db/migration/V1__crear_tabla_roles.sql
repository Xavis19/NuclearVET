-- =============================================
-- MIGRACIÓN V1: CREAR TABLA DE ROLES
-- Módulo: Usuarios y Accesos
-- =============================================

CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(255),
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insertar roles por defecto del sistema
INSERT INTO roles (nombre, descripcion) VALUES
('ADMINISTRADOR', 'Acceso total al sistema, gestión de usuarios y configuración'),
('VETERINARIO', 'Gestión de pacientes, consultas, historias clínicas y servicios'),
('ASISTENTE', 'Soporte en atención, agenda de citas e inventario'),
('CLIENTE', 'Consulta de información de sus mascotas y gestión de citas');

COMMENT ON TABLE roles IS 'Catálogo de roles del sistema para control de accesos';
