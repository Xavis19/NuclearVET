package com.nuclearvet.dominio.enums;

/**
 * Estados posibles de una consulta médica
 */
public enum EstadoConsulta {
    EN_PROCESO("En proceso"),
    COMPLETADA("Completada"),
    CANCELADA("Cancelada");

    private final String descripcion;

    EstadoConsulta(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
