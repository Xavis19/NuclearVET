-- =============================================
-- MIGRACIÓN V2: CREAR TABLA DE USUARIOS
-- Módulo: Usuarios y Accesos
-- =============================================

CREATE TABLE usuarios (
    id BIGSERIAL PRIMARY KEY,
    nombre_completo VARCHAR(255) NOT NULL,
    numero_documento VARCHAR(20) NOT NULL UNIQUE,
    tipo_documento VARCHAR(10) NOT NULL DEFAULT 'CC',
    correo_electronico VARCHAR(255) NOT NULL UNIQUE,
    telefono VARCHAR(20),
    direccion VARCHAR(500),
    ciudad VARCHAR(100) DEFAULT 'Bogotá',
    departamento VARCHAR(100) DEFAULT 'Cundinamarca',
    contrasena VARCHAR(255) NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    token_recuperacion VARCHAR(500),
    fecha_expiracion_token TIMESTAMP,
    rol_id BIGINT NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_usuario_rol FOREIGN KEY (rol_id) REFERENCES roles(id) ON DELETE RESTRICT
);

-- Índices para optimizar búsquedas
CREATE INDEX idx_usuario_correo ON usuarios(correo_electronico);
CREATE INDEX idx_usuario_documento ON usuarios(numero_documento);
CREATE INDEX idx_usuario_activo ON usuarios(activo);
CREATE INDEX idx_usuario_rol ON usuarios(rol_id);

-- Usuario administrador por defecto (contraseña: Admin123!)
INSERT INTO usuarios (nombre_completo, numero_documento, tipo_documento, correo_electronico, telefono, contrasena, rol_id, ciudad)
VALUES ('Administrador Sistema', '1234567890', 'CC', 'admin@nuclearvet.com', '3001234567', 
        '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 1, 'Bogotá');

COMMENT ON TABLE usuarios IS 'Tabla de usuarios del sistema con información personal y credenciales';
COMMENT ON COLUMN usuarios.tipo_documento IS 'CC=Cédula, CE=Cédula Extranjería, TI=Tarjeta Identidad, PA=Pasaporte';
