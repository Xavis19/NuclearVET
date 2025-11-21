package com.nuclearvet.aplicacion.controladores;

import com.nuclearvet.aplicacion.dto.notificaciones.ActualizarPlantillaDTO;
import com.nuclearvet.aplicacion.dto.notificaciones.CrearPlantillaDTO;
import com.nuclearvet.aplicacion.dto.notificaciones.PlantillaMensajeDTO;
import com.nuclearvet.dominio.enumeraciones.TipoPlantilla;
import com.nuclearvet.dominio.servicios.PlantillaMensajeServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controlador REST para gestión de plantillas de mensajes
 */
@Tag(name = "Plantillas de Mensajes", description = "API para gestión de plantillas de mensajes")
@RestController
@RequestMapping("/api/plantillas-mensajes")
@RequiredArgsConstructor
public class PlantillaMensajeControlador {

    private final PlantillaMensajeServicio plantillaServicio;

    @Operation(summary = "Listar todas las plantillas")
    @GetMapping
    public ResponseEntity<List<PlantillaMensajeDTO>> listarTodas() {
        return ResponseEntity.ok(plantillaServicio.listarTodas());
    }

    @Operation(summary = "Listar plantillas activas")
    @GetMapping("/activas")
    public ResponseEntity<List<PlantillaMensajeDTO>> listarActivas() {
        return ResponseEntity.ok(plantillaServicio.listarActivas());
    }

    @Operation(summary = "Obtener plantilla por ID")
    @GetMapping("/{id}")
    public ResponseEntity<PlantillaMensajeDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(plantillaServicio.obtenerPorId(id));
    }

    @Operation(summary = "Obtener plantilla por nombre")
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<PlantillaMensajeDTO> obtenerPorNombre(@PathVariable String nombre) {
        return ResponseEntity.ok(plantillaServicio.obtenerPorNombre(nombre));
    }

    @Operation(summary = "Listar plantillas por tipo")
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<PlantillaMensajeDTO>> listarPorTipo(@PathVariable TipoPlantilla tipo) {
        return ResponseEntity.ok(plantillaServicio.listarPorTipo(tipo));
    }

    @Operation(summary = "Buscar plantillas")
    @GetMapping("/buscar")
    public ResponseEntity<List<PlantillaMensajeDTO>> buscar(@RequestParam String termino) {
        return ResponseEntity.ok(plantillaServicio.buscar(termino));
    }

    @Operation(summary = "Crear plantilla")
    @PostMapping
    public ResponseEntity<PlantillaMensajeDTO> crear(@Valid @RequestBody CrearPlantillaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(plantillaServicio.crear(dto));
    }

    @Operation(summary = "Actualizar plantilla")
    @PutMapping("/{id}")
    public ResponseEntity<PlantillaMensajeDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarPlantillaDTO dto) {
        return ResponseEntity.ok(plantillaServicio.actualizar(id, dto));
    }

    @Operation(summary = "Eliminar plantilla")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        plantillaServicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Activar plantilla")
    @PatchMapping("/{id}/activar")
    public ResponseEntity<PlantillaMensajeDTO> activar(@PathVariable Long id) {
        return ResponseEntity.ok(plantillaServicio.activar(id));
    }

    @Operation(summary = "Desactivar plantilla")
    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<PlantillaMensajeDTO> desactivar(@PathVariable Long id) {
        return ResponseEntity.ok(plantillaServicio.desactivar(id));
    }

    @Operation(summary = "Renderizar plantilla con variables")
    @PostMapping("/{id}/renderizar")
    public ResponseEntity<String> renderizar(
            @PathVariable Long id,
            @RequestBody Map<String, String> variables) {
        return ResponseEntity.ok(plantillaServicio.renderizar(id, variables));
    }

    @Operation(summary = "Renderizar asunto con variables")
    @PostMapping("/{id}/renderizar-asunto")
    public ResponseEntity<String> renderizarAsunto(
            @PathVariable Long id,
            @RequestBody Map<String, String> variables) {
        return ResponseEntity.ok(plantillaServicio.renderizarAsunto(id, variables));
    }

    @Operation(summary = "Contar plantillas por tipo")
    @GetMapping("/estadisticas/por-tipo")
    public ResponseEntity<Map<TipoPlantilla, Long>> contarPorTipo() {
        return ResponseEntity.ok(plantillaServicio.contarPorTipo());
    }

    @Operation(summary = "Contar plantillas activas")
    @GetMapping("/estadisticas/activas")
    public ResponseEntity<Long> contarActivas() {
        return ResponseEntity.ok(plantillaServicio.contarActivas());
    }
}
