package com.nuclearvet.dominio.enumeraciones;

/**
 * Niveles de prioridad para notificaciones
 */
public enum PrioridadNotificacion {
    BAJA("Prioridad baja"),
    NORMAL("Prioridad normal"),
    ALTA("Prioridad alta"),
    URGENTE("Urgente");

    private final String descripcion;

    PrioridadNotificacion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
