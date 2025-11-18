-- Migración para agregar columna alergias a pacientes
-- Fecha: 2025-11-16

ALTER TABLE pacientes ADD COLUMN IF NOT EXISTS alergias TEXT;

COMMENT ON COLUMN pacientes.alergias IS 'Alergias conocidas del paciente';
