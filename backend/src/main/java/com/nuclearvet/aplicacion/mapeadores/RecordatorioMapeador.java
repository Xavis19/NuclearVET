package com.nuclearvet.aplicacion.mapeadores;

import com.nuclearvet.aplicacion.dto.notificaciones.CrearRecordatorioDTO;
import com.nuclearvet.aplicacion.dto.notificaciones.RecordatorioDTO;
import com.nuclearvet.dominio.entidades.Recordatorio;
import org.mapstruct.*;

/**
 * Mapeador MapStruct para Recordatorio
 */
@Mapper(componentModel = "spring")
public interface RecordatorioMapeador {

    @Mapping(source = "paciente.id", target = "pacienteId")
    @Mapping(source = "paciente.nombre", target = "pacienteNombre")
    @Mapping(source = "paciente.propietario.id", target = "propietarioId")
    @Mapping(source = "paciente.propietario.nombreCompleto", target = "propietarioNombre")
    @Mapping(source = "cita.id", target = "citaId")
    @Mapping(target = "diasParaRecordatorio", expression = "java(recordatorio.diasParaRecordatorio())")
    RecordatorioDTO aDTO(Recordatorio recordatorio);

    @Mapping(source = "pacienteId", target = "paciente.id")
    @Mapping(source = "citaId", target = "cita.id")
    Recordatorio aEntidad(CrearRecordatorioDTO dto);
}
