package com.nuclearvet.aplicacion.mapeadores;

import com.nuclearvet.aplicacion.dtos.ConsultaDTO;
import com.nuclearvet.aplicacion.dtos.CrearConsultaDTO;
import com.nuclearvet.dominio.entidades.Consulta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

/**
 * Mapeador para Consultas
 */
@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ConsultaMapeador {

    /**
     * Convertir entidad a DTO
     */
    @Mapping(target = "historiaClinicaId", source = "historiaClinica.id")
    @Mapping(target = "numeroHistoria", source = "historiaClinica.numeroHistoria")
    @Mapping(target = "pacienteId", source = "historiaClinica.paciente.id")
    @Mapping(target = "pacienteNombre", source = "historiaClinica.paciente.nombre")
    @Mapping(target = "pacienteCodigo", source = "historiaClinica.paciente.codigo")
    @Mapping(target = "veterinarioId", source = "veterinario.id")
    @Mapping(target = "veterinarioNombre", source = "veterinario.nombreCompleto")
    @Mapping(target = "citaId", source = "cita.id")
    @Mapping(target = "numeroCita", source = "cita.numeroCita")
    ConsultaDTO aDTO(Consulta consulta);

    /**
     * Convertir lista de entidades a DTOs
     */
    List<ConsultaDTO> aDTOLista(List<Consulta> consultas);

    /**
     * Convertir DTO de creación a entidad
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "numeroConsulta", ignore = true)
    @Mapping(target = "historiaClinica", ignore = true)
    @Mapping(target = "veterinario", ignore = true)
    @Mapping(target = "cita", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "fechaActualizacion", ignore = true)
    Consulta aEntidad(CrearConsultaDTO dto);
}
