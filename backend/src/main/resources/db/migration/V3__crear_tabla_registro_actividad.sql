-- =============================================
-- MIGRACIÓN V3: CREAR TABLA DE REGISTRO DE ACTIVIDAD
-- Módulo: Usuarios y Accesos
-- =============================================

CREATE TABLE registro_actividad (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    tipo_accion VARCHAR(50) NOT NULL,
    descripcion VARCHAR(500),
    ip_origen VARCHAR(45),
    fecha_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_actividad_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

-- Índices para consultas de auditoría
CREATE INDEX idx_actividad_usuario ON registro_actividad(usuario_id);
CREATE INDEX idx_actividad_fecha ON registro_actividad(fecha_hora DESC);
CREATE INDEX idx_actividad_tipo ON registro_actividad(tipo_accion);

COMMENT ON TABLE registro_actividad IS 'Registro de auditoría de acciones críticas del sistema';
COMMENT ON COLUMN registro_actividad.tipo_accion IS 'INICIO_SESION, CREAR_USUARIO, ACTUALIZAR_PACIENTE, etc.';
