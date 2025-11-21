package com.nuclearvet.dominio.servicios;

import com.nuclearvet.aplicacion.dto.administrativo.CrearServicioDTO;
import com.nuclearvet.aplicacion.dto.administrativo.ServicioDTO;
import com.nuclearvet.aplicacion.mapeadores.ServicioMapeador;
import com.nuclearvet.compartido.excepciones.RecursoNoEncontradoExcepcion;
import com.nuclearvet.dominio.entidades.Servicio;
import com.nuclearvet.dominio.enumeraciones.TipoServicio;
import com.nuclearvet.infraestructura.persistencia.ServicioRepositorio;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServicioServicio {

    private final ServicioRepositorio servicioRepositorio;
    private final ServicioMapeador servicioMapeador;

    @Transactional(readOnly = true)
    public List<ServicioDTO> listarTodos() {
        log.debug("Listando todos los servicios");
        return servicioRepositorio.findAll().stream()
                .map(servicioMapeador::aDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ServicioDTO> listarActivos() {
        log.debug("Listando servicios activos");
        return servicioRepositorio.findAllActivosOrdenados().stream()
                .map(servicioMapeador::aDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ServicioDTO obtenerPorId(Long id) {
        log.debug("Obteniendo servicio con ID: {}", id);
        Servicio servicio = buscarServicioPorId(id);
        return servicioMapeador.aDTO(servicio);
    }

    @Transactional(readOnly = true)
    public ServicioDTO obtenerPorCodigo(String codigo) {
        log.debug("Obteniendo servicio con código: {}", codigo);
        Servicio servicio = servicioRepositorio.findByCodigo(codigo)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Servicio con código " + codigo + " no encontrado"));
        return servicioMapeador.aDTO(servicio);
    }

    @Transactional(readOnly = true)
    public List<ServicioDTO> listarPorTipo(TipoServicio tipo) {
        log.debug("Listando servicios por tipo: {}", tipo);
        return servicioRepositorio.findActivosPorTipo(tipo).stream()
                .map(servicioMapeador::aDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ServicioDTO> buscar(String termino) {
        log.debug("Buscando servicios con término: {}", termino);
        return servicioRepositorio.buscarPorNombreOCodigo(termino).stream()
                .map(servicioMapeador::aDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ServicioDTO> listarPorRangoPrecio(BigDecimal precioMin, BigDecimal precioMax) {
        log.debug("Listando servicios entre {} y {}", precioMin, precioMax);
        return servicioRepositorio.findByRangoPrecio(precioMin, precioMax).stream()
                .map(servicioMapeador::aDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ServicioDTO crear(CrearServicioDTO dto) {
        log.info("Creando nuevo servicio: {}", dto.getNombre());

        Servicio servicio = servicioMapeador.aEntidad(dto);
        Servicio servicioGuardado = servicioRepositorio.save(servicio);

        log.info("Servicio creado exitosamente con ID: {}", servicioGuardado.getId());
        return servicioMapeador.aDTO(servicioGuardado);
    }

    @Transactional
    public ServicioDTO actualizar(Long id, CrearServicioDTO dto) {
        log.info("Actualizando servicio con ID: {}", id);

        Servicio servicio = buscarServicioPorId(id);
        servicioMapeador.actualizarEntidad(dto, servicio);
        Servicio servicioActualizado = servicioRepositorio.save(servicio);

        log.info("Servicio actualizado exitosamente: {}", id);
        return servicioMapeador.aDTO(servicioActualizado);
    }

    @Transactional
    public void eliminar(Long id) {
        log.info("Eliminando servicio con ID: {}", id);
        Servicio servicio = buscarServicioPorId(id);
        servicioRepositorio.delete(servicio);
        log.info("Servicio eliminado exitosamente: {}", id);
    }

    @Transactional
    public ServicioDTO activar(Long id) {
        log.info("Activando servicio con ID: {}", id);
        Servicio servicio = buscarServicioPorId(id);
        servicio.setActivo(true);
        return servicioMapeador.aDTO(servicioRepositorio.save(servicio));
    }

    @Transactional
    public ServicioDTO desactivar(Long id) {
        log.info("Desactivando servicio con ID: {}", id);
        Servicio servicio = buscarServicioPorId(id);
        servicio.setActivo(false);
        return servicioMapeador.aDTO(servicioRepositorio.save(servicio));
    }

    @Transactional(readOnly = true)
    public Map<TipoServicio, Long> contarPorTipo() {
        log.debug("Contando servicios por tipo");
        return servicioRepositorio.contarPorTipo().stream()
                .collect(Collectors.toMap(
                        obj -> (TipoServicio) obj[0],
                        obj -> (Long) obj[1]
                ));
    }

    @Transactional(readOnly = true)
    public Long contarActivos() {
        return servicioRepositorio.contarActivos();
    }

    private Servicio buscarServicioPorId(Long id) {
        return servicioRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Servicio con ID " + id + " no encontrado"));
    }
}
