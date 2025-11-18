package com.nuclearvet.aplicacion.controladores;

import com.nuclearvet.aplicacion.dtos.ActualizarHistoriaClinicaDTO;
import com.nuclearvet.aplicacion.dtos.HistoriaClinicaDTO;
import com.nuclearvet.aplicacion.servicios.HistoriaClinicaServicio;
import io.swagger.v3.oas.annotations.Operation;
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
import java.util.Map;

/**
 * Controlador REST para gestión de historias clínicas
 */
@RestController
@RequestMapping("/api/historias-clinicas")
@RequiredArgsConstructor
@Tag(name = "Historias Clínicas", description = "Endpoints para gestión de historias clínicas")
@SecurityRequirement(name = "bearer-jwt")
public class HistoriaClinicaControlador {

    private final HistoriaClinicaServicio historiaClinicaServicio;

    /**
     * Crear historia clínica para un paciente
     */
    @PostMapping("/paciente/{pacienteId}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO')")
    @Operation(summary = "Crear historia clínica", description = "Crea una historia clínica para un paciente")
    public ResponseEntity<HistoriaClinicaDTO> crearHistoriaClinica(
            @PathVariable Long pacienteId,
            HttpServletRequest request
    ) {
        HistoriaClinicaDTO historiaClinica = historiaClinicaServicio.crearHistoriaClinica(
                pacienteId, 
                request
        );
        return new ResponseEntity<>(historiaClinica, HttpStatus.CREATED);
    }

    /**
     * Obtener historia clínica por ID
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO', 'ASISTENTE')")
    @Operation(summary = "Obtener historia clínica por ID")
    public ResponseEntity<HistoriaClinicaDTO> obtenerPorId(@PathVariable Long id) {
        HistoriaClinicaDTO historiaClinica = historiaClinicaServicio.obtenerPorId(id);
        return ResponseEntity.ok(historiaClinica);
    }

    /**
     * Obtener historia clínica por número
     */
    @GetMapping("/numero/{numeroHistoria}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO', 'ASISTENTE')")
    @Operation(summary = "Obtener historia clínica por número")
    public ResponseEntity<HistoriaClinicaDTO> obtenerPorNumero(@PathVariable String numeroHistoria) {
        HistoriaClinicaDTO historiaClinica = historiaClinicaServicio.obtenerPorNumero(numeroHistoria);
        return ResponseEntity.ok(historiaClinica);
    }

    /**
     * Obtener historia clínica por paciente
     */
    @GetMapping("/paciente/{pacienteId}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO', 'ASISTENTE')")
    @Operation(summary = "Obtener historia clínica por paciente")
    public ResponseEntity<HistoriaClinicaDTO> obtenerPorPaciente(@PathVariable Long pacienteId) {
        HistoriaClinicaDTO historiaClinica = historiaClinicaServicio.obtenerPorPaciente(pacienteId);
        return ResponseEntity.ok(historiaClinica);
    }

    /**
     * Obtener historia clínica por paciente con consultas
     */
    @GetMapping("/paciente/{pacienteId}/con-consultas")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO', 'ASISTENTE')")
    @Operation(summary = "Obtener historia clínica con consultas")
    public ResponseEntity<HistoriaClinicaDTO> obtenerPorPacienteConConsultas(@PathVariable Long pacienteId) {
        HistoriaClinicaDTO historiaClinica = historiaClinicaServicio.obtenerPorPacienteConConsultas(pacienteId);
        return ResponseEntity.ok(historiaClinica);
    }

    /**
     * Obtener historia clínica por paciente con archivos
     */
    @GetMapping("/paciente/{pacienteId}/con-archivos")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO', 'ASISTENTE')")
    @Operation(summary = "Obtener historia clínica con archivos")
    public ResponseEntity<HistoriaClinicaDTO> obtenerPorPacienteConArchivos(@PathVariable Long pacienteId) {
        HistoriaClinicaDTO historiaClinica = historiaClinicaServicio.obtenerPorPacienteConArchivos(pacienteId);
        return ResponseEntity.ok(historiaClinica);
    }

    /**
     * Listar todas las historias clínicas
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO')")
    @Operation(summary = "Listar todas las historias clínicas")
    public ResponseEntity<List<HistoriaClinicaDTO>> listarTodas() {
        List<HistoriaClinicaDTO> historiasClinicas = historiaClinicaServicio.listarTodas();
        return ResponseEntity.ok(historiasClinicas);
    }

    /**
     * Actualizar historia clínica
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO')")
    @Operation(summary = "Actualizar historia clínica", description = "Actualiza información general de la historia clínica")
    public ResponseEntity<HistoriaClinicaDTO> actualizarHistoriaClinica(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarHistoriaClinicaDTO dto,
            HttpServletRequest request
    ) {
        HistoriaClinicaDTO historiaClinica = historiaClinicaServicio.actualizarHistoriaClinica(
                id, 
                dto, 
                request
        );
        return ResponseEntity.ok(historiaClinica);
    }

    /**
     * Obtener historias clínicas con alergias
     */
    @GetMapping("/con-alergias")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO')")
    @Operation(summary = "Listar historias clínicas con alergias registradas")
    public ResponseEntity<List<HistoriaClinicaDTO>> obtenerHistoriasConAlergias() {
        List<HistoriaClinicaDTO> historiasClinicas = historiaClinicaServicio.obtenerHistoriasConAlergias();
        return ResponseEntity.ok(historiasClinicas);
    }

    /**
     * Obtener historias clínicas con enfermedades crónicas
     */
    @GetMapping("/con-enfermedades-cronicas")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO')")
    @Operation(summary = "Listar historias clínicas con enfermedades crónicas")
    public ResponseEntity<List<HistoriaClinicaDTO>> obtenerHistoriasConEnfermedadesCronicas() {
        List<HistoriaClinicaDTO> historiasClinicas = historiaClinicaServicio.obtenerHistoriasConEnfermedadesCronicas();
        return ResponseEntity.ok(historiasClinicas);
    }

    /**
     * Obtener estadísticas de historias clínicas
     */
    @GetMapping("/estadisticas")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO')")
    @Operation(summary = "Obtener estadísticas generales de historias clínicas")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticas() {
        Map<String, Object> estadisticas = historiaClinicaServicio.obtenerEstadisticas();
        return ResponseEntity.ok(estadisticas);
    }
}
