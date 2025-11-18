package com.nuclearvet.aplicacion.mapeadores;

import com.nuclearvet.aplicacion.dtos.ActualizarHistoriaClinicaDTO;
import com.nuclearvet.aplicacion.dtos.HistoriaClinicaDTO;
import com.nuclearvet.dominio.entidades.HistoriaClinica;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

/**
 * Mapeador para Historia Clínica
 */
@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface HistoriaClinicaMapeador {

    /**
     * Convertir entidad a DTO
     */
    @Mapping(target = "pacienteId", source = "paciente.id")
    @Mapping(target = "pacienteNombre", source = "paciente.nombre")
    @Mapping(target = "pacienteCodigo", source = "paciente.codigo")
    @Mapping(target = "totalConsultas", expression = "java(historiaClinica.contarConsultas())")
    @Mapping(target = "fechaUltimaConsulta", expression = "java(historiaClinica.getUltimaConsulta() != null ? historiaClinica.getUltimaConsulta().getFechaConsulta() : null)")
    HistoriaClinicaDTO aDTO(HistoriaClinica historiaClinica);

    /**
     * Convertir lista de entidades a DTOs
     */
    List<HistoriaClinicaDTO> aDTOLista(List<HistoriaClinica> historiasClinicas);

    /**
     * Actualizar entidad desde DTO (solo campos modificables)
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "numeroHistoria", ignore = true)
    @Mapping(target = "paciente", ignore = true)
    @Mapping(target = "consultas", ignore = true)
    @Mapping(target = "archivosMedicos", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "fechaActualizacion", ignore = true)
    void actualizarEntidad(ActualizarHistoriaClinicaDTO dto, @MappingTarget HistoriaClinica historiaClinica);
}
