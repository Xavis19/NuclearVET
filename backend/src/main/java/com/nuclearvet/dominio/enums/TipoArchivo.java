package com.nuclearvet.dominio.enums;

/**
 * Tipos de archivos médicos
 */
public enum TipoArchivo {
    RADIOGRAFIA("Radiografía"),
    ECOGRAFIA("Ecografía"),
    EXAMEN_SANGRE("Examen de sangre"),
    EXAMEN_ORINA("Examen de orina"),
    BIOPSIA("Biopsia"),
    ELECTROCARDIOGRAMA("Electrocardiograma"),
    RECETA("Receta médica"),
    CERTIFICADO("Certificado"),
    OTRO("Otro");

    private final String descripcion;

    TipoArchivo(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
