package com.nuclearvet.dominio.enumeraciones;

/**
 * Tipos de recordatorios programables
 */
public enum TipoRecordatorio {
    CITA("Recordatorio de cita"),
    VACUNA("Recordatorio de vacunación"),
    DESPARASITACION("Recordatorio de desparasitación"),
    CONTROL("Recordatorio de control médico"),
    MEDICAMENTO("Recordatorio de medicamento"),
    BANO("Recordatorio de baño/aseo"),
    REVISION("Recordatorio de revisión general");

    private final String descripcion;

    TipoRecordatorio(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
