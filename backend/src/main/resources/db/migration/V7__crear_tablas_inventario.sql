-- =============================================
-- MIGRACIÓN V7: CREAR TABLAS DE INVENTARIO Y MEDICAMENTOS
-- Módulo: Inventario y Medicamentos
-- =============================================

-- Tabla de categorías de productos
CREATE TABLE categorias_productos (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion VARCHAR(255),
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insertar categorías por defecto
INSERT INTO categorias_productos (nombre, descripcion) VALUES
('MEDICAMENTOS', 'Medicamentos veterinarios'),
('VACUNAS', 'Vacunas y biológicos'),
('ANTIPARASITARIOS', 'Productos antiparasitarios'),
('INSUMOS_MEDICOS', 'Material médico y quirúrgico'),
('ALIMENTOS', 'Alimentos y suplementos'),
('ACCESORIOS', 'Accesorios y productos de higiene');

-- Tabla de productos (insumos y medicamentos)
CREATE TABLE productos (
    id BIGSERIAL PRIMARY KEY,
    codigo VARCHAR(50) NOT NULL UNIQUE,
    nombre VARCHAR(255) NOT NULL,
    categoria_id BIGINT NOT NULL,
    descripcion TEXT,
    unidad_medida VARCHAR(20) NOT NULL,
    presentacion VARCHAR(100),
    laboratorio VARCHAR(150),
    principio_activo VARCHAR(255),
    concentracion VARCHAR(100),
    cantidad_actual INTEGER DEFAULT 0,
    stock_minimo INTEGER DEFAULT 10,
    stock_maximo INTEGER DEFAULT 1000,
    precio_compra DECIMAL(12,2),
    precio_venta DECIMAL(12,2),
    requiere_receta BOOLEAN DEFAULT FALSE,
    activo BOOLEAN DEFAULT TRUE,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_producto_categoria FOREIGN KEY (categoria_id) REFERENCES categorias_productos(id) ON DELETE RESTRICT,
    CONSTRAINT chk_cantidad_no_negativa CHECK (cantidad_actual >= 0),
    CONSTRAINT chk_stock_minimo_valido CHECK (stock_minimo >= 0),
    CONSTRAINT chk_precios_validos CHECK (precio_compra >= 0 AND precio_venta >= 0)
);

-- Tabla de lotes
CREATE TABLE lotes (
    id BIGSERIAL PRIMARY KEY,
    producto_id BIGINT NOT NULL,
    numero_lote VARCHAR(100) NOT NULL,
    fecha_fabricacion DATE,
    fecha_vencimiento DATE NOT NULL,
    cantidad INTEGER NOT NULL,
    proveedor VARCHAR(255),
    precio_compra DECIMAL(12,2),
    activo BOOLEAN DEFAULT TRUE,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_lote_producto FOREIGN KEY (producto_id) REFERENCES productos(id) ON DELETE CASCADE,
    CONSTRAINT chk_fecha_vencimiento CHECK (fecha_vencimiento > fecha_fabricacion),
    CONSTRAINT chk_cantidad_lote CHECK (cantidad > 0)
);

-- Tabla de movimientos de inventario
CREATE TABLE movimientos_inventario (
    id BIGSERIAL PRIMARY KEY,
    producto_id BIGINT NOT NULL,
    lote_id BIGINT,
    tipo_movimiento VARCHAR(20) NOT NULL,
    cantidad INTEGER NOT NULL,
    cantidad_anterior INTEGER NOT NULL,
    cantidad_nueva INTEGER NOT NULL,
    motivo VARCHAR(255),
    consulta_id BIGINT,
    usuario_id BIGINT NOT NULL,
    fecha_movimiento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_movimiento_producto FOREIGN KEY (producto_id) REFERENCES productos(id) ON DELETE CASCADE,
    CONSTRAINT fk_movimiento_lote FOREIGN KEY (lote_id) REFERENCES lotes(id) ON DELETE SET NULL,
    CONSTRAINT fk_movimiento_consulta FOREIGN KEY (consulta_id) REFERENCES consultas(id) ON DELETE SET NULL,
    CONSTRAINT fk_movimiento_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE RESTRICT,
    CONSTRAINT chk_cantidad_movimiento CHECK (cantidad > 0)
);

-- Tabla de alertas de inventario
CREATE TABLE alertas_inventario (
    id BIGSERIAL PRIMARY KEY,
    producto_id BIGINT NOT NULL,
    tipo_alerta VARCHAR(50) NOT NULL,
    mensaje TEXT NOT NULL,
    nivel_prioridad VARCHAR(20) DEFAULT 'MEDIA',
    leida BOOLEAN DEFAULT FALSE,
    fecha_alerta TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_alerta_producto FOREIGN KEY (producto_id) REFERENCES productos(id) ON DELETE CASCADE
);

-- Índices
CREATE INDEX idx_producto_codigo ON productos(codigo);
CREATE INDEX idx_producto_nombre ON productos(nombre);
CREATE INDEX idx_producto_categoria ON productos(categoria_id);
CREATE INDEX idx_producto_activo ON productos(activo);
CREATE INDEX idx_lote_producto ON lotes(producto_id);
CREATE INDEX idx_lote_vencimiento ON lotes(fecha_vencimiento);
CREATE INDEX idx_movimiento_producto ON movimientos_inventario(producto_id);
CREATE INDEX idx_movimiento_fecha ON movimientos_inventario(fecha_movimiento DESC);
CREATE INDEX idx_movimiento_tipo ON movimientos_inventario(tipo_movimiento);
CREATE INDEX idx_alerta_producto ON alertas_inventario(producto_id);
CREATE INDEX idx_alerta_leida ON alertas_inventario(leida);

COMMENT ON TABLE productos IS 'Catálogo de productos: medicamentos, insumos, vacunas';
COMMENT ON TABLE lotes IS 'Control de lotes y fechas de vencimiento';
COMMENT ON TABLE movimientos_inventario IS 'Trazabilidad de entradas y salidas de inventario';
COMMENT ON TABLE alertas_inventario IS 'Alertas de stock bajo y productos próximos a vencer';
COMMENT ON COLUMN productos.unidad_medida IS 'UNIDAD, CAJA, FRASCO, ML, MG, TABLETA, etc.';
COMMENT ON COLUMN movimientos_inventario.tipo_movimiento IS 'ENTRADA, SALIDA, AJUSTE, DEVOLUCION, CONSUMO';
COMMENT ON COLUMN alertas_inventario.tipo_alerta IS 'STOCK_BAJO, PRODUCTO_VENCIDO, PROXIMO_VENCER';
COMMENT ON COLUMN alertas_inventario.nivel_prioridad IS 'BAJA, MEDIA, ALTA';
