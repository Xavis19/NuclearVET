package com.nuclearvet.aplicacion.servicios;

import com.nuclearvet.aplicacion.dtos.ActualizarHistoriaClinicaDTO;
import com.nuclearvet.aplicacion.dtos.HistoriaClinicaDTO;
import com.nuclearvet.aplicacion.mapeadores.HistoriaClinicaMapeador;
import com.nuclearvet.compartido.excepciones.RecursoNoEncontradoExcepcion;
import com.nuclearvet.dominio.entidades.HistoriaClinica;
import com.nuclearvet.dominio.entidades.Paciente;
import com.nuclearvet.dominio.enums.TipoAccion;
import com.nuclearvet.infraestructura.persistencia.HistoriaClinicaRepositorio;
import com.nuclearvet.infraestructura.persistencia.PacienteRepositorio;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servicio para gestión de historias clínicas
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class HistoriaClinicaServicio {

    private final HistoriaClinicaRepositorio historiaClinicaRepositorio;
    private final PacienteRepositorio pacienteRepositorio;
    private final HistoriaClinicaMapeador historiaClinicaMapeador;
    private final RegistroActividadServicio registroActividadServicio;

    /**
     * Crear historia clínica para un paciente
     */
    public HistoriaClinicaDTO crearHistoriaClinica(Long pacienteId, HttpServletRequest request) {
        log.info("Creando historia clínica para paciente ID: {}", pacienteId);

        // Validar que el paciente existe
        Paciente paciente = pacienteRepositorio.findById(pacienteId)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Paciente no encontrado con ID: " + pacienteId));

        // Verificar si ya tiene historia clínica
        if (historiaClinicaRepositorio.findByPacienteId(pacienteId).isPresent()) {
            throw new IllegalStateException("El paciente ya tiene una historia clínica");
        }

        // Generar número de historia clínica
        String numeroHistoria = generarNumeroHistoria();

        // Crear historia clínica
        HistoriaClinica historiaClinica = HistoriaClinica.builder()
                .numeroHistoria(numeroHistoria)
                .paciente(paciente)
                .alergiasConocidas(paciente.getAlergias()) // Copiar alergias desde el paciente
                .build();

        historiaClinica = historiaClinicaRepositorio.save(historiaClinica);

        // Registrar actividad
        registroActividadServicio.registrarActividad(
                null, // El servicio de registro actividad obtendrá el usuario del contexto de seguridad
                TipoAccion.CREAR_HISTORIA_CLINICA,
                "Historia clínica creada para paciente: " + paciente.getNombre() + " (" + paciente.getCodigo() + ")",
                request
        );

        log.info("Historia clínica creada exitosamente con número: {}", numeroHistoria);
        return historiaClinicaMapeador.aDTO(historiaClinica);
    }

    /**
     * Obtener historia clínica por ID
     */
    @Transactional(readOnly = true)
    public HistoriaClinicaDTO obtenerPorId(Long id) {
        log.debug("Obteniendo historia clínica por ID: {}", id);
        
        HistoriaClinica historiaClinica = historiaClinicaRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Historia clínica no encontrada con ID: " + id));
        
        return historiaClinicaMapeador.aDTO(historiaClinica);
    }

    /**
     * Obtener historia clínica por número
     */
    @Transactional(readOnly = true)
    public HistoriaClinicaDTO obtenerPorNumero(String numeroHistoria) {
        log.debug("Obteniendo historia clínica por número: {}", numeroHistoria);
        
        HistoriaClinica historiaClinica = historiaClinicaRepositorio.findByNumeroHistoria(numeroHistoria)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Historia clínica no encontrada con número: " + numeroHistoria));
        
        return historiaClinicaMapeador.aDTO(historiaClinica);
    }

    /**
     * Obtener historia clínica por paciente
     */
    @Transactional(readOnly = true)
    public HistoriaClinicaDTO obtenerPorPaciente(Long pacienteId) {
        log.debug("Obteniendo historia clínica para paciente ID: {}", pacienteId);
        
        HistoriaClinica historiaClinica = historiaClinicaRepositorio.findByPacienteId(pacienteId)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Historia clínica no encontrada para paciente ID: " + pacienteId));
        
        return historiaClinicaMapeador.aDTO(historiaClinica);
    }

    /**
     * Obtener historia clínica por paciente con consultas
     */
    @Transactional(readOnly = true)
    public HistoriaClinicaDTO obtenerPorPacienteConConsultas(Long pacienteId) {
        log.debug("Obteniendo historia clínica con consultas para paciente ID: {}", pacienteId);
        
        HistoriaClinica historiaClinica = historiaClinicaRepositorio.findByPacienteIdWithConsultas(pacienteId)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Historia clínica no encontrada para paciente ID: " + pacienteId));
        
        return historiaClinicaMapeador.aDTO(historiaClinica);
    }

    /**
     * Obtener historia clínica por paciente con archivos
     */
    @Transactional(readOnly = true)
    public HistoriaClinicaDTO obtenerPorPacienteConArchivos(Long pacienteId) {
        log.debug("Obteniendo historia clínica con archivos para paciente ID: {}", pacienteId);
        
        HistoriaClinica historiaClinica = historiaClinicaRepositorio.findByPacienteIdWithArchivos(pacienteId)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Historia clínica no encontrada para paciente ID: " + pacienteId));
        
        return historiaClinicaMapeador.aDTO(historiaClinica);
    }

    /**
     * Listar todas las historias clínicas
     */
    @Transactional(readOnly = true)
    public List<HistoriaClinicaDTO> listarTodas() {
        log.debug("Listando todas las historias clínicas");
        
        List<HistoriaClinica> historiasClinicas = historiaClinicaRepositorio.findAll();
        return historiaClinicaMapeador.aDTOLista(historiasClinicas);
    }

    /**
     * Actualizar historia clínica
     */
    public HistoriaClinicaDTO actualizarHistoriaClinica(Long id, ActualizarHistoriaClinicaDTO dto, HttpServletRequest request) {
        log.info("Actualizando historia clínica ID: {}", id);

        HistoriaClinica historiaClinica = historiaClinicaRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Historia clínica no encontrada con ID: " + id));

        // Actualizar campos usando el mapeador
        historiaClinicaMapeador.actualizarEntidad(dto, historiaClinica);

        historiaClinica = historiaClinicaRepositorio.save(historiaClinica);

        // Registrar actividad
        registroActividadServicio.registrarActividad(
                null,
                TipoAccion.ACTUALIZAR_HISTORIA_CLINICA,
                "Historia clínica actualizada: " + historiaClinica.getNumeroHistoria(),
                request
        );

        log.info("Historia clínica actualizada exitosamente");
        return historiaClinicaMapeador.aDTO(historiaClinica);
    }

    /**
     * Obtener historias clínicas con alergias
     */
    @Transactional(readOnly = true)
    public List<HistoriaClinicaDTO> obtenerHistoriasConAlergias() {
        log.debug("Obteniendo historias clínicas con alergias");
        
        List<HistoriaClinica> historiasClinicas = historiaClinicaRepositorio.findHistoriasConAlergias();
        return historiaClinicaMapeador.aDTOLista(historiasClinicas);
    }

    /**
     * Obtener historias clínicas con enfermedades crónicas
     */
    @Transactional(readOnly = true)
    public List<HistoriaClinicaDTO> obtenerHistoriasConEnfermedadesCronicas() {
        log.debug("Obteniendo historias clínicas con enfermedades crónicas");
        
        List<HistoriaClinica> historiasClinicas = historiaClinicaRepositorio.findHistoriasConEnfermedadesCronicas();
        return historiaClinicaMapeador.aDTOLista(historiasClinicas);
    }

    /**
     * Obtener estadísticas de historias clínicas
     */
    @Transactional(readOnly = true)
    public Map<String, Object> obtenerEstadisticas() {
        log.debug("Obteniendo estadísticas de historias clínicas");
        
        Map<String, Object> estadisticas = new HashMap<>();
        estadisticas.put("totalHistoriasClinicas", historiaClinicaRepositorio.contarTotal());
        estadisticas.put("historiasConAlergias", historiaClinicaRepositorio.findHistoriasConAlergias().size());
        estadisticas.put("historiasConEnfermedadesCronicas", historiaClinicaRepositorio.findHistoriasConEnfermedadesCronicas().size());
        
        return estadisticas;
    }

    /**
     * Generar número único de historia clínica
     * Formato: HIST-YYYYMMDD-XXXXX
     */
    private String generarNumeroHistoria() {
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String prefijo = "HIST-" + fecha + "-";
        
        // Buscar el último número generado hoy
        Long contador = historiaClinicaRepositorio.contarTotal() + 1;
        
        String numeroHistoria;
        do {
            numeroHistoria = prefijo + String.format("%05d", contador);
            contador++;
        } while (historiaClinicaRepositorio.existsByNumeroHistoria(numeroHistoria));
        
        return numeroHistoria;
    }
}
