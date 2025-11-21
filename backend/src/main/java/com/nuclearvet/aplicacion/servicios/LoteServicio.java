package com.nuclearvet.aplicacion.servicios;

import com.nuclearvet.aplicacion.dtos.LoteDTO;
import com.nuclearvet.aplicacion.mapeadores.LoteMapeador;
import com.nuclearvet.compartido.excepciones.RecursoNoEncontradoExcepcion;
import com.nuclearvet.dominio.entidades.Lote;
import com.nuclearvet.dominio.entidades.Producto;
import com.nuclearvet.dominio.enumeraciones.EstadoLote;
import com.nuclearvet.infraestructura.persistencia.LoteRepositorio;
import com.nuclearvet.infraestructura.persistencia.ProductoRepositorio;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Servicio para gestión de lotes de inventario
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class LoteServicio {

    private final LoteRepositorio loteRepositorio;
    private final ProductoRepositorio productoRepositorio;
    private final AlertaInventarioServicio alertaServicio;
    private final LoteMapeador loteMapeador;

    @Transactional
    public LoteDTO crear(LoteDTO dto) {
        log.info("Creando nuevo lote: {}", dto.getNumeroLote());

        if (loteRepositorio.existsByNumeroLote(dto.getNumeroLote())) {
            throw new IllegalArgumentException("Ya existe un lote con ese número");
        }

        Producto producto = productoRepositorio.findById(dto.getProductoId())
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Producto no encontrado"));

        Lote lote = loteMapeador.aEntidad(dto);
        lote.setProducto(producto);
        lote.setCantidadInicial(dto.getCantidadDisponible());
        lote.actualizarEstado(); // Calcula el estado según fecha de vencimiento

        Lote guardado = loteRepositorio.save(lote);
        log.info("Lote creado con ID: {}", guardado.getId());

        // Generar alerta si está próximo a vencer
        if (guardado.getEstado() == EstadoLote.PROXIMO_VENCER) {
            alertaServicio.generarAlertaLoteProximoVencer(guardado);
        } else if (guardado.getEstado() == EstadoLote.VENCIDO) {
            alertaServicio.generarAlertaLoteVencido(guardado);
        }

        return loteMapeador.aDTO(guardado);
    }

    @Transactional
    public LoteDTO actualizar(Long id, LoteDTO dto) {
        log.info("Actualizando lote ID: {}", id);

        Lote lote = loteRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Lote no encontrado"));

        if (!lote.getNumeroLote().equals(dto.getNumeroLote()) &&
                loteRepositorio.existsByNumeroLote(dto.getNumeroLote())) {
            throw new IllegalArgumentException("Ya existe un lote con ese número");
        }

        lote.setNumeroLote(dto.getNumeroLote());
        lote.setFechaFabricacion(dto.getFechaFabricacion());
        lote.setFechaVencimiento(dto.getFechaVencimiento());
        lote.setPrecioCompraUnitario(dto.getPrecioCompraUnitario());
        lote.setObservaciones(dto.getObservaciones());
        lote.actualizarEstado();

        Lote actualizado = loteRepositorio.save(lote);
        log.info("Lote ID {} actualizado", id);

        return loteMapeador.aDTO(actualizado);
    }

    @Transactional(readOnly = true)
    public LoteDTO obtenerPorId(Long id) {
        Lote lote = loteRepositorio.findByIdConProducto(id)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Lote no encontrado"));
        return loteMapeador.aDTO(lote);
    }

    @Transactional(readOnly = true)
    public LoteDTO obtenerPorNumero(String numeroLote) {
        Lote lote = loteRepositorio.findByNumeroLote(numeroLote)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Lote no encontrado"));
        return loteMapeador.aDTO(lote);
    }

    @Transactional(readOnly = true)
    public List<LoteDTO> listarPorProducto(Long productoId) {
        return loteMapeador.aDTOLista(loteRepositorio.findByProductoIdOrderByFechaVencimiento(productoId));
    }

    @Transactional(readOnly = true)
    public List<LoteDTO> listarDisponibles() {
        return loteMapeador.aDTOLista(loteRepositorio.findLotesDisponibles());
    }

    @Transactional(readOnly = true)
    public List<LoteDTO> listarDisponiblesPorProducto(Long productoId) {
        return loteMapeador.aDTOLista(loteRepositorio.findLotesDisponiblesPorProducto(productoId));
    }

    @Transactional(readOnly = true)
    public List<LoteDTO> listarProximosVencer(int dias) {
        LocalDate fechaLimite = LocalDate.now().plusDays(dias);
        return loteMapeador.aDTOLista(loteRepositorio.findLotesProximosVencer(fechaLimite));
    }

    @Transactional(readOnly = true)
    public List<LoteDTO> listarVencidos() {
        return loteMapeador.aDTOLista(loteRepositorio.findLotesVencidos());
    }

    @Transactional(readOnly = true)
    public List<LoteDTO> listarPorEstado(EstadoLote estado) {
        return loteMapeador.aDTOLista(loteRepositorio.findByEstado(estado));
    }

    @Transactional
    public void actualizarEstados() {
        log.info("Actualizando estados de todos los lotes");
        List<Lote> lotes = loteRepositorio.findAll();
        
        for (Lote lote : lotes) {
            EstadoLote estadoAnterior = lote.getEstado();
            lote.actualizarEstado();
            
            if (estadoAnterior != lote.getEstado()) {
                loteRepositorio.save(lote);
                
                // Generar alertas según nuevo estado
                if (lote.getEstado() == EstadoLote.PROXIMO_VENCER) {
                    alertaServicio.generarAlertaLoteProximoVencer(lote);
                } else if (lote.getEstado() == EstadoLote.VENCIDO) {
                    alertaServicio.generarAlertaLoteVencido(lote);
                } else if (lote.getEstado() == EstadoLote.AGOTADO) {
                    log.info("Lote {} agotado", lote.getNumeroLote());
                }
            }
        }
        
        log.info("Estados de lotes actualizados");
    }

    @Transactional
    public void bloquear(Long id, String motivo) {
        Lote lote = loteRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Lote no encontrado"));

        lote.setEstado(EstadoLote.BLOQUEADO);
        lote.setObservaciones("BLOQUEADO: " + motivo);
        loteRepositorio.save(lote);
        log.warn("Lote ID {} bloqueado. Motivo: {}", id, motivo);
    }

    @Transactional
    public void desbloquear(Long id) {
        Lote lote = loteRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Lote no encontrado"));

        if (lote.getEstado() != EstadoLote.BLOQUEADO) {
            throw new IllegalStateException("El lote no está bloqueado");
        }

        lote.actualizarEstado(); // Recalcula el estado correcto
        loteRepositorio.save(lote);
        log.info("Lote ID {} desbloqueado", id);
    }

    @Transactional
    public void eliminar(Long id) {
        Lote lote = loteRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Lote no encontrado"));

        if (lote.getCantidadDisponible() > 0) {
            throw new IllegalStateException("No se puede eliminar un lote con stock disponible");
        }

        loteRepositorio.delete(lote);
        log.info("Lote ID {} eliminado", id);
    }

    @Transactional(readOnly = true)
    public BigDecimal calcularValorInventarioLote(Long id) {
        Lote lote = loteRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Lote no encontrado"));
        
        return lote.getPrecioCompraUnitario()
                .multiply(BigDecimal.valueOf(lote.getCantidadDisponible()));
    }
}
