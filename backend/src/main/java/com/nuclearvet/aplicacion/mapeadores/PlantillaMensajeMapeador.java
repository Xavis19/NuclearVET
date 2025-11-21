package com.nuclearvet.aplicacion.mapeadores;

import com.nuclearvet.aplicacion.dto.notificaciones.ActualizarPlantillaDTO;
import com.nuclearvet.aplicacion.dto.notificaciones.CrearPlantillaDTO;
import com.nuclearvet.aplicacion.dto.notificaciones.PlantillaMensajeDTO;
import com.nuclearvet.dominio.entidades.PlantillaMensaje;
import org.mapstruct.*;

/**
 * Mapeador MapStruct para PlantillaMensaje
 */
@Mapper(componentModel = "spring")
public interface PlantillaMensajeMapeador {

    PlantillaMensajeDTO aDTO(PlantillaMensaje plantilla);

    PlantillaMensaje aEntidad(CrearPlantillaDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void actualizarEntidad(ActualizarPlantillaDTO dto, @MappingTarget PlantillaMensaje plantilla);
}
