package com.nuclearvet.aplicacion.controladores;

import com.nuclearvet.aplicacion.dtos.AlertaInventarioDTO;
import com.nuclearvet.aplicacion.servicios.AlertaInventarioServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestión de alertas de inventario
 */
@RestController
@RequestMapping("/api/inventario/alertas")
@RequiredArgsConstructor
@Tag(name = "Alertas de Inventario", description = "API para gestión de alertas de inventario")
public class AlertaInventarioControlador {

    private final AlertaInventarioServicio alertaServicio;

    @GetMapping("/{id}")
    @Operation(summary = "Obtener alerta por ID", description = "Obtiene una alerta por su identificador")
    public ResponseEntity<AlertaInventarioDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(alertaServicio.obtenerPorId(id));
    }

    @GetMapping("/no-leidas")
    @Operation(summary = "Listar alertas no leídas", description = "Lista todas las alertas que no han sido leídas")
    public ResponseEntity<List<AlertaInventarioDTO>> listarNoLeidas() {
        return ResponseEntity.ok(alertaServicio.listarNoLeidas());
    }

    @GetMapping("/prioridad/{prioridad}")
    @Operation(summary = "Listar por prioridad", description = "Lista alertas no leídas por nivel de prioridad")
    public ResponseEntity<List<AlertaInventarioDTO>> listarPorPrioridad(@PathVariable String prioridad) {
        return ResponseEntity.ok(alertaServicio.listarPorPrioridad(prioridad));
    }

    @GetMapping("/producto/{productoId}")
    @Operation(summary = "Listar por producto", description = "Lista alertas relacionadas con un producto específico")
    public ResponseEntity<List<AlertaInventarioDTO>> listarPorProducto(@PathVariable Long productoId) {
        return ResponseEntity.ok(alertaServicio.listarPorProducto(productoId));
    }

    @GetMapping("/recientes")
    @Operation(summary = "Alertas recientes", description = "Lista alertas generadas en los últimos N días")
    public ResponseEntity<List<AlertaInventarioDTO>> listarRecientes(
            @RequestParam(defaultValue = "7") int dias) {
        return ResponseEntity.ok(alertaServicio.listarRecientes(dias));
    }

    @PatchMapping("/{id}/marcar-leida")
    @Operation(summary = "Marcar como leída", description = "Marca una alerta específica como leída")
    public ResponseEntity<Void> marcarComoLeida(@PathVariable Long id) {
        alertaServicio.marcarComoLeida(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/marcar-todas-leidas")
    @Operation(summary = "Marcar todas como leídas", description = "Marca todas las alertas como leídas")
    public ResponseEntity<Void> marcarTodasComoLeidas() {
        alertaServicio.marcarTodasComoLeidas();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verificar")
    @Operation(summary = "Verificar y generar alertas", description = "Verifica el inventario y genera alertas automáticas")
    public ResponseEntity<Void> verificarYGenerarAlertas() {
        alertaServicio.verificarYGenerarAlertas();
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar alerta", description = "Elimina una alerta del sistema")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        alertaServicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/contar-no-leidas")
    @Operation(summary = "Contar alertas no leídas", description = "Retorna el número de alertas no leídas")
    public ResponseEntity<Long> contarNoLeidas() {
        return ResponseEntity.ok(alertaServicio.contarNoLeidas());
    }
}
