package com.nuclearvet.dominio.servicios;

import com.nuclearvet.aplicacion.dto.notificaciones.CrearNotificacionDTO;
import com.nuclearvet.aplicacion.dto.notificaciones.NotificacionDTO;
import com.nuclearvet.aplicacion.mapeadores.NotificacionMapeador;
import com.nuclearvet.compartido.excepciones.RecursoNoEncontradoExcepcion;
import com.nuclearvet.dominio.entidades.Notificacion;
import com.nuclearvet.dominio.entidades.Usuario;
import com.nuclearvet.dominio.enumeraciones.PrioridadNotificacion;
import com.nuclearvet.dominio.enumeraciones.TipoNotificacion;
import com.nuclearvet.infraestructura.persistencia.NotificacionRepositorio;
import com.nuclearvet.infraestructura.persistencia.UsuarioRepositorio;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Servicio para gestión de notificaciones
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NotificacionServicio {

    private final NotificacionRepositorio notificacionRepositorio;
    private final UsuarioRepositorio usuarioRepositorio;
    private final NotificacionMapeador notificacionMapeador;

    @Transactional(readOnly = true)
    public List<NotificacionDTO> listarPorUsuario(Long usuarioId) {
        log.debug("Listando notificaciones para usuario: {}", usuarioId);
        return notificacionRepositorio.findByUsuarioOrdenadas(usuarioId).stream()
                .map(notificacionMapeador::aDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<NotificacionDTO> listarNoLeidasPorUsuario(Long usuarioId) {
        log.debug("Listando notificaciones no leídas para usuario: {}", usuarioId);
        return notificacionRepositorio.findNoLeidasPorUsuario(usuarioId).stream()
                .map(notificacionMapeador::aDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<NotificacionDTO> listarPorUsuarioYTipo(Long usuarioId, TipoNotificacion tipo) {
        log.debug("Listando notificaciones para usuario: {} y tipo: {}", usuarioId, tipo);
        return notificacionRepositorio.findByUsuarioYTipo(usuarioId, tipo).stream()
                .map(notificacionMapeador::aDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<NotificacionDTO> listarNoLeidasPorUsuarioYPrioridad(Long usuarioId, PrioridadNotificacion prioridad) {
        log.debug("Listando notificaciones no leídas para usuario: {} y prioridad: {}", usuarioId, prioridad);
        return notificacionRepositorio.findNoLeidasPorUsuarioYPrioridad(usuarioId, prioridad).stream()
                .map(notificacionMapeador::aDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<NotificacionDTO> listarRecientesPorUsuario(Long usuarioId, int dias) {
        log.debug("Listando notificaciones recientes para usuario: {} (últimos {} días)", usuarioId, dias);
        LocalDateTime fecha = LocalDateTime.now().minusDays(dias);
        return notificacionRepositorio.findRecientesPorUsuario(usuarioId, fecha).stream()
                .map(notificacionMapeador::aDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public NotificacionDTO obtenerPorId(Long id) {
        log.debug("Obteniendo notificación con ID: {}", id);
        Notificacion notificacion = buscarNotificacionPorId(id);
        return notificacionMapeador.aDTO(notificacion);
    }

    @Transactional
    public NotificacionDTO crear(CrearNotificacionDTO dto) {
        log.info("Creando nueva notificación para usuario: {}", dto.getUsuarioDestinatarioId());
        
        // Verificar que existe el usuario
        Usuario usuario = usuarioRepositorio.findById(dto.getUsuarioDestinatarioId())
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Usuario con ID " + dto.getUsuarioDestinatarioId() + " no encontrado"));
        
        Notificacion notificacion = notificacionMapeador.aEntidad(dto);
        notificacion.setUsuarioDestinatario(usuario);
        Notificacion notificacionGuardada = notificacionRepositorio.save(notificacion);
        
        log.info("Notificación creada exitosamente con ID: {}", notificacionGuardada.getId());
        return notificacionMapeador.aDTO(notificacionGuardada);
    }

    @Transactional
    public NotificacionDTO marcarComoLeida(Long id) {
        log.info("Marcando notificación como leída: {}", id);
        Notificacion notificacion = buscarNotificacionPorId(id);
        notificacion.marcarComoLeida();
        Notificacion notificacionActualizada = notificacionRepositorio.save(notificacion);
        return notificacionMapeador.aDTO(notificacionActualizada);
    }

    @Transactional
    public NotificacionDTO marcarComoNoLeida(Long id) {
        log.info("Marcando notificación como no leída: {}", id);
        Notificacion notificacion = buscarNotificacionPorId(id);
        notificacion.marcarComoNoLeida();
        Notificacion notificacionActualizada = notificacionRepositorio.save(notificacion);
        return notificacionMapeador.aDTO(notificacionActualizada);
    }

    @Transactional
    public void marcarTodasComoLeidasPorUsuario(Long usuarioId) {
        log.info("Marcando todas las notificaciones como leídas para usuario: {}", usuarioId);
        notificacionRepositorio.marcarTodasComoLeidasPorUsuario(usuarioId);
    }

    @Transactional
    public void marcarComoLeidasPorTipo(Long usuarioId, TipoNotificacion tipo) {
        log.info("Marcando notificaciones como leídas para usuario: {} y tipo: {}", usuarioId, tipo);
        notificacionRepositorio.marcarComoLeidasPorTipo(usuarioId, tipo);
    }

    @Transactional
    public void eliminar(Long id) {
        log.info("Eliminando notificación con ID: {}", id);
        Notificacion notificacion = buscarNotificacionPorId(id);
        notificacionRepositorio.delete(notificacion);
        log.info("Notificación eliminada exitosamente: {}", id);
    }

    @Transactional
    public void eliminarLeidasAntiguas(int dias) {
        log.info("Eliminando notificaciones leídas antiguas (más de {} días)", dias);
        LocalDateTime fechaLimite = LocalDateTime.now().minusDays(dias);
        List<Notificacion> notificaciones = notificacionRepositorio.findLeidasAntiguas(fechaLimite);
        notificacionRepositorio.deleteAll(notificaciones);
        log.info("Eliminadas {} notificaciones antiguas", notificaciones.size());
    }

    @Transactional(readOnly = true)
    public Long contarNoLeidasPorUsuario(Long usuarioId) {
        return notificacionRepositorio.contarNoLeidasPorUsuario(usuarioId);
    }

    @Transactional(readOnly = true)
    public Long contarUrgentesNoLeidasPorUsuario(Long usuarioId) {
        return notificacionRepositorio.contarUrgentesNoLeidasPorUsuario(usuarioId);
    }

    @Transactional(readOnly = true)
    public Map<TipoNotificacion, Long> contarNoLeidasPorTipo(Long usuarioId) {
        log.debug("Contando notificaciones no leídas por tipo para usuario: {}", usuarioId);
        return notificacionRepositorio.contarNoLeidasPorTipo(usuarioId).stream()
                .collect(Collectors.toMap(
                        obj -> (TipoNotificacion) obj[0],
                        obj -> (Long) obj[1]
                ));
    }

    private Notificacion buscarNotificacionPorId(Long id) {
        return notificacionRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Notificación con ID " + id + " no encontrada"));
    }
}
