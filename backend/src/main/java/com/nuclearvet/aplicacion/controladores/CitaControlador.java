package com.nuclearvet.aplicacion.controladores;

import com.nuclearvet.aplicacion.dtos.CitaDTO;
import com.nuclearvet.aplicacion.dtos.CrearCitaDTO;
import com.nuclearvet.aplicacion.servicios.CitaServicio;
import com.nuclearvet.dominio.enums.EstadoCita;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controlador REST para gestión de citas
 */
@RestController
@RequestMapping("/api/citas")
@RequiredArgsConstructor
@Tag(name = "Citas", description = "Gestión de citas médicas")
@SecurityRequirement(name = "bearerAuth")
public class CitaControlador {

    private final CitaServicio citaServicio;

    /**
     * Crear nueva cita
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('VETERINARIO', 'ASISTENTE', 'CLIENTE')")
    @Operation(summary = "Crear nueva cita", description = "Crea una cita médica para un paciente")
    public ResponseEntity<CitaDTO> crearCita(
            @Valid @RequestBody CrearCitaDTO dto,
            HttpServletRequest request) {
        CitaDTO citaCreada = citaServicio.crearCita(dto, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(citaCreada);
    }

    /**
     * Obtener cita por ID
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('VETERINARIO', 'ASISTENTE', 'CLIENTE')")
    @Operation(summary = "Obtener cita por ID", description = "Consulta los detalles de una cita específica")
    public ResponseEntity<CitaDTO> obtenerCitaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(citaServicio.obtenerCitaPorId(id));
    }

    /**
     * Listar todas las citas
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('VETERINARIO', 'ASISTENTE')")
    @Operation(summary = "Listar todas las citas", description = "Obtiene el listado completo de citas")
    public ResponseEntity<List<CitaDTO>> listarCitas() {
        return ResponseEntity.ok(citaServicio.listarCitas());
    }

    /**
     * Listar citas por estado
     */
    @GetMapping("/estado/{estado}")
    @PreAuthorize("hasAnyRole('VETERINARIO', 'ASISTENTE')")
    @Operation(summary = "Listar citas por estado", description = "Filtra las citas por su estado")
    public ResponseEntity<List<CitaDTO>> listarCitasPorEstado(@PathVariable EstadoCita estado) {
        return ResponseEntity.ok(citaServicio.listarCitasPorEstado(estado));
    }

    /**
     * Listar citas de un paciente
     */
    @GetMapping("/paciente/{pacienteId}")
    @PreAuthorize("hasAnyRole('VETERINARIO', 'ASISTENTE', 'CLIENTE')")
    @Operation(summary = "Listar citas de un paciente", description = "Obtiene todas las citas de un paciente específico")
    public ResponseEntity<List<CitaDTO>> listarCitasPorPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(citaServicio.listarCitasPorPaciente(pacienteId));
    }

    /**
     * Listar citas de un veterinario
     */
    @GetMapping("/veterinario/{veterinarioId}")
    @PreAuthorize("hasAnyRole('VETERINARIO', 'ASISTENTE')")
    @Operation(summary = "Listar citas de un veterinario", description = "Obtiene todas las citas asignadas a un veterinario")
    public ResponseEntity<List<CitaDTO>> listarCitasPorVeterinario(@PathVariable Long veterinarioId) {
        return ResponseEntity.ok(citaServicio.listarCitasPorVeterinario(veterinarioId));
    }

    /**
     * Listar citas por fecha
     */
    @GetMapping("/fecha/{fecha}")
    @PreAuthorize("hasAnyRole('VETERINARIO', 'ASISTENTE')")
    @Operation(summary = "Listar citas por fecha", description = "Obtiene todas las citas de un día específico")
    public ResponseEntity<List<CitaDTO>> listarCitasPorFecha(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return ResponseEntity.ok(citaServicio.listarCitasPorFecha(fecha));
    }

    /**
     * Confirmar cita
     */
    @PatchMapping("/{id}/confirmar")
    @PreAuthorize("hasAnyRole('VETERINARIO', 'ASISTENTE')")
    @Operation(summary = "Confirmar cita", description = "Cambia el estado de una cita a CONFIRMADA")
    public ResponseEntity<CitaDTO> confirmarCita(
            @PathVariable Long id,
            HttpServletRequest request) {
        return ResponseEntity.ok(citaServicio.confirmarCita(id, request));
    }

    /**
     * Cancelar cita
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('VETERINARIO', 'ASISTENTE', 'CLIENTE')")
    @Operation(summary = "Cancelar cita", description = "Cancela una cita existente")
    public ResponseEntity<Void> cancelarCita(
            @PathVariable Long id,
            @RequestParam String motivo,
            HttpServletRequest request) {
        citaServicio.cancelarCita(id, motivo, request);
        return ResponseEntity.noContent().build();
    }
}
