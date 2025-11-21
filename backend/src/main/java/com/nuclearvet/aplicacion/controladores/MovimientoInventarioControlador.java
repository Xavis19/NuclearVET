package com.nuclearvet.aplicacion.controladores;

import com.nuclearvet.aplicacion.dtos.CrearMovimientoEntradaDTO;
import com.nuclearvet.aplicacion.dtos.CrearMovimientoSalidaDTO;
import com.nuclearvet.aplicacion.dtos.MovimientoInventarioDTO;
import com.nuclearvet.aplicacion.servicios.MovimientoInventarioServicio;
import com.nuclearvet.dominio.enumeraciones.TipoMovimiento;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controlador REST para gestión de movimientos de inventario
 */
@RestController
@RequestMapping("/api/inventario/movimientos")
@RequiredArgsConstructor
@Tag(name = "Movimientos de Inventario", description = "API para registro y consulta de movimientos de inventario")
public class MovimientoInventarioControlador {

    private final MovimientoInventarioServicio movimientoServicio;

    @PostMapping("/entrada")
    @Operation(summary = "Registrar entrada", description = "Registra una entrada de mercancía al inventario")
    public ResponseEntity<MovimientoInventarioDTO> registrarEntrada(
            @Valid @RequestBody CrearMovimientoEntradaDTO dto,
            HttpServletRequest request) {
        return new ResponseEntity<>(movimientoServicio.registrarEntrada(dto, request), HttpStatus.CREATED);
    }

    @PostMapping("/salida")
    @Operation(summary = "Registrar salida", description = "Registra una salida de mercancía del inventario")
    public ResponseEntity<MovimientoInventarioDTO> registrarSalida(
            @Valid @RequestBody CrearMovimientoSalidaDTO dto,
            HttpServletRequest request) {
        return new ResponseEntity<>(movimientoServicio.registrarSalida(dto, request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener movimiento por ID", description = "Obtiene un movimiento por su identificador")
    public ResponseEntity<MovimientoInventarioDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(movimientoServicio.obtenerPorId(id));
    }

    @GetMapping("/producto/{productoId}")
    @Operation(summary = "Listar por producto", description = "Lista todos los movimientos de un producto")
    public ResponseEntity<List<MovimientoInventarioDTO>> listarPorProducto(@PathVariable Long productoId) {
        return ResponseEntity.ok(movimientoServicio.listarPorProducto(productoId));
    }

    @GetMapping("/fechas")
    @Operation(summary = "Listar por rango de fechas", description = "Lista movimientos en un rango de fechas")
    public ResponseEntity<List<MovimientoInventarioDTO>> listarPorFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return ResponseEntity.ok(movimientoServicio.listarPorFechas(inicio, fin));
    }

    @GetMapping("/tipo/{tipo}")
    @Operation(summary = "Listar por tipo", description = "Lista movimientos de un tipo específico")
    public ResponseEntity<List<MovimientoInventarioDTO>> listarPorTipo(@PathVariable TipoMovimiento tipo) {
        return ResponseEntity.ok(movimientoServicio.listarPorTipo(tipo));
    }

    @GetMapping("/recientes")
    @Operation(summary = "Movimientos recientes", description = "Lista los movimientos de los últimos N días")
    public ResponseEntity<List<MovimientoInventarioDTO>> listarRecientes(
            @RequestParam(defaultValue = "7") int dias) {
        return ResponseEntity.ok(movimientoServicio.listarRecientes(dias));
    }
}
