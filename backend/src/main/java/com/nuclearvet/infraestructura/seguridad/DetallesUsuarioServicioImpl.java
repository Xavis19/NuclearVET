package com.nuclearvet.infraestructura.seguridad;

import com.nuclearvet.dominio.entidades.Usuario;
import com.nuclearvet.infraestructura.persistencia.UsuarioRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Implementación del servicio de detalles de usuario para Spring Security
 * RF1.3 - Inicio de sesión seguro
 */
@Service
@RequiredArgsConstructor
public class DetallesUsuarioServicioImpl implements UserDetailsService {

    private final UsuarioRepositorio usuarioRepositorio;

    @Override
    public UserDetails loadUserByUsername(String correoElectronico) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepositorio.findByCorreoElectronico(correoElectronico)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuario no encontrado con correo: " + correoElectronico));

        if (!usuario.getActivo()) {
            throw new UsernameNotFoundException("El usuario está desactivado");
        }

        return User.builder()
                .username(usuario.getCorreoElectronico())
                .password(usuario.getContrasena())
                .authorities(Collections.singletonList(
                        new SimpleGrantedAuthority(usuario.getRol().getNombre())))
                .accountExpired(false)
                .accountLocked(!usuario.getActivo())
                .credentialsExpired(false)
                .disabled(!usuario.getActivo())
                .build();
    }
}
