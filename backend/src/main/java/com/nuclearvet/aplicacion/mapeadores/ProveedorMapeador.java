package com.nuclearvet.aplicacion.mapeadores;

import com.nuclearvet.aplicacion.dtos.ProveedorDTO;
import com.nuclearvet.dominio.entidades.Proveedor;
import org.mapstruct.*;

import java.util.List;

/**
 * Mapeador para convertir entre Proveedor y ProveedorDTO
 */
@Mapper(componentModel = "spring")
public interface ProveedorMapeador {

    @Mapping(target = "cantidadProductos", expression = "java(proveedor.getProductos().size())")
    ProveedorDTO aDTO(Proveedor proveedor);

    List<ProveedorDTO> aDTOLista(List<Proveedor> proveedores);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "fechaActualizacion", ignore = true)
    @Mapping(target = "productos", ignore = true)
    Proveedor aEntidad(ProveedorDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "fechaActualizacion", ignore = true)
    @Mapping(target = "productos", ignore = true)
    void actualizarEntidad(ProveedorDTO dto, @MappingTarget Proveedor proveedor);
}
