package com.nuclearvet.aplicacion.controladores;

import com.nuclearvet.aplicacion.dto.notificaciones.HistorialCorreoDTO;
import com.nuclearvet.dominio.enumeraciones.EstadoCorreo;
import com.nuclearvet.dominio.servicios.HistorialCorreoServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST para gestión de historial de correos
 */
@Tag(name = "Historial de Correos", description = "API para gestión de historial de correos electrónicos")
@RestController
@RequestMapping("/api/historial-correos")
@RequiredArgsConstructor
public class HistorialCorreoControlador {

    private final HistorialCorreoServicio historialServicio;

    @Operation(summary = "Listar todo el historial de correos")
    @GetMapping
    public ResponseEntity<List<HistorialCorreoDTO>> listarTodos() {
        return ResponseEntity.ok(historialServicio.listarTodos());
    }

    @Operation(summary = "Listar correos por destinatario")
    @GetMapping("/destinatario/{email}")
    public ResponseEntity<List<HistorialCorreoDTO>> listarPorDestinatario(@PathVariable String email) {
        return ResponseEntity.ok(historialServicio.listarPorDestinatario(email));
    }

    @Operation(summary = "Listar correos por estado")
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<HistorialCorreoDTO>> listarPorEstado(@PathVariable EstadoCorreo estado) {
        return ResponseEntity.ok(historialServicio.listarPorEstado(estado));
    }

    @Operation(summary = "Listar correos pendientes")
    @GetMapping("/pendientes")
    public ResponseEntity<List<HistorialCorreoDTO>> listarPendientes() {
        return ResponseEntity.ok(historialServicio.listarPendientes());
    }

    @Operation(summary = "Listar correos con error")
    @GetMapping("/con-error")
    public ResponseEntity<List<HistorialCorreoDTO>> listarConError() {
        return ResponseEntity.ok(historialServicio.listarConError());
    }

    @Operation(summary = "Listar correos por plantilla")
    @GetMapping("/plantilla/{plantillaId}")
    public ResponseEntity<List<HistorialCorreoDTO>> listarPorPlantilla(@PathVariable Long plantillaId) {
        return ResponseEntity.ok(historialServicio.listarPorPlantilla(plantillaId));
    }

    @Operation(summary = "Listar correos recientes")
    @GetMapping("/recientes")
    public ResponseEntity<List<HistorialCorreoDTO>> listarRecientes(
            @RequestParam(defaultValue = "7") int dias) {
        return ResponseEntity.ok(historialServicio.listarRecientes(dias));
    }

    @Operation(summary = "Listar correos por rango de fechas")
    @GetMapping("/rango-fechas")
    public ResponseEntity<List<HistorialCorreoDTO>> listarPorRangoFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return ResponseEntity.ok(historialServicio.listarPorRangoFechas(inicio, fin));
    }

    @Operation(summary = "Obtener historial de correo por ID")
    @GetMapping("/{id}")
    public ResponseEntity<HistorialCorreoDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(historialServicio.obtenerPorId(id));
    }

    @Operation(summary = "Marcar correo como enviado")
    @PatchMapping("/{id}/marcar-enviado")
    public ResponseEntity<HistorialCorreoDTO> marcarComoEnviado(@PathVariable Long id) {
        return ResponseEntity.ok(historialServicio.marcarComoEnviado(id));
    }

    @Operation(summary = "Marcar correo con error")
    @PatchMapping("/{id}/marcar-error")
    public ResponseEntity<HistorialCorreoDTO> marcarComoError(
            @PathVariable Long id,
            @RequestParam String mensajeError) {
        return ResponseEntity.ok(historialServicio.marcarComoError(id, mensajeError));
    }

    @Operation(summary = "Marcar correo para reintento")
    @PatchMapping("/{id}/marcar-reintentando")
    public ResponseEntity<HistorialCorreoDTO> marcarComoReintentando(@PathVariable Long id) {
        return ResponseEntity.ok(historialServicio.marcarComoReintentando(id));
    }

    @Operation(summary = "Reintentar envíos fallidos")
    @PostMapping("/reintentar-fallidos")
    public ResponseEntity<Void> reintentarEnviosFallidos() {
        historialServicio.reintentarEnviosFallidos();
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Eliminar historial de correo")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        historialServicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Eliminar correos enviados antiguos")
    @DeleteMapping("/limpiar-antiguos")
    public ResponseEntity<Void> eliminarEnviadosAntiguos(
            @RequestParam(defaultValue = "90") int dias) {
        historialServicio.eliminarEnviadosAntiguos(dias);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Contar correos por estado")
    @GetMapping("/estadisticas/por-estado")
    public ResponseEntity<Map<EstadoCorreo, Long>> contarPorEstado() {
        return ResponseEntity.ok(historialServicio.contarPorEstado());
    }

    @Operation(summary = "Contar correos enviados")
    @GetMapping("/estadisticas/enviados")
    public ResponseEntity<Long> contarEnviados() {
        return ResponseEntity.ok(historialServicio.contarEnviados());
    }

    @Operation(summary = "Contar correos con error")
    @GetMapping("/estadisticas/con-error")
    public ResponseEntity<Long> contarConError() {
        return ResponseEntity.ok(historialServicio.contarConError());
    }

    @Operation(summary = "Contar correos pendientes")
    @GetMapping("/estadisticas/pendientes")
    public ResponseEntity<Long> contarPendientes() {
        return ResponseEntity.ok(historialServicio.contarPendientes());
    }

    @Operation(summary = "Contar correos enviados por destinatario")
    @GetMapping("/estadisticas/enviados-por-destinatario")
    public ResponseEntity<Long> contarEnviadosPorDestinatario(@RequestParam String email) {
        return ResponseEntity.ok(historialServicio.contarEnviadosPorDestinatario(email));
    }

    @Operation(summary = "Contar correos por plantilla")
    @GetMapping("/plantilla/{plantillaId}/estadisticas")
    public ResponseEntity<Long> contarPorPlantilla(@PathVariable Long plantillaId) {
        return ResponseEntity.ok(historialServicio.contarPorPlantilla(plantillaId));
    }
}
