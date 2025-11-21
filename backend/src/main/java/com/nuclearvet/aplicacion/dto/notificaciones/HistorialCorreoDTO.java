package com.nuclearvet.aplicacion.dto.notificaciones;

import com.nuclearvet.dominio.enumeraciones.EstadoCorreo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para respuesta de historial de correo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistorialCorreoDTO {

    private Long id;
    private String destinatarioEmail;
    private String asunto;
    private String contenido;
    private Long plantillaId;
    private String plantillaNombre;
    private EstadoCorreo estado;
    private String errorMensaje;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaEnvio;
}
