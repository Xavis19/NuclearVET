package com.nuclearvet.dominio.enumeraciones;

/**
 * Estados de envío de correos
 */
public enum EstadoCorreo {
    PENDIENTE("Pendiente de envío"),
    ENVIADO("Enviado correctamente"),
    ERROR("Error en el envío"),
    REINTENTANDO("Reintentando envío");

    private final String descripcion;

    EstadoCorreo(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
