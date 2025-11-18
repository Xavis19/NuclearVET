package com.nuclearvet.dominio.enums;

/**
 * Tipos de identificación válidos en Colombia
 */
public enum TipoIdentificacion {
    CEDULA_CIUDADANIA("Cédula de Ciudadanía"),
    CEDULA_EXTRANJERIA("Cédula de Extranjería"),
    PASAPORTE("Pasaporte"),
    NIT("NIT"),
    TARJETA_IDENTIDAD("Tarjeta de Identidad"),
    REGISTRO_CIVIL("Registro Civil");

    private final String descripcion;

    TipoIdentificacion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
