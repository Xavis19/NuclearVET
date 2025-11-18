-- Migración para corregir tabla propietarios
-- Fecha: 2025-11-16

-- La tabla propietarios original tenía relación 1:1 con usuarios
-- Ahora debe ser independiente con todos sus datos

-- Eliminar relación con usuarios
ALTER TABLE propietarios DROP CONSTRAINT IF EXISTS fk_propietario_usuario;
ALTER TABLE propietarios DROP COLUMN IF EXISTS usuario_id;

-- Agregar todas las columnas necesarias
ALTER TABLE propietarios ADD COLUMN IF NOT EXISTS tipo_identificacion VARCHAR(50) NOT NULL DEFAULT 'CEDULA_CIUDADANIA';
ALTER TABLE propietarios ADD COLUMN IF NOT EXISTS numero_identificacion VARCHAR(50);
ALTER TABLE propietarios ADD COLUMN IF NOT EXISTS nombres VARCHAR(100) NOT NULL DEFAULT 'Nombre Pendiente';
ALTER TABLE propietarios ADD COLUMN IF NOT EXISTS apellidos VARCHAR(100) NOT NULL DEFAULT 'Apellido Pendiente';
ALTER TABLE propietarios ADD COLUMN IF NOT EXISTS telefono_principal VARCHAR(20);
ALTER TABLE propietarios ADD COLUMN IF NOT EXISTS telefono_secundario VARCHAR(20);
ALTER TABLE propietarios ADD COLUMN IF NOT EXISTS correo_electronico VARCHAR(100);
ALTER TABLE propietarios ADD COLUMN IF NOT EXISTS direccion VARCHAR(200);
ALTER TABLE propietarios ADD COLUMN IF NOT EXISTS ciudad VARCHAR(100);
ALTER TABLE propietarios ADD COLUMN IF NOT EXISTS departamento VARCHAR(100);
ALTER TABLE propietarios ADD COLUMN IF NOT EXISTS codigo_postal VARCHAR(10);
ALTER TABLE propietarios ADD COLUMN IF NOT EXISTS activo BOOLEAN NOT NULL DEFAULT true;
ALTER TABLE propietarios ADD COLUMN IF NOT EXISTS fecha_actualizacion TIMESTAMP;

-- Hacer numero_identificacion único
ALTER TABLE propietarios ADD CONSTRAINT uk_propietario_identificacion UNIQUE (numero_identificacion);

-- Crear índices
CREATE INDEX IF NOT EXISTS idx_propietario_identificacion ON propietarios(numero_identificacion);
CREATE INDEX IF NOT EXISTS idx_propietario_nombres ON propietarios(nombres);
CREATE INDEX IF NOT EXISTS idx_propietario_activo ON propietarios(activo);
CREATE INDEX IF NOT EXISTS idx_propietario_ciudad ON propietarios(ciudad);

-- Actualizar registros existentes si los hay
UPDATE propietarios SET numero_identificacion = 'PROP' || LPAD(id::TEXT, 6, '0') WHERE numero_identificacion IS NULL;

-- Comentarios
COMMENT ON COLUMN propietarios.tipo_identificacion IS 'Tipo de documento de identidad';
COMMENT ON COLUMN propietarios.numero_identificacion IS 'Número único de identificación';
COMMENT ON COLUMN propietarios.activo IS 'Indica si el propietario está activo';
