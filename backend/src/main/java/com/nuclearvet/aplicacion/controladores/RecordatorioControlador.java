package com.nuclearvet.aplicacion.controladores;

import com.nuclearvet.aplicacion.dto.notificaciones.CrearRecordatorioDTO;
import com.nuclearvet.aplicacion.dto.notificaciones.RecordatorioDTO;
import com.nuclearvet.dominio.enumeraciones.TipoRecordatorio;
import com.nuclearvet.dominio.servicios.RecordatorioServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST para gestión de recordatorios
 */
@Tag(name = "Recordatorios", description = "API para gestión de recordatorios")
@RestController
@RequestMapping("/api/recordatorios")
@RequiredArgsConstructor
public class RecordatorioControlador {

    private final RecordatorioServicio recordatorioServicio;

    @Operation(summary = "Listar todos los recordatorios")
    @GetMapping
    public ResponseEntity<List<RecordatorioDTO>> listarTodos() {
        return ResponseEntity.ok(recordatorioServicio.listarTodos());
    }

    @Operation(summary = "Listar recordatorios por paciente")
    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<RecordatorioDTO>> listarPorPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(recordatorioServicio.listarPorPaciente(pacienteId));
    }

    @Operation(summary = "Listar recordatorios pendientes")
    @GetMapping("/pendientes")
    public ResponseEntity<List<RecordatorioDTO>> listarPendientes() {
        return ResponseEntity.ok(recordatorioServicio.listarPendientes());
    }

    @Operation(summary = "Listar recordatorios pendientes por paciente")
    @GetMapping("/paciente/{pacienteId}/pendientes")
    public ResponseEntity<List<RecordatorioDTO>> listarPendientesPorPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(recordatorioServicio.listarPendientesPorPaciente(pacienteId));
    }

    @Operation(summary = "Listar recordatorios por tipo")
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<RecordatorioDTO>> listarPorTipo(@PathVariable TipoRecordatorio tipo) {
        return ResponseEntity.ok(recordatorioServicio.listarPorTipo(tipo));
    }

    @Operation(summary = "Listar recordatorios pendientes por tipo")
    @GetMapping("/tipo/{tipo}/pendientes")
    public ResponseEntity<List<RecordatorioDTO>> listarPendientesPorTipo(@PathVariable TipoRecordatorio tipo) {
        return ResponseEntity.ok(recordatorioServicio.listarPendientesPorTipo(tipo));
    }

    @Operation(summary = "Listar recordatorios pendientes de envío")
    @GetMapping("/pendientes-envio")
    public ResponseEntity<List<RecordatorioDTO>> listarPendientesDeEnvio() {
        return ResponseEntity.ok(recordatorioServicio.listarPendientesDeEnvio());
    }

    @Operation(summary = "Listar recordatorios próximos a enviar")
    @GetMapping("/proximos-enviar")
    public ResponseEntity<List<RecordatorioDTO>> listarProximosAEnviar(
            @RequestParam(defaultValue = "24") int horas) {
        return ResponseEntity.ok(recordatorioServicio.listarProximosAEnviar(horas));
    }

    @Operation(summary = "Listar recordatorios por rango de fechas")
    @GetMapping("/rango-fechas")
    public ResponseEntity<List<RecordatorioDTO>> listarPorRangoFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return ResponseEntity.ok(recordatorioServicio.listarPorRangoFechas(inicio, fin));
    }

    @Operation(summary = "Listar recordatorios por propietario")
    @GetMapping("/propietario/{propietarioId}")
    public ResponseEntity<List<RecordatorioDTO>> listarPorPropietario(@PathVariable Long propietarioId) {
        return ResponseEntity.ok(recordatorioServicio.listarPorPropietario(propietarioId));
    }

    @Operation(summary = "Obtener recordatorio por ID")
    @GetMapping("/{id}")
    public ResponseEntity<RecordatorioDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(recordatorioServicio.obtenerPorId(id));
    }

    @Operation(summary = "Crear recordatorio")
    @PostMapping
    public ResponseEntity<RecordatorioDTO> crear(@Valid @RequestBody CrearRecordatorioDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(recordatorioServicio.crear(dto));
    }

    @Operation(summary = "Marcar recordatorio como enviado")
    @PatchMapping("/{id}/marcar-enviado")
    public ResponseEntity<RecordatorioDTO> marcarComoEnviado(@PathVariable Long id) {
        return ResponseEntity.ok(recordatorioServicio.marcarComoEnviado(id));
    }

    @Operation(summary = "Eliminar recordatorio")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        recordatorioServicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Eliminar recordatorios enviados antiguos")
    @DeleteMapping("/limpiar-antiguos")
    public ResponseEntity<Void> eliminarEnviadosAntiguos(
            @RequestParam(defaultValue = "90") int dias) {
        recordatorioServicio.eliminarEnviadosAntiguos(dias);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Enviar recordatorios pendientes")
    @PostMapping("/enviar-pendientes")
    public ResponseEntity<Void> enviarRecordatoriosPendientes() {
        recordatorioServicio.enviarRecordatoriosPendientes();
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Contar recordatorios pendientes por tipo")
    @GetMapping("/estadisticas/pendientes-por-tipo")
    public ResponseEntity<Map<TipoRecordatorio, Long>> contarPendientesPorTipo() {
        return ResponseEntity.ok(recordatorioServicio.contarPendientesPorTipo());
    }

    @Operation(summary = "Contar recordatorios vencidos")
    @GetMapping("/estadisticas/vencidos")
    public ResponseEntity<Long> contarVencidos() {
        return ResponseEntity.ok(recordatorioServicio.contarVencidos());
    }

    @Operation(summary = "Contar recordatorios pendientes por paciente")
    @GetMapping("/paciente/{pacienteId}/estadisticas/pendientes")
    public ResponseEntity<Long> contarPendientesPorPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(recordatorioServicio.contarPendientesPorPaciente(pacienteId));
    }
}
