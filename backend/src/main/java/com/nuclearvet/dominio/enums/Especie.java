package com.nuclearvet.dominio.enums;

/**
 * Especies de animales atendidas en la clínica
 */
public enum Especie {
    CANINO("Canino"),
    FELINO("Felino"),
    AVE("Ave"),
    ROEDOR("Roedor"),
    REPTIL("Reptil"),
    CONEJO("Conejo"),
    EXOTICO("Exótico"),
    OTRO("Otro");

    private final String descripcion;

    Especie(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
