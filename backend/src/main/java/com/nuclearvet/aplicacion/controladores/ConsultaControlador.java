package com.nuclearvet.aplicacion.controladores;

import com.nuclearvet.aplicacion.dtos.ConsultaDTO;
import com.nuclearvet.aplicacion.dtos.CrearConsultaDTO;
import com.nuclearvet.aplicacion.servicios.ConsultaServicio;
import com.nuclearvet.dominio.enums.EstadoConsulta;
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

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controlador REST para gestión de consultas médicas
 */
@RestController
@RequestMapping("/api/consultas")
@RequiredArgsConstructor
@Tag(name = "Consultas", description = "Endpoints para gestión de consultas médicas")
@SecurityRequirement(name = "bearer-jwt")
public class ConsultaControlador {

    private final ConsultaServicio consultaServicio;

    /**
     * Crear nueva consulta
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO')")
    @Operation(summary = "Crear consulta", description = "Crea una nueva consulta médica")
    public ResponseEntity<ConsultaDTO> crearConsulta(
            @Valid @RequestBody CrearConsultaDTO dto,
            HttpServletRequest request
    ) {
        ConsultaDTO consulta = consultaServicio.crearConsulta(dto, request);
        return new ResponseEntity<>(consulta, HttpStatus.CREATED);
    }

    /**
     * Obtener consulta por ID
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO', 'ASISTENTE')")
    @Operation(summary = "Obtener consulta por ID")
    public ResponseEntity<ConsultaDTO> obtenerPorId(@PathVariable Long id) {
        ConsultaDTO consulta = consultaServicio.obtenerPorId(id);
        return ResponseEntity.ok(consulta);
    }

    /**
     * Obtener consulta por número
     */
    @GetMapping("/numero/{numeroConsulta}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO', 'ASISTENTE')")
    @Operation(summary = "Obtener consulta por número")
    public ResponseEntity<ConsultaDTO> obtenerPorNumero(@PathVariable String numeroConsulta) {
        ConsultaDTO consulta = consultaServicio.obtenerPorNumero(numeroConsulta);
        return ResponseEntity.ok(consulta);
    }

    /**
     * Listar consultas por historia clínica
     */
    @GetMapping("/historia/{historiaClinicaId}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO', 'ASISTENTE')")
    @Operation(summary = "Listar consultas por historia clínica")
    public ResponseEntity<List<ConsultaDTO>> listarPorHistoriaClinica(@PathVariable Long historiaClinicaId) {
        List<ConsultaDTO> consultas = consultaServicio.listarPorHistoriaClinica(historiaClinicaId);
        return ResponseEntity.ok(consultas);
    }

    /**
     * Listar consultas por paciente
     */
    @GetMapping("/paciente/{pacienteId}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO', 'ASISTENTE')")
    @Operation(summary = "Listar consultas por paciente")
    public ResponseEntity<List<ConsultaDTO>> listarPorPaciente(@PathVariable Long pacienteId) {
        List<ConsultaDTO> consultas = consultaServicio.listarPorPaciente(pacienteId);
        return ResponseEntity.ok(consultas);
    }

    /**
     * Listar consultas por veterinario
     */
    @GetMapping("/veterinario/{veterinarioId}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO')")
    @Operation(summary = "Listar consultas por veterinario")
    public ResponseEntity<List<ConsultaDTO>> listarPorVeterinario(@PathVariable Long veterinarioId) {
        List<ConsultaDTO> consultas = consultaServicio.listarPorVeterinario(veterinarioId);
        return ResponseEntity.ok(consultas);
    }

    /**
     * Listar consultas del día actual para un veterinario
     */
    @GetMapping("/veterinario/{veterinarioId}/hoy")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO')")
    @Operation(summary = "Listar consultas del día por veterinario")
    public ResponseEntity<List<ConsultaDTO>> listarConsultasDelDia(@PathVariable Long veterinarioId) {
        List<ConsultaDTO> consultas = consultaServicio.listarConsultasDelDia(veterinarioId);
        return ResponseEntity.ok(consultas);
    }

    /**
     * Listar consultas por estado
     */
    @GetMapping("/estado/{estado}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO')")
    @Operation(summary = "Listar consultas por estado")
    public ResponseEntity<List<ConsultaDTO>> listarPorEstado(@PathVariable EstadoConsulta estado) {
        List<ConsultaDTO> consultas = consultaServicio.listarPorEstado(estado);
        return ResponseEntity.ok(consultas);
    }

    /**
     * Listar consultas por rango de fechas
     */
    @GetMapping("/rango-fechas")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO')")
    @Operation(summary = "Listar consultas por rango de fechas")
    public ResponseEntity<List<ConsultaDTO>> listarPorRangoFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin
    ) {
        List<ConsultaDTO> consultas = consultaServicio.listarPorRangoFechas(fechaInicio, fechaFin);
        return ResponseEntity.ok(consultas);
    }

    /**
     * Listar consultas recientes (últimos 30 días)
     */
    @GetMapping("/recientes")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO')")
    @Operation(summary = "Listar consultas recientes", description = "Obtiene consultas de los últimos 30 días")
    public ResponseEntity<List<ConsultaDTO>> listarConsultasRecientes() {
        List<ConsultaDTO> consultas = consultaServicio.listarConsultasRecientes();
        return ResponseEntity.ok(consultas);
    }

    /**
     * Completar consulta
     */
    @PatchMapping("/{id}/completar")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO')")
    @Operation(summary = "Completar consulta", description = "Marca una consulta como completada")
    public ResponseEntity<ConsultaDTO> completarConsulta(
            @PathVariable Long id,
            HttpServletRequest request
    ) {
        ConsultaDTO consulta = consultaServicio.completarConsulta(id, request);
        return ResponseEntity.ok(consulta);
    }

    /**
     * Cancelar consulta
     */
    @PatchMapping("/{id}/cancelar")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO')")
    @Operation(summary = "Cancelar consulta", description = "Cancela una consulta con motivo")
    public ResponseEntity<ConsultaDTO> cancelarConsulta(
            @PathVariable Long id,
            @RequestParam String motivo,
            HttpServletRequest request
    ) {
        ConsultaDTO consulta = consultaServicio.cancelarConsulta(id, motivo, request);
        return ResponseEntity.ok(consulta);
    }

    /**
     * Obtener consulta por cita
     */
    @GetMapping("/cita/{citaId}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO', 'ASISTENTE')")
    @Operation(summary = "Obtener consulta asociada a una cita")
    public ResponseEntity<ConsultaDTO> obtenerPorCita(@PathVariable Long citaId) {
        ConsultaDTO consulta = consultaServicio.obtenerPorCita(citaId);
        return ResponseEntity.ok(consulta);
    }
}
