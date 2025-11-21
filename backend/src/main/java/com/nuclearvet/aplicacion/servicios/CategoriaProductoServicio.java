package com.nuclearvet.aplicacion.servicios;

import com.nuclearvet.aplicacion.dtos.CategoriaProductoDTO;
import com.nuclearvet.aplicacion.mapeadores.CategoriaProductoMapeador;
import com.nuclearvet.compartido.excepciones.RecursoNoEncontradoExcepcion;
import com.nuclearvet.dominio.entidades.CategoriaProducto;
import com.nuclearvet.infraestructura.persistencia.CategoriaProductoRepositorio;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio para gestión de categorías de productos
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CategoriaProductoServicio {

    private final CategoriaProductoRepositorio categoriaRepositorio;
    private final CategoriaProductoMapeador categoriaMapeador;

    @Transactional
    public CategoriaProductoDTO crear(CategoriaProductoDTO dto) {
        log.info("Creando nueva categoría: {}", dto.getNombre());

        if (categoriaRepositorio.existsByNombre(dto.getNombre())) {
            throw new IllegalArgumentException("Ya existe una categoría con ese nombre");
        }

        CategoriaProducto categoria = categoriaMapeador.aEntidad(dto);
        categoria.setActivo(true);

        CategoriaProducto guardada = categoriaRepositorio.save(categoria);
        log.info("Categoría creada con ID: {}", guardada.getId());

        return categoriaMapeador.aDTO(guardada);
    }

    @Transactional
    public CategoriaProductoDTO actualizar(Long id, CategoriaProductoDTO dto) {
        log.info("Actualizando categoría ID: {}", id);

        CategoriaProducto categoria = categoriaRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Categoría no encontrada"));

        if (!categoria.getNombre().equals(dto.getNombre()) &&
                categoriaRepositorio.existsByNombre(dto.getNombre())) {
            throw new IllegalArgumentException("Ya existe una categoría con ese nombre");
        }

        categoria.setNombre(dto.getNombre());
        categoria.setDescripcion(dto.getDescripcion());

        CategoriaProducto actualizada = categoriaRepositorio.save(categoria);
        log.info("Categoría ID {} actualizada", id);

        return categoriaMapeador.aDTO(actualizada);
    }

    @Transactional(readOnly = true)
    public CategoriaProductoDTO obtenerPorId(Long id) {
        CategoriaProducto categoria = categoriaRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Categoría no encontrada"));
        return categoriaMapeador.aDTO(categoria);
    }

    @Transactional(readOnly = true)
    public List<CategoriaProductoDTO> listarTodas() {
        return categoriaMapeador.aDTOLista(categoriaRepositorio.findAllActivasOrdenadas());
    }

    @Transactional(readOnly = true)
    public List<CategoriaProductoDTO> listarActivas() {
        return categoriaMapeador.aDTOLista(categoriaRepositorio.findByActivoTrue());
    }

    @Transactional(readOnly = true)
    public List<CategoriaProductoDTO> buscarPorNombre(String termino) {
        return categoriaMapeador.aDTOLista(
                categoriaRepositorio.buscarPorNombre(termino));
    }

    @Transactional
    public void eliminar(Long id) {
        CategoriaProducto categoria = categoriaRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Categoría no encontrada"));

        long cantidadProductos = categoriaRepositorio.contarProductosActivos(id);
        if (cantidadProductos > 0) {
            throw new IllegalStateException(
                    "No se puede eliminar la categoría porque tiene " + cantidadProductos + " productos asociados");
        }

        categoriaRepositorio.delete(categoria);
        log.info("Categoría ID {} eliminada", id);
    }

    @Transactional
    public void desactivar(Long id) {
        CategoriaProducto categoria = categoriaRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Categoría no encontrada"));

        categoria.setActivo(false);
        categoriaRepositorio.save(categoria);
        log.info("Categoría ID {} desactivada", id);
    }

    @Transactional
    public void activar(Long id) {
        CategoriaProducto categoria = categoriaRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Categoría no encontrada"));

        categoria.setActivo(true);
        categoriaRepositorio.save(categoria);
        log.info("Categoría ID {} activada", id);
    }
}
