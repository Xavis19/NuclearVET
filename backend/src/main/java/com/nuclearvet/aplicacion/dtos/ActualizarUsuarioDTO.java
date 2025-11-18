package com.nuclearvet.aplicacion.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para actualizar información de un usuario
 * RF1.1 - Gestión de usuarios
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActualizarUsuarioDTO {

    @Size(min = 3, max = 255, message = "El nombre debe tener entre 3 y 255 caracteres")
    private String nombreCompleto;

    @Email(message = "El correo electrónico debe ser válido")
    private String correoElectronico;

    @Size(max = 20, message = "El teléfono no debe exceder 20 caracteres")
    private String telefono;

    @Size(max = 500, message = "La dirección no debe exceder 500 caracteres")
    private String direccion;

    @Size(max = 100, message = "La ciudad no debe exceder 100 caracteres")
    private String ciudad;

    @Size(max = 100, message = "El departamento no debe exceder 100 caracteres")
    private String departamento;

    private Boolean activo;

    private Long rolId;
}
