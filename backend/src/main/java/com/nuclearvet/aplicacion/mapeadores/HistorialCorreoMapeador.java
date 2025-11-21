package com.nuclearvet.aplicacion.mapeadores;

import com.nuclearvet.aplicacion.dto.notificaciones.HistorialCorreoDTO;
import com.nuclearvet.dominio.entidades.HistorialCorreo;
import org.mapstruct.*;

/**
 * Mapeador MapStruct para HistorialCorreo
 */
@Mapper(componentModel = "spring")
public interface HistorialCorreoMapeador {

    @Mapping(source = "plantilla.id", target = "plantillaId")
    @Mapping(source = "plantilla.nombre", target = "plantillaNombre")
    HistorialCorreoDTO aDTO(HistorialCorreo historial);
}
