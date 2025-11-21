package com.nuclearvet.aplicacion.servicios;

import com.nuclearvet.aplicacion.dtos.AlertaInventarioDTO;
import com.nuclearvet.aplicacion.mapeadores.AlertaInventarioMapeador;
import com.nuclearvet.compartido.excepciones.RecursoNoEncontradoExcepcion;
import com.nuclearvet.dominio.entidades.AlertaInventario;
import com.nuclearvet.dominio.entidades.Lote;
import com.nuclearvet.dominio.entidades.Producto;
import com.nuclearvet.dominio.enumeraciones.EstadoLote;
import com.nuclearvet.infraestructura.persistencia.AlertaInventarioRepositorio;
import com.nuclearvet.infraestructura.persistencia.LoteRepositorio;
import com.nuclearvet.infraestructura.persistencia.ProductoRepositorio;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio para gestión de alertas de inventario
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AlertaInventarioServicio {

    private final AlertaInventarioRepositorio alertaRepositorio;
    private final ProductoRepositorio productoRepositorio;
    private final LoteRepositorio loteRepositorio;
    private final AlertaInventarioMapeador alertaMapeador;

    @Transactional
    public void generarAlertaStockBajo(Producto producto) {
        log.info("Generando alerta de stock bajo para producto: {}", producto.getNombre());

        // Verificar si ya existe alerta activa para este producto
        boolean existeAlerta = alertaRepositorio.existsByProductoIdAndLeidaFalse(producto.getId());
        if (existeAlerta) {
            log.debug("Ya existe alerta activa de stock bajo para producto ID: {}", producto.getId());
            return;
        }

        String mensaje = String.format("Stock bajo para %s. Disponible: %d/%d unidades",
                producto.getNombre(),
                producto.getStockActual(),
                producto.getStockMinimo());

        AlertaInventario alerta = AlertaInventario.builder()
                .tipo("STOCK_BAJO")
                .mensaje(mensaje)
                .prioridad("ALTA")
                .leida(false)
                .producto(producto)
                .build();

        alertaRepositorio.save(alerta);
        log.info("Alerta de stock bajo generada para producto: {}", producto.getNombre());
    }

    @Transactional
    public void generarAlertaProductoAgotado(Producto producto) {
        log.info("Generando alerta de producto agotado: {}", producto.getNombre());

        boolean existeAlerta = alertaRepositorio.existsByProductoIdAndLeidaFalse(producto.getId());
        if (existeAlerta) {
            return;
        }

        String mensaje = String.format("Producto AGOTADO: %s. Stock actual: 0", producto.getNombre());

        AlertaInventario alerta = AlertaInventario.builder()
                .tipo("PRODUCTO_AGOTADO")
                .mensaje(mensaje)
                .prioridad("CRITICA")
                .leida(false)
                .producto(producto)
                .build();

        alertaRepositorio.save(alerta);
        log.warn("Alerta de producto agotado generada: {}", producto.getNombre());
    }

    @Transactional
    public void generarAlertaLoteProximoVencer(Lote lote) {
        log.info("Generando alerta de lote próximo a vencer: {}", lote.getNumeroLote());

        boolean existeAlerta = alertaRepositorio.existsByLoteIdAndLeidaFalse(lote.getId());
        if (existeAlerta) {
            return;
        }

        String mensaje = String.format("Lote %s del producto %s vence el %s (%d días)",
                lote.getNumeroLote(),
                lote.getProducto().getNombre(),
                lote.getFechaVencimiento(),
                lote.diasParaVencer());

        AlertaInventario alerta = AlertaInventario.builder()
                .tipo("PROXIMO_VENCER")
                .mensaje(mensaje)
                .prioridad("MEDIA")
                .leida(false)
                .lote(lote)
                .producto(lote.getProducto())
                .build();

        alertaRepositorio.save(alerta);
        log.info("Alerta de lote próximo a vencer generada");
    }

    @Transactional
    public void generarAlertaLoteVencido(Lote lote) {
        log.info("Generando alerta de lote vencido: {}", lote.getNumeroLote());

        boolean existeAlerta = alertaRepositorio.existsByLoteIdAndLeidaFalse(lote.getId());
        if (existeAlerta) {
            return;
        }

        String mensaje = String.format("Lote %s del producto %s VENCIDO desde %s",
                lote.getNumeroLote(),
                lote.getProducto().getNombre(),
                lote.getFechaVencimiento());

        AlertaInventario alerta = AlertaInventario.builder()
                .tipo("LOTE_VENCIDO")
                .mensaje(mensaje)
                .prioridad("CRITICA")
                .leida(false)
                .lote(lote)
                .producto(lote.getProducto())
                .build();

        alertaRepositorio.save(alerta);
        log.warn("Alerta de lote vencido generada");
    }

    @Transactional
    public void verificarYGenerarAlertas() {
        log.info("Verificando inventario para generar alertas automáticas");

        // Productos con stock bajo
        List<Producto> productosStockBajo = productoRepositorio.findProductosConStockBajo();
        for (Producto producto : productosStockBajo) {
            generarAlertaStockBajo(producto);
        }

        // Productos agotados
        List<Producto> productosAgotados = productoRepositorio.findProductosAgotados();
        for (Producto producto : productosAgotados) {
            generarAlertaProductoAgotado(producto);
        }

        // Lotes próximos a vencer
        LocalDate fechaLimite = LocalDate.now().plusDays(30);
        List<Lote> lotesProximosVencer = loteRepositorio.findLotesProximosVencer(fechaLimite);
        for (Lote lote : lotesProximosVencer) {
            if (lote.getEstado() == EstadoLote.PROXIMO_VENCER) {
                generarAlertaLoteProximoVencer(lote);
            }
        }

        // Lotes vencidos
        List<Lote> lotesVencidos = loteRepositorio.findLotesVencidos();
        for (Lote lote : lotesVencidos) {
            generarAlertaLoteVencido(lote);
        }

        log.info("Verificación de alertas completada");
    }

    @Transactional(readOnly = true)
    public AlertaInventarioDTO obtenerPorId(Long id) {
        AlertaInventario alerta = alertaRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Alerta no encontrada"));
        return alertaMapeador.aDTO(alerta);
    }

    @Transactional(readOnly = true)
    public List<AlertaInventarioDTO> listarNoLeidas() {
        return alertaMapeador.aDTOLista(alertaRepositorio.findNoLeidas());
    }

    @Transactional(readOnly = true)
    public List<AlertaInventarioDTO> listarPorPrioridad(String prioridad) {
        return alertaMapeador.aDTOLista(alertaRepositorio.findByPrioridadAndLeidaFalse(prioridad));
    }

    @Transactional(readOnly = true)
    public List<AlertaInventarioDTO> listarPorProducto(Long productoId) {
        return alertaMapeador.aDTOLista(alertaRepositorio.findByProductoId(productoId));
    }

    @Transactional(readOnly = true)
    public List<AlertaInventarioDTO> listarRecientes(int dias) {
        LocalDateTime fecha = LocalDateTime.now().minusDays(dias);
        return alertaMapeador.aDTOLista(alertaRepositorio.findAlertasRecientes(fecha));
    }

    @Transactional
    public void marcarComoLeida(Long id) {
        AlertaInventario alerta = alertaRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Alerta no encontrada"));
        alerta.marcarComoLeida();
        alertaRepositorio.save(alerta);
        log.info("Alerta ID {} marcada como leída", id);
    }

    @Transactional
    public void marcarTodasComoLeidas() {
        alertaRepositorio.marcarTodasComoLeidas();
        log.info("Todas las alertas marcadas como leídas");
    }

    @Transactional
    public void eliminar(Long id) {
        if (!alertaRepositorio.existsById(id)) {
            throw new RecursoNoEncontradoExcepcion("Alerta no encontrada");
        }
        alertaRepositorio.deleteById(id);
        log.info("Alerta ID {} eliminada", id);
    }

    @Transactional(readOnly = true)
    public long contarNoLeidas() {
        return alertaRepositorio.countByLeidaFalse();
    }
}
