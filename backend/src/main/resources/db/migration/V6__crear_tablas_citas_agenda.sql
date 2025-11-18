-- =============================================
-- MIGRACIÓN V6: CREAR TABLAS DE CITAS Y AGENDA
-- Módulo: Citas y Agenda
-- =============================================

-- Tabla de citas
CREATE TABLE citas (
    id BIGSERIAL PRIMARY KEY,
    paciente_id BIGINT NOT NULL,
    propietario_id BIGINT NOT NULL,
    veterinario_id BIGINT NOT NULL,
    tipo_servicio VARCHAR(50) NOT NULL,
    fecha_cita TIMESTAMP NOT NULL,
    duracion_minutos INTEGER DEFAULT 30,
    motivo TEXT,
    nivel_urgencia VARCHAR(20) DEFAULT 'NORMAL',
    estado VARCHAR(20) DEFAULT 'PROGRAMADA',
    observaciones TEXT,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_cita_paciente FOREIGN KEY (paciente_id) REFERENCES pacientes(id) ON DELETE CASCADE,
    CONSTRAINT fk_cita_propietario FOREIGN KEY (propietario_id) REFERENCES propietarios(id) ON DELETE CASCADE,
    CONSTRAINT fk_cita_veterinario FOREIGN KEY (veterinario_id) REFERENCES usuarios(id) ON DELETE RESTRICT,
    CONSTRAINT chk_duracion_positiva CHECK (duracion_minutos > 0)
);

-- Tabla de historial de cambios de citas
CREATE TABLE historial_citas (
    id BIGSERIAL PRIMARY KEY,
    cita_id BIGINT NOT NULL,
    accion VARCHAR(50) NOT NULL,
    fecha_anterior TIMESTAMP,
    fecha_nueva TIMESTAMP,
    motivo_cambio TEXT,
    usuario_id BIGINT NOT NULL,
    fecha_cambio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_historial_cita FOREIGN KEY (cita_id) REFERENCES citas(id) ON DELETE CASCADE,
    CONSTRAINT fk_historial_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE RESTRICT
);

-- Tabla de disponibilidad de veterinarios
CREATE TABLE disponibilidad_veterinarios (
    id BIGSERIAL PRIMARY KEY,
    veterinario_id BIGINT NOT NULL,
    dia_semana INTEGER NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fin TIME NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    
    CONSTRAINT fk_disponibilidad_veterinario FOREIGN KEY (veterinario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    CONSTRAINT chk_dia_semana CHECK (dia_semana BETWEEN 1 AND 7),
    CONSTRAINT chk_horas_validas CHECK (hora_inicio < hora_fin)
);

-- Índices
CREATE INDEX idx_cita_paciente ON citas(paciente_id);
CREATE INDEX idx_cita_veterinario ON citas(veterinario_id);
CREATE INDEX idx_cita_fecha ON citas(fecha_cita);
CREATE INDEX idx_cita_estado ON citas(estado);
CREATE INDEX idx_cita_urgencia ON citas(nivel_urgencia);
CREATE INDEX idx_historial_cita ON historial_citas(cita_id);
CREATE INDEX idx_disponibilidad_veterinario ON disponibilidad_veterinarios(veterinario_id);
CREATE INDEX idx_disponibilidad_dia ON disponibilidad_veterinarios(dia_semana);

-- Constraint único: Un veterinario no puede tener dos citas al mismo tiempo
CREATE UNIQUE INDEX idx_veterinario_fecha_unica ON citas(veterinario_id, fecha_cita) 
    WHERE estado IN ('PROGRAMADA', 'EN_CURSO');

COMMENT ON TABLE citas IS 'Citas programadas en la clínica';
COMMENT ON TABLE historial_citas IS 'Auditoría de cambios en citas';
COMMENT ON TABLE disponibilidad_veterinarios IS 'Horarios de disponibilidad del personal';
COMMENT ON COLUMN citas.nivel_urgencia IS 'BAJA, NORMAL, ALTA, EMERGENCIA';
COMMENT ON COLUMN citas.estado IS 'PROGRAMADA, CONFIRMADA, EN_CURSO, COMPLETADA, CANCELADA, NO_ASISTIO';
COMMENT ON COLUMN disponibilidad_veterinarios.dia_semana IS '1=Lunes, 2=Martes, 3=Miércoles, 4=Jueves, 5=Viernes, 6=Sábado, 7=Domingo';
