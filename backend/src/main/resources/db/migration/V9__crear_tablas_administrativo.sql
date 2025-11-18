-- =============================================
-- MIGRACIÓN V9: CREAR TABLAS DEL MÓDULO ADMINISTRATIVO
-- Módulo: Administrativo (Facturación, Pagos, Reportes, Configuración)
-- =============================================

-- Tabla de servicios veterinarios
CREATE TABLE servicios_veterinarios (
    id BIGSERIAL PRIMARY KEY,
    codigo VARCHAR(50) NOT NULL UNIQUE,
    nombre VARCHAR(255) NOT NULL,
    descripcion TEXT,
    categoria VARCHAR(100),
    precio_base DECIMAL(12,2) NOT NULL,
    duracion_estimada_minutos INTEGER DEFAULT 30,
    requiere_cita BOOLEAN DEFAULT TRUE,
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT chk_precio_positivo CHECK (precio_base >= 0)
);

-- Insertar servicios por defecto
INSERT INTO servicios_veterinarios (codigo, nombre, categoria, precio_base, duracion_estimada_minutos) VALUES
('CONS-001', 'Consulta General', 'CONSULTA', 50000.00, 30),
('CONS-002', 'Consulta de Emergencia', 'CONSULTA', 100000.00, 45),
('VAC-001', 'Vacunación Múltiple Caninos', 'VACUNACION', 80000.00, 20),
('VAC-002', 'Vacunación Múltiple Felinos', 'VACUNACION', 70000.00, 20),
('CIR-001', 'Esterilización Caninos', 'CIRUGIA', 300000.00, 120),
('CIR-002', 'Esterilización Felinos', 'CIRUGIA', 250000.00, 90),
('LAB-001', 'Examen de Sangre Completo', 'LABORATORIO', 120000.00, 15),
('DESP-001', 'Desparasitación Interna', 'DESPARASITACION', 35000.00, 15);

-- Tabla de facturas
CREATE TABLE facturas (
    id BIGSERIAL PRIMARY KEY,
    numero_factura VARCHAR(50) NOT NULL UNIQUE,
    propietario_id BIGINT NOT NULL,
    paciente_id BIGINT,
    fecha_emision TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    subtotal DECIMAL(12,2) NOT NULL,
    impuesto DECIMAL(12,2) DEFAULT 0,
    descuento DECIMAL(12,2) DEFAULT 0,
    total DECIMAL(12,2) NOT NULL,
    estado VARCHAR(20) DEFAULT 'PENDIENTE',
    observaciones TEXT,
    usuario_emisor_id BIGINT NOT NULL,
    
    CONSTRAINT fk_factura_propietario FOREIGN KEY (propietario_id) REFERENCES propietarios(id) ON DELETE RESTRICT,
    CONSTRAINT fk_factura_paciente FOREIGN KEY (paciente_id) REFERENCES pacientes(id) ON DELETE SET NULL,
    CONSTRAINT fk_factura_usuario FOREIGN KEY (usuario_emisor_id) REFERENCES usuarios(id) ON DELETE RESTRICT,
    CONSTRAINT chk_totales_validos CHECK (total >= 0 AND subtotal >= 0)
);

-- Tabla de detalles de factura
CREATE TABLE detalles_factura (
    id BIGSERIAL PRIMARY KEY,
    factura_id BIGINT NOT NULL,
    tipo_item VARCHAR(20) NOT NULL,
    servicio_id BIGINT,
    producto_id BIGINT,
    descripcion VARCHAR(500) NOT NULL,
    cantidad INTEGER NOT NULL,
    precio_unitario DECIMAL(12,2) NOT NULL,
    subtotal DECIMAL(12,2) NOT NULL,
    
    CONSTRAINT fk_detalle_factura FOREIGN KEY (factura_id) REFERENCES facturas(id) ON DELETE CASCADE,
    CONSTRAINT fk_detalle_servicio FOREIGN KEY (servicio_id) REFERENCES servicios_veterinarios(id) ON DELETE SET NULL,
    CONSTRAINT fk_detalle_producto FOREIGN KEY (producto_id) REFERENCES productos(id) ON DELETE SET NULL,
    CONSTRAINT chk_cantidad_positiva CHECK (cantidad > 0)
);

