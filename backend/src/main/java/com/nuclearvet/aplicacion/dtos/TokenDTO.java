package com.nuclearvet.aplicacion.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO para respuesta con token JWT
 * RF1.3 - Inicio de sesión seguro
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenDTO {

    private String token;
    private String tipo = "Bearer";
    private Long usuarioId;
    private String nombreCompleto;
    private String correoElectronico;
    private String rol;
    private List<String> permisos;
    private Long expiracionMs;
}
