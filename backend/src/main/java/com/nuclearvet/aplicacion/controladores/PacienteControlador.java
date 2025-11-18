package com.nuclearvet.aplicacion.controladores;

import com.nuclearvet.aplicacion.dtos.ActualizarPacienteDTO;
import com.nuclearvet.aplicacion.dtos.CrearPacienteDTO;
import com.nuclearvet.aplicacion.dtos.PacienteDTO;
import com.nuclearvet.aplicacion.servicios.PacienteServicio;
import com.nuclearvet.dominio.enums.Especie;
import com.nuclearvet.dominio.enums.EstadoPaciente;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
 * Controlador REST para gestión de pacientes
 * RF2.2 - Registro y gestión de pacientes
 */
@RestController
@RequestMapping("/api/pacientes")
@RequiredArgsConstructor
@Tag(name = "Pacientes", description = "Endpoints para gestión de pacientes (mascotas)")
@SecurityRequirement(name = "Bearer Authentication")
public class PacienteControlador {

    private final PacienteServicio pacienteServicio;

    @Operation(summary = "Listar todos los pacientes")
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'VETERINARIO', 'ASISTENTE')")
    public ResponseEntity<List<PacienteDTO>> listarPacientes() {
        return ResponseEntity.ok(pacienteServicio.listarPacientes());
    }

    @Operation(summary = "Listar pacientes activos")
    @GetMapping("/activos")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'VETERINARIO', 'ASISTENTE')")
    public ResponseEntity<List<PacienteDTO>> listarPacientesActivos() {
        return ResponseEntity.ok(pacienteServicio.listarPacientesActivos());
    }

    @Operation(summary = "Obtener paciente por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente encontrado"),
            @ApiResponse(responseCode = "404", description = "Paciente no encontrado")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'VETERINARIO', 'ASISTENTE', 'CLIENTE')")
    public ResponseEntity<PacienteDTO> obtenerPaciente(
            @Parameter(description = "ID del paciente") @PathVariable Long id) {
        return ResponseEntity.ok(pacienteServicio.obtenerPacientePorId(id));
    }

    @Operation(summary = "Obtener paciente por código")
    @GetMapping("/codigo/{codigo}")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'VETERINARIO', 'ASISTENTE')")
    public ResponseEntity<PacienteDTO> obtenerPorCodigo(
            @Parameter(description = "Código del paciente") @PathVariable String codigo) {
        return ResponseEntity.ok(pacienteServicio.obtenerPacientePorCodigo(codigo));
    }

    @Operation(summary = "Buscar pacientes por nombre")
    @GetMapping("/buscar")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'VETERINARIO', 'ASISTENTE')")
    public ResponseEntity<List<PacienteDTO>> buscarPacientes(
            @Parameter(description = "Nombre del paciente") @RequestParam String nombre) {
        return ResponseEntity.ok(pacienteServicio.buscarPacientesPorNombre(nombre));
    }

    @Operation(summary = "Listar pacientes por propietario")
    @GetMapping("/propietario/{propietarioId}")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'VETERINARIO', 'ASISTENTE', 'CLIENTE')")
    public ResponseEntity<List<PacienteDTO>> listarPorPropietario(
            @Parameter(description = "ID del propietario") @PathVariable Long propietarioId) {
        return ResponseEntity.ok(pacienteServicio.listarPacientesPorPropietario(propietarioId));
    }

    @Operation(summary = "Listar pacientes por especie")
    @GetMapping("/especie/{especie}")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'VETERINARIO', 'ASISTENTE')")
    public ResponseEntity<List<PacienteDTO>> listarPorEspecie(
            @Parameter(description = "Especie del paciente") @PathVariable Especie especie) {
        return ResponseEntity.ok(pacienteServicio.listarPacientesPorEspecie(especie));
    }

    @Operation(summary = "Listar pacientes por estado")
    @GetMapping("/estado/{estado}")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'VETERINARIO', 'ASISTENTE')")
    public ResponseEntity<List<PacienteDTO>> listarPorEstado(
            @Parameter(description = "Estado del paciente") @PathVariable EstadoPaciente estado) {
        return ResponseEntity.ok(pacienteServicio.listarPacientesPorEstado(estado));
    }

    @Operation(summary = "Listar pacientes por veterinario asignado")
    @GetMapping("/veterinario/{veterinarioId}")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'VETERINARIO', 'ASISTENTE')")
    public ResponseEntity<List<PacienteDTO>> listarPorVeterinario(
            @Parameter(description = "ID del veterinario") @PathVariable Long veterinarioId) {
        return ResponseEntity.ok(pacienteServicio.listarPacientesPorVeterinario(veterinarioId));
    }

    @Operation(summary = "Listar pacientes en atención")
    @GetMapping("/en-atencion")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'VETERINARIO', 'ASISTENTE')")
    public ResponseEntity<List<PacienteDTO>> listarPacientesEnAtencion() {
        return ResponseEntity.ok(pacienteServicio.listarPacientesEnAtencion());
    }

    @Operation(summary = "Crear nuevo paciente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Paciente creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Propietario no encontrado")
    })
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'VETERINARIO', 'ASISTENTE')")
    public ResponseEntity<PacienteDTO> crearPaciente(
            @Valid @RequestBody CrearPacienteDTO dto,
            HttpServletRequest request) {
        PacienteDTO paciente = pacienteServicio.crearPaciente(dto, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(paciente);
    }

    @Operation(summary = "Actualizar paciente")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'VETERINARIO', 'ASISTENTE')")
    public ResponseEntity<PacienteDTO> actualizarPaciente(
            @Parameter(description = "ID del paciente") @PathVariable Long id,
            @Valid @RequestBody ActualizarPacienteDTO dto,
            HttpServletRequest request) {
        return ResponseEntity.ok(pacienteServicio.actualizarPaciente(id, dto, request));
    }

    @Operation(summary = "Cambiar estado del paciente")
    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'VETERINARIO')")
    public ResponseEntity<PacienteDTO> cambiarEstado(
            @Parameter(description = "ID del paciente") @PathVariable Long id,
            @Parameter(description = "Nuevo estado") @RequestParam EstadoPaciente estado,
            HttpServletRequest request) {
        return ResponseEntity.ok(pacienteServicio.cambiarEstadoPaciente(id, estado, request));
    }

    @Operation(summary = "Eliminar paciente permanentemente")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    public ResponseEntity<Void> eliminarPaciente(
            @Parameter(description = "ID del paciente") @PathVariable Long id) {
        pacienteServicio.eliminarPaciente(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Contar pacientes activos")
    @GetMapping("/contar/activos")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'VETERINARIO')")
    public ResponseEntity<Long> contarPacientesActivos() {
        return ResponseEntity.ok(pacienteServicio.contarPacientesActivos());
    }

    @Operation(summary = "Contar pacientes por especie")
    @GetMapping("/contar/especie/{especie}")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'VETERINARIO')")
    public ResponseEntity<Long> contarPorEspecie(
            @Parameter(description = "Especie") @PathVariable Especie especie) {
        return ResponseEntity.ok(pacienteServicio.contarPacientesPorEspecie(especie));
    }
}
