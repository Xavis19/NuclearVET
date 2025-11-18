package com.nuclearvet.aplicacion.mapeadores;

import com.nuclearvet.aplicacion.dtos.ActualizarPropietarioDTO;
import com.nuclearvet.aplicacion.dtos.CrearPropietarioDTO;
import com.nuclearvet.aplicacion.dtos.PropietarioDTO;
import com.nuclearvet.dominio.entidades.Propietario;
import org.mapstruct.*;

import java.util.List;

/**
 * Mapeador para conversiones entre Propietario y sus DTOs
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PropietarioMapeador {

    /**
     * Convertir entidad a DTO
     */
    @Mapping(target = "nombreCompleto", expression = "java(propietario.getNombreCompleto())")
    @Mapping(target = "cantidadPacientes", expression = "java(propietario.getPacientes() != null ? propietario.getPacientes().size() : 0)")
    PropietarioDTO aDTO(Propietario propietario);

    /**
     * Convertir lista de entidades a lista de DTOs
     */
    List<PropietarioDTO> aDTOLista(List<Propietario> propietarios);

    /**
     * Convertir CrearPropietarioDTO a entidad
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activo", constant = "true")
    @Mapping(target = "pacientes", ignore = true)
    @Mapping(target = "fechaRegistro", ignore = true)
    @Mapping(target = "fechaActualizacion", ignore = true)
    Propietario aEntidad(CrearPropietarioDTO dto);

    /**
     * Actualizar entidad existente con datos del DTO
     * Solo actualiza campos no nulos
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pacientes", ignore = true)
    @Mapping(target = "activo", ignore = true)
    @Mapping(target = "fechaRegistro", ignore = true)
    @Mapping(target = "fechaActualizacion", ignore = true)
    void actualizarEntidad(ActualizarPropietarioDTO dto, @MappingTarget Propietario propietario);
}
