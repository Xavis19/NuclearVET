package com.nuclearvet.aplicacion.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para respuesta de registro de actividad
 * RF1.5 - Registro de actividad relevante
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistroActividadDTO {

    private Long id;
    private Long usuarioId;
    private String nombreUsuario;
    private String tipoAccion;
    private String descripcion;
    private String ipOrigen;
    private LocalDateTime fechaHora;
}
