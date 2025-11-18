package com.nuclearvet.aplicacion.mapeadores;

import com.nuclearvet.aplicacion.dtos.CitaDTO;
import com.nuclearvet.aplicacion.dtos.CrearCitaDTO;
import com.nuclearvet.dominio.entidades.Cita;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

/**
 * Mapeador para entidad Cita y sus DTOs
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CitaMapeador {

    /**
     * Mapear de entidad a DTO
     */
    @Mapping(source = "paciente.id", target = "pacienteId")
    @Mapping(source = "paciente.nombre", target = "pacienteNombre")
    @Mapping(source = "paciente.codigo", target = "pacienteCodigo")
    @Mapping(source = "propietario.id", target = "propietarioId")
    @Mapping(source = "propietario.nombres", target = "propietarioNombre")
    @Mapping(source = "propietario.telefonoPrincipal", target = "propietarioTelefono")
    @Mapping(source = "veterinario.id", target = "veterinarioId")
    @Mapping(source = "veterinario.nombreCompleto", target = "veterinarioNombre")
    CitaDTO aDTO(Cita cita);

    /**
     * Mapear lista de entidades a lista de DTOs
     */
    List<CitaDTO> aDTOLista(List<Cita> citas);

    /**
     * Mapear de CrearCitaDTO a entidad (sin paciente/veterinario/propietario)
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "numeroCita", ignore = true)
    @Mapping(target = "paciente", ignore = true)
    @Mapping(target = "veterinario", ignore = true)
    @Mapping(target = "propietario", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "sintomas", ignore = true)
    @Mapping(target = "diagnostico", ignore = true)
    @Mapping(target = "tratamiento", ignore = true)
    @Mapping(target = "fechaConfirmacion", ignore = true)
    @Mapping(target = "fechaAtencion", ignore = true)
    @Mapping(target = "fechaFinalizacion", ignore = true)
    @Mapping(target = "motivoCancelacion", ignore = true)
    @Mapping(target = "recordatorioEnviado", ignore = true)
    @Mapping(target = "historial", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "fechaActualizacion", ignore = true)
    Cita aEntidad(CrearCitaDTO dto);
}
