package com.nuclearvet.aplicacion.servicios;

import com.nuclearvet.aplicacion.dtos.UsuarioDTO;
import com.nuclearvet.aplicacion.mapeadores.UsuarioMapeador;
import com.nuclearvet.dominio.entidades.Usuario;
import com.nuclearvet.infraestructura.persistencia.UsuarioRepositorio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServicioTest {

    @Mock
    private UsuarioRepositorio usuarioRepositorio;

    @Mock
    private UsuarioMapeador usuarioMapeador;

    @InjectMocks
    private UsuarioServicio usuarioServicio;

    @Test
    void listarUsuarios_DeberiaInvocarRepositorio() {
        when(usuarioRepositorio.findAll()).thenReturn(Arrays.asList());
        usuarioServicio.listarUsuarios();
        verify(usuarioRepositorio, times(1)).findAll();
    }

    @Test
    void listarUsuariosActivos_DeberiaInvocarRepositorio() {
        when(usuarioRepositorio.findByActivo(true)).thenReturn(Arrays.asList());
        usuarioServicio.listarUsuariosActivos();
        verify(usuarioRepositorio, times(1)).findByActivo(true);
    }

    @Test
    void obtenerUsuarioPorCorreo_DeberiaRetornarUsuario() {
        String correo = "test@test.com";
        Usuario usuario = new Usuario();
        UsuarioDTO dto = new UsuarioDTO();
        
        when(usuarioRepositorio.findByCorreoElectronico(correo)).thenReturn(Optional.of(usuario));
        when(usuarioMapeador.aDTO(usuario)).thenReturn(dto);
        
        usuarioServicio.obtenerUsuarioPorCorreo(correo);
        
        verify(usuarioRepositorio, times(1)).findByCorreoElectronico(correo);
        verify(usuarioMapeador, times(1)).aDTO(usuario);
    }

    @Test
    void buscarUsuariosPorNombre_DeberiaInvocarRepositorio() {
        String nombre = "Juan";
        when(usuarioRepositorio.buscarPorNombre(nombre)).thenReturn(Arrays.asList());
        usuarioServicio.buscarUsuariosPorNombre(nombre);
        verify(usuarioRepositorio, times(1)).buscarPorNombre(nombre);
    }
}
