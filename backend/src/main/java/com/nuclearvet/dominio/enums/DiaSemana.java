package com.nuclearvet.dominio.enums;

/**
 * Días de la semana para disponibilidad
 */
public enum DiaSemana {
    LUNES("Lunes", 1),
    MARTES("Martes", 2),
    MIERCOLES("Miércoles", 3),
    JUEVES("Jueves", 4),
    VIERNES("Viernes", 5),
    SABADO("Sábado", 6),
    DOMINGO("Domingo", 7);

    private final String descripcion;
    private final int numero;

    DiaSemana(String descripcion, int numero) {
        this.descripcion = descripcion;
        this.numero = numero;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getNumero() {
        return numero;
    }
}
