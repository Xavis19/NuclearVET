-- Migración para sincronizar tabla pacientes con entidad
-- Fecha: 2025-11-16

-- Agregar columnas faltantes
ALTER TABLE pacientes ADD COLUMN IF NOT EXISTS codigo VARCHAR(20) UNIQUE;
ALTER TABLE pacientes ADD COLUMN IF NOT EXISTS estado VARCHAR(50) NOT NULL DEFAULT 'ACTIVO';
ALTER TABLE pacientes ADD COLUMN IF NOT EXISTS observaciones TEXT;
ALTER TABLE pacientes ADD COLUMN IF NOT EXISTS veterinario_asignado_id BIGINT;

-- Renombrar columna peso_actual a peso
ALTER TABLE pacientes RENAME COLUMN peso_actual TO peso;

-- Eliminar columna edad_estimada (ya no se usa, se calcula desde fecha_nacimiento)
ALTER TABLE pacientes DROP COLUMN IF EXISTS edad_estimada;

-- Eliminar columna activo (ahora usamos estado)
ALTER TABLE pacientes DROP COLUMN IF EXISTS activo;

-- Agregar foreign key para veterinario asignado
ALTER TABLE pacientes 
    ADD CONSTRAINT fk_paciente_veterinario 
    FOREIGN KEY (veterinario_asignado_id) 
    REFERENCES usuarios(id) ON DELETE SET NULL;

-- Crear índices
CREATE INDEX IF NOT EXISTS idx_paciente_codigo ON pacientes(codigo);
CREATE INDEX IF NOT EXISTS idx_paciente_estado ON pacientes(estado);
CREATE INDEX IF NOT EXISTS idx_paciente_veterinario ON pacientes(veterinario_asignado_id);

-- Comentarios
COMMENT ON COLUMN pacientes.codigo IS 'Código único del paciente';
COMMENT ON COLUMN pacientes.estado IS 'Estado del paciente (ACTIVO, INACTIVO, FALLECIDO, etc.)';
COMMENT ON COLUMN pacientes.observaciones IS 'Observaciones generales del paciente';
COMMENT ON COLUMN pacientes.veterinario_asignado_id IS 'Veterinario asignado al paciente';

-- Generar códigos únicos para pacientes existentes (si los hay)
UPDATE pacientes SET codigo = 'PAC' || LPAD(id::TEXT, 6, '0') WHERE codigo IS NULL;
