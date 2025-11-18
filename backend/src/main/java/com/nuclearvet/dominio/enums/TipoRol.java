package com.nuclearvet.dominio.enums;

/**
 * Tipos de roles en el sistema
 */
public enum TipoRol {
    ADMINISTRADOR("Administrador del sistema"),
    VETERINARIO("Médico veterinario"),
    ASISTENTE("Asistente veterinario"),
    CLIENTE("Cliente/Propietario");

    private final String descripcion;

    TipoRol(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
