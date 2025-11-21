package com.nuclearvet.dominio.enumeraciones;

/**
 * Estados posibles de un lote de productos
 */
public enum EstadoLote {
    DISPONIBLE("Disponible"),
    PROXIMO_VENCER("Próximo a Vencer"),
    VENCIDO("Vencido"),
    AGOTADO("Agotado"),
    BLOQUEADO("Bloqueado");

    private final String descripcion;

    EstadoLote(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
