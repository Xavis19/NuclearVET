package com.nuclearvet.aplicacion.mapeadores;

import com.nuclearvet.aplicacion.dtos.ActualizarPacienteDTO;
import com.nuclearvet.aplicacion.dtos.CrearPacienteDTO;
import com.nuclearvet.aplicacion.dtos.PacienteDTO;
import com.nuclearvet.dominio.entidades.Paciente;
import org.mapstruct.*;

import java.util.List;

/**
 * Mapeador para conversiones entre Paciente y sus DTOs
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PacienteMapeador {

    /**
     * Convertir entidad a DTO
     */
    @Mapping(target = "edadAnios", expression = "java(paciente.calcularEdad())")
    @Mapping(target = "edadMeses", expression = "java(paciente.calcularEdadEnMeses())")
    @Mapping(target = "propietarioId", source = "propietario.id")
    @Mapping(target = "propietarioNombre", expression = "java(paciente.getPropietario() != null ? paciente.getPropietario().getNombreCompleto() : null)")
    @Mapping(target = "propietarioTelefono", source = "propietario.telefonoPrincipal")
    @Mapping(target = "veterinarioAsignadoId", source = "veterinarioAsignado.id")
    @Mapping(target = "veterinarioAsignadoNombre", expression = "java(paciente.getVeterinarioAsignado() != null ? paciente.getVeterinarioAsignado().getNombreCompleto() : null)")
    PacienteDTO aDTO(Paciente paciente);

    /**
     * Convertir lista de entidades a lista de DTOs
     */
    List<PacienteDTO> aDTOLista(List<Paciente> pacientes);

    /**
     * Convertir CrearPacienteDTO a entidad
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "codigo", ignore = true) // Se genera automáticamente en el servicio
    @Mapping(target = "estado", constant = "ACTIVO")
    @Mapping(target = "propietario", ignore = true) // Se asigna en el servicio
    @Mapping(target = "veterinarioAsignado", ignore = true) // Se asigna en el servicio
    @Mapping(target = "fechaRegistro", ignore = true)
    @Mapping(target = "fechaActualizacion", ignore = true)
    Paciente aEntidad(CrearPacienteDTO dto);

    /**
     * Actualizar entidad existente con datos del DTO
     * Solo actualiza campos no nulos
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "codigo", ignore = true)
    @Mapping(target = "propietario", ignore = true)
    @Mapping(target = "veterinarioAsignado", ignore = true)
    @Mapping(target = "fechaRegistro", ignore = true)
    @Mapping(target = "fechaActualizacion", ignore = true)
    void actualizarEntidad(ActualizarPacienteDTO dto, @MappingTarget Paciente paciente);
}
