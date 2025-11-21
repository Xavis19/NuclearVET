package com.nuclearvet.aplicacion.controladores;

import com.nuclearvet.aplicacion.dto.administrativo.ConfiguracionClinicaDTO;
import com.nuclearvet.dominio.servicios.ConfiguracionClinicaServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/configuracion")
@RequiredArgsConstructor
@Tag(name = "Configuración", description = "Gestión de la configuración de la clínica")
public class ConfiguracionClinicaControlador {

    private final ConfiguracionClinicaServicio configuracionServicio;

    @GetMapping
    @Operation(summary = "Obtener configuración actual")
    public ResponseEntity<ConfiguracionClinicaDTO> obtenerConfiguracionActual() {
        return ResponseEntity.ok(configuracionServicio.obtenerConfiguracionActual());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Operation(summary = "Obtener configuración por ID")
    public ResponseEntity<ConfiguracionClinicaDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(configuracionServicio.obtenerPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Operation(summary = "Crear configuración")
    public ResponseEntity<ConfiguracionClinicaDTO> crear(@Valid @RequestBody ConfiguracionClinicaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(configuracionServicio.crear(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Operation(summary = "Actualizar configuración")
    public ResponseEntity<ConfiguracionClinicaDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ConfiguracionClinicaDTO dto) {
        return ResponseEntity.ok(configuracionServicio.actualizar(id, dto));
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Operation(summary = "Actualizar configuración actual")
    public ResponseEntity<ConfiguracionClinicaDTO> actualizarActual(@Valid @RequestBody ConfiguracionClinicaDTO dto) {
        return ResponseEntity.ok(configuracionServicio.actualizarActual(dto));
    }
}
