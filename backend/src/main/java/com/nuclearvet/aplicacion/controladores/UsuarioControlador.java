package com.nuclearvet.aplicacion.controladores;

import com.nuclearvet.aplicacion.dtos.ActualizarUsuarioDTO;
import com.nuclearvet.aplicacion.dtos.CambiarContrasenaDTO;
import com.nuclearvet.aplicacion.dtos.CrearUsuarioDTO;
import com.nuclearvet.aplicacion.dtos.UsuarioDTO;
import com.nuclearvet.aplicacion.servicios.UsuarioServicio;
import com.nuclearvet.dominio.enums.TipoRol;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestión de usuarios
 * RF1.1 - Gestión de usuarios
 */
@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "Endpoints para gestión de usuarios del sistema")
@SecurityRequirement(name = "Bearer Authentication")
public class UsuarioControlador {

    private final UsuarioServicio usuarioServicio;

    @Operation(summary = "Listar todos los usuarios",
            description = "Obtiene la lista completa de usuarios del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioDTO.class)))
    })
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'VETERINARIO', 'ASISTENTE')")
    public ResponseEntity<List<UsuarioDTO>> listarUsuarios() {
        return ResponseEntity.ok(usuarioServicio.listarUsuarios());
    }

    @Operation(summary = "Listar usuarios activos",
            description = "Obtiene la lista de usuarios activos en el sistema")
    @GetMapping("/activos")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'VETERINARIO', 'ASISTENTE')")
    public ResponseEntity<List<UsuarioDTO>> listarUsuariosActivos() {
        return ResponseEntity.ok(usuarioServicio.listarUsuariosActivos());
    }

    @Operation(summary = "Obtener usuario por ID",
            description = "Obtiene la información detallada de un usuario específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'VETERINARIO', 'ASISTENTE')")
    public ResponseEntity<UsuarioDTO> obtenerUsuario(
            @Parameter(description = "ID del usuario") @PathVariable Long id) {
        return ResponseEntity.ok(usuarioServicio.obtenerUsuarioPorId(id));
    }

    @Operation(summary = "Buscar usuarios por nombre",
            description = "Busca usuarios que coincidan con el nombre proporcionado")
    @GetMapping("/buscar")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'VETERINARIO', 'ASISTENTE')")
    public ResponseEntity<List<UsuarioDTO>> buscarUsuarios(
            @Parameter(description = "Nombre a buscar") @RequestParam String nombre) {
        return ResponseEntity.ok(usuarioServicio.buscarUsuariosPorNombre(nombre));
    }

    @Operation(summary = "Listar usuarios por rol",
            description = "Obtiene la lista de usuarios que tienen un rol específico")
    @GetMapping("/rol/{rol}")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'VETERINARIO')")
    public ResponseEntity<List<UsuarioDTO>> listarUsuariosPorRol(
            @Parameter(description = "Tipo de rol") @PathVariable TipoRol rol) {
        return ResponseEntity.ok(usuarioServicio.listarUsuariosPorRol(rol.name()));
    }

    @Operation(summary = "Crear nuevo usuario",
            description = "Registra un nuevo usuario en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "409", description = "El usuario ya existe")
    })
    @PostMapping
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    public ResponseEntity<UsuarioDTO> crearUsuario(
            @Valid @RequestBody CrearUsuarioDTO crearUsuarioDTO,
            HttpServletRequest request) {
        UsuarioDTO usuarioCreado = usuarioServicio.crearUsuario(crearUsuarioDTO, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioCreado);
    }

    @Operation(summary = "Actualizar usuario",
            description = "Actualiza la información de un usuario existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'VETERINARIO')")
    public ResponseEntity<UsuarioDTO> actualizarUsuario(
            @Parameter(description = "ID del usuario") @PathVariable Long id,
            @Valid @RequestBody ActualizarUsuarioDTO actualizarUsuarioDTO,
            HttpServletRequest request) {
        return ResponseEntity.ok(usuarioServicio.actualizarUsuario(id, actualizarUsuarioDTO, request));
    }

    @Operation(summary = "Desactivar usuario",
            description = "Desactiva un usuario (soft delete)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuario desactivado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    public ResponseEntity<Void> desactivarUsuario(
            @Parameter(description = "ID del usuario") @PathVariable Long id,
            HttpServletRequest request) {
        usuarioServicio.desactivarUsuario(id, request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Activar usuario",
            description = "Reactiva un usuario previamente desactivado")
    @PatchMapping("/{id}/activar")
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    public ResponseEntity<UsuarioDTO> activarUsuario(
            @Parameter(description = "ID del usuario") @PathVariable Long id,
            HttpServletRequest request) {
        return ResponseEntity.ok(usuarioServicio.activarUsuario(id, request));
    }

    @Operation(summary = "Cambiar contraseña",
            description = "Permite a un usuario cambiar su contraseña")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contraseña cambiada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Contraseña actual incorrecta"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PutMapping("/{id}/cambiar-contrasena")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'VETERINARIO', 'ASISTENTE', 'CLIENTE')")
    public ResponseEntity<Void> cambiarContrasena(
            @Parameter(description = "ID del usuario") @PathVariable Long id,
            @Valid @RequestBody CambiarContrasenaDTO cambiarContrasenaDTO,
            HttpServletRequest request) {
        usuarioServicio.cambiarContrasena(id, cambiarContrasenaDTO, request);
        return ResponseEntity.ok().build();
    }
}
