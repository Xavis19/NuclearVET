package com.nuclearvet.aplicacion.servicios;

import com.nuclearvet.aplicacion.dtos.CrearMovimientoEntradaDTO;
import com.nuclearvet.aplicacion.dtos.CrearMovimientoSalidaDTO;
import com.nuclearvet.aplicacion.dtos.MovimientoInventarioDTO;
import com.nuclearvet.aplicacion.mapeadores.MovimientoInventarioMapeador;
import com.nuclearvet.compartido.excepciones.RecursoNoEncontradoExcepcion;
import com.nuclearvet.dominio.entidades.*;
import com.nuclearvet.dominio.enumeraciones.TipoMovimiento;
import com.nuclearvet.infraestructura.persistencia.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Servicio para gestión de movimientos de inventario
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MovimientoInventarioServicio {

    private final MovimientoInventarioRepositorio movimientoRepositorio;
    private final ProductoRepositorio productoRepositorio;
    private final LoteRepositorio loteRepositorio;
    private final UsuarioRepositorio usuarioRepositorio;
    private final AlertaInventarioServicio alertaServicio;
    private final MovimientoInventarioMapeador movimientoMapeador;

    @Transactional
    public MovimientoInventarioDTO registrarEntrada(CrearMovimientoEntradaDTO dto, HttpServletRequest request) {
        log.info("Registrando entrada de inventario para producto ID: {}", dto.getProductoId());

        Producto producto = productoRepositorio.findById(dto.getProductoId())
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Producto no encontrado"));

        Lote lote = loteRepositorio.findById(dto.getLoteId())
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Lote no encontrado"));

        Usuario usuario = obtenerUsuarioActual(request);

        int stockAnterior = producto.getStockActual();
        int stockNuevo = stockAnterior + dto.getCantidad();

        MovimientoInventario movimiento = MovimientoInventario.builder()
                .numeroMovimiento(generarNumeroMovimiento(TipoMovimiento.ENTRADA_COMPRA))
                .tipoMovimiento(TipoMovimiento.ENTRADA_COMPRA)
                .cantidad(dto.getCantidad())
                .precioUnitario(lote.getPrecioCompraUnitario())
                .stockAnterior(stockAnterior)
                .stockNuevo(stockNuevo)
                .numeroDocumento(dto.getNumeroDocumento())
                .observaciones(dto.getObservaciones())
                .producto(producto)
                .lote(lote)
                .usuario(usuario)
                .build();

        // Actualizar stock del producto y lote
        producto.actualizarStock(dto.getCantidad());
        lote.agregarCantidad(dto.getCantidad());

        productoRepositorio.save(producto);
        loteRepositorio.save(lote);
        MovimientoInventario guardado = movimientoRepositorio.save(movimiento);

        log.info("Entrada registrada. Stock actualizado de {} a {}", stockAnterior, stockNuevo);

        return movimientoMapeador.aDTO(guardado);
    }

    @Transactional
    public MovimientoInventarioDTO registrarSalida(CrearMovimientoSalidaDTO dto, HttpServletRequest request) {
        log.info("Registrando salida de inventario para producto ID: {}", dto.getProductoId());

        Producto producto = productoRepositorio.findById(dto.getProductoId())
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Producto no encontrado"));

        if (!producto.tieneStock(dto.getCantidad())) {
            throw new IllegalArgumentException("Stock insuficiente. Disponible: " + producto.getStockActual());
        }

        Usuario usuario = obtenerUsuarioActual(request);

        // Seleccionar lote automáticamente si no se especifica (FIFO)
        Lote lote = null;
        if (dto.getLoteId() != null) {
            lote = loteRepositorio.findById(dto.getLoteId())
                    .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Lote no encontrado"));
            
            if (!lote.tieneDisponible(dto.getCantidad())) {
                throw new IllegalArgumentException("Lote no tiene cantidad suficiente");
            }
        } else {
            // Seleccionar primer lote disponible (FIFO)
            List<Lote> lotesDisponibles = loteRepositorio.findLotesDisponiblesPorProducto(dto.getProductoId());
            for (Lote l : lotesDisponibles) {
                if (l.tieneDisponible(dto.getCantidad())) {
                    lote = l;
                    break;
                }
            }
            if (lote == null) {
                throw new IllegalArgumentException("No hay lotes disponibles con cantidad suficiente");
            }
        }

        int stockAnterior = producto.getStockActual();
        int stockNuevo = stockAnterior - dto.getCantidad();

        MovimientoInventario movimiento = MovimientoInventario.builder()
                .numeroMovimiento(generarNumeroMovimiento(dto.getTipoSalida()))
                .tipoMovimiento(dto.getTipoSalida())
                .cantidad(dto.getCantidad())
                .precioUnitario(producto.getPrecioVenta())
                .stockAnterior(stockAnterior)
                .stockNuevo(stockNuevo)
                .numeroDocumento(dto.getNumeroDocumento())
                .observaciones(dto.getObservaciones())
                .producto(producto)
                .lote(lote)
                .usuario(usuario)
                .build();

        // Actualizar stock del producto y lote
        producto.actualizarStock(-dto.getCantidad());
        lote.descontarCantidad(dto.getCantidad());

        productoRepositorio.save(producto);
        loteRepositorio.save(lote);
        MovimientoInventario guardado = movimientoRepositorio.save(movimiento);

        // Generar alerta si stock bajo
        if (producto.stockBajo()) {
            alertaServicio.generarAlertaStockBajo(producto);
        }

        log.info("Salida registrada. Stock actualizado de {} a {}", stockAnterior, stockNuevo);

        return movimientoMapeador.aDTO(guardado);
    }

    @Transactional(readOnly = true)
    public MovimientoInventarioDTO obtenerPorId(Long id) {
        MovimientoInventario movimiento = movimientoRepositorio.findByIdConRelaciones(id)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Movimiento no encontrado"));
        return movimientoMapeador.aDTO(movimiento);
    }

    @Transactional(readOnly = true)
    public List<MovimientoInventarioDTO> listarPorProducto(Long productoId) {
        return movimientoMapeador.aDTOLista(
                movimientoRepositorio.findByProductoIdOrdenadoPorFecha(productoId));
    }

    @Transactional(readOnly = true)
    public List<MovimientoInventarioDTO> listarPorFechas(LocalDateTime inicio, LocalDateTime fin) {
        return movimientoMapeador.aDTOLista(
                movimientoRepositorio.findByFechaMovimientoBetween(inicio, fin));
    }

    @Transactional(readOnly = true)
    public List<MovimientoInventarioDTO> listarPorTipo(TipoMovimiento tipo) {
        return movimientoMapeador.aDTOLista(movimientoRepositorio.findByTipoMovimiento(tipo));
    }

    @Transactional(readOnly = true)
    public List<MovimientoInventarioDTO> listarRecientes(int dias) {
        LocalDateTime fecha = LocalDateTime.now().minusDays(dias);
        return movimientoMapeador.aDTOLista(movimientoRepositorio.findMovimientosRecientes(fecha));
    }

    private String generarNumeroMovimiento(TipoMovimiento tipo) {
        String prefijo = tipo.name().startsWith("ENTRADA") ? "ENT" : "SAL";
        String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        Long cantidad = movimientoRepositorio.count();
        return String.format("MOV-%s-%s-%05d", prefijo, fecha, cantidad + 1);
    }

    private Usuario obtenerUsuarioActual(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        return usuarioRepositorio.findByEmail(username)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Usuario no encontrado"));
    }
}
