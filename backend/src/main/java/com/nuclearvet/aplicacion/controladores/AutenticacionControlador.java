package com.nuclearvet.aplicacion.controladores;

import com.nuclearvet.aplicacion.dtos.LoginDTO;
import com.nuclearvet.aplicacion.dtos.RecuperarContrasenaDTO;
import com.nuclearvet.aplicacion.dtos.TokenDTO;
import com.nuclearvet.aplicacion.servicios.AutenticacionServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controlador REST para autenticación y seguridad
 * RF1.3 - Inicio de sesión seguro
 * RF1.4 - Recuperación de acceso
 */
@RestController
@RequestMapping("/api/autenticacion")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "Endpoints para autenticación y recuperación de acceso")
public class AutenticacionControlador {

    private final AutenticacionServicio autenticacionServicio;

    @Operation(summary = "Iniciar sesión",
            description = "Autentica un usuario y genera un token JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inicio de sesión exitoso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TokenDTO.class))),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    })
    @PostMapping("/iniciar-sesion")
    public ResponseEntity<TokenDTO> iniciarSesion(
            @Valid @RequestBody LoginDTO loginDTO,
            HttpServletRequest request) {
        TokenDTO token = autenticacionServicio.iniciarSesion(loginDTO, request);
        return ResponseEntity.ok(token);
    }

    @Operation(summary = "Cerrar sesión",
            description = "Cierra la sesión del usuario actual")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/cerrar-sesion")
    public ResponseEntity<Map<String, String>> cerrarSesion(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String correoElectronico = authentication.getName();
        
        autenticacionServicio.cerrarSesion(correoElectronico, request);
        
        return ResponseEntity.ok(Map.of("mensaje", "Sesión cerrada exitosamente"));
    }

    @Operation(summary = "Solicitar recuperación de contraseña",
            description = "Envía un correo con un token para recuperar la contraseña")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Correo enviado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PostMapping("/recuperar-contrasena")
    public ResponseEntity<Map<String, String>> recuperarContrasena(
            @Valid @RequestBody RecuperarContrasenaDTO dto,
            HttpServletRequest request) {
        autenticacionServicio.solicitarRecuperacionContrasena(dto, request);
        return ResponseEntity.ok(Map.of(
                "mensaje", "Si el correo existe, recibirás instrucciones para recuperar tu contraseña"
        ));
    }

    @Operation(summary = "Restablecer contraseña",
            description = "Restablece la contraseña usando el token recibido por correo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contraseña restablecida exitosamente"),
            @ApiResponse(responseCode = "400", description = "Token inválido o expirado")
    })
    @PostMapping("/restablecer-contrasena")
    public ResponseEntity<Map<String, String>> restablecerContrasena(
            @Parameter(description = "Token de recuperación") @RequestParam String token,
            @Parameter(description = "Nueva contraseña") @RequestParam String nuevaContrasena,
            HttpServletRequest request) {
        autenticacionServicio.restablecerContrasena(token, nuevaContrasena, request);
        return ResponseEntity.ok(Map.of("mensaje", "Contraseña restablecida exitosamente"));
    }

    @Operation(summary = "Validar token",
            description = "Verifica si un token JWT es válido")
    @GetMapping("/validar-token")
    public ResponseEntity<Map<String, Boolean>> validarToken(
            @Parameter(description = "Token JWT a validar") @RequestParam String token) {
        boolean valido = autenticacionServicio.validarToken(token);
        return ResponseEntity.ok(Map.of("valido", valido));
    }

    @Operation(summary = "Obtener perfil del usuario actual",
            description = "Retorna la información del usuario autenticado")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/perfil")
    public ResponseEntity<Map<String, String>> obtenerPerfil() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(Map.of(
                "correoElectronico", authentication.getName(),
                "rol", authentication.getAuthorities().toString()
        ));
    }
}
