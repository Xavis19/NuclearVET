package com.nuclearvet.dominio.enums;

/**
 * Nivel de prioridad de una cita
 */
public enum Prioridad {
    NORMAL("Normal"),
    ALTA("Alta"),
    URGENTE("Urgente"),
    EMERGENCIA("Emergencia");

    private final String descripcion;

    Prioridad(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
