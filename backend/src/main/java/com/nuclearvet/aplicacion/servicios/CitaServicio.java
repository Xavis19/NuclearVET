package com.nuclearvet.aplicacion.servicios;

import com.nuclearvet.aplicacion.dtos.CitaDTO;
import com.nuclearvet.aplicacion.dtos.CrearCitaDTO;
import com.nuclearvet.aplicacion.mapeadores.CitaMapeador;
import com.nuclearvet.compartido.excepciones.RecursoNoEncontradoExcepcion;
import com.nuclearvet.dominio.entidades.Cita;
import com.nuclearvet.dominio.entidades.Paciente;
import com.nuclearvet.dominio.entidades.Propietario;
import com.nuclearvet.dominio.entidades.Usuario;
import com.nuclearvet.dominio.enums.EstadoCita;
import com.nuclearvet.dominio.enums.TipoAccion;
import com.nuclearvet.infraestructura.persistencia.CitaRepositorio;
import com.nuclearvet.infraestructura.persistencia.PacienteRepositorio;
import com.nuclearvet.infraestructura.persistencia.UsuarioRepositorio;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio para gestión de citas
 * RF3.1 - Agendamiento de citas
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CitaServicio {

    private final CitaRepositorio citaRepositorio;
    private final PacienteRepositorio pacienteRepositorio;
    private final UsuarioRepositorio usuarioRepositorio;
    private final CitaMapeador citaMapeador;
    private final RegistroActividadServicio registroActividadServicio;

    /**
     * Crear una nueva cita
     */
    @Transactional
    public CitaDTO crearCita(CrearCitaDTO dto, HttpServletRequest request) {
        // Validar paciente
        Paciente paciente = pacienteRepositorio.findById(dto.getPacienteId())
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Paciente", "id", dto.getPacienteId()));

        // Validar veterinario
        Usuario veterinario = usuarioRepositorio.findById(dto.getVeterinarioId())
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Veterinario", "id", dto.getVeterinarioId()));

        // Verificar disponibilidad
        if (citaRepositorio.existeSolapamiento(veterinario.getId(), dto.getFechaHora(), 
                dto.getFechaHora().plusMinutes(dto.getDuracionEstimada()))) {
            throw new IllegalStateException("El veterinario no está disponible en ese horario");
        }

        // Crear entidad
        Cita cita = citaMapeador.aEntidad(dto);
        cita.setPaciente(paciente);
        cita.setVeterinario(veterinario);
        cita.setPropietario(paciente.getPropietario());
        cita.setEstado(EstadoCita.PENDIENTE);
        
        // Generar número de cita
        cita.setNumeroCita(generarNumeroCita());

        Cita citaGuardada = citaRepositorio.save(cita);

        // Registrar actividad
        registroActividadServicio.registrarActividad(
                veterinario.getId(),
                TipoAccion.CREAR_CITA,
                "Cita creada: " + citaGuardada.getNumeroCita(),
                request
        );

        log.info("Cita creada: {}", citaGuardada.getNumeroCita());
        return citaMapeador.aDTO(citaGuardada);
    }

    /**
     * Obtener cita por ID
     */
    @Transactional(readOnly = true)
    public CitaDTO obtenerCitaPorId(Long id) {
        Cita cita = citaRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Cita", "id", id));
        return citaMapeador.aDTO(cita);
    }

    /**
     * Listar todas las citas
     */
    @Transactional(readOnly = true)
    public List<CitaDTO> listarCitas() {
        return citaMapeador.aDTOLista(citaRepositorio.findAll());
    }

    /**
     * Listar citas por estado
     */
    @Transactional(readOnly = true)
    public List<CitaDTO> listarCitasPorEstado(EstadoCita estado) {
        return citaMapeador.aDTOLista(citaRepositorio.findByEstado(estado));
    }

    /**
     * Listar citas de un paciente
     */
    @Transactional(readOnly = true)
    public List<CitaDTO> listarCitasPorPaciente(Long pacienteId) {
        return citaMapeador.aDTOLista(citaRepositorio.findByPacienteId(pacienteId));
    }

    /**
     * Listar citas de un veterinario
     */
    @Transactional(readOnly = true)
    public List<CitaDTO> listarCitasPorVeterinario(Long veterinarioId) {
        return citaMapeador.aDTOLista(citaRepositorio.findByVeterinarioId(veterinarioId));
    }

    /**
     * Listar citas por fecha
     */
    @Transactional(readOnly = true)
    public List<CitaDTO> listarCitasPorFecha(LocalDate fecha) {
        LocalDateTime inicio = fecha.atStartOfDay();
        LocalDateTime fin = fecha.plusDays(1).atStartOfDay();
        return citaMapeador.aDTOLista(citaRepositorio.findByFechaHoraBetween(inicio, fin));
    }

    /**
     * Confirmar cita
     */
    @Transactional
    public CitaDTO confirmarCita(Long id, HttpServletRequest request) {
        Cita cita = citaRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Cita", "id", id));

        cita.setEstado(EstadoCita.CONFIRMADA);
        Cita citaActualizada = citaRepositorio.save(cita);

        registroActividadServicio.registrarActividad(
                cita.getVeterinario().getId(),
                TipoAccion.ACTUALIZAR_CITA,
                "Cita confirmada: " + cita.getNumeroCita(),
                request
        );

        return citaMapeador.aDTO(citaActualizada);
    }

    /**
     * Cancelar cita
     */
    @Transactional
    public void cancelarCita(Long id, String motivo, HttpServletRequest request) {
        Cita cita = citaRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Cita", "id", id));

        cita.setEstado(EstadoCita.CANCELADA);
        cita.setObservaciones((cita.getObservaciones() != null ? cita.getObservaciones() + "\n" : "") 
                + "CANCELADA: " + motivo);
        citaRepositorio.save(cita);

        registroActividadServicio.registrarActividad(
                cita.getVeterinario().getId(),
                TipoAccion.CANCELAR_CITA,
                "Cita cancelada: " + cita.getNumeroCita() + " - Motivo: " + motivo,
                request
        );

        log.info("Cita cancelada: {}", cita.getNumeroCita());
    }

    /**
     * Generar número único de cita
     */
    private String generarNumeroCita() {
        LocalDateTime now = LocalDateTime.now();
        long count = citaRepositorio.count() + 1;
        return String.format("CIT-%04d%02d%02d-%05d", 
                now.getYear(), now.getMonthValue(), now.getDayOfMonth(), count);
    }
}
