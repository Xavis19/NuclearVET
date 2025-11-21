package com.nuclearvet.dominio.enumeraciones;

/**
 * Métodos de pago aceptados en la clínica
 */
public enum MetodoPago {
    EFECTIVO("Efectivo"),
    TARJETA_CREDITO("Tarjeta de Crédito"),
    TARJETA_DEBITO("Tarjeta de Débito"),
    TRANSFERENCIA("Transferencia Bancaria"),
    PSE("PSE"),
    NEQUI("Nequi"),
    DAVIPLATA("Daviplata"),
    CREDITO("Crédito de la Clínica");

    private final String descripcion;

    MetodoPago(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
