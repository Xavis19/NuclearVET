package com.nuclearvet.aplicacion.dto.notificaciones;

import com.nuclearvet.dominio.enumeraciones.PrioridadNotificacion;
import com.nuclearvet.dominio.enumeraciones.TipoNotificacion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para respuesta de notificación
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificacionDTO {

    private Long id;
    private Long usuarioDestinatarioId;
    private String usuarioDestinatarioNombre;
    private TipoNotificacion tipoNotificacion;
    private String titulo;
    private String mensaje;
    private PrioridadNotificacion nivelPrioridad;
    private Boolean leida;
    private LocalDateTime fechaLeida;
    private String enlace;
    private LocalDateTime fechaCreacion;
}
