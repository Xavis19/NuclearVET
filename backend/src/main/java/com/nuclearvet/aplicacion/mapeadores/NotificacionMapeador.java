package com.nuclearvet.aplicacion.mapeadores;

import com.nuclearvet.aplicacion.dto.notificaciones.CrearNotificacionDTO;
import com.nuclearvet.aplicacion.dto.notificaciones.NotificacionDTO;
import com.nuclearvet.dominio.entidades.Notificacion;
import org.mapstruct.*;

/**
 * Mapeador MapStruct para Notificacion
 */
@Mapper(componentModel = "spring")
public interface NotificacionMapeador {

    @Mapping(source = "usuarioDestinatario.id", target = "usuarioDestinatarioId")
    @Mapping(source = "usuarioDestinatario.nombreCompleto", target = "usuarioDestinatarioNombre")
    NotificacionDTO aDTO(Notificacion notificacion);

    @Mapping(source = "usuarioDestinatarioId", target = "usuarioDestinatario.id")
    Notificacion aEntidad(CrearNotificacionDTO dto);
}
