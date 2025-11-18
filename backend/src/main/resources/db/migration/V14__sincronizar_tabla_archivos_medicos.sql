-- Migración V14: Sincronizar tabla archivos_medicos con la entidad ArchivoMedico
-- Autor: Sistema NuclearVET
-- Fecha: 2025-01-16

-- Agregar columnas faltantes en archivos_medicos
DO $$
BEGIN
    -- Agregar columna tamano_bytes si no existe
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_name='archivos_medicos' AND column_name='tamano_bytes') THEN
        ALTER TABLE archivos_medicos ADD COLUMN tamano_bytes BIGINT;
        COMMENT ON COLUMN archivos_medicos.tamano_bytes IS 'Tamaño del archivo en bytes';
    END IF;

    -- Agregar columna tipo_contenido si no existe
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_name='archivos_medicos' AND column_name='tipo_contenido') THEN
        ALTER TABLE archivos_medicos ADD COLUMN tipo_contenido VARCHAR(255);
        COMMENT ON COLUMN archivos_medicos.tipo_contenido IS 'MIME type del archivo';
    END IF;
    
    -- Renombrar columna tipo a tipo_archivo si existe
    IF EXISTS (SELECT 1 FROM information_schema.columns 
               WHERE table_name='archivos_medicos' AND column_name='tipo') THEN
        ALTER TABLE archivos_medicos RENAME COLUMN tipo TO tipo_archivo;
    END IF;
    
    -- Renombrar columna url_archivo a ruta_archivo si existe
    IF EXISTS (SELECT 1 FROM information_schema.columns 
               WHERE table_name='archivos_medicos' AND column_name='url_archivo') THEN
        ALTER TABLE archivos_medicos RENAME COLUMN url_archivo TO ruta_archivo;
    END IF;
    
    -- Renombrar columna nombre a nombre_archivo si existe
    IF EXISTS (SELECT 1 FROM information_schema.columns 
               WHERE table_name='archivos_medicos' AND column_name='nombre') THEN
        ALTER TABLE archivos_medicos RENAME COLUMN nombre TO nombre_archivo;
    END IF;
    
    -- Agregar columna comentario a historial_citas si no existe
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_name='historial_citas' AND column_name='comentario') THEN
        ALTER TABLE historial_citas ADD COLUMN comentario TEXT;
        COMMENT ON COLUMN historial_citas.comentario IS 'Comentario sobre el cambio de estado';
    END IF;
END $$;
