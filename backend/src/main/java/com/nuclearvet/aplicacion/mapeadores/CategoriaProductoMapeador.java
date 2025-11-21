package com.nuclearvet.aplicacion.mapeadores;

import com.nuclearvet.aplicacion.dtos.CategoriaProductoDTO;
import com.nuclearvet.dominio.entidades.CategoriaProducto;
import org.mapstruct.*;

import java.util.List;

/**
 * Mapeador para convertir entre CategoriaProducto y CategoriaProductoDTO
 */
@Mapper(componentModel = "spring")
public interface CategoriaProductoMapeador {

    @Mapping(target = "cantidadProductos", expression = "java(categoria.getProductos().size())")
    CategoriaProductoDTO aDTO(CategoriaProducto categoria);

    List<CategoriaProductoDTO> aDTOLista(List<CategoriaProducto> categorias);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "fechaActualizacion", ignore = true)
    @Mapping(target = "productos", ignore = true)
    CategoriaProducto aEntidad(CategoriaProductoDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "fechaActualizacion", ignore = true)
    @Mapping(target = "productos", ignore = true)
    void actualizarEntidad(CategoriaProductoDTO dto, @MappingTarget CategoriaProducto categoria);
}
