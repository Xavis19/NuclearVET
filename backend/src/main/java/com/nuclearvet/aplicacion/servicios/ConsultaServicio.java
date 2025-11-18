package com.nuclearvet.aplicacion.servicios;

import com.nuclearvet.aplicacion.dtos.ConsultaDTO;
import com.nuclearvet.aplicacion.dtos.CrearConsultaDTO;
import com.nuclearvet.aplicacion.mapeadores.ConsultaMapeador;
import com.nuclearvet.compartido.excepciones.RecursoNoEncontradoExcepcion;
import com.nuclearvet.dominio.entidades.*;
import com.nuclearvet.dominio.enums.EstadoCita;
import com.nuclearvet.dominio.enums.EstadoConsulta;
import com.nuclearvet.dominio.enums.TipoAccion;
import com.nuclearvet.infraestructura.persistencia.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Servicio para gestión de consultas médicas
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ConsultaServicio {

    private final ConsultaRepositorio consultaRepositorio;
    private final HistoriaClinicaRepositorio historiaClinicaRepositorio;
    private final UsuarioRepositorio usuarioRepositorio;
    private final CitaRepositorio citaRepositorio;
    private final ConsultaMapeador consultaMapeador;
    private final RegistroActividadServicio registroActividadServicio;

    /**
     * Crear nueva consulta
     */
    public ConsultaDTO crearConsulta(CrearConsultaDTO dto, HttpServletRequest request) {
        log.info("Creando nueva consulta para historia clínica ID: {}", dto.getHistoriaClinicaId());

        // Validar historia clínica
        HistoriaClinica historiaClinica = historiaClinicaRepositorio.findById(dto.getHistoriaClinicaId())
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Historia clínica no encontrada con ID: " + dto.getHistoriaClinicaId()));

        // Validar veterinario
        Usuario veterinario = usuarioRepositorio.findById(dto.getVeterinarioId())
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Veterinario no encontrado con ID: " + dto.getVeterinarioId()));

        // Generar número de consulta
        String numeroConsulta = generarNumeroConsulta();

        // Mapear DTO a entidad
        Consulta consulta = consultaMapeador.aEntidad(dto);
        consulta.setNumeroConsulta(numeroConsulta);
        consulta.setHistoriaClinica(historiaClinica);
        consulta.setVeterinario(veterinario);

        // Vincular con cita si existe
        if (dto.getCitaId() != null) {
            Cita cita = citaRepositorio.findById(dto.getCitaId())
                    .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Cita no encontrada con ID: " + dto.getCitaId()));
            
            consulta.setCita(cita);
            
            // Actualizar estado de la cita a EN_CURSO
            if (cita.getEstado() == EstadoCita.CONFIRMADA) {
                cita.setEstado(EstadoCita.EN_CURSO);
                citaRepositorio.save(cita);
            }
        }

        consulta = consultaRepositorio.save(consulta);

        // Agregar consulta a la historia clínica
        historiaClinica.agregarConsulta(consulta);
        historiaClinicaRepositorio.save(historiaClinica);

        // Registrar actividad
        registroActividadServicio.registrarActividad(
                null,
                TipoAccion.CREAR_CONSULTA,
                "Consulta creada: " + numeroConsulta + " para paciente: " + historiaClinica.getPaciente().getNombre(),
                request
        );

        log.info("Consulta creada exitosamente con número: {}", numeroConsulta);
        return consultaMapeador.aDTO(consulta);
    }

    /**
     * Obtener consulta por ID
     */
    @Transactional(readOnly = true)
    public ConsultaDTO obtenerPorId(Long id) {
        log.debug("Obteniendo consulta por ID: {}", id);
        
        Consulta consulta = consultaRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Consulta no encontrada con ID: " + id));
        
        return consultaMapeador.aDTO(consulta);
    }

    /**
     * Obtener consulta por número
     */
    @Transactional(readOnly = true)
    public ConsultaDTO obtenerPorNumero(String numeroConsulta) {
        log.debug("Obteniendo consulta por número: {}", numeroConsulta);
        
        Consulta consulta = consultaRepositorio.findByNumeroConsulta(numeroConsulta)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Consulta no encontrada con número: " + numeroConsulta));
        
        return consultaMapeador.aDTO(consulta);
    }

    /**
     * Listar consultas por historia clínica
     */
    @Transactional(readOnly = true)
    public List<ConsultaDTO> listarPorHistoriaClinica(Long historiaClinicaId) {
        log.debug("Listando consultas para historia clínica ID: {}", historiaClinicaId);
        
        // Verificar que existe la historia clínica
        if (!historiaClinicaRepositorio.existsById(historiaClinicaId)) {
            throw new RecursoNoEncontradoExcepcion("Historia clínica no encontrada con ID: " + historiaClinicaId);
        }
        
        List<Consulta> consultas = consultaRepositorio.findByHistoriaClinicaIdOrderByFechaConsultaDesc(historiaClinicaId);
        return consultaMapeador.aDTOLista(consultas);
    }

    /**
     * Listar consultas por paciente
     */
    @Transactional(readOnly = true)
    public List<ConsultaDTO> listarPorPaciente(Long pacienteId) {
        log.debug("Listando consultas para paciente ID: {}", pacienteId);
        
        List<Consulta> consultas = consultaRepositorio.findUltimasConsultasPorPaciente(pacienteId);
        return consultaMapeador.aDTOLista(consultas);
    }

    /**
     * Listar consultas por veterinario
     */
    @Transactional(readOnly = true)
    public List<ConsultaDTO> listarPorVeterinario(Long veterinarioId) {
        log.debug("Listando consultas para veterinario ID: {}", veterinarioId);
        
        List<Consulta> consultas = consultaRepositorio.findByVeterinarioIdOrderByFechaConsultaDesc(veterinarioId);
        return consultaMapeador.aDTOLista(consultas);
    }

    /**
     * Listar consultas del día para un veterinario
     */
    @Transactional(readOnly = true)
    public List<ConsultaDTO> listarConsultasDelDia(Long veterinarioId) {
        log.debug("Listando consultas del día para veterinario ID: {}", veterinarioId);
        
        LocalDateTime inicioDia = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime finDia = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
        
        List<Consulta> consultas = consultaRepositorio.findConsultasDelDiaPorVeterinario(veterinarioId, inicioDia, finDia);
        return consultaMapeador.aDTOLista(consultas);
    }

    /**
     * Listar consultas por estado
     */
    @Transactional(readOnly = true)
    public List<ConsultaDTO> listarPorEstado(EstadoConsulta estado) {
        log.debug("Listando consultas con estado: {}", estado);
        
        List<Consulta> consultas = consultaRepositorio.findByEstadoOrderByFechaConsultaDesc(estado);
        return consultaMapeador.aDTOLista(consultas);
    }

    /**
     * Listar consultas por rango de fechas
     */
    @Transactional(readOnly = true)
    public List<ConsultaDTO> listarPorRangoFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        log.debug("Listando consultas entre {} y {}", fechaInicio, fechaFin);
        
        List<Consulta> consultas = consultaRepositorio.findByFechaConsultaBetweenOrderByFechaConsultaDesc(fechaInicio, fechaFin);
        return consultaMapeador.aDTOLista(consultas);
    }

    /**
     * Listar consultas recientes (últimos 30 días)
     */
    @Transactional(readOnly = true)
    public List<ConsultaDTO> listarConsultasRecientes() {
        log.debug("Listando consultas recientes");
        
        LocalDateTime hace30Dias = LocalDateTime.now().minusDays(30);
        List<Consulta> consultas = consultaRepositorio.findConsultasRecientes(hace30Dias);
        return consultaMapeador.aDTOLista(consultas);
    }

    /**
     * Completar consulta
     */
    public ConsultaDTO completarConsulta(Long id, HttpServletRequest request) {
        log.info("Completando consulta ID: {}", id);

        Consulta consulta = consultaRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Consulta no encontrada con ID: " + id));

        // Verificar que la consulta esté en proceso
        if (consulta.getEstado() != EstadoConsulta.EN_PROCESO) {
            throw new IllegalStateException("Solo se pueden completar consultas en proceso");
        }

        // Validar que tenga diagnóstico y tratamiento
        if (consulta.getDiagnostico() == null || consulta.getDiagnostico().isEmpty()) {
            throw new IllegalStateException("La consulta debe tener un diagnóstico antes de completarse");
        }

        // Completar consulta
        consulta.completar();
        consulta = consultaRepositorio.save(consulta);

        // Si está vinculada a una cita, actualizar estado
        if (consulta.getCita() != null) {
            Cita cita = consulta.getCita();
            cita.setEstado(EstadoCita.COMPLETADA);
            citaRepositorio.save(cita);
        }

        // Registrar actividad
        registroActividadServicio.registrarActividad(
                null,
                TipoAccion.COMPLETAR_CONSULTA,
                "Consulta completada: " + consulta.getNumeroConsulta(),
                request
        );

        log.info("Consulta completada exitosamente");
        return consultaMapeador.aDTO(consulta);
    }

    /**
     * Cancelar consulta
     */
    public ConsultaDTO cancelarConsulta(Long id, String motivo, HttpServletRequest request) {
        log.info("Cancelando consulta ID: {} - Motivo: {}", id, motivo);

        Consulta consulta = consultaRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Consulta no encontrada con ID: " + id));

        // Verificar que no esté ya completada o cancelada
        if (consulta.getEstado() == EstadoConsulta.COMPLETADA) {
            throw new IllegalStateException("No se puede cancelar una consulta completada");
        }

        if (consulta.getEstado() == EstadoConsulta.CANCELADA) {
            throw new IllegalStateException("La consulta ya está cancelada");
        }

        // Cancelar consulta
        consulta.cancelar();
        
        // Agregar motivo a las observaciones
        String observacionesActualizadas = (consulta.getObservaciones() != null ? consulta.getObservaciones() + "\n" : "") 
                + "CANCELADA - Motivo: " + motivo;
        consulta.setObservaciones(observacionesActualizadas);
        
        consulta = consultaRepositorio.save(consulta);

        // Registrar actividad
        registroActividadServicio.registrarActividad(
                null,
                TipoAccion.CANCELAR_CONSULTA,
                "Consulta cancelada: " + consulta.getNumeroConsulta() + " - Motivo: " + motivo,
                request
        );

        log.info("Consulta cancelada exitosamente");
        return consultaMapeador.aDTO(consulta);
    }

    /**
     * Obtener consulta por cita
     */
    @Transactional(readOnly = true)
    public ConsultaDTO obtenerPorCita(Long citaId) {
        log.debug("Obteniendo consulta para cita ID: {}", citaId);
        
        Consulta consulta = consultaRepositorio.findByCitaId(citaId)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("No se encontró consulta para la cita ID: " + citaId));
        
        return consultaMapeador.aDTO(consulta);
    }

    /**
     * Generar número único de consulta
     * Formato: CONS-YYYYMMDD-XXXXX
     */
    private String generarNumeroConsulta() {
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String prefijo = "CONS-" + fecha + "-";
        
        // Buscar el último número generado hoy
        LocalDateTime inicioDia = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime finDia = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
        
        List<Consulta> consultasHoy = consultaRepositorio.findByFechaConsultaBetweenOrderByFechaConsultaDesc(inicioDia, finDia);
        Long contador = (long) consultasHoy.size() + 1;
        
        String numeroConsulta;
        do {
            numeroConsulta = prefijo + String.format("%05d", contador);
            contador++;
        } while (consultaRepositorio.existsByNumeroConsulta(numeroConsulta));
        
        return numeroConsulta;
    }
}
