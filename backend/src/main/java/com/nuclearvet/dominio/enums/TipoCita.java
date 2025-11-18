package com.nuclearvet.dominio.enums;

/**
 * Tipos de cita veterinaria
 */
public enum TipoCita {
    CONSULTA_GENERAL("Consulta General"),
    VACUNACION("Vacunación"),
    CONTROL("Control"),
    CIRUGIA("Cirugía"),
    EMERGENCIA("Emergencia"),
    DESPARASITACION("Desparasitación"),
    ESTETICA("Estética/Peluquería"),
    LABORATORIO("Exámenes de Laboratorio"),
    IMAGENOLOGIA("Imagenología"),
    OTRO("Otro");

    private final String descripcion;

    TipoCita(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
