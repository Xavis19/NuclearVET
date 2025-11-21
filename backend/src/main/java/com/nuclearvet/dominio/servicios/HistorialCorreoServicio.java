package com.nuclearvet.dominio.servicios;

import com.nuclearvet.aplicacion.dto.notificaciones.HistorialCorreoDTO;
import com.nuclearvet.aplicacion.mapeadores.HistorialCorreoMapeador;
import com.nuclearvet.compartido.excepciones.RecursoNoEncontradoExcepcion;
import com.nuclearvet.dominio.entidades.HistorialCorreo;
import com.nuclearvet.dominio.entidades.PlantillaMensaje;
import com.nuclearvet.dominio.enumeraciones.EstadoCorreo;
import com.nuclearvet.infraestructura.persistencia.HistorialCorreoRepositorio;
import com.nuclearvet.infraestructura.persistencia.PlantillaMensajeRepositorio;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Servicio para gestión de historial de correos
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HistorialCorreoServicio {

    private final HistorialCorreoRepositorio historialRepositorio;
    private final PlantillaMensajeRepositorio plantillaRepositorio;
    private final HistorialCorreoMapeador historialMapeador;

    @Transactional(readOnly = true)
    public List<HistorialCorreoDTO> listarTodos() {
        log.debug("Listando todo el historial de correos");
        return historialRepositorio.findAll().stream()
                .map(historialMapeador::aDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<HistorialCorreoDTO> listarPorDestinatario(String email) {
        log.debug("Listando historial de correos para destinatario: {}", email);
        return historialRepositorio.findByDestinatarioConPlantilla(email).stream()
                .map(historialMapeador::aDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<HistorialCorreoDTO> listarPorEstado(EstadoCorreo estado) {
        log.debug("Listando correos por estado: {}", estado);
        return historialRepositorio.findByEstado(estado).stream()
                .map(historialMapeador::aDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<HistorialCorreoDTO> listarPendientes() {
        log.debug("Listando correos pendientes");
        return historialRepositorio.findPendientesDeEnvio().stream()
                .map(historialMapeador::aDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<HistorialCorreoDTO> listarConError() {
        log.debug("Listando correos con error");
        return historialRepositorio.findConError().stream()
                .map(historialMapeador::aDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<HistorialCorreoDTO> listarPorPlantilla(Long plantillaId) {
        log.debug("Listando correos por plantilla: {}", plantillaId);
        return historialRepositorio.findByPlantillaOrdenados(plantillaId).stream()
                .map(historialMapeador::aDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<HistorialCorreoDTO> listarRecientes(int dias) {
        log.debug("Listando correos recientes (últimos {} días)", dias);
        LocalDateTime fecha = LocalDateTime.now().minusDays(dias);
        return historialRepositorio.findRecientes(fecha).stream()
                .map(historialMapeador::aDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<HistorialCorreoDTO> listarPorRangoFechas(LocalDateTime inicio, LocalDateTime fin) {
        log.debug("Listando correos entre {} y {}", inicio, fin);
        return historialRepositorio.findByRangoFechasEnvio(inicio, fin).stream()
                .map(historialMapeador::aDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public HistorialCorreoDTO obtenerPorId(Long id) {
        log.debug("Obteniendo historial de correo con ID: {}", id);
        HistorialCorreo historial = buscarHistorialPorIdConPlantilla(id);
        return historialMapeador.aDTO(historial);
    }

    @Transactional
    public HistorialCorreoDTO registrar(String destinatarioEmail, String asunto, String contenido, Long plantillaId) {
        log.info("Registrando correo para destinatario: {}", destinatarioEmail);
        
        HistorialCorreo historial = HistorialCorreo.builder()
                .destinatarioEmail(destinatarioEmail)
                .asunto(asunto)
                .contenido(contenido)
                .estado(EstadoCorreo.PENDIENTE)
                .build();
        
        if (plantillaId != null) {
            PlantillaMensaje plantilla = plantillaRepositorio.findById(plantillaId)
                    .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Plantilla con ID " + plantillaId + " no encontrada"));
            historial.setPlantilla(plantilla);
        }
        
        HistorialCorreo historialGuardado = historialRepositorio.save(historial);
        log.info("Correo registrado exitosamente con ID: {}", historialGuardado.getId());
        return historialMapeador.aDTO(historialGuardado);
    }

    @Transactional
    public HistorialCorreoDTO marcarComoEnviado(Long id) {
        log.info("Marcando correo como enviado: {}", id);
        HistorialCorreo historial = buscarHistorialPorId(id);
        historial.marcarComoEnviado();
        HistorialCorreo historialActualizado = historialRepositorio.save(historial);
        return historialMapeador.aDTO(historialActualizado);
    }

    @Transactional
    public HistorialCorreoDTO marcarComoError(Long id, String mensajeError) {
        log.warn("Marcando correo con error: {} - {}", id, mensajeError);
        HistorialCorreo historial = buscarHistorialPorId(id);
        historial.marcarComoError(mensajeError);
        HistorialCorreo historialActualizado = historialRepositorio.save(historial);
        return historialMapeador.aDTO(historialActualizado);
    }

    @Transactional
    public HistorialCorreoDTO marcarComoReintentando(Long id) {
        log.info("Marcando correo para reintento: {}", id);
        HistorialCorreo historial = buscarHistorialPorId(id);
        historial.marcarComoReintentando();
        HistorialCorreo historialActualizado = historialRepositorio.save(historial);
        return historialMapeador.aDTO(historialActualizado);
    }

    @Transactional
    public void reintentarEnviosFallidos() {
        log.info("Reintentando envíos fallidos");
        List<HistorialCorreo> correosFallidos = historialRepositorio.findConError();
        
        for (HistorialCorreo correo : correosFallidos) {
            // Aquí se implementaría la lógica de reintento de envío
            log.info("Reintentando envío de correo ID: {} a {}", correo.getId(), correo.getDestinatarioEmail());
            correo.marcarComoReintentando();
            historialRepositorio.save(correo);
        }
        
        log.info("Procesados {} correos fallidos para reintento", correosFallidos.size());
    }

    @Transactional
    public void eliminar(Long id) {
        log.info("Eliminando historial de correo con ID: {}", id);
        HistorialCorreo historial = buscarHistorialPorId(id);
        historialRepositorio.delete(historial);
        log.info("Historial eliminado exitosamente: {}", id);
    }

    @Transactional
    public void eliminarEnviadosAntiguos(int dias) {
        log.info("Eliminando correos enviados antiguos (más de {} días)", dias);
        LocalDateTime fechaLimite = LocalDateTime.now().minusDays(dias);
        List<HistorialCorreo> correos = historialRepositorio.findEnviadosAntiguos(fechaLimite);
        historialRepositorio.deleteAll(correos);
        log.info("Eliminados {} correos antiguos", correos.size());
    }

    @Transactional(readOnly = true)
    public Map<EstadoCorreo, Long> contarPorEstado() {
        log.debug("Contando correos por estado");
        return historialRepositorio.contarPorEstado().stream()
                .collect(Collectors.toMap(
                        obj -> (EstadoCorreo) obj[0],
                        obj -> (Long) obj[1]
                ));
    }

    @Transactional(readOnly = true)
    public Long contarEnviados() {
        return historialRepositorio.contarEnviados();
    }

    @Transactional(readOnly = true)
    public Long contarConError() {
        return historialRepositorio.contarConError();
    }

    @Transactional(readOnly = true)
    public Long contarPendientes() {
        return historialRepositorio.contarPendientes();
    }

    @Transactional(readOnly = true)
    public Long contarEnviadosPorDestinatario(String email) {
        return historialRepositorio.contarEnviadosPorDestinatario(email);
    }

    @Transactional(readOnly = true)
    public Long contarPorPlantilla(Long plantillaId) {
        return historialRepositorio.contarPorPlantilla(plantillaId);
    }

    private HistorialCorreo buscarHistorialPorId(Long id) {
        return historialRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Historial de correo con ID " + id + " no encontrado"));
    }

    private HistorialCorreo buscarHistorialPorIdConPlantilla(Long id) {
        return historialRepositorio.findByIdConPlantilla(id)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Historial de correo con ID " + id + " no encontrado"));
    }
}
