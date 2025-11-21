package com.nuclearvet.aplicacion.controladores;

import com.nuclearvet.aplicacion.dtos.LoteDTO;
import com.nuclearvet.aplicacion.servicios.LoteServicio;
import com.nuclearvet.dominio.enumeraciones.EstadoLote;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Controlador REST para gestión de lotes de inventario
 */
@RestController
@RequestMapping("/api/inventario/lotes")
@RequiredArgsConstructor
@Tag(name = "Lotes", description = "API para gestión de lotes de inventario")
public class LoteControlador {

    private final LoteServicio loteServicio;

    @PostMapping
    @Operation(summary = "Crear lote", description = "Registra un nuevo lote en el inventario")
    public ResponseEntity<LoteDTO> crear(@Valid @RequestBody LoteDTO loteDTO) {
        return new ResponseEntity<>(loteServicio.crear(loteDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar lote", description = "Actualiza la información de un lote")
    public ResponseEntity<LoteDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody LoteDTO loteDTO) {
        return ResponseEntity.ok(loteServicio.actualizar(id, loteDTO));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener lote por ID", description = "Obtiene un lote por su identificador")
    public ResponseEntity<LoteDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(loteServicio.obtenerPorId(id));
    }

    @GetMapping("/numero/{numeroLote}")
    @Operation(summary = "Obtener lote por número", description = "Obtiene un lote por su número único")
    public ResponseEntity<LoteDTO> obtenerPorNumero(@PathVariable String numeroLote) {
        return ResponseEntity.ok(loteServicio.obtenerPorNumero(numeroLote));
    }

    @GetMapping("/producto/{productoId}")
    @Operation(summary = "Listar lotes por producto", description = "Lista todos los lotes de un producto específico")
    public ResponseEntity<List<LoteDTO>> listarPorProducto(@PathVariable Long productoId) {
        return ResponseEntity.ok(loteServicio.listarPorProducto(productoId));
    }

    @GetMapping("/disponibles")
    @Operation(summary = "Listar lotes disponibles", description = "Lista todos los lotes con stock disponible")
    public ResponseEntity<List<LoteDTO>> listarDisponibles() {
        return ResponseEntity.ok(loteServicio.listarDisponibles());
    }

    @GetMapping("/producto/{productoId}/disponibles")
    @Operation(summary = "Lotes disponibles por producto", description = "Lista lotes disponibles de un producto")
    public ResponseEntity<List<LoteDTO>> listarDisponiblesPorProducto(@PathVariable Long productoId) {
        return ResponseEntity.ok(loteServicio.listarDisponiblesPorProducto(productoId));
    }

    @GetMapping("/proximos-vencer")
    @Operation(summary = "Lotes próximos a vencer", description = "Lista lotes próximos a vencer en los próximos N días")
    public ResponseEntity<List<LoteDTO>> listarProximosVencer(@RequestParam(defaultValue = "30") int dias) {
        return ResponseEntity.ok(loteServicio.listarProximosVencer(dias));
    }

    @GetMapping("/vencidos")
    @Operation(summary = "Lotes vencidos", description = "Lista todos los lotes que ya han vencido")
    public ResponseEntity<List<LoteDTO>> listarVencidos() {
        return ResponseEntity.ok(loteServicio.listarVencidos());
    }

    @GetMapping("/estado/{estado}")
    @Operation(summary = "Listar por estado", description = "Lista lotes según su estado")
    public ResponseEntity<List<LoteDTO>> listarPorEstado(@PathVariable EstadoLote estado) {
        return ResponseEntity.ok(loteServicio.listarPorEstado(estado));
    }

    @PatchMapping("/actualizar-estados")
    @Operation(summary = "Actualizar estados", description = "Actualiza los estados de todos los lotes según fechas")
    public ResponseEntity<Void> actualizarEstados() {
        loteServicio.actualizarEstados();
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/bloquear")
    @Operation(summary = "Bloquear lote", description = "Bloquea un lote impidiendo su uso")
    public ResponseEntity<Void> bloquear(
            @PathVariable Long id,
            @RequestParam String motivo) {
        loteServicio.bloquear(id, motivo);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/desbloquear")
    @Operation(summary = "Desbloquear lote", description = "Desbloquea un lote previamente bloqueado")
    public ResponseEntity<Void> desbloquear(@PathVariable Long id) {
        loteServicio.desbloquear(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar lote", description = "Elimina un lote del sistema")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        loteServicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/valor")
    @Operation(summary = "Calcular valor del lote", description = "Calcula el valor total del stock en un lote")
    public ResponseEntity<BigDecimal> calcularValor(@PathVariable Long id) {
        return ResponseEntity.ok(loteServicio.calcularValorInventarioLote(id));
    }
}
