package com.nuclearvet.dominio.enumeraciones;

/**
 * Tipos de productos disponibles en el inventario veterinario
 */
public enum TipoProducto {
    MEDICAMENTO("Medicamento"),
    VACUNA("Vacuna"),
    SUPLEMENTO("Suplemento"),
    ALIMENTO("Alimento"),
    ACCESORIO("Accesorio"),
    MATERIAL_QUIRURGICO("Material Quirúrgico"),
    MATERIAL_DIAGNOSTICO("Material de Diagnóstico"),
    HIGIENE("Producto de Higiene"),
    OTRO("Otro");

    private final String descripcion;

    TipoProducto(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
