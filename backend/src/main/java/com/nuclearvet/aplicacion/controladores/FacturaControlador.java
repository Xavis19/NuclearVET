package com.nuclearvet.aplicacion.controladores;

import com.nuclearvet.aplicacion.dto.administrativo.CrearFacturaDTO;
import com.nuclearvet.aplicacion.dto.administrativo.FacturaDTO;
import com.nuclearvet.dominio.enumeraciones.EstadoFactura;
import com.nuclearvet.dominio.servicios.FacturaServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/facturas")
@RequiredArgsConstructor
@Tag(name = "Facturas", description = "Gestión de facturación y cuentas por cobrar")
public class FacturaControlador {

    private final FacturaServicio facturaServicio;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'RECEPCIONISTA', 'VETERINARIO')")
    @Operation(summary = "Listar todas las facturas")
    public ResponseEntity<List<FacturaDTO>> listarTodas() {
        return ResponseEntity.ok(facturaServicio.listarTodas());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'RECEPCIONISTA', 'VETERINARIO')")
    @Operation(summary = "Obtener factura por ID")
    public ResponseEntity<FacturaDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(facturaServicio.obtenerPorId(id));
    }

    @GetMapping("/numero/{numero}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'RECEPCIONISTA', 'VETERINARIO')")
    @Operation(summary = "Obtener factura por número")
    public ResponseEntity<FacturaDTO> obtenerPorNumero(@PathVariable String numero) {
        return ResponseEntity.ok(facturaServicio.obtenerPorNumero(numero));
    }

    @GetMapping("/propietario/{propietarioId}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'RECEPCIONISTA', 'VETERINARIO')")
    @Operation(summary = "Listar facturas por propietario")
    public ResponseEntity<List<FacturaDTO>> listarPorPropietario(@PathVariable Long propietarioId) {
        return ResponseEntity.ok(facturaServicio.listarPorPropietario(propietarioId));
    }

    @GetMapping("/paciente/{pacienteId}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'RECEPCIONISTA', 'VETERINARIO')")
    @Operation(summary = "Listar facturas por paciente")
    public ResponseEntity<List<FacturaDTO>> listarPorPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(facturaServicio.listarPorPaciente(pacienteId));
    }

    @GetMapping("/estado/{estado}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'RECEPCIONISTA')")
    @Operation(summary = "Listar facturas por estado")
    public ResponseEntity<List<FacturaDTO>> listarPorEstado(@PathVariable EstadoFactura estado) {
        return ResponseEntity.ok(facturaServicio.listarPorEstado(estado));
    }

    @GetMapping("/rango-fechas")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'RECEPCIONISTA')")
    @Operation(summary = "Listar facturas por rango de fechas")
    public ResponseEntity<List<FacturaDTO>> listarPorRangoFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return ResponseEntity.ok(facturaServicio.listarPorRangoFechas(inicio, fin));
    }

    @GetMapping("/vencidas")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'RECEPCIONISTA')")
    @Operation(summary = "Listar facturas vencidas")
    public ResponseEntity<List<FacturaDTO>> listarVencidas() {
        return ResponseEntity.ok(facturaServicio.listarVencidas());
    }

    @GetMapping("/saldo-pendiente")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'RECEPCIONISTA')")
    @Operation(summary = "Listar facturas con saldo pendiente")
    public ResponseEntity<List<FacturaDTO>> listarConSaldoPendiente() {
        return ResponseEntity.ok(facturaServicio.listarConSaldoPendiente());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'RECEPCIONISTA', 'VETERINARIO')")
    @Operation(summary = "Crear nueva factura")
    public ResponseEntity<FacturaDTO> crear(@Valid @RequestBody CrearFacturaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(facturaServicio.crear(dto));
    }

    @PatchMapping("/{id}/anular")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Operation(summary = "Anular factura")
    public ResponseEntity<FacturaDTO> anular(@PathVariable Long id, @RequestParam String motivo) {
        return ResponseEntity.ok(facturaServicio.anular(id, motivo));
    }

    @PostMapping("/actualizar-estados-vencidas")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Operation(summary = "Actualizar estados de facturas vencidas")
    public ResponseEntity<Void> actualizarEstadosVencidas() {
        facturaServicio.actualizarEstadosVencidas();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/reportes/total-ventas")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'RECEPCIONISTA')")
    @Operation(summary = "Calcular total de ventas")
    public ResponseEntity<BigDecimal> calcularTotalVentas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return ResponseEntity.ok(facturaServicio.calcularTotalVentas(inicio, fin));
    }

    @GetMapping("/reportes/cuentas-por-cobrar")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'RECEPCIONISTA')")
    @Operation(summary = "Calcular cuentas por cobrar")
    public ResponseEntity<BigDecimal> calcularCuentasPorCobrar() {
        return ResponseEntity.ok(facturaServicio.calcularCuentasPorCobrar());
    }

    @GetMapping("/estadisticas/por-estado")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'RECEPCIONISTA')")
    @Operation(summary = "Contar facturas por estado")
    public ResponseEntity<Map<EstadoFactura, Long>> contarPorEstado() {
        return ResponseEntity.ok(facturaServicio.contarPorEstado());
    }
}
