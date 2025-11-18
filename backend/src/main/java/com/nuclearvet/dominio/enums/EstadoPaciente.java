package com.nuclearvet.dominio.enums;

/**
 * Estado del paciente en la clínica
 */
public enum EstadoPaciente {
    ACTIVO("Activo"),
    INACTIVO("Inactivo"),
    FALLECIDO("Fallecido"),
    EN_TRATAMIENTO("En Tratamiento"),
    EN_OBSERVACION("En Observación");

    private final String descripcion;

    EstadoPaciente(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
