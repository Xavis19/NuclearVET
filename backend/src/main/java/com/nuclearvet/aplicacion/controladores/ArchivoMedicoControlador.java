package com.nuclearvet.aplicacion.controladores;

import com.nuclearvet.aplicacion.dtos.ArchivoMedicoDTO;
import com.nuclearvet.aplicacion.servicios.ArchivoMedicoServicio;
import com.nuclearvet.dominio.enums.TipoArchivo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Controlador REST para gestión de archivos médicos
 */
@RestController
@RequestMapping("/api/archivos-medicos")
@RequiredArgsConstructor
@Tag(name = "Archivos Médicos", description = "Endpoints para gestión de archivos médicos")
@SecurityRequirement(name = "bearer-jwt")
public class ArchivoMedicoControlador {

    private final ArchivoMedicoServicio archivoMedicoServicio;

    /**
     * Subir archivo médico
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO', 'ASISTENTE')")
    @Operation(summary = "Subir archivo médico", description = "Sube un archivo médico asociado a una historia clínica")
    public ResponseEntity<ArchivoMedicoDTO> subirArchivo(
            @RequestParam("archivo") MultipartFile archivo,
            @RequestParam("tipoArchivo") TipoArchivo tipoArchivo,
            @RequestParam("historiaClinicaId") Long historiaClinicaId,
            @RequestParam(value = "consultaId", required = false) Long consultaId,
            @RequestParam(value = "descripcion", required = false) String descripcion,
            HttpServletRequest request
    ) {
        ArchivoMedicoDTO archivoMedico = archivoMedicoServicio.subirArchivo(
                archivo,
                tipoArchivo,
                historiaClinicaId,
                consultaId,
                descripcion,
                request
        );
        return new ResponseEntity<>(archivoMedico, HttpStatus.CREATED);
    }

    /**
     * Obtener archivo por ID
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO', 'ASISTENTE')")
    @Operation(summary = "Obtener información de archivo por ID")
    public ResponseEntity<ArchivoMedicoDTO> obtenerPorId(@PathVariable Long id) {
        ArchivoMedicoDTO archivoMedico = archivoMedicoServicio.obtenerPorId(id);
        return ResponseEntity.ok(archivoMedico);
    }

    /**
     * Listar archivos por historia clínica
     */
    @GetMapping("/historia/{historiaClinicaId}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO', 'ASISTENTE')")
    @Operation(summary = "Listar archivos por historia clínica")
    public ResponseEntity<List<ArchivoMedicoDTO>> listarPorHistoriaClinica(@PathVariable Long historiaClinicaId) {
        List<ArchivoMedicoDTO> archivos = archivoMedicoServicio.listarPorHistoriaClinica(historiaClinicaId);
        return ResponseEntity.ok(archivos);
    }

    /**
     * Listar archivos por consulta
     */
    @GetMapping("/consulta/{consultaId}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO', 'ASISTENTE')")
    @Operation(summary = "Listar archivos por consulta")
    public ResponseEntity<List<ArchivoMedicoDTO>> listarPorConsulta(@PathVariable Long consultaId) {
        List<ArchivoMedicoDTO> archivos = archivoMedicoServicio.listarPorConsulta(consultaId);
        return ResponseEntity.ok(archivos);
    }

    /**
     * Listar archivos por tipo
     */
    @GetMapping("/tipo/{tipoArchivo}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO')")
    @Operation(summary = "Listar archivos por tipo")
    public ResponseEntity<List<ArchivoMedicoDTO>> listarPorTipo(@PathVariable TipoArchivo tipoArchivo) {
        List<ArchivoMedicoDTO> archivos = archivoMedicoServicio.listarPorTipo(tipoArchivo);
        return ResponseEntity.ok(archivos);
    }

    /**
     * Listar archivos recientes
     */
    @GetMapping("/recientes")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO')")
    @Operation(summary = "Listar archivos recientes", description = "Obtiene archivos de los últimos 30 días")
    public ResponseEntity<List<ArchivoMedicoDTO>> listarArchivosRecientes() {
        List<ArchivoMedicoDTO> archivos = archivoMedicoServicio.listarArchivosRecientes();
        return ResponseEntity.ok(archivos);
    }

    /**
     * Descargar archivo
     */
    @GetMapping("/{id}/descargar")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO', 'ASISTENTE')")
    @Operation(summary = "Descargar archivo", description = "Descarga el contenido del archivo médico")
    public ResponseEntity<Resource> descargarArchivo(@PathVariable Long id) {
        // Obtener información del archivo
        ArchivoMedicoDTO archivoInfo = archivoMedicoServicio.obtenerPorId(id);
        
        // Obtener recurso del archivo
        Resource recurso = archivoMedicoServicio.descargarArchivo(id);
        
        // Configurar headers para descarga
        String contentType = archivoInfo.getTipoContenido() != null 
                ? archivoInfo.getTipoContenido() 
                : "application/octet-stream";
        
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, 
                        "attachment; filename=\"" + archivoInfo.getNombreArchivo() + "\"")
                .body(recurso);
    }

    /**
     * Eliminar archivo
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO')")
    @Operation(summary = "Eliminar archivo", description = "Elimina un archivo médico del sistema")
    public ResponseEntity<Void> eliminarArchivo(
            @PathVariable Long id,
            HttpServletRequest request
    ) {
        archivoMedicoServicio.eliminarArchivo(id, request);
        return ResponseEntity.noContent().build();
    }
}
