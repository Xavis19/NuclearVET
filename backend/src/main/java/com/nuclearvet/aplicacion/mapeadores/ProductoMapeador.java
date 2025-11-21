package com.nuclearvet.aplicacion.mapeadores;

import com.nuclearvet.aplicacion.dtos.ProductoDTO;
import com.nuclearvet.dominio.entidades.Producto;
import org.mapstruct.*;

import java.util.List;

/**
 * Mapeador para convertir entre Producto y ProductoDTO
 */
@Mapper(componentModel = "spring")
public interface ProductoMapeador {

    @Mapping(source = "categoria.id", target = "categoriaId")
    @Mapping(source = "categoria.nombre", target = "categoriaNombre")
    @Mapping(source = "proveedor.id", target = "proveedorId")
    @Mapping(source = "proveedor.nombre", target = "proveedorNombre")
    @Mapping(target = "stockBajo", expression = "java(producto.stockBajo())")
    ProductoDTO aDTO(Producto producto);

    List<ProductoDTO> aDTOLista(List<Producto> productos);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "fechaActualizacion", ignore = true)
    @Mapping(target = "lotes", ignore = true)
    @Mapping(target = "movimientos", ignore = true)
    @Mapping(target = "alertas", ignore = true)
    @Mapping(target = "categoria", ignore = true)
    @Mapping(target = "proveedor", ignore = true)
    Producto aEntidad(ProductoDTO productoDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "codigo", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "fechaActualizacion", ignore = true)
    @Mapping(target = "lotes", ignore = true)
    @Mapping(target = "movimientos", ignore = true)
    @Mapping(target = "alertas", ignore = true)
    @Mapping(target = "categoria", ignore = true)
    @Mapping(target = "proveedor", ignore = true)
    void actualizarEntidad(ProductoDTO dto, @MappingTarget Producto producto);
}
