package com.nuclearvet.aplicacion.mapeadores;

import com.nuclearvet.aplicacion.dtos.ArchivoMedicoDTO;
import com.nuclearvet.dominio.entidades.ArchivoMedico;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

/**
 * Mapeador para Archivos Médicos
 */
@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ArchivoMedicoMapeador {

    /**
     * Convertir entidad a DTO
     */
    @Mapping(target = "historiaClinicaId", source = "historiaClinica.id")
    @Mapping(target = "numeroHistoria", source = "historiaClinica.numeroHistoria")
    @Mapping(target = "consultaId", source = "consulta.id")
    @Mapping(target = "numeroConsulta", source = "consulta.numeroConsulta")
    @Mapping(target = "subidoPorId", source = "subidoPor.id")
    @Mapping(target = "subidoPorNombre", source = "subidoPor.nombreCompleto")
    @Mapping(target = "tamanoFormateado", expression = "java(archivoMedico.getTamanoFormateado())")
    ArchivoMedicoDTO aDTO(ArchivoMedico archivoMedico);

    /**
     * Convertir lista de entidades a DTOs
     */
    List<ArchivoMedicoDTO> aDTOLista(List<ArchivoMedico> archivosMedicos);
}
