package com.nuclearvet.aplicacion.servicios;

import com.nuclearvet.aplicacion.dtos.ProveedorDTO;
import com.nuclearvet.aplicacion.mapeadores.ProveedorMapeador;
import com.nuclearvet.compartido.excepciones.RecursoNoEncontradoExcepcion;
import com.nuclearvet.dominio.entidades.Proveedor;
import com.nuclearvet.infraestructura.persistencia.ProveedorRepositorio;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio para gestión de proveedores
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProveedorServicio {

    private final ProveedorRepositorio proveedorRepositorio;
    private final ProveedorMapeador proveedorMapeador;

    @Transactional
    public ProveedorDTO crear(ProveedorDTO dto) {
        log.info("Creando nuevo proveedor: {}", dto.getNombre());

        if (proveedorRepositorio.existsByNit(dto.getNit())) {
            throw new IllegalArgumentException("Ya existe un proveedor con ese NIT");
        }

        Proveedor proveedor = proveedorMapeador.aEntidad(dto);
        proveedor.setActivo(true);

        Proveedor guardado = proveedorRepositorio.save(proveedor);
        log.info("Proveedor creado con ID: {}", guardado.getId());

        return proveedorMapeador.aDTO(guardado);
    }

    @Transactional
    public ProveedorDTO actualizar(Long id, ProveedorDTO dto) {
        log.info("Actualizando proveedor ID: {}", id);

        Proveedor proveedor = proveedorRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Proveedor no encontrado"));

        if (!proveedor.getNit().equals(dto.getNit()) &&
                proveedorRepositorio.existsByNit(dto.getNit())) {
            throw new IllegalArgumentException("Ya existe un proveedor con ese NIT");
        }

        proveedor.setNombre(dto.getNombre());
        proveedor.setNit(dto.getNit());
        proveedor.setContactoPrincipal(dto.getContactoPrincipal());
        proveedor.setTelefonoContacto(dto.getTelefonoContacto());
        proveedor.setTelefono(dto.getTelefono());
        proveedor.setEmail(dto.getEmail());
        proveedor.setDireccion(dto.getDireccion());
        proveedor.setObservaciones(dto.getObservaciones());

        Proveedor actualizado = proveedorRepositorio.save(proveedor);
        log.info("Proveedor ID {} actualizado", id);

        return proveedorMapeador.aDTO(actualizado);
    }

    @Transactional(readOnly = true)
    public ProveedorDTO obtenerPorId(Long id) {
        Proveedor proveedor = proveedorRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Proveedor no encontrado"));
        return proveedorMapeador.aDTO(proveedor);
    }

    @Transactional(readOnly = true)
    public ProveedorDTO obtenerPorNit(String nit) {
        Proveedor proveedor = proveedorRepositorio.findByNit(nit)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Proveedor no encontrado"));
        return proveedorMapeador.aDTO(proveedor);
    }

    @Transactional(readOnly = true)
    public List<ProveedorDTO> listarTodos() {
        return proveedorMapeador.aDTOLista(proveedorRepositorio.findAllActivosOrdenados());
    }

    @Transactional(readOnly = true)
    public List<ProveedorDTO> listarActivos() {
        return proveedorMapeador.aDTOLista(proveedorRepositorio.findByActivoTrue());
    }

    @Transactional(readOnly = true)
    public List<ProveedorDTO> buscarPorNombre(String termino) {
        return proveedorMapeador.aDTOLista(proveedorRepositorio.buscarPorNombreONit(termino));
    }

    @Transactional
    public void eliminar(Long id) {
        Proveedor proveedor = proveedorRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Proveedor no encontrado"));

        long cantidadProductos = proveedorRepositorio.contarProductosActivos(id);
        if (cantidadProductos > 0) {
            throw new IllegalStateException(
                    "No se puede eliminar el proveedor porque tiene " + cantidadProductos + " productos asociados");
        }

        proveedorRepositorio.delete(proveedor);
        log.info("Proveedor ID {} eliminado", id);
    }

    @Transactional
    public void desactivar(Long id) {
        Proveedor proveedor = proveedorRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Proveedor no encontrado"));

        proveedor.setActivo(false);
        proveedorRepositorio.save(proveedor);
        log.info("Proveedor ID {} desactivado", id);
    }

    @Transactional
    public void activar(Long id) {
        Proveedor proveedor = proveedorRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Proveedor no encontrado"));

        proveedor.setActivo(true);
        proveedorRepositorio.save(proveedor);
        log.info("Proveedor ID {} activado", id);
    }
}
