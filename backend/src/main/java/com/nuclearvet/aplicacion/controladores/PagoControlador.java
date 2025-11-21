package com.nuclearvet.aplicacion.controladores;

import com.nuclearvet.aplicacion.dto.administrativo.PagoDTO;
import com.nuclearvet.aplicacion.dto.administrativo.RegistrarPagoDTO;
import com.nuclearvet.dominio.enumeraciones.MetodoPago;
import com.nuclearvet.dominio.servicios.PagoServicio;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
@Tag(name = "Pagos", description = "Gestión de pagos y registro de transacciones")
public class PagoControlador {

    private final PagoServicio pagoServicio;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'RECEPCIONISTA')")
    @Operation(summary = "Listar todos los pagos")
    public ResponseEntity<List<PagoDTO>> listarTodos() {
        return ResponseEntity.ok(pagoServicio.listarTodos());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'RECEPCIONISTA')")
    @Operation(summary = "Obtener pago por ID")
    public ResponseEntity<PagoDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pagoServicio.obtenerPorId(id));
    }

    @GetMapping("/numero/{numero}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'RECEPCIONISTA')")
    @Operation(summary = "Obtener pago por número")
    public ResponseEntity<PagoDTO> obtenerPorNumero(@PathVariable String numero) {
        return ResponseEntity.ok(pagoServicio.obtenerPorNumero(numero));
    }

    @GetMapping("/factura/{facturaId}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'RECEPCIONISTA', 'VETERINARIO')")
    @Operation(summary = "Listar pagos por factura")
    public ResponseEntity<List<PagoDTO>> listarPorFactura(@PathVariable Long facturaId) {
        return ResponseEntity.ok(pagoServicio.listarPorFactura(facturaId));
    }

    @GetMapping("/metodo/{metodo}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'RECEPCIONISTA')")
    @Operation(summary = "Listar pagos por método de pago")
    public ResponseEntity<List<PagoDTO>> listarPorMetodoPago(@PathVariable MetodoPago metodo) {
        return ResponseEntity.ok(pagoServicio.listarPorMetodoPago(metodo));
    }

    @GetMapping("/rango-fechas")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'RECEPCIONISTA')")
    @Operation(summary = "Listar pagos por rango de fechas")
    public ResponseEntity<List<PagoDTO>> listarPorRangoFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return ResponseEntity.ok(pagoServicio.listarPorRangoFechas(inicio, fin));
    }

    @GetMapping("/propietario/{propietarioId}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'RECEPCIONISTA', 'VETERINARIO')")
    @Operation(summary = "Listar pagos por propietario")
    public ResponseEntity<List<PagoDTO>> listarPorPropietario(@PathVariable Long propietarioId) {
        return ResponseEntity.ok(pagoServicio.listarPorPropietario(propietarioId));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'RECEPCIONISTA')")
    @Operation(summary = "Registrar nuevo pago")
    public ResponseEntity<PagoDTO> registrar(@Valid @RequestBody RegistrarPagoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pagoServicio.registrar(dto));
    }

    @GetMapping("/reportes/total")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'RECEPCIONISTA')")
    @Operation(summary = "Calcular total de pagos")
    public ResponseEntity<BigDecimal> calcularTotalPagos(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return ResponseEntity.ok(pagoServicio.calcularTotalPagos(inicio, fin));
    }

    @GetMapping("/reportes/por-metodo")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'RECEPCIONISTA')")
    @Operation(summary = "Calcular pagos por método de pago")
    public ResponseEntity<Map<MetodoPago, BigDecimal>> calcularPagosPorMetodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return ResponseEntity.ok(pagoServicio.calcularPagosPorMetodo(inicio, fin));
    }

    @GetMapping("/estadisticas/por-metodo")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'RECEPCIONISTA')")
    @Operation(summary = "Contar pagos por método")
    public ResponseEntity<Long> contarPorMetodo(@RequestParam MetodoPago metodo) {
        return ResponseEntity.ok(pagoServicio.contarPorMetodo(metodo));
    }
}
