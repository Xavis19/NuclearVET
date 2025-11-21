package com.nuclearvet.aplicacion.mapeadores;

import com.nuclearvet.aplicacion.dto.administrativo.CrearServicioDTO;
import com.nuclearvet.aplicacion.dto.administrativo.ServicioDTO;
import com.nuclearvet.dominio.entidades.Servicio;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ServicioMapeador {

    @Mapping(target = "precioTotal", expression = "java(servicio.calcularPrecioTotal())")
    ServicioDTO aDTO(Servicio servicio);

    Servicio aEntidad(CrearServicioDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void actualizarEntidad(CrearServicioDTO dto, @MappingTarget Servicio servicio);
}
