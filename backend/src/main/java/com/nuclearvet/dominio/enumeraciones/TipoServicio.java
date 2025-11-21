package com.nuclearvet.dominio.enumeraciones;

/**
 * Tipos de servicio ofrecidos por la clínica veterinaria
 */
public enum TipoServicio {
    CONSULTA("Consulta Veterinaria"),
    VACUNACION("Vacunación"),
    DESPARASITACION("Desparasitación"),
    CIRUGIA("Cirugía"),
    HOSPITALIZACION("Hospitalización"),
    LABORATORIO("Exámenes de Laboratorio"),
    IMAGENOLOGIA("Imagenología"),
    ESTETICA("Estética y Peluquería"),
    URGENCIAS("Atención de Urgencias"),
    CREMACION("Servicio de Cremación"),
    VENTA_PRODUCTO("Venta de Producto"),
    VENTA_MEDICAMENTO("Venta de Medicamento");

    private final String descripcion;

    TipoServicio(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
