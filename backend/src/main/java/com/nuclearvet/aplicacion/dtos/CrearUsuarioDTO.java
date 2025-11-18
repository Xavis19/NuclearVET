package com.nuclearvet.aplicacion.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para crear un nuevo usuario
 * RF1.1 - Gestión de usuarios
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrearUsuarioDTO {

    @NotBlank(message = "El nombre completo es obligatorio")
    @Size(min = 3, max = 255, message = "El nombre debe tener entre 3 y 255 caracteres")
    private String nombreCompleto;

    @NotBlank(message = "El número de documento es obligatorio")
    @Size(min = 5, max = 20, message = "El número de documento debe tener entre 5 y 20 caracteres")
    private String numeroDocumento;

    @NotBlank(message = "El tipo de documento es obligatorio")
    private String tipoDocumento; // CC, CE, TI, PASAPORTE

    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El correo electrónico debe ser válido")
    private String correoElectronico;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, max = 100, message = "La contraseña debe tener entre 8 y 100 caracteres")
    private String contrasena;

    @Size(max = 20, message = "El teléfono no debe exceder 20 caracteres")
    private String telefono;

    @Size(max = 500, message = "La dirección no debe exceder 500 caracteres")
    private String direccion;

    @Size(max = 100, message = "La ciudad no debe exceder 100 caracteres")
    private String ciudad;

    @Size(max = 100, message = "El departamento no debe exceder 100 caracteres")
    private String departamento;

    @NotNull(message = "El rol es obligatorio")
    private Long rolId;
}
