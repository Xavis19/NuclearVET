package com.nuclearvet.dominio.enums;

/**
 * Tipos de acciones registradas en auditoría
 */
public enum TipoAccion {
    // Acciones de autenticación
    INICIO_SESION,
    CIERRE_SESION,
    INTENTO_ACCESO_FALLIDO,
    
    // Acciones de usuarios
    CREAR_USUARIO,
    ACTUALIZAR_USUARIO,
    DESACTIVAR_USUARIO,
    ACTIVAR_USUARIO,
    CAMBIAR_CONTRASENA,
    RECUPERAR_CONTRASENA,
    
    // Acciones de pacientes
    CREAR_PACIENTE,
    ACTUALIZAR_PACIENTE,
    ELIMINAR_PACIENTE,
    
    // Acciones de historias clínicas
    CREAR_HISTORIA_CLINICA,
    ACTUALIZAR_HISTORIA_CLINICA,
    CREAR_CONSULTA,
    ACTUALIZAR_CONSULTA,
    COMPLETAR_CONSULTA,
    CANCELAR_CONSULTA,
    SUBIR_ARCHIVO_MEDICO,
    ELIMINAR_ARCHIVO_MEDICO,
    
    // Acciones de citas
    CREAR_CITA,
    ACTUALIZAR_CITA,
    CANCELAR_CITA,
    CONFIRMAR_CITA,
    
    // Acciones de inventario
    AGREGAR_PRODUCTO,
    ACTUALIZAR_PRODUCTO,
    ELIMINAR_PRODUCTO,
    ENTRADA_INVENTARIO,
    SALIDA_INVENTARIO,
    AJUSTE_INVENTARIO,
    
    // Acciones administrativas
    GENERAR_FACTURA,
    ANULAR_FACTURA,
    REGISTRAR_PAGO,
    GENERAR_REPORTE,
    MODIFICAR_CONFIGURACION
}
