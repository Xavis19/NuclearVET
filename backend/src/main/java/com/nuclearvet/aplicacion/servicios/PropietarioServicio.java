package com.nuclearvet.aplicacion.servicios;

import com.nuclearvet.aplicacion.dtos.ActualizarPropietarioDTO;
import com.nuclearvet.aplicacion.dtos.CrearPropietarioDTO;
import com.nuclearvet.aplicacion.dtos.PropietarioDTO;
import com.nuclearvet.aplicacion.mapeadores.PropietarioMapeador;
import com.nuclearvet.compartido.excepciones.RecursoNoEncontradoExcepcion;
import com.nuclearvet.dominio.entidades.Propietario;
import com.nuclearvet.dominio.enums.TipoAccion;
import com.nuclearvet.dominio.enums.TipoIdentificacion;
import com.nuclearvet.infraestructura.persistencia.PropietarioRepositorio;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio para gestión de propietarios
 * RF2.1 - Registro y gestión de propietarios
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PropietarioServicio {

    private final PropietarioRepositorio propietarioRepositorio;
    private final PropietarioMapeador propietarioMapeador;
    private final RegistroActividadServicio registroActividadServicio;

    /**
     * Crear un nuevo propietario
     */
    @Transactional
    public PropietarioDTO crearPropietario(CrearPropietarioDTO dto, HttpServletRequest request) {
        // Validar que no exista otro propietario con el mismo número de identificación
        if (propietarioRepositorio.existsByNumeroIdentificacion(dto.getNumeroIdentificacion())) {
            throw new IllegalArgumentException(
                    "Ya existe un propietario con el número de identificación: " + dto.getNumeroIdentificacion());
        }

        Propietario propietario = propietarioMapeador.aEntidad(dto);
        propietario = propietarioRepositorio.save(propietario);

        log.info("Propietario creado: {} - {}", propietario.getId(), propietario.getNombreCompleto());

        return propietarioMapeador.aDTO(propietario);
    }

    /**
     * Obtener propietario por ID
     */
    @Transactional(readOnly = true)
    public PropietarioDTO obtenerPropietarioPorId(Long id) {
        Propietario propietario = propietarioRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Propietario", "id", id));
        return propietarioMapeador.aDTO(propietario);
    }

    /**
     * Obtener propietario por número de identificación
     */
    @Transactional(readOnly = true)
    public PropietarioDTO obtenerPropietarioPorIdentificacion(String numeroIdentificacion) {
        Propietario propietario = propietarioRepositorio.findByNumeroIdentificacion(numeroIdentificacion)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion(
                        "Propietario", "número de identificación", numeroIdentificacion));
        return propietarioMapeador.aDTO(propietario);
    }

    /**
     * Listar todos los propietarios
     */
    @Transactional(readOnly = true)
    public List<PropietarioDTO> listarPropietarios() {
        List<Propietario> propietarios = propietarioRepositorio.findAll();
        return propietarioMapeador.aDTOLista(propietarios);
    }

    /**
     * Listar propietarios activos
     */
    @Transactional(readOnly = true)
    public List<PropietarioDTO> listarPropietariosActivos() {
        List<Propietario> propietarios = propietarioRepositorio.findByActivoTrue();
        return propietarioMapeador.aDTOLista(propietarios);
    }

    /**
     * Listar propietarios por tipo de identificación
     */
    @Transactional(readOnly = true)
    public List<PropietarioDTO> listarPropietariosPorTipoIdentificacion(TipoIdentificacion tipo) {
        List<Propietario> propietarios = propietarioRepositorio.findByTipoIdentificacion(tipo);
        return propietarioMapeador.aDTOLista(propietarios);
    }

    /**
     * Buscar propietarios por nombre o apellido
     */
    @Transactional(readOnly = true)
    public List<PropietarioDTO> buscarPropietariosPorNombre(String termino) {
        List<Propietario> propietarios = propietarioRepositorio.buscarPorNombre(termino);
        return propietarioMapeador.aDTOLista(propietarios);
    }

    /**
     * Buscar propietarios por ciudad
     */
    @Transactional(readOnly = true)
    public List<PropietarioDTO> buscarPropietariosPorCiudad(String ciudad) {
        List<Propietario> propietarios = propietarioRepositorio.findByCiudad(ciudad);
        return propietarioMapeador.aDTOLista(propietarios);
    }

    /**
     * Listar propietarios que tienen al menos un paciente
     */
    @Transactional(readOnly = true)
    public List<PropietarioDTO> listarPropietariosConPacientes() {
        List<Propietario> propietarios = propietarioRepositorio.findPropietariosConPacientes();
        return propietarioMapeador.aDTOLista(propietarios);
    }

    /**
     * Actualizar propietario
     */
    @Transactional
    public PropietarioDTO actualizarPropietario(Long id, ActualizarPropietarioDTO dto, HttpServletRequest request) {
        Propietario propietario = propietarioRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Propietario", "id", id));

        // Validar que no exista otro propietario con el mismo número de identificación (si se está cambiando)
        if (dto.getNumeroIdentificacion() != null &&
                !dto.getNumeroIdentificacion().equals(propietario.getNumeroIdentificacion()) &&
                propietarioRepositorio.existsByNumeroIdentificacion(dto.getNumeroIdentificacion())) {
            throw new IllegalArgumentException(
                    "Ya existe un propietario con el número de identificación: " + dto.getNumeroIdentificacion());
        }

        propietarioMapeador.actualizarEntidad(dto, propietario);
        propietario = propietarioRepositorio.save(propietario);

        log.info("Propietario actualizado: {} - {}", propietario.getId(), propietario.getNombreCompleto());

        return propietarioMapeador.aDTO(propietario);
    }

    /**
     * Desactivar propietario (soft delete)
     */
    @Transactional
    public void desactivarPropietario(Long id, HttpServletRequest request) {
        Propietario propietario = propietarioRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Propietario", "id", id));

        propietario.setActivo(false);
        propietarioRepositorio.save(propietario);

        log.info("Propietario desactivado: {} - {}", propietario.getId(), propietario.getNombreCompleto());
    }

    /**
     * Activar propietario
     */
    @Transactional
    public PropietarioDTO activarPropietario(Long id, HttpServletRequest request) {
        Propietario propietario = propietarioRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Propietario", "id", id));

        propietario.setActivo(true);
        propietario = propietarioRepositorio.save(propietario);

        log.info("Propietario activado: {} - {}", propietario.getId(), propietario.getNombreCompleto());

        return propietarioMapeador.aDTO(propietario);
    }

    /**
     * Eliminar propietario permanentemente
     */
    @Transactional
    public void eliminarPropietario(Long id) {
        Propietario propietario = propietarioRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Propietario", "id", id));

        if (propietario.getPacientes() != null && !propietario.getPacientes().isEmpty()) {
            throw new IllegalStateException(
                    "No se puede eliminar un propietario que tiene pacientes registrados");
        }

        propietarioRepositorio.delete(propietario);
        log.warn("Propietario eliminado permanentemente: {}", id);
    }

    /**
     * Contar propietarios activos
     */
    @Transactional(readOnly = true)
    public Long contarPropietariosActivos() {
        return propietarioRepositorio.contarPropietariosActivos();
    }
}
