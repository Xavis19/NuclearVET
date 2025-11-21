package com.nuclearvet.dominio.enumeraciones;

/**
 * Estados de una factura en el sistema
 */
public enum EstadoFactura {
    BORRADOR("Borrador"),
    PENDIENTE("Pendiente de Pago"),
    PAGADA("Pagada"),
    PAGADA_PARCIAL("Pagada Parcialmente"),
    VENCIDA("Vencida"),
    ANULADA("Anulada"),
    CANCELADA("Cancelada");

    private final String descripcion;

    EstadoFactura(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
