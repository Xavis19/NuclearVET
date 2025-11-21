package com.nuclearvet.dominio.servicios;

import com.nuclearvet.aplicacion.dto.administrativo.PagoDTO;
import com.nuclearvet.aplicacion.dto.administrativo.RegistrarPagoDTO;
import com.nuclearvet.aplicacion.mapeadores.PagoMapeador;
import com.nuclearvet.compartido.excepciones.RecursoNoEncontradoExcepcion;
import com.nuclearvet.dominio.entidades.Factura;
import com.nuclearvet.dominio.entidades.Pago;
import com.nuclearvet.dominio.entidades.Usuario;
import com.nuclearvet.dominio.enumeraciones.MetodoPago;
import com.nuclearvet.infraestructura.persistencia.FacturaRepositorio;
import com.nuclearvet.infraestructura.persistencia.PagoRepositorio;
import com.nuclearvet.infraestructura.persistencia.UsuarioRepositorio;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PagoServicio {

    private final PagoRepositorio pagoRepositorio;
    private final FacturaRepositorio facturaRepositorio;
    private final UsuarioRepositorio usuarioRepositorio;
    private final PagoMapeador pagoMapeador;

    @Transactional(readOnly = true)
    public List<PagoDTO> listarTodos() {
        log.debug("Listando todos los pagos");
        return pagoRepositorio.findAll().stream()
                .map(pagoMapeador::aDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PagoDTO obtenerPorId(Long id) {
        log.debug("Obteniendo pago con ID: {}", id);
        Pago pago = buscarPagoPorId(id);
        return pagoMapeador.aDTO(pago);
    }

    @Transactional(readOnly = true)
    public PagoDTO obtenerPorNumero(String numero) {
        log.debug("Obteniendo pago con número: {}", numero);
        Pago pago = pagoRepositorio.findByNumeroPago(numero)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Pago " + numero + " no encontrado"));
        return pagoMapeador.aDTO(pago);
    }

    @Transactional(readOnly = true)
    public List<PagoDTO> listarPorFactura(Long facturaId) {
        log.debug("Listando pagos para factura: {}", facturaId);
        return pagoRepositorio.findByFacturaOrdenados(facturaId).stream()
                .map(pagoMapeador::aDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PagoDTO> listarPorMetodoPago(MetodoPago metodo) {
        log.debug("Listando pagos por método: {}", metodo);
        return pagoRepositorio.findByMetodoPago(metodo).stream()
                .map(pagoMapeador::aDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PagoDTO> listarPorRangoFechas(LocalDateTime inicio, LocalDateTime fin) {
        log.debug("Listando pagos entre {} y {}", inicio, fin);
        return pagoRepositorio.findByRangoFechas(inicio, fin).stream()
                .map(pagoMapeador::aDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PagoDTO> listarPorPropietario(Long propietarioId) {
        log.debug("Listando pagos para propietario: {}", propietarioId);
        return pagoRepositorio.findByPropietarioId(propietarioId).stream()
                .map(pagoMapeador::aDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public PagoDTO registrar(RegistrarPagoDTO dto) {
        log.info("Registrando nuevo pago para factura: {}", dto.getFacturaId());

        // Verificar factura
        Factura factura = facturaRepositorio.findById(dto.getFacturaId())
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Factura no encontrada"));

        // Validar que el monto no exceda el saldo pendiente
        if (dto.getMonto().compareTo(factura.getSaldoPendiente()) > 0) {
            throw new IllegalArgumentException("El monto del pago excede el saldo pendiente de la factura");
        }

        // Obtener usuario actual
        Usuario usuarioRegistro = obtenerUsuarioActual();

        // Crear pago
        Pago pago = Pago.builder()
                .numeroPago(generarNumeroPago())
                .factura(factura)
                .fechaPago(dto.getFechaPago())
                .monto(dto.getMonto())
                .metodoPago(dto.getMetodoPago())
                .referencia(dto.getReferencia())
                .observaciones(dto.getObservaciones())
                .usuarioRegistro(usuarioRegistro)
                .build();

        Pago pagoGuardado = pagoRepositorio.save(pago);

        // Actualizar factura
        factura.recalcularSaldoPendiente();
        factura.actualizarEstado();
        facturaRepositorio.save(factura);

        log.info("Pago registrado exitosamente: {}", pagoGuardado.getNumeroPago());
        return pagoMapeador.aDTO(pagoGuardado);
    }

    @Transactional(readOnly = true)
    public BigDecimal calcularTotalPagos(LocalDateTime inicio, LocalDateTime fin) {
        BigDecimal total = pagoRepositorio.calcularTotalPagos(inicio, fin);
        return total != null ? total : BigDecimal.ZERO;
    }

    @Transactional(readOnly = true)
    public Map<MetodoPago, BigDecimal> calcularPagosPorMetodo(LocalDateTime inicio, LocalDateTime fin) {
        log.debug("Calculando pagos por método entre {} y {}", inicio, fin);
        return pagoRepositorio.calcularPagosPorMetodo(inicio, fin).stream()
                .collect(Collectors.toMap(
                        obj -> (MetodoPago) obj[0],
                        obj -> (BigDecimal) obj[1]
                ));
    }

    @Transactional(readOnly = true)
    public Long contarPorMetodo(MetodoPago metodo) {
        return pagoRepositorio.contarPorMetodoPago(metodo);
    }

    private String generarNumeroPago() {
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        long count = pagoRepositorio.count() + 1;
        return String.format("PAG-%s-%04d", fecha, count);
    }

    private Usuario obtenerUsuarioActual() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return usuarioRepositorio.findByCorreoElectronico(email)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Usuario actual no encontrado"));
    }

    private Pago buscarPagoPorId(Long id) {
        return pagoRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Pago con ID " + id + " no encontrado"));
    }
}
