package com.nuclearvet.dominio.enums;

/**
 * Sexo biológico del paciente
 */
public enum Sexo {
    MACHO("Macho"),
    HEMBRA("Hembra"),
    INDEFINIDO("Indefinido");

    private final String descripcion;

    Sexo(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
