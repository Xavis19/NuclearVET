package com.nuclearvet.aplicacion.servicios;

import com.nuclearvet.aplicacion.dtos.ActualizarUsuarioDTO;
import com.nuclearvet.aplicacion.dtos.CambiarContrasenaDTO;
import com.nuclearvet.aplicacion.dtos.CrearUsuarioDTO;
import com.nuclearvet.aplicacion.dtos.UsuarioDTO;
import com.nuclearvet.aplicacion.mapeadores.UsuarioMapeador;
import com.nuclearvet.compartido.excepciones.RecursoNoEncontradoExcepcion;
import com.nuclearvet.dominio.entidades.Rol;
import com.nuclearvet.dominio.entidades.Usuario;
import com.nuclearvet.dominio.enums.TipoAccion;
import com.nuclearvet.infraestructura.persistencia.RolRepositorio;
import com.nuclearvet.infraestructura.persistencia.UsuarioRepositorio;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio para gestión de usuarios
 * RF1.1 - Gestión de usuarios
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UsuarioServicio {

    private final UsuarioRepositorio usuarioRepositorio;
    private final RolRepositorio rolRepositorio;
    private final UsuarioMapeador usuarioMapeador;
    private final PasswordEncoder passwordEncoder;
    private final RegistroActividadServicio registroActividadServicio;

    /**
     * Crear un nuevo usuario
     */
    @Transactional
    public UsuarioDTO crearUsuario(CrearUsuarioDTO dto, HttpServletRequest request) {
        // Validar que no exista el correo
        if (usuarioRepositorio.existsByCorreoElectronico(dto.getCorreoElectronico())) {
            throw new IllegalArgumentException("El correo electrónico ya está registrado");
        }

        // Validar que no exista el documento
        if (usuarioRepositorio.existsByNumeroDocumento(dto.getNumeroDocumento())) {
            throw new IllegalArgumentException("El número de documento ya está registrado");
        }

        // Buscar el rol
        Rol rol = rolRepositorio.findById(dto.getRolId())
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Rol", "id", dto.getRolId()));

        // Crear usuario
        Usuario usuario = usuarioMapeador.aEntidad(dto);
        usuario.setContrasena(passwordEncoder.encode(dto.getContrasena()));
        usuario.setRol(rol);
        usuario.setActivo(true);

        // Guardar
        Usuario usuarioGuardado = usuarioRepositorio.save(usuario);

        // Registrar actividad
        registroActividadServicio.registrarActividad(
                usuarioGuardado.getId(),
                TipoAccion.CREAR_USUARIO,
                "Usuario creado: " + usuarioGuardado.getCorreoElectronico(),
                request
        );

        log.info("Usuario creado exitosamente: {}", usuarioGuardado.getCorreoElectronico());
        return usuarioMapeador.aDTO(usuarioGuardado);
    }

    /**
     * Obtener usuario por ID
     */
    @Transactional(readOnly = true)
    public UsuarioDTO obtenerUsuarioPorId(Long id) {
        Usuario usuario = usuarioRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Usuario", "id", id));
        return usuarioMapeador.aDTO(usuario);
    }

    /**
     * Obtener usuario por correo electrónico
     */
    @Transactional(readOnly = true)
    public UsuarioDTO obtenerUsuarioPorCorreo(String correoElectronico) {
        Usuario usuario = usuarioRepositorio.findByCorreoElectronico(correoElectronico)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Usuario", "correo", correoElectronico));
        return usuarioMapeador.aDTO(usuario);
    }

    /**
     * Listar todos los usuarios
     */
    @Transactional(readOnly = true)
    public List<UsuarioDTO> listarUsuarios() {
        List<Usuario> usuarios = usuarioRepositorio.findAll();
        return usuarioMapeador.aDTOLista(usuarios);
    }

    /**
     * Listar usuarios activos
     */
    @Transactional(readOnly = true)
    public List<UsuarioDTO> listarUsuariosActivos() {
        List<Usuario> usuarios = usuarioRepositorio.findByActivo(true);
        return usuarioMapeador.aDTOLista(usuarios);
    }

    /**
     * Listar usuarios por rol
     */
    @Transactional(readOnly = true)
    public List<UsuarioDTO> listarUsuariosPorRol(String nombreRol) {
        List<Usuario> usuarios = usuarioRepositorio.findByRolNombre(nombreRol);
        return usuarioMapeador.aDTOLista(usuarios);
    }

    /**
     * Buscar usuarios por nombre
     */
    @Transactional(readOnly = true)
    public List<UsuarioDTO> buscarUsuariosPorNombre(String nombre) {
        List<Usuario> usuarios = usuarioRepositorio.buscarPorNombre(nombre);
        return usuarioMapeador.aDTOLista(usuarios);
    }

    /**
     * Actualizar usuario
     */
    @Transactional
    public UsuarioDTO actualizarUsuario(Long id, ActualizarUsuarioDTO dto, HttpServletRequest request) {
        Usuario usuario = usuarioRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Usuario", "id", id));

        // Validar correo único si se está cambiando
        if (dto.getCorreoElectronico() != null && 
            !dto.getCorreoElectronico().equals(usuario.getCorreoElectronico()) &&
            usuarioRepositorio.existsByCorreoElectronico(dto.getCorreoElectronico())) {
            throw new IllegalArgumentException("El correo electrónico ya está en uso");
        }

        // Actualizar campos
        usuarioMapeador.actualizarEntidad(dto, usuario);

        // Actualizar rol si se proporciona
        if (dto.getRolId() != null) {
            Rol rol = rolRepositorio.findById(dto.getRolId())
                    .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Rol", "id", dto.getRolId()));
            usuario.setRol(rol);
        }

        // Guardar
        Usuario usuarioActualizado = usuarioRepositorio.save(usuario);

        // Registrar actividad
        registroActividadServicio.registrarActividad(
                usuarioActualizado.getId(),
                TipoAccion.ACTUALIZAR_USUARIO,
                "Usuario actualizado: " + usuarioActualizado.getCorreoElectronico(),
                request
        );

        log.info("Usuario actualizado exitosamente: {}", usuarioActualizado.getCorreoElectronico());
        return usuarioMapeador.aDTO(usuarioActualizado);
    }

    /**
     * Desactivar usuario
     */
    @Transactional
    public void desactivarUsuario(Long id, HttpServletRequest request) {
        Usuario usuario = usuarioRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Usuario", "id", id));

        usuario.setActivo(false);
        usuarioRepositorio.save(usuario);

        // Registrar actividad
        registroActividadServicio.registrarActividad(
                usuario.getId(),
                TipoAccion.DESACTIVAR_USUARIO,
                "Usuario desactivado: " + usuario.getCorreoElectronico(),
                request
        );

        log.info("Usuario desactivado: {}", usuario.getCorreoElectronico());
    }

    /**
     * Activar usuario
     */
    @Transactional
    public UsuarioDTO activarUsuario(Long id, HttpServletRequest request) {
        Usuario usuario = usuarioRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Usuario", "id", id));

        usuario.setActivo(true);
        usuarioRepositorio.save(usuario);

        // Registrar actividad
        registroActividadServicio.registrarActividad(
                usuario.getId(),
                TipoAccion.ACTIVAR_USUARIO,
                "Usuario activado: " + usuario.getCorreoElectronico(),
                request
        );

        log.info("Usuario activado: {}", usuario.getCorreoElectronico());
        return usuarioMapeador.aDTO(usuario);
    }

    /**
     * Cambiar contraseña del usuario
     */
    @Transactional
    public void cambiarContrasena(Long id, CambiarContrasenaDTO dto, HttpServletRequest request) {
        Usuario usuario = usuarioRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Usuario", "id", id));

        // Validar contraseña actual
        if (!passwordEncoder.matches(dto.getContrasenaActual(), usuario.getContrasena())) {
            throw new IllegalArgumentException("La contraseña actual es incorrecta");
        }

        // Validar que la nueva contraseña coincida con la confirmación
        if (!dto.getContrasenaNueva().equals(dto.getConfirmacionContrasena())) {
            throw new IllegalArgumentException("La nueva contraseña y su confirmación no coinciden");
        }

        // Actualizar contraseña
        usuario.setContrasena(passwordEncoder.encode(dto.getContrasenaNueva()));
        usuarioRepositorio.save(usuario);

        // Registrar actividad
        registroActividadServicio.registrarActividad(
                usuario.getId(),
                TipoAccion.CAMBIAR_CONTRASENA,
                "Contraseña cambiada exitosamente",
                request
        );

        log.info("Contraseña cambiada para usuario: {}", usuario.getCorreoElectronico());
    }

    /**
     * Eliminar usuario (hard delete - solo para testing o casos especiales)
     */
    @Transactional
    public void eliminarUsuario(Long id) {
        Usuario usuario = usuarioRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Usuario", "id", id));

        usuarioRepositorio.delete(usuario);
        log.warn("Usuario eliminado permanentemente: {}", usuario.getCorreoElectronico());
    }
}
