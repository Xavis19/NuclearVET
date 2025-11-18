-- =============================================
-- MIGRACIÓN V13: SINCRONIZAR TABLA CITAS CON ENTIDAD
-- Autor: Sistema
-- Fecha: 2025-11-16
-- =============================================

-- Primero verificar que las columnas existen o agregarlas
DO $$
BEGIN
    -- Agregar numero_cita si no existe
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='citas' AND column_name='numero_cita') THEN
        ALTER TABLE citas ADD COLUMN numero_cita VARCHAR(20) UNIQUE;
    END IF;
    
    -- Agregar tipo_cita si no existe
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='citas' AND column_name='tipo_cita') THEN
        ALTER TABLE citas ADD COLUMN tipo_cita VARCHAR(50);
    END IF;
    
    -- Agregar fecha_hora si no existe
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='citas' AND column_name='fecha_hora') THEN
        ALTER TABLE citas ADD COLUMN fecha_hora TIMESTAMP;
    END IF;
    
    -- Agregar duracion_estimada si no existe
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='citas' AND column_name='duracion_estimada') THEN
        ALTER TABLE citas ADD COLUMN duracion_estimada INTEGER DEFAULT 30;
    END IF;
    
    -- Agregar prioridad si no existe
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='citas' AND column_name='prioridad') THEN
        ALTER TABLE citas ADD COLUMN prioridad VARCHAR(50) DEFAULT 'NORMAL';
    END IF;
    
    -- Agregar motivo_consulta si no existe
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='citas' AND column_name='motivo_consulta') THEN
        ALTER TABLE citas ADD COLUMN motivo_consulta TEXT;
    END IF;
    
    -- Agregar sintomas si no existe
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='citas' AND column_name='sintomas') THEN
        ALTER TABLE citas ADD COLUMN sintomas TEXT;
    END IF;
    
    -- Agregar diagnostico si no existe
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='citas' AND column_name='diagnostico') THEN
        ALTER TABLE citas ADD COLUMN diagnostico TEXT;
    END IF;
    
    -- Agregar tratamiento si no existe
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='citas' AND column_name='tratamiento') THEN
        ALTER TABLE citas ADD COLUMN tratamiento TEXT;
    END IF;
    
    -- Agregar costo_consulta si no existe
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='citas' AND column_name='costo_consulta') THEN
        ALTER TABLE citas ADD COLUMN costo_consulta DECIMAL(10,2);
    END IF;
    
    -- Agregar fecha_confirmacion si no existe
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='citas' AND column_name='fecha_confirmacion') THEN
        ALTER TABLE citas ADD COLUMN fecha_confirmacion TIMESTAMP;
    END IF;
    
    -- Agregar fecha_atencion si no existe
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='citas' AND column_name='fecha_atencion') THEN
        ALTER TABLE citas ADD COLUMN fecha_atencion TIMESTAMP;
    END IF;
    
    -- Agregar fecha_finalizacion si no existe
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='citas' AND column_name='fecha_finalizacion') THEN
        ALTER TABLE citas ADD COLUMN fecha_finalizacion TIMESTAMP;
    END IF;
    
    -- Agregar motivo_cancelacion si no existe
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='citas' AND column_name='motivo_cancelacion') THEN
        ALTER TABLE citas ADD COLUMN motivo_cancelacion TEXT;
    END IF;
    
    -- Agregar recordatorio_enviado si no existe
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='citas' AND column_name='recordatorio_enviado') THEN
        ALTER TABLE citas ADD COLUMN recordatorio_enviado BOOLEAN DEFAULT FALSE;
    END IF;
END$$;

-- =============================================
-- SINCRONIZAR TABLA HISTORIAL_CITAS
-- =============================================

DO $$
BEGIN
    -- Agregar estado_anterior si no existe
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='historial_citas' AND column_name='estado_anterior') THEN
        ALTER TABLE historial_citas ADD COLUMN estado_anterior VARCHAR(50);
    END IF;
    
    -- Agregar estado_nuevo si no existe
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='historial_citas' AND column_name='estado_nuevo') THEN
        ALTER TABLE historial_citas ADD COLUMN estado_nuevo VARCHAR(50);
    END IF;
    
    -- Agregar comentario si no existe
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='historial_citas' AND column_name='comentario') THEN
        ALTER TABLE historial_citas ADD COLUMN comentario TEXT;
    END IF;
    
    -- Agregar fecha_cambio si no existe
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='historial_citas' AND column_name='fecha_cambio') THEN
        ALTER TABLE historial_citas ADD COLUMN fecha_cambio TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
    END IF;
END$$;

COMMENT ON TABLE citas IS 'Citas programadas en la clínica veterinaria';
COMMENT ON TABLE historial_citas IS 'Auditoría de cambios de estado en citas';
