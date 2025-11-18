-- =============================================
-- MIGRACIÓN V5: CREAR TABLAS DE HISTORIAS CLÍNICAS
-- Módulo: Pacientes y Atención Clínica
-- =============================================

-- Tabla de historias clínicas
CREATE TABLE historias_clinicas (
    id BIGSERIAL PRIMARY KEY,
    paciente_id BIGINT NOT NULL UNIQUE,
    numero_historia VARCHAR(50) NOT NULL UNIQUE,
    alergias TEXT,
    enfermedades_previas TEXT,
    cirugias_previas TEXT,
    medicamentos_actuales TEXT,
    observaciones_generales TEXT,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_historia_paciente FOREIGN KEY (paciente_id) REFERENCES pacientes(id) ON DELETE CASCADE
);

-- Tabla de consultas/atenciones
CREATE TABLE consultas (
    id BIGSERIAL PRIMARY KEY,
    historia_clinica_id BIGINT NOT NULL,
    veterinario_id BIGINT NOT NULL,
    tipo_servicio VARCHAR(50) NOT NULL,
    motivo_consulta TEXT NOT NULL,
    sintomas TEXT,
    temperatura DECIMAL(4,2),
    frecuencia_cardiaca INTEGER,
    frecuencia_respiratoria INTEGER,
    peso DECIMAL(6,2),
    diagnostico TEXT,
    tratamiento TEXT,
    recomendaciones TEXT,
    proximo_control DATE,
    estado VARCHAR(20) DEFAULT 'COMPLETADA',
    fecha_atencion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_consulta_historia FOREIGN KEY (historia_clinica_id) REFERENCES historias_clinicas(id) ON DELETE CASCADE,
    CONSTRAINT fk_consulta_veterinario FOREIGN KEY (veterinario_id) REFERENCES usuarios(id) ON DELETE RESTRICT
);

-- Tabla de archivos médicos adjuntos
CREATE TABLE archivos_medicos (
    id BIGSERIAL PRIMARY KEY,
    consulta_id BIGINT NOT NULL,
    nombre_archivo VARCHAR(255) NOT NULL,
    tipo_archivo VARCHAR(50) NOT NULL,
    ruta_archivo VARCHAR(500) NOT NULL,
    descripcion VARCHAR(500),
    fecha_subida TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_archivo_consulta FOREIGN KEY (consulta_id) REFERENCES consultas(id) ON DELETE CASCADE
);

-- Índices
CREATE INDEX idx_historia_paciente ON historias_clinicas(paciente_id);
CREATE INDEX idx_historia_numero ON historias_clinicas(numero_historia);
CREATE INDEX idx_consulta_historia ON consultas(historia_clinica_id);
CREATE INDEX idx_consulta_veterinario ON consultas(veterinario_id);
CREATE INDEX idx_consulta_fecha ON consultas(fecha_atencion DESC);
CREATE INDEX idx_archivo_consulta ON archivos_medicos(consulta_id);

COMMENT ON TABLE historias_clinicas IS 'Historia clínica única por paciente';
COMMENT ON TABLE consultas IS 'Registro de cada atención veterinaria';
COMMENT ON TABLE archivos_medicos IS 'Exámenes, radiografías y documentos clínicos';
COMMENT ON COLUMN consultas.tipo_servicio IS 'CONSULTA, CIRUGIA, VACUNACION, DESPARASITACION, CONTROL, EMERGENCIA';
COMMENT ON COLUMN consultas.estado IS 'COMPLETADA, EN_PROCESO, CANCELADA';
