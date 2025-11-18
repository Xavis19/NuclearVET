package com.nuclearvet.aplicacion.mapeadores;

import com.nuclearvet.aplicacion.dtos.UsuarioDTO;
import com.nuclearvet.aplicacion.dtos.CrearUsuarioDTO;
import com.nuclearvet.aplicacion.dtos.RegistroActividadDTO;
import com.nuclearvet.dominio.entidades.Usuario;
import com.nuclearvet.dominio.entidades.RegistroActividad;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

/**
 * Mapeador de entidades Usuario a DTOs y viceversa
 * Usa MapStruct para conversiones automáticas
 */
@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UsuarioMapeador {

    /**
     * Convierte Usuario entidad a UsuarioDTO
     */
    @Mapping(source = "rol.nombre", target = "rolNombre")
    UsuarioDTO aDTO(Usuario usuario);

    /**
     * Convierte lista de Usuario a lista de UsuarioDTO
     */
    List<UsuarioDTO> aDTOLista(List<Usuario> usuarios);

    /**
     * Convierte CrearUsuarioDTO a Usuario entidad
     * No mapea la contraseña (se manejará en el servicio con encriptación)
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "contrasena", ignore = true)
    @Mapping(target = "rol", ignore = true)
    @Mapping(target = "activo", ignore = true)
    @Mapping(target = "tokenRecuperacion", ignore = true)
    @Mapping(target = "fechaExpiracionToken", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "fechaActualizacion", ignore = true)
    Usuario aEntidad(CrearUsuarioDTO dto);

    /**
     * Actualiza un Usuario existente con datos del DTO
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "numeroDocumento", ignore = true)
    @Mapping(target = "tipoDocumento", ignore = true)
    @Mapping(target = "contrasena", ignore = true)
    @Mapping(target = "rol", ignore = true)
    @Mapping(target = "tokenRecuperacion", ignore = true)
    @Mapping(target = "fechaExpiracionToken", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "fechaActualizacion", ignore = true)
    void actualizarEntidad(com.nuclearvet.aplicacion.dtos.ActualizarUsuarioDTO dto, @MappingTarget Usuario usuario);

    /**
     * Convierte RegistroActividad a RegistroActividadDTO
     */
    @Mapping(source = "usuario.id", target = "usuarioId")
    @Mapping(source = "usuario.nombreCompleto", target = "nombreUsuario")
    @Mapping(source = "tipoAccion", target = "tipoAccion")
    RegistroActividadDTO aDTO(RegistroActividad registroActividad);

    /**
     * Convierte lista de RegistroActividad a lista de RegistroActividadDTO
     */
    List<RegistroActividadDTO> aRegistroActividadDTOLista(List<RegistroActividad> registros);
}
