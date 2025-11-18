-- =============================================
-- MIGRACIÓN V8: CREAR TABLAS DE NOTIFICACIONES Y RECORDATORIOS
-- Módulo: Notificaciones y Recordatorios
-- =============================================

-- Tabla de plantillas de mensajes
CREATE TABLE plantillas_mensajes (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    tipo_plantilla VARCHAR(50) NOT NULL,
    asunto VARCHAR(255),
    contenido TEXT NOT NULL,
    variables_disponibles TEXT,
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insertar plantillas por defecto
INSERT INTO plantillas_mensajes (nombre, tipo_plantilla, asunto, contenido, variables_disponibles) VALUES
('RECORDATORIO_CITA', 'EMAIL', 'Recordatorio de cita - NuclearVET', 
 'Hola {{nombre_propietario}},\n\nLe recordamos que tiene una cita programada para {{nombre_paciente}} el día {{fecha_cita}} a las {{hora_cita}}.\n\nVeterinario: {{nombre_veterinario}}\nMotivo: {{motivo}}\n\nPor favor confirme su asistencia.\n\nSaludos,\nNuclearVET',
 'nombre_propietario, nombre_paciente, fecha_cita, hora_cita, nombre_veterinario, motivo'),

('RECORDATORIO_VACUNA', 'EMAIL', 'Recordatorio de vacunación - NuclearVET',
 'Hola {{nombre_propietario}},\n\nLe recordamos que {{nombre_paciente}} debe recibir la siguiente vacuna: {{nombre_vacuna}}.\n\nFecha programada: {{fecha_vacuna}}\n\nPor favor agende una cita.\n\nSaludos,\nNuclearVET',
 'nombre_propietario, nombre_paciente, nombre_vacuna, fecha_vacuna'),

('ALERTA_INVENTARIO', 'INTERNO', 'Alerta de inventario bajo',
 'El producto {{nombre_producto}} (código: {{codigo_producto}}) tiene un stock de {{cantidad_actual}} unidades.\nStock mínimo: {{stock_minimo}}.\n\nPor favor realizar pedido.',
 'nombre_producto, codigo_producto, cantidad_actual, stock_minimo'),

('CONFIRMACION_CITA', 'EMAIL', 'Confirmación de cita - NuclearVET',
 'Hola {{nombre_propietario}},\n\nSu cita ha sido confirmada:\n\nPaciente: {{nombre_paciente}}\nFecha: {{fecha_cita}}\nHora: {{hora_cita}}\nVeterinario: {{nombre_veterinario}}\n\nNos vemos pronto!\n\nSaludos,\nNuclearVET',
 'nombre_propietario, nombre_paciente, fecha_cita, hora_cita, nombre_veterinario');

-- Tabla de notificaciones
CREATE TABLE notificaciones (
    id BIGSERIAL PRIMARY KEY,
    usuario_destinatario_id BIGINT NOT NULL,
    tipo_notificacion VARCHAR(50) NOT NULL,
    titulo VARCHAR(255) NOT NULL,
    mensaje TEXT NOT NULL,
    nivel_prioridad VARCHAR(20) DEFAULT 'NORMAL',
    leida BOOLEAN DEFAULT FALSE,
    fecha_leida TIMESTAMP,
    enlace VARCHAR(500),
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_notificacion_usuario FOREIGN KEY (usuario_destinatario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

-- Tabla de recordatorios programados
CREATE TABLE recordatorios (
    id BIGSERIAL PRIMARY KEY,
    paciente_id BIGINT NOT NULL,
    tipo_recordatorio VARCHAR(50) NOT NULL,
    titulo VARCHAR(255) NOT NULL,
    descripcion TEXT,
    fecha_programada TIMESTAMP NOT NULL,
    enviado BOOLEAN DEFAULT FALSE,
    fecha_envio TIMESTAMP,
    cita_id BIGINT,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_recordatorio_paciente FOREIGN KEY (paciente_id) REFERENCES pacientes(id) ON DELETE CASCADE,
    CONSTRAINT fk_recordatorio_cita FOREIGN KEY (cita_id) REFERENCES citas(id) ON DELETE CASCADE
);

-- Tabla de historial de correos enviados
CREATE TABLE historial_correos (
    id BIGSERIAL PRIMARY KEY,
    destinatario_email VARCHAR(255) NOT NULL,
    asunto VARCHAR(255) NOT NULL,
    contenido TEXT NOT NULL,
    plantilla_id BIGINT,
    estado VARCHAR(20) DEFAULT 'PENDIENTE',
    error_mensaje TEXT,
    fecha_envio TIMESTAMP,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_correo_plantilla FOREIGN KEY (plantilla_id) REFERENCES plantillas_mensajes(id) ON DELETE SET NULL
);

-- Índices
CREATE INDEX idx_notificacion_usuario ON notificaciones(usuario_destinatario_id);
CREATE INDEX idx_notificacion_leida ON notificaciones(leida);
CREATE INDEX idx_notificacion_fecha ON notificaciones(fecha_creacion DESC);
CREATE INDEX idx_recordatorio_paciente ON recordatorios(paciente_id);
CREATE INDEX idx_recordatorio_fecha ON recordatorios(fecha_programada);
CREATE INDEX idx_recordatorio_enviado ON recordatorios(enviado);
CREATE INDEX idx_correo_estado ON historial_correos(estado);
CREATE INDEX idx_correo_fecha ON historial_correos(fecha_creacion DESC);

COMMENT ON TABLE plantillas_mensajes IS 'Plantillas configurables para correos y notificaciones';
COMMENT ON TABLE notificaciones IS 'Notificaciones internas del sistema para usuarios';
COMMENT ON TABLE recordatorios IS 'Recordatorios automáticos de citas, vacunas y tratamientos';
COMMENT ON TABLE historial_correos IS 'Historial de correos electrónicos enviados';
COMMENT ON COLUMN notificaciones.tipo_notificacion IS 'CITA, INVENTARIO, URGENCIA, SISTEMA';
COMMENT ON COLUMN notificaciones.nivel_prioridad IS 'BAJA, NORMAL, ALTA';
COMMENT ON COLUMN recordatorios.tipo_recordatorio IS 'CITA, VACUNA, DESPARASITACION, CONTROL, MEDICAMENTO';
COMMENT ON COLUMN historial_correos.estado IS 'PENDIENTE, ENVIADO, ERROR';
