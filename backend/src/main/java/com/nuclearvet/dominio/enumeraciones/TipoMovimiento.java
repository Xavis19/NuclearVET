package com.nuclearvet.dominio.enumeraciones;

/**
 * Tipos de movimientos en el inventario
 */
public enum TipoMovimiento {
    ENTRADA_COMPRA("Entrada por Compra"),
    ENTRADA_DEVOLUCION("Entrada por Devolución"),
    ENTRADA_AJUSTE("Entrada por Ajuste"),
    SALIDA_VENTA("Salida por Venta"),
    SALIDA_CONSUMO("Salida por Consumo Interno"),
    SALIDA_BAJA("Salida por Baja/Vencimiento"),
    SALIDA_AJUSTE("Salida por Ajuste"),
    TRASLADO("Traslado entre ubicaciones");

    private final String descripcion;

    TipoMovimiento(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
