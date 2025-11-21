package com.nuclearvet.dominio.servicios;

import com.nuclearvet.aplicacion.dto.administrativo.CrearFacturaDTO;
import com.nuclearvet.aplicacion.dto.administrativo.FacturaDTO;
import com.nuclearvet.aplicacion.mapeadores.FacturaMapeador;
import com.nuclearvet.compartido.excepciones.RecursoNoEncontradoExcepcion;
import com.nuclearvet.dominio.entidades.*;
import com.nuclearvet.dominio.enumeraciones.EstadoFactura;
import com.nuclearvet.infraestructura.persistencia.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FacturaServicio {

    private final FacturaRepositorio facturaRepositorio;
    private final PropietarioRepositorio propietarioRepositorio;
    private final PacienteRepositorio pacienteRepositorio;
    private final UsuarioRepositorio usuarioRepositorio;
    private final ServicioRepositorio servicioRepositorio;
    private final ProductoRepositorio productoRepositorio;
    private final FacturaMapeador facturaMapeador;

    @Transactional(readOnly = true)
    public List<FacturaDTO> listarTodas() {
        log.debug("Listando todas las facturas");
        return facturaRepositorio.findAll().stream()
                .map(facturaMapeador::aDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FacturaDTO obtenerPorId(Long id) {
        log.debug("Obteniendo factura con ID: {}", id);
        Factura factura = buscarFacturaPorId(id);
        return facturaMapeador.aDTO(factura);
    }

    @Transactional(readOnly = true)
    public FacturaDTO obtenerPorNumero(String numero) {
        log.debug("Obteniendo factura con número: {}", numero);
        Factura factura = facturaRepositorio.findByNumeroFactura(numero)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Factura " + numero + " no encontrada"));
        return facturaMapeador.aDTO(factura);
    }

    @Transactional(readOnly = true)
    public List<FacturaDTO> listarPorEstado(EstadoFactura estado) {
        log.debug("Listando facturas por estado: {}", estado);
        return facturaRepositorio.findByEstadoOrdenadas(estado).stream()
                .map(facturaMapeador::aDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FacturaDTO> listarPorPropietario(Long propietarioId) {
        log.debug("Listando facturas para propietario: {}", propietarioId);
        return facturaRepositorio.findByPropietarioOrdenadas(propietarioId).stream()
                .map(facturaMapeador::aDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FacturaDTO> listarPorPaciente(Long pacienteId) {
        log.debug("Listando facturas para paciente: {}", pacienteId);
        return facturaRepositorio.findByPacienteId(pacienteId).stream()
                .map(facturaMapeador::aDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FacturaDTO> listarPorRangoFechas(LocalDate inicio, LocalDate fin) {
        log.debug("Listando facturas entre {} y {}", inicio, fin);
        return facturaRepositorio.findByRangoFechas(inicio, fin).stream()
                .map(facturaMapeador::aDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FacturaDTO> listarVencidas() {
        log.debug("Listando facturas vencidas");
        return facturaRepositorio.findFacturasVencidas().stream()
                .map(facturaMapeador::aDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FacturaDTO> listarConSaldoPendiente() {
        log.debug("Listando facturas con saldo pendiente");
        return facturaRepositorio.findConSaldoPendiente().stream()
                .map(facturaMapeador::aDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public FacturaDTO crear(CrearFacturaDTO dto) {
        log.info("Creando nueva factura para propietario: {}", dto.getPropietarioId());

        // Verificar propietario
        Propietario propietario = propietarioRepositorio.findById(dto.getPropietarioId())
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Propietario no encontrado"));

        // Verificar paciente si aplica
        Paciente paciente = null;
        if (dto.getPacienteId() != null) {
            paciente = pacienteRepositorio.findById(dto.getPacienteId())
                    .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Paciente no encontrado"));
        }

        // Obtener usuario actual
        Usuario usuarioCreador = obtenerUsuarioActual();

        // Crear factura
        Factura factura = Factura.builder()
                .numeroFactura(generarNumeroFactura())
                .propietario(propietario)
                .paciente(paciente)
                .fechaEmision(dto.getFechaEmision())
                .fechaVencimiento(dto.getFechaVencimiento())
                .estado(EstadoFactura.PENDIENTE)
                .descuento(dto.getDescuento() != null ? dto.getDescuento() : BigDecimal.ZERO)
                .observaciones(dto.getObservaciones())
                .usuarioCreador(usuarioCreador)
                .build();

        // Agregar items
        dto.getItems().forEach(itemDTO -> {
            ItemFactura item = ItemFactura.builder()
                    .descripcion(itemDTO.getDescripcion())
                    .cantidad(itemDTO.getCantidad())
                    .precioUnitario(itemDTO.getPrecioUnitario())
                    .tipoImpuesto(itemDTO.getTipoImpuesto())
                    .descuento(itemDTO.getDescuento() != null ? itemDTO.getDescuento() : BigDecimal.ZERO)
                    .observaciones(itemDTO.getObservaciones())
                    .build();

            // Asociar servicio o producto
            if (itemDTO.getServicioId() != null) {
                com.nuclearvet.dominio.entidades.Servicio servicio = servicioRepositorio.findById(itemDTO.getServicioId())
                        .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Servicio no encontrado"));
                item.setServicio(servicio);
            } else if (itemDTO.getProductoId() != null) {
                Producto producto = productoRepositorio.findById(itemDTO.getProductoId())
                        .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Producto no encontrado"));
                item.setProducto(producto);
            }

            factura.agregarItem(item);
        });

        Factura facturaGuardada = facturaRepositorio.save(factura);
        log.info("Factura creada exitosamente: {}", facturaGuardada.getNumeroFactura());
        return facturaMapeador.aDTO(facturaGuardada);
    }

    @Transactional
    public FacturaDTO anular(Long id, String motivo) {
        log.info("Anulando factura con ID: {}", id);
        Factura factura = buscarFacturaPorId(id);
        
        if (factura.getEstado() == EstadoFactura.ANULADA) {
            throw new IllegalStateException("La factura ya está anulada");
        }
        
        if (factura.getPagos() != null && !factura.getPagos().isEmpty()) {
            throw new IllegalStateException("No se puede anular una factura con pagos registrados");
        }

        factura.setEstado(EstadoFactura.ANULADA);
        factura.setObservaciones(factura.getObservaciones() + "\nANULADA: " + motivo);
        
        return facturaMapeador.aDTO(facturaRepositorio.save(factura));
    }

    @Transactional
    public void actualizarEstadosVencidas() {
        log.info("Actualizando estados de facturas vencidas");
        List<Factura> vencidas = facturaRepositorio.findFacturasVencidas();
        
        vencidas.forEach(factura -> {
            if (factura.getEstado() != EstadoFactura.VENCIDA) {
                factura.setEstado(EstadoFactura.VENCIDA);
                facturaRepositorio.save(factura);
            }
        });
        
        log.info("Actualizadas {} facturas vencidas", vencidas.size());
    }

    @Transactional(readOnly = true)
    public BigDecimal calcularTotalVentas(LocalDate inicio, LocalDate fin) {
        BigDecimal total = facturaRepositorio.calcularTotalVentas(inicio, fin);
        return total != null ? total : BigDecimal.ZERO;
    }

    @Transactional(readOnly = true)
    public BigDecimal calcularCuentasPorCobrar() {
        BigDecimal total = facturaRepositorio.calcularTotalCuentasPorCobrar();
        return total != null ? total : BigDecimal.ZERO;
    }

    @Transactional(readOnly = true)
    public Map<EstadoFactura, Long> contarPorEstado() {
        log.debug("Contando facturas por estado");
        return facturaRepositorio.contarPorEstados().stream()
                .collect(Collectors.toMap(
                        obj -> (EstadoFactura) obj[0],
                        obj -> (Long) obj[1]
                ));
    }

    private String generarNumeroFactura() {
        String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long count = facturaRepositorio.count() + 1;
        return String.format("FAC-%s-%05d", fecha, count);
    }

    private Usuario obtenerUsuarioActual() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return usuarioRepositorio.findByCorreoElectronico(email)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Usuario actual no encontrado"));
    }

    private Factura buscarFacturaPorId(Long id) {
        return facturaRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Factura con ID " + id + " no encontrada"));
    }
}
