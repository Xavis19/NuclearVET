package com.nuclearvet.dominio.enumeraciones;

/**
 * Tipos de plantillas de mensajes
 */
public enum TipoPlantilla {
    EMAIL("Plantilla de correo electrónico"),
    SMS("Plantilla de mensaje SMS"),
    INTERNO("Plantilla de notificación interna"),
    WHATSAPP("Plantilla de mensaje WhatsApp");

    private final String descripcion;

    TipoPlantilla(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
