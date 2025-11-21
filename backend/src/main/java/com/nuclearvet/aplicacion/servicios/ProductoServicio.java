package com.nuclearvet.aplicacion.servicios;

import com.nuclearvet.aplicacion.dtos.ProductoDTO;
import com.nuclearvet.aplicacion.mapeadores.ProductoMapeador;
import com.nuclearvet.compartido.excepciones.RecursoNoEncontradoExcepcion;
import com.nuclearvet.dominio.entidades.CategoriaProducto;
import com.nuclearvet.dominio.entidades.Producto;
import com.nuclearvet.dominio.entidades.Proveedor;
import com.nuclearvet.dominio.enumeraciones.TipoProducto;
import com.nuclearvet.infraestructura.persistencia.CategoriaProductoRepositorio;
import com.nuclearvet.infraestructura.persistencia.ProductoRepositorio;
import com.nuclearvet.infraestructura.persistencia.ProveedorRepositorio;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio para gestión de productos del inventario
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductoServicio {

    private final ProductoRepositorio productoRepositorio;
    private final CategoriaProductoRepositorio categoriaRepositorio;
    private final ProveedorRepositorio proveedorRepositorio;
    private final ProductoMapeador productoMapeador;

    @Transactional
    public ProductoDTO crear(ProductoDTO productoDTO) {
        log.info("Creando producto: {}", productoDTO.getNombre());

        if (productoRepositorio.existsByCodigo(productoDTO.getCodigo())) {
            throw new IllegalArgumentException("Ya existe un producto con el código: " + productoDTO.getCodigo());
        }

        CategoriaProducto categoria = categoriaRepositorio.findById(productoDTO.getCategoriaId())
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Categoría no encontrada"));

        Producto producto = productoMapeador.aEntidad(productoDTO);
        producto.setCategoria(categoria);
        producto.setActivo(true);
        producto.setStockActual(0);

        if (productoDTO.getProveedorId() != null) {
            Proveedor proveedor = proveedorRepositorio.findById(productoDTO.getProveedorId())
                    .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Proveedor no encontrado"));
            producto.setProveedor(proveedor);
        }

        Producto guardado = productoRepositorio.save(producto);
        log.info("Producto creado exitosamente con ID: {}", guardado.getId());

        return productoMapeador.aDTO(guardado);
    }

    @Transactional
    public ProductoDTO actualizar(Long id, ProductoDTO productoDTO) {
        log.info("Actualizando producto con ID: {}", id);

        Producto producto = productoRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Producto no encontrado"));

        if (!producto.getCodigo().equals(productoDTO.getCodigo()) &&
            productoRepositorio.existsByCodigoAndIdNot(productoDTO.getCodigo(), id)) {
            throw new IllegalArgumentException("Ya existe otro producto con el código: " + productoDTO.getCodigo());
        }

        productoMapeador.actualizarEntidad(productoDTO, producto);

        if (productoDTO.getCategoriaId() != null && !productoDTO.getCategoriaId().equals(producto.getCategoria().getId())) {
            CategoriaProducto categoria = categoriaRepositorio.findById(productoDTO.getCategoriaId())
                    .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Categoría no encontrada"));
            producto.setCategoria(categoria);
        }

        if (productoDTO.getProveedorId() != null) {
            Proveedor proveedor = proveedorRepositorio.findById(productoDTO.getProveedorId())
                    .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Proveedor no encontrado"));
            producto.setProveedor(proveedor);
        }

        Producto actualizado = productoRepositorio.save(producto);
        log.info("Producto actualizado exitosamente");

        return productoMapeador.aDTO(actualizado);
    }

    @Transactional(readOnly = true)
    public ProductoDTO obtenerPorId(Long id) {
        Producto producto = productoRepositorio.findByIdConRelaciones(id)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Producto no encontrado"));
        return productoMapeador.aDTO(producto);
    }

    @Transactional(readOnly = true)
    public ProductoDTO obtenerPorCodigo(String codigo) {
        Producto producto = productoRepositorio.findByCodigo(codigo)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Producto no encontrado con código: " + codigo));
        return productoMapeador.aDTO(producto);
    }

    @Transactional(readOnly = true)
    public List<ProductoDTO> listarTodos() {
        return productoMapeador.aDTOLista(productoRepositorio.findAll());
    }

    @Transactional(readOnly = true)
    public List<ProductoDTO> listarActivos() {
        return productoMapeador.aDTOLista(productoRepositorio.findAllActivosOrdenados());
    }

    @Transactional(readOnly = true)
    public List<ProductoDTO> buscarPorNombreOCodigo(String termino) {
        return productoMapeador.aDTOLista(productoRepositorio.buscarPorNombreOCodigo(termino));
    }

    @Transactional(readOnly = true)
    public List<ProductoDTO> listarPorCategoria(Long categoriaId) {
        return productoMapeador.aDTOLista(productoRepositorio.findByCategoriaIdOrdenados(categoriaId));
    }

    @Transactional(readOnly = true)
    public List<ProductoDTO> listarPorTipo(TipoProducto tipo) {
        return productoMapeador.aDTOLista(productoRepositorio.findByTipoProductoOrdenados(tipo));
    }

    @Transactional(readOnly = true)
    public List<ProductoDTO> listarProductosStockBajo() {
        return productoMapeador.aDTOLista(productoRepositorio.findProductosStockBajo());
    }

    @Transactional(readOnly = true)
    public List<ProductoDTO> listarProductosAgotados() {
        return productoMapeador.aDTOLista(productoRepositorio.findProductosAgotados());
    }

    @Transactional
    public void eliminar(Long id) {
        log.info("Eliminando (desactivando) producto con ID: {}", id);
        Producto producto = productoRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Producto no encontrado"));
        
        producto.setActivo(false);
        productoRepositorio.save(producto);
        log.info("Producto desactivado exitosamente");
    }

    @Transactional
    public void activar(Long id) {
        log.info("Activando producto con ID: {}", id);
        Producto producto = productoRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Producto no encontrado"));
        
        producto.setActivo(true);
        productoRepositorio.save(producto);
        log.info("Producto activado exitosamente");
    }

    public String generarCodigoProducto() {
        Long cantidad = productoRepositorio.count();
        return String.format("PROD-%06d", cantidad + 1);
    }
}
