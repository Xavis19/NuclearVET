package com.nuclearvet.aplicacion.servicios;

import com.nuclearvet.aplicacion.dtos.ActualizarPacienteDTO;
import com.nuclearvet.aplicacion.dtos.CrearPacienteDTO;
import com.nuclearvet.aplicacion.dtos.PacienteDTO;
import com.nuclearvet.aplicacion.mapeadores.PacienteMapeador;
import com.nuclearvet.compartido.excepciones.RecursoNoEncontradoExcepcion;
import com.nuclearvet.dominio.entidades.Paciente;
import com.nuclearvet.dominio.entidades.Propietario;
import com.nuclearvet.dominio.entidades.Usuario;
import com.nuclearvet.dominio.enums.Especie;
import com.nuclearvet.dominio.enums.EstadoPaciente;
import com.nuclearvet.infraestructura.persistencia.PacienteRepositorio;
import com.nuclearvet.infraestructura.persistencia.PropietarioRepositorio;
import com.nuclearvet.infraestructura.persistencia.UsuarioRepositorio;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Servicio para gestión de pacientes
 * RF2.2 - Registro y gestión de pacientes
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PacienteServicio {

    private final PacienteRepositorio pacienteRepositorio;
    private final PropietarioRepositorio propietarioRepositorio;
    private final UsuarioRepositorio usuarioRepositorio;
    private final PacienteMapeador pacienteMapeador;

    /**
     * Crear un nuevo paciente
     */
    @Transactional
    public PacienteDTO crearPaciente(CrearPacienteDTO dto, HttpServletRequest request) {
        // Validar que el propietario existe
        Propietario propietario = propietarioRepositorio.findById(dto.getPropietarioId())
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Propietario", "id", dto.getPropietarioId()));

        // Validar que no exista otro paciente con el mismo microchip (si se proporciona)
        if (dto.getMicrochip() != null && pacienteRepositorio.existsByMicrochip(dto.getMicrochip())) {
            throw new IllegalArgumentException("Ya existe un paciente con el microchip: " + dto.getMicrochip());
        }

        Paciente paciente = pacienteMapeador.aEntidad(dto);
        
        // Generar código único para el paciente
        paciente.setCodigo(generarCodigoPaciente(dto.getEspecie()));
        
        // Asignar propietario
        paciente.setPropietario(propietario);

        // Asignar veterinario si se proporciona
        if (dto.getVeterinarioAsignadoId() != null) {
            Usuario veterinario = usuarioRepositorio.findById(dto.getVeterinarioAsignadoId())
                    .orElseThrow(() -> new RecursoNoEncontradoExcepcion(
                            "Usuario", "id", dto.getVeterinarioAsignadoId()));
            paciente.setVeterinarioAsignado(veterinario);
        }

        paciente = pacienteRepositorio.save(paciente);

        log.info("Paciente creado: {} - {} ({})", 
                paciente.getCodigo(), paciente.getNombre(), paciente.getEspecie());

        return pacienteMapeador.aDTO(paciente);
    }

    /**
     * Generar código único para el paciente
     * Formato: [INICIALES_ESPECIE][AAAAMM][SECUENCIAL]
     * Ejemplo: CAN202511001
     */
    private String generarCodigoPaciente(Especie especie) {
        String iniciales = obtenerInicialesEspecie(especie);
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
        
        // Buscar el último código generado para esta especie y mes
        String patron = iniciales + fecha;
        Long count = pacienteRepositorio.count();
        
        // Generar secuencial (3 dígitos)
        String secuencial = String.format("%03d", (count % 1000) + 1);
        
        return patron + secuencial;
    }

    /**
     * Obtener iniciales de la especie
     */
    private String obtenerInicialesEspecie(Especie especie) {
        return switch (especie) {
            case CANINO -> "CAN";
            case FELINO -> "FEL";
            case AVE -> "AVE";
            case ROEDOR -> "ROE";
            case REPTIL -> "REP";
            case CONEJO -> "CON";
            case EXOTICO -> "EXO";
            case OTRO -> "OTR";
        };
    }

    /**
     * Obtener paciente por ID
     */
    @Transactional(readOnly = true)
    public PacienteDTO obtenerPacientePorId(Long id) {
        Paciente paciente = pacienteRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Paciente", "id", id));
        return pacienteMapeador.aDTO(paciente);
    }

    /**
     * Obtener paciente por código
     */
    @Transactional(readOnly = true)
    public PacienteDTO obtenerPacientePorCodigo(String codigo) {
        Paciente paciente = pacienteRepositorio.findByCodigo(codigo)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Paciente", "código", codigo));
        return pacienteMapeador.aDTO(paciente);
    }

    /**
     * Listar todos los pacientes
     */
    @Transactional(readOnly = true)
    public List<PacienteDTO> listarPacientes() {
        List<Paciente> pacientes = pacienteRepositorio.findAll();
        return pacienteMapeador.aDTOLista(pacientes);
    }

    /**
     * Listar pacientes activos
     */
    @Transactional(readOnly = true)
    public List<PacienteDTO> listarPacientesActivos() {
        List<Paciente> pacientes = pacienteRepositorio.findPacientesActivos();
        return pacienteMapeador.aDTOLista(pacientes);
    }

    /**
     * Listar pacientes por propietario
     */
    @Transactional(readOnly = true)
    public List<PacienteDTO> listarPacientesPorPropietario(Long propietarioId) {
        List<Paciente> pacientes = pacienteRepositorio.findByPropietarioId(propietarioId);
        return pacienteMapeador.aDTOLista(pacientes);
    }

    /**
     * Listar pacientes por especie
     */
    @Transactional(readOnly = true)
    public List<PacienteDTO> listarPacientesPorEspecie(Especie especie) {
        List<Paciente> pacientes = pacienteRepositorio.findByEspecie(especie);
        return pacienteMapeador.aDTOLista(pacientes);
    }

    /**
     * Listar pacientes por estado
     */
    @Transactional(readOnly = true)
    public List<PacienteDTO> listarPacientesPorEstado(EstadoPaciente estado) {
        List<Paciente> pacientes = pacienteRepositorio.findByEstado(estado);
        return pacienteMapeador.aDTOLista(pacientes);
    }

    /**
     * Buscar pacientes por nombre
     */
    @Transactional(readOnly = true)
    public List<PacienteDTO> buscarPacientesPorNombre(String nombre) {
        List<Paciente> pacientes = pacienteRepositorio.buscarPorNombre(nombre);
        return pacienteMapeador.aDTOLista(pacientes);
    }

    /**
     * Listar pacientes por veterinario asignado
     */
    @Transactional(readOnly = true)
    public List<PacienteDTO> listarPacientesPorVeterinario(Long veterinarioId) {
        List<Paciente> pacientes = pacienteRepositorio.findByVeterinarioAsignadoId(veterinarioId);
        return pacienteMapeador.aDTOLista(pacientes);
    }

    /**
     * Listar pacientes en atención (tratamiento u observación)
     */
    @Transactional(readOnly = true)
    public List<PacienteDTO> listarPacientesEnAtencion() {
        List<Paciente> pacientes = pacienteRepositorio.findPacientesEnAtencion();
        return pacienteMapeador.aDTOLista(pacientes);
    }

    /**
     * Actualizar paciente
     */
    @Transactional
    public PacienteDTO actualizarPaciente(Long id, ActualizarPacienteDTO dto, HttpServletRequest request) {
        Paciente paciente = pacienteRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Paciente", "id", id));

        // Validar microchip único (si se está cambiando)
        if (dto.getMicrochip() != null &&
                !dto.getMicrochip().equals(paciente.getMicrochip()) &&
                pacienteRepositorio.existsByMicrochip(dto.getMicrochip())) {
            throw new IllegalArgumentException("Ya existe un paciente con el microchip: " + dto.getMicrochip());
        }

        // Actualizar veterinario asignado si se proporciona
        if (dto.getVeterinarioAsignadoId() != null) {
            Usuario veterinario = usuarioRepositorio.findById(dto.getVeterinarioAsignadoId())
                    .orElseThrow(() -> new RecursoNoEncontradoExcepcion(
                            "Usuario", "id", dto.getVeterinarioAsignadoId()));
            paciente.setVeterinarioAsignado(veterinario);
        }

        pacienteMapeador.actualizarEntidad(dto, paciente);
        paciente = pacienteRepositorio.save(paciente);

        log.info("Paciente actualizado: {} - {}", paciente.getCodigo(), paciente.getNombre());

        return pacienteMapeador.aDTO(paciente);
    }

    /**
     * Cambiar estado del paciente
     */
    @Transactional
    public PacienteDTO cambiarEstadoPaciente(Long id, EstadoPaciente nuevoEstado, HttpServletRequest request) {
        Paciente paciente = pacienteRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Paciente", "id", id));

        EstadoPaciente estadoAnterior = paciente.getEstado();
        paciente.setEstado(nuevoEstado);
        paciente = pacienteRepositorio.save(paciente);

        log.info("Estado del paciente {} cambiado de {} a {}", 
                paciente.getCodigo(), estadoAnterior, nuevoEstado);

        return pacienteMapeador.aDTO(paciente);
    }

    /**
     * Eliminar paciente permanentemente
     */
    @Transactional
    public void eliminarPaciente(Long id) {
        Paciente paciente = pacienteRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Paciente", "id", id));

        // Solo permitir eliminación si el paciente no tiene historial médico
        // (Esta validación se implementará cuando tengamos el módulo de historias clínicas)
        
        pacienteRepositorio.delete(paciente);
        log.warn("Paciente eliminado permanentemente: {}", paciente.getCodigo());
    }

    /**
     * Contar pacientes activos
     */
    @Transactional(readOnly = true)
    public Long contarPacientesActivos() {
        return pacienteRepositorio.contarPacientesActivos();
    }

    /**
     * Contar pacientes por especie
     */
    @Transactional(readOnly = true)
    public Long contarPacientesPorEspecie(Especie especie) {
        return pacienteRepositorio.contarPorEspecie(especie);
    }
}
