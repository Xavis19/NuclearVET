package com.nuclearvet.aplicacion.controladores;

import com.nuclearvet.aplicacion.dtos.ActualizarPropietarioDTO;
import com.nuclearvet.aplicacion.dtos.CrearPropietarioDTO;
import com.nuclearvet.aplicacion.dtos.PropietarioDTO;
import com.nuclearvet.aplicacion.servicios.PropietarioServicio;
import com.nuclearvet.dominio.enums.TipoIdentificacion;
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
 * Controlador REST para gestión de propietarios
 * RF2.1 - Registro y gestión de propietarios
 */
@RestController
@RequestMapping("/api/propietarios")
@RequiredArgsConstructor
@Tag(name = "Propietarios", description = "Endpoints para gestión de propietarios de mascotas")
@SecurityRequirement(name = "Bearer Authentication")
public class PropietarioControlador {

    private final PropietarioServicio propietarioServicio;

    @Operation(summary = "Listar todos los propietarios",
            description = "Obtiene la lista completa de propietarios registrados")
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'VETERINARIO', 'ASISTENTE')")
    public ResponseEntity<List<PropietarioDTO>> listarPropietarios() {
        return ResponseEntity.ok(propietarioServicio.listarPropietarios());
    }

    @Operation(summary = "Listar propietarios activos")
    @GetMapping("/activos")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'VETERINARIO', 'ASISTENTE')")
    public ResponseEntity<List<PropietarioDTO>> listarPropietariosActivos() {
        return ResponseEntity.ok(propietarioServicio.listarPropietariosActivos());
    }

    @Operation(summary = "Obtener propietario por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Propietario encontrado"),
            @ApiResponse(responseCode = "404", description = "Propietario no encontrado")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'VETERINARIO', 'ASISTENTE', 'CLIENTE')")
    public ResponseEntity<PropietarioDTO> obtenerPropietario(
            @Parameter(description = "ID del propietario") @PathVariable Long id) {
        return ResponseEntity.ok(propietarioServicio.obtenerPropietarioPorId(id));
    }

    @Operation(summary = "Obtener propietario por número de identificación")
    @GetMapping("/identificacion/{numeroIdentificacion}")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'VETERINARIO', 'ASISTENTE')")
    public ResponseEntity<PropietarioDTO> obtenerPorIdentificacion(
            @Parameter(description = "Número de identificación") @PathVariable String numeroIdentificacion) {
        return ResponseEntity.ok(propietarioServicio.obtenerPropietarioPorIdentificacion(numeroIdentificacion));
    }

    @Operation(summary = "Buscar propietarios por nombre o apellido")
    @GetMapping("/buscar")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'VETERINARIO', 'ASISTENTE')")
    public ResponseEntity<List<PropietarioDTO>> buscarPropietarios(
            @Parameter(description = "Término de búsqueda") @RequestParam String termino) {
        return ResponseEntity.ok(propietarioServicio.buscarPropietariosPorNombre(termino));
    }

    @Operation(summary = "Buscar propietarios por ciudad")
    @GetMapping("/ciudad/{ciudad}")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'VETERINARIO', 'ASISTENTE')")
    public ResponseEntity<List<PropietarioDTO>> buscarPorCiudad(
            @Parameter(description = "Ciudad") @PathVariable String ciudad) {
        return ResponseEntity.ok(propietarioServicio.buscarPropietariosPorCiudad(ciudad));
    }

    @Operation(summary = "Listar propietarios por tipo de identificación")
    @GetMapping("/tipo-identificacion/{tipo}")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'VETERINARIO', 'ASISTENTE')")
    public ResponseEntity<List<PropietarioDTO>> listarPorTipoIdentificacion(
            @Parameter(description = "Tipo de identificación") @PathVariable TipoIdentificacion tipo) {
        return ResponseEntity.ok(propietarioServicio.listarPropietariosPorTipoIdentificacion(tipo));
    }

    @Operation(summary = "Listar propietarios con pacientes")
    @GetMapping("/con-pacientes")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'VETERINARIO', 'ASISTENTE')")
    public ResponseEntity<List<PropietarioDTO>> listarPropietariosConPacientes() {
        return ResponseEntity.ok(propietarioServicio.listarPropietariosConPacientes());
    }

    @Operation(summary = "Crear nuevo propietario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Propietario creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "409", description = "El propietario ya existe")
    })
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'VETERINARIO', 'ASISTENTE')")
    public ResponseEntity<PropietarioDTO> crearPropietario(
            @Valid @RequestBody CrearPropietarioDTO dto,
            HttpServletRequest request) {
        PropietarioDTO propietario = propietarioServicio.crearPropietario(dto, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(propietario);
    }

    @Operation(summary = "Actualizar propietario")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'VETERINARIO', 'ASISTENTE')")
    public ResponseEntity<PropietarioDTO> actualizarPropietario(
            @Parameter(description = "ID del propietario") @PathVariable Long id,
            @Valid @RequestBody ActualizarPropietarioDTO dto,
            HttpServletRequest request) {
        return ResponseEntity.ok(propietarioServicio.actualizarPropietario(id, dto, request));
    }

    @Operation(summary = "Desactivar propietario")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    public ResponseEntity<Void> desactivarPropietario(
            @Parameter(description = "ID del propietario") @PathVariable Long id,
            HttpServletRequest request) {
        propietarioServicio.desactivarPropietario(id, request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Activar propietario")
    @PatchMapping("/{id}/activar")
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    public ResponseEntity<PropietarioDTO> activarPropietario(
            @Parameter(description = "ID del propietario") @PathVariable Long id,
            HttpServletRequest request) {
        return ResponseEntity.ok(propietarioServicio.activarPropietario(id, request));
    }

    @Operation(summary = "Contar propietarios activos")
    @GetMapping("/contar/activos")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'VETERINARIO')")
    public ResponseEntity<Long> contarPropietariosActivos() {
        return ResponseEntity.ok(propietarioServicio.contarPropietariosActivos());
    }
}
