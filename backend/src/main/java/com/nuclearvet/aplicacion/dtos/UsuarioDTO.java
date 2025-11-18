package com.nuclearvet.aplicacion.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para respuesta de información de usuario
 * No incluye información sensible como contraseña
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {

    private Long id;
    private String nombreCompleto;
    private String numeroDocumento;
    private String tipoDocumento;
    private String correoElectronico;
    private String telefono;
    private String direccion;
    private String ciudad;
    private String departamento;
    private Boolean activo;
    private String rolNombre;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}
