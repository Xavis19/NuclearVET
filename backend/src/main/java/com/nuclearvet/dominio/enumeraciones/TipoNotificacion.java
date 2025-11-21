package com.nuclearvet.dominio.enumeraciones;

/**
 * Tipos de notificaciones en el sistema
 */
public enum TipoNotificacion {
    CITA("Cita médica"),
    INVENTARIO("Alerta de inventario"),
    URGENCIA("Urgencia médica"),
    SISTEMA("Notificación del sistema"),
    RECORDATORIO("Recordatorio general"),
    PAGO("Notificación de pago"),
    RESULTADO("Resultado de análisis");

    private final String descripcion;

    TipoNotificacion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
