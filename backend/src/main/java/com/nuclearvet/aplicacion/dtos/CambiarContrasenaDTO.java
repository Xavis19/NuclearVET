package com.nuclearvet.aplicacion.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para cambiar contraseña
 * RF1.4 - Recuperación de acceso
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CambiarContrasenaDTO {

    @NotBlank(message = "La contraseña actual es obligatoria")
    private String contrasenaActual;

    @NotBlank(message = "La nueva contraseña es obligatoria")
    @Size(min = 8, max = 100, message = "La nueva contraseña debe tener entre 8 y 100 caracteres")
    private String contrasenaNueva;

    @NotBlank(message = "La confirmación de contraseña es obligatoria")
    private String confirmacionContrasena;
}
