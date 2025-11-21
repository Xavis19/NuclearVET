package com.nuclearvet.dominio.enumeraciones;

/**
 * Unidades de medida para productos del inventario
 */
public enum UnidadMedida {
    UNIDAD("Unidad"),
    CAJA("Caja"),
    FRASCO("Frasco"),
    AMPOLLA("Ampolla"),
    TABLETA("Tableta"),
    CAPSULA("Cápsula"),
    SOBRE("Sobre"),
    KILOGRAMO("Kilogramo"),
    GRAMO("Gramo"),
    LITRO("Litro"),
    MILILITRO("Mililitro"),
    METRO("Metro"),
    CENTIMETRO("Centímetro");

    private final String descripcion;

    UnidadMedida(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
