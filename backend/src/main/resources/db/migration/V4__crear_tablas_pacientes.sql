-- =============================================
-- MIGRACIÓN V4: CREAR TABLAS DE PACIENTES
-- Módulo: Pacientes y Atención Clínica
-- =============================================

-- Tabla de propietarios (clientes)
CREATE TABLE propietarios (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL UNIQUE,
    ocupacion VARCHAR(100),
    observaciones TEXT,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_propietario_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

-- Tabla de pacientes (mascotas)
CREATE TABLE pacientes (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    especie VARCHAR(50) NOT NULL,
    raza VARCHAR(100),
    sexo VARCHAR(10) NOT NULL,
    fecha_nacimiento DATE,
    edad_estimada VARCHAR(20),
    color VARCHAR(100),
    peso_actual DECIMAL(6,2),
    microchip VARCHAR(50),
    esterilizado BOOLEAN DEFAULT FALSE,
    propietario_id BIGINT NOT NULL,
    foto_url VARCHAR(500),
    activo BOOLEAN DEFAULT TRUE,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_paciente_propietario FOREIGN KEY (propietario_id) REFERENCES propietarios(id) ON DELETE RESTRICT
);

-- Índices
CREATE INDEX idx_paciente_nombre ON pacientes(nombre);
CREATE INDEX idx_paciente_propietario ON pacientes(propietario_id);
CREATE INDEX idx_paciente_especie ON pacientes(especie);
CREATE INDEX idx_paciente_activo ON pacientes(activo);

COMMENT ON TABLE propietarios IS 'Información adicional de clientes que son dueños de mascotas';
COMMENT ON TABLE pacientes IS 'Mascotas registradas en el sistema';
COMMENT ON COLUMN pacientes.sexo IS 'MACHO, HEMBRA, INDEFINIDO';
COMMENT ON COLUMN pacientes.especie IS 'CANINO, FELINO, AVE, ROEDOR, REPTIL, OTRO';
