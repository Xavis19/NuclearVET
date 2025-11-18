package com.nuclearvet.aplicacion.servicios;

import com.nuclearvet.aplicacion.dtos.RegistroActividadDTO;
import com.nuclearvet.aplicacion.mapeadores.UsuarioMapeador;
import com.nuclearvet.compartido.excepciones.RecursoNoEncontradoExcepcion;
import com.nuclearvet.dominio.entidades.RegistroActividad;
import com.nuclearvet.dominio.entidades.Usuario;
import com.nuclearvet.dominio.enums.TipoAccion;
import com.nuclearvet.infraestructura.persistencia.RegistroActividadRepositorio;
import com.nuclearvet.infraestructura.persistencia.UsuarioRepositorio;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio para gestión de registro de actividad
 * RF1.5 - Registro de actividad relevante
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RegistroActividadServicio {

    private final RegistroActividadRepositorio registroActividadRepositorio;
    private final UsuarioRepositorio usuarioRepositorio;
    private final UsuarioMapeador usuarioMapeador;

    /**
     * Registra una actividad del usuario
     */
    @Transactional
    public void registrarActividad(Long usuarioId, TipoAccion tipoAccion, String descripcion, HttpServletRequest request) {
        try {
            Usuario usuario = usuarioRepositorio.findById(usuarioId)
                    .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Usuario", "id", usuarioId));

            String ipOrigen = obtenerIpCliente(request);

            RegistroActividad registro = RegistroActividad.builder()
                    .usuario(usuario)
                    .tipoAccion(tipoAccion)
                    .descripcion(descripcion)
                    .ipOrigen(ipOrigen)
                    .fechaHora(LocalDateTime.now())
                    .build();

            registroActividadRepositorio.save(registro);
            
            log.info("Actividad registrada: Usuario={}, Acción={}, IP={}", 
                    usuario.getCorreoElectronico(), tipoAccion, ipOrigen);
        } catch (Exception e) {
            log.error("Error al registrar actividad: {}", e.getMessage());
        }
    }

    /**
     * Obtiene el historial de actividad de un usuario
     */
    @Transactional(readOnly = true)
    public List<RegistroActividadDTO> obtenerActividadesPorUsuario(Long usuarioId) {
        List<RegistroActividad> registros = registroActividadRepositorio.findByUsuarioIdOrderByFechaHoraDesc(usuarioId);
        return usuarioMapeador.aRegistroActividadDTOLista(registros);
    }

    /**
     * Obtiene actividades por tipo de acción
     */
    @Transactional(readOnly = true)
    public List<RegistroActividadDTO> obtenerActividadesPorTipo(TipoAccion tipoAccion) {
        List<RegistroActividad> registros = registroActividadRepositorio.findByTipoAccion(tipoAccion);
        return usuarioMapeador.aRegistroActividadDTOLista(registros);
    }

    /**
     * Obtiene las últimas 100 actividades
     */
    @Transactional(readOnly = true)
    public List<RegistroActividadDTO> obtenerUltimasActividades() {
        List<RegistroActividad> registros = registroActividadRepositorio.findTop100ByOrderByFechaHoraDesc();
        return usuarioMapeador.aRegistroActividadDTOLista(registros);
    }

    /**
     * Obtiene actividades en un rango de fechas
     */
    @Transactional(readOnly = true)
    public List<RegistroActividadDTO> obtenerActividadesPorRangoFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        List<RegistroActividad> registros = registroActividadRepositorio
                .findByFechaHoraBetweenOrderByFechaHoraDesc(fechaInicio, fechaFin);
        return usuarioMapeador.aRegistroActividadDTOLista(registros);
    }

    /**
     * Obtiene la dirección IP del cliente
     */
    private String obtenerIpCliente(HttpServletRequest request) {
        if (request == null) {
            return "desconocida";
        }

        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip != null ? ip : "desconocida";
    }
}
