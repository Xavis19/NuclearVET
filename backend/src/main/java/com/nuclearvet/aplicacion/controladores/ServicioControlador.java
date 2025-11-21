package com.nuclearvet.aplicacion.controladores;

import com.nuclearvet.aplicacion.dto.administrativo.CrearServicioDTO;
import com.nuclearvet.aplicacion.dto.administrativo.ServicioDTO;
import com.nuclearvet.dominio.enumeraciones.TipoServicio;
import com.nuclearvet.dominio.servicios.ServicioServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/servicios")
@RequiredArgsConstructor
@Tag(name = "Servicios", description = "Gestión del catálogo de servicios de la clínica")
public class ServicioControlador {

    private final ServicioServicio servicioServicio;

    @GetMapping
    @Operation(summary = "Listar todos los servicios")
    public ResponseEntity<List<ServicioDTO>> listarTodos() {
        return ResponseEntity.ok(servicioServicio.listarTodos());
    }

    @GetMapping("/activos")
    @Operation(summary = "Listar servicios activos")
    public ResponseEntity<List<ServicioDTO>> listarActivos() {
        return ResponseEntity.ok(servicioServicio.listarActivos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener servicio por ID")
    public ResponseEntity<ServicioDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(servicioServicio.obtenerPorId(id));
    }

    @GetMapping("/codigo/{codigo}")
    @Operation(summary = "Obtener servicio por código")
    public ResponseEntity<ServicioDTO> obtenerPorCodigo(@PathVariable String codigo) {
        return ResponseEntity.ok(servicioServicio.obtenerPorCodigo(codigo));
    }

    @GetMapping("/tipo/{tipo}")
    @Operation(summary = "Listar servicios por tipo")
    public ResponseEntity<List<ServicioDTO>> listarPorTipo(@PathVariable TipoServicio tipo) {
        return ResponseEntity.ok(servicioServicio.listarPorTipo(tipo));
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar servicios por nombre o código")
    public ResponseEntity<List<ServicioDTO>> buscar(@RequestParam String termino) {
        return ResponseEntity.ok(servicioServicio.buscar(termino));
    }

    @GetMapping("/precio-rango")
    @Operation(summary = "Listar servicios por rango de precio")
    public ResponseEntity<List<ServicioDTO>> listarPorRangoPrecio(
            @RequestParam BigDecimal minimo,
            @RequestParam BigDecimal maximo) {
        return ResponseEntity.ok(servicioServicio.listarPorRangoPrecio(minimo, maximo));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO')")
    @Operation(summary = "Crear nuevo servicio")
    public ResponseEntity<ServicioDTO> crear(@Valid @RequestBody CrearServicioDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(servicioServicio.crear(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO')")
    @Operation(summary = "Actualizar servicio")
    public ResponseEntity<ServicioDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody CrearServicioDTO dto) {
        return ResponseEntity.ok(servicioServicio.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Operation(summary = "Eliminar servicio")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        servicioServicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/activar")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO')")
    @Operation(summary = "Activar servicio")
    public ResponseEntity<ServicioDTO> activar(@PathVariable Long id) {
        return ResponseEntity.ok(servicioServicio.activar(id));
    }

    @PatchMapping("/{id}/desactivar")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO')")
    @Operation(summary = "Desactivar servicio")
    public ResponseEntity<ServicioDTO> desactivar(@PathVariable Long id) {
        return ResponseEntity.ok(servicioServicio.desactivar(id));
    }

    @GetMapping("/estadisticas/por-tipo")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO')")
    @Operation(summary = "Contar servicios por tipo")
    public ResponseEntity<Map<TipoServicio, Long>> contarPorTipo() {
        return ResponseEntity.ok(servicioServicio.contarPorTipo());
    }

    @GetMapping("/estadisticas/activos")
    @Operation(summary = "Contar servicios activos")
    public ResponseEntity<Long> contarActivos() {
        return ResponseEntity.ok(servicioServicio.contarActivos());
    }
}