-- Tabla de pagos
CREATE TABLE pagos (
    id BIGSERIAL PRIMARY KEY,
    factura_id BIGINT NOT NULL,
    fecha_pago TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    monto DECIMAL(12,2) NOT NULL,
    metodo_pago VARCHAR(50) NOT NULL,
    referencia_pago VARCHAR(100),
    observaciones TEXT,
    usuario_registro_id BIGINT NOT NULL,
    
    CONSTRAINT fk_pago_factura FOREIGN KEY (factura_id) REFERENCES facturas(id) ON DELETE CASCADE,
    CONSTRAINT fk_pago_usuario FOREIGN KEY (usuario_registro_id) REFERENCES usuarios(id) ON DELETE RESTRICT,
    CONSTRAINT chk_monto_positivo CHECK (monto > 0)
);

-- Tabla de configuración del sistema
CREATE TABLE configuracion_sistema (
    id BIGSERIAL PRIMARY KEY,
    clave VARCHAR(100) NOT NULL UNIQUE,
    valor TEXT NOT NULL,
    descripcion VARCHAR(500),
    tipo_dato VARCHAR(20) DEFAULT 'STRING',
    categoria VARCHAR(50),
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insertar configuraciones por defecto
INSERT INTO configuracion_sistema (clave, valor, descripcion, categoria) VALUES
('NOMBRE_CLINICA', 'NuclearVET', 'Nombre de la clínica', 'GENERAL'),
('NIT_CLINICA', '900123456-7', 'NIT de la empresa', 'GENERAL'),
('DIRECCION_CLINICA', 'Calle 123 #45-67, Bogotá', 'Dirección física', 'GENERAL'),
('TELEFONO_CLINICA', '+57 1 234 5678', 'Teléfono principal', 'GENERAL'),
('EMAIL_CLINICA', 'info@nuclearvet.com', 'Correo electrónico', 'GENERAL'),
('HORARIO_INICIO', '08:00', 'Hora de apertura', 'HORARIOS'),
('HORARIO_FIN', '18:00', 'Hora de cierre', 'HORARIOS'),
('DURACION_CITA_DEFAULT', '30', 'Duración por defecto de citas en minutos', 'CITAS'),
('DIAS_RECORDATORIO_CITA', '1', 'Días de anticipación para recordatorio', 'NOTIFICACIONES'),
('IVA_PORCENTAJE', '19', 'Porcentaje de IVA', 'FACTURACION'),
('MONEDA', 'COP', 'Moneda del sistema', 'FACTURACION');

-- Índices
CREATE INDEX idx_factura_numero ON facturas(numero_factura);
CREATE INDEX idx_factura_propietario ON facturas(propietario_id);
CREATE INDEX idx_factura_fecha ON facturas(fecha_emision DESC);
CREATE INDEX idx_factura_estado ON facturas(estado);
CREATE INDEX idx_detalle_factura ON detalles_factura(factura_id);
CREATE INDEX idx_pago_factura ON pagos(factura_id);
CREATE INDEX idx_pago_fecha ON pagos(fecha_pago DESC);
CREATE INDEX idx_servicio_codigo ON servicios_veterinarios(codigo);
CREATE INDEX idx_servicio_activo ON servicios_veterinarios(activo);

COMMENT ON TABLE servicios_veterinarios IS 'Catálogo de servicios ofrecidos por la clínica';
COMMENT ON TABLE facturas IS 'Facturas emitidas a clientes';
COMMENT ON TABLE detalles_factura IS 'Líneas de detalle de cada factura';
COMMENT ON TABLE pagos IS 'Registro de pagos recibidos';
COMMENT ON TABLE configuracion_sistema IS 'Parámetros configurables del sistema';
COMMENT ON COLUMN facturas.estado IS 'PENDIENTE, PAGADA, PARCIAL, ANULADA';
COMMENT ON COLUMN detalles_factura.tipo_item IS 'SERVICIO, PRODUCTO';
COMMENT ON COLUMN pagos.metodo_pago IS 'EFECTIVO, TARJETA, TRANSFERENCIA, DATAFONO';
