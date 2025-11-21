package com.nuclearvet.dominio.enumeraciones;

/**
 * Tipos de impuesto aplicables en Colombia
 */
public enum TipoImpuesto {
    IVA_0("IVA 0%", 0.0),
    IVA_5("IVA 5%", 5.0),
    IVA_19("IVA 19%", 19.0),
    EXCLUIDO("Excluido de IVA", 0.0),
    EXENTO("Exento de IVA", 0.0);

    private final String descripcion;
    private final Double porcentaje;

    TipoImpuesto(String descripcion, Double porcentaje) {
        this.descripcion = descripcion;
        this.porcentaje = porcentaje;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Double getPorcentaje() {
        return porcentaje;
    }
}
