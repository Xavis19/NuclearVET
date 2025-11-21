package com.nuclearvet.aplicacion.dto.notificaciones;

import com.nuclearvet.dominio.enumeraciones.PrioridadNotificacion;
import com.nuclearvet.dominio.enumeraciones.TipoNotificacion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para crear notificación
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrearNotificacionDTO {

    @NotNull(message = "El ID del usuario destinatario es obligatorio")
    private Long usuarioDestinatarioId;

    @NotNull(message = "El tipo de notificación es obligatorio")
    private TipoNotificacion tipoNotificacion;

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 200, message = "El título no puede exceder 200 caracteres")
    private String titulo;

    @NotBlank(message = "El mensaje es obligatorio")
    private String mensaje;

    @Builder.Default
    private PrioridadNotificacion nivelPrioridad = PrioridadNotificacion.NORMAL;

    @Size(max = 500, message = "El enlace no puede exceder 500 caracteres")
    private String enlace;
}
