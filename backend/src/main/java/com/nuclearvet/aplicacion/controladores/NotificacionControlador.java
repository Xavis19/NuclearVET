package com.nuclearvet.aplicacion.controladores;

import com.nuclearvet.aplicacion.dto.notificaciones.CrearNotificacionDTO;
import com.nuclearvet.aplicacion.dto.notificaciones.NotificacionDTO;
import com.nuclearvet.dominio.enumeraciones.PrioridadNotificacion;
import com.nuclearvet.dominio.enumeraciones.TipoNotificacion;
import com.nuclearvet.dominio.servicios.NotificacionServicio;
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
 * Controlador REST para gestión de notificaciones
 */
@Tag(name = "Notificaciones", description = "API para gestión de notificaciones")
@RestController
@RequestMapping("/api/notificaciones")
@RequiredArgsConstructor
public class NotificacionControlador {

    private final NotificacionServicio notificacionServicio;

    @Operation(summary = "Listar notificaciones por usuario")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<NotificacionDTO>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(notificacionServicio.listarPorUsuario(usuarioId));
    }

    @Operation(summary = "Listar notificaciones no leídas por usuario")
    @GetMapping("/usuario/{usuarioId}/no-leidas")
    public ResponseEntity<List<NotificacionDTO>> listarNoLeidasPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(notificacionServicio.listarNoLeidasPorUsuario(usuarioId));
    }

    @Operation(summary = "Listar notificaciones por usuario y tipo")
    @GetMapping("/usuario/{usuarioId}/tipo/{tipo}")
    public ResponseEntity<List<NotificacionDTO>> listarPorUsuarioYTipo(
            @PathVariable Long usuarioId,
            @PathVariable TipoNotificacion tipo) {
        return ResponseEntity.ok(notificacionServicio.listarPorUsuarioYTipo(usuarioId, tipo));
    }

    @Operation(summary = "Listar notificaciones no leídas por usuario y prioridad")
    @GetMapping("/usuario/{usuarioId}/no-leidas/prioridad/{prioridad}")
    public ResponseEntity<List<NotificacionDTO>> listarNoLeidasPorUsuarioYPrioridad(
            @PathVariable Long usuarioId,
            @PathVariable PrioridadNotificacion prioridad) {
        return ResponseEntity.ok(notificacionServicio.listarNoLeidasPorUsuarioYPrioridad(usuarioId, prioridad));
    }

    @Operation(summary = "Listar notificaciones recientes por usuario")
    @GetMapping("/usuario/{usuarioId}/recientes")
    public ResponseEntity<List<NotificacionDTO>> listarRecientesPorUsuario(
            @PathVariable Long usuarioId,
            @RequestParam(defaultValue = "7") int dias) {
        return ResponseEntity.ok(notificacionServicio.listarRecientesPorUsuario(usuarioId, dias));
    }

    @Operation(summary = "Obtener notificación por ID")
    @GetMapping("/{id}")
    public ResponseEntity<NotificacionDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(notificacionServicio.obtenerPorId(id));
    }

    @Operation(summary = "Crear notificación")
    @PostMapping
    public ResponseEntity<NotificacionDTO> crear(@Valid @RequestBody CrearNotificacionDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(notificacionServicio.crear(dto));
    }

    @Operation(summary = "Marcar notificación como leída")
    @PatchMapping("/{id}/marcar-leida")
    public ResponseEntity<NotificacionDTO> marcarComoLeida(@PathVariable Long id) {
        return ResponseEntity.ok(notificacionServicio.marcarComoLeida(id));
    }

    @Operation(summary = "Marcar notificación como no leída")
    @PatchMapping("/{id}/marcar-no-leida")
    public ResponseEntity<NotificacionDTO> marcarComoNoLeida(@PathVariable Long id) {
        return ResponseEntity.ok(notificacionServicio.marcarComoNoLeida(id));
    }

    @Operation(summary = "Marcar todas como leídas por usuario")
    @PatchMapping("/usuario/{usuarioId}/marcar-todas-leidas")
    public ResponseEntity<Void> marcarTodasComoLeidasPorUsuario(@PathVariable Long usuarioId) {
        notificacionServicio.marcarTodasComoLeidasPorUsuario(usuarioId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Marcar como leídas por tipo")
    @PatchMapping("/usuario/{usuarioId}/tipo/{tipo}/marcar-leidas")
    public ResponseEntity<Void> marcarComoLeidasPorTipo(
            @PathVariable Long usuarioId,
            @PathVariable TipoNotificacion tipo) {
        notificacionServicio.marcarComoLeidasPorTipo(usuarioId, tipo);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Eliminar notificación")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        notificacionServicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Eliminar notificaciones leídas antiguas")
    @DeleteMapping("/usuario/{usuarioId}/limpiar-antiguas")
    public ResponseEntity<Void> eliminarLeidasAntiguas(@RequestParam(defaultValue = "30") int dias) {
        notificacionServicio.eliminarLeidasAntiguas(dias);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Contar notificaciones no leídas por usuario")
    @GetMapping("/usuario/{usuarioId}/estadisticas/no-leidas")
    public ResponseEntity<Long> contarNoLeidasPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(notificacionServicio.contarNoLeidasPorUsuario(usuarioId));
    }

    @Operation(summary = "Contar notificaciones urgentes no leídas por usuario")
    @GetMapping("/usuario/{usuarioId}/estadisticas/urgentes-no-leidas")
    public ResponseEntity<Long> contarUrgentesNoLeidasPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(notificacionServicio.contarUrgentesNoLeidasPorUsuario(usuarioId));
    }

    @Operation(summary = "Contar notificaciones no leídas por tipo")
    @GetMapping("/usuario/{usuarioId}/estadisticas/por-tipo")
    public ResponseEntity<Map<TipoNotificacion, Long>> contarNoLeidasPorTipo(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(notificacionServicio.contarNoLeidasPorTipo(usuarioId));
    }
}
