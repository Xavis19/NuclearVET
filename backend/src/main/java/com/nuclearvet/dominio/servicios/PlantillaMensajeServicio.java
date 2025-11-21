package com.nuclearvet.dominio.servicios;

import com.nuclearvet.aplicacion.dto.notificaciones.ActualizarPlantillaDTO;
import com.nuclearvet.aplicacion.dto.notificaciones.CrearPlantillaDTO;
import com.nuclearvet.aplicacion.dto.notificaciones.PlantillaMensajeDTO;
import com.nuclearvet.aplicacion.mapeadores.PlantillaMensajeMapeador;
import com.nuclearvet.compartido.excepciones.RecursoNoEncontradoExcepcion;
import com.nuclearvet.dominio.entidades.PlantillaMensaje;
import com.nuclearvet.dominio.enumeraciones.TipoPlantilla;
import com.nuclearvet.infraestructura.persistencia.PlantillaMensajeRepositorio;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Servicio para gestión de plantillas de mensajes
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PlantillaMensajeServicio {

    private final PlantillaMensajeRepositorio plantillaRepositorio;
    private final PlantillaMensajeMapeador plantillaMapeador;

    @Transactional(readOnly = true)
    public List<PlantillaMensajeDTO> listarTodas() {
        log.debug("Listando todas las plantillas de mensajes");
        return plantillaRepositorio.findAll().stream()
                .map(plantillaMapeador::aDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PlantillaMensajeDTO> listarActivas() {
        log.debug("Listando plantillas activas");
        return plantillaRepositorio.findByActivoTrue().stream()
                .map(plantillaMapeador::aDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PlantillaMensajeDTO obtenerPorId(Long id) {
        log.debug("Obteniendo plantilla con ID: {}", id);
        PlantillaMensaje plantilla = buscarPlantillaPorId(id);
        return plantillaMapeador.aDTO(plantilla);
    }

    @Transactional(readOnly = true)
    public PlantillaMensajeDTO obtenerPorNombre(String nombre) {
        log.debug("Obteniendo plantilla con nombre: {}", nombre);
        PlantillaMensaje plantilla = plantillaRepositorio.findByNombre(nombre)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Plantilla con nombre " + nombre + " no encontrada"));
        return plantillaMapeador.aDTO(plantilla);
    }

    @Transactional(readOnly = true)
    public List<PlantillaMensajeDTO> listarPorTipo(TipoPlantilla tipo) {
        log.debug("Listando plantillas por tipo: {}", tipo);
        return plantillaRepositorio.findByTipoPlantilla(tipo).stream()
                .map(plantillaMapeador::aDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PlantillaMensajeDTO> buscar(String termino) {
        log.debug("Buscando plantillas con término: {}", termino);
        return plantillaRepositorio.buscarPorNombreOAsunto(termino).stream()
                .map(plantillaMapeador::aDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public PlantillaMensajeDTO crear(CrearPlantillaDTO dto) {
        log.info("Creando nueva plantilla: {}", dto.getNombre());
        
        PlantillaMensaje plantilla = plantillaMapeador.aEntidad(dto);
        PlantillaMensaje plantillaGuardada = plantillaRepositorio.save(plantilla);
        
        log.info("Plantilla creada exitosamente con ID: {}", plantillaGuardada.getId());
        return plantillaMapeador.aDTO(plantillaGuardada);
    }

    @Transactional
    public PlantillaMensajeDTO actualizar(Long id, ActualizarPlantillaDTO dto) {
        log.info("Actualizando plantilla con ID: {}", id);
        
        PlantillaMensaje plantilla = buscarPlantillaPorId(id);
        plantillaMapeador.actualizarEntidad(dto, plantilla);
        PlantillaMensaje plantillaActualizada = plantillaRepositorio.save(plantilla);
        
        log.info("Plantilla actualizada exitosamente: {}", id);
        return plantillaMapeador.aDTO(plantillaActualizada);
    }

    @Transactional
    public void eliminar(Long id) {
        log.info("Eliminando plantilla con ID: {}", id);
        PlantillaMensaje plantilla = buscarPlantillaPorId(id);
        plantillaRepositorio.delete(plantilla);
        log.info("Plantilla eliminada exitosamente: {}", id);
    }

    @Transactional
    public PlantillaMensajeDTO activar(Long id) {
        log.info("Activando plantilla con ID: {}", id);
        PlantillaMensaje plantilla = buscarPlantillaPorId(id);
        plantilla.setActivo(true);
        PlantillaMensaje plantillaActualizada = plantillaRepositorio.save(plantilla);
        return plantillaMapeador.aDTO(plantillaActualizada);
    }

    @Transactional
    public PlantillaMensajeDTO desactivar(Long id) {
        log.info("Desactivando plantilla con ID: {}", id);
        PlantillaMensaje plantilla = buscarPlantillaPorId(id);
        plantilla.setActivo(false);
        PlantillaMensaje plantillaActualizada = plantillaRepositorio.save(plantilla);
        return plantillaMapeador.aDTO(plantillaActualizada);
    }

    @Transactional(readOnly = true)
    public String renderizar(Long id, Map<String, String> variables) {
        log.debug("Renderizando plantilla con ID: {}", id);
        PlantillaMensaje plantilla = buscarPlantillaPorId(id);
        return plantilla.renderizar(variables);
    }

    @Transactional(readOnly = true)
    public String renderizarAsunto(Long id, Map<String, String> variables) {
        log.debug("Renderizando asunto de plantilla con ID: {}", id);
        PlantillaMensaje plantilla = buscarPlantillaPorId(id);
        return plantilla.renderizarAsunto(variables);
    }

    @Transactional(readOnly = true)
    public Map<TipoPlantilla, Long> contarPorTipo() {
        log.debug("Contando plantillas por tipo");
        return plantillaRepositorio.contarPorTipo().stream()
                .collect(Collectors.toMap(
                        obj -> (TipoPlantilla) obj[0],
                        obj -> (Long) obj[1]
                ));
    }

    @Transactional(readOnly = true)
    public Long contarActivas() {
        return plantillaRepositorio.contarActivas();
    }

    private PlantillaMensaje buscarPlantillaPorId(Long id) {
        return plantillaRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Plantilla con ID " + id + " no encontrada"));
    }
}
