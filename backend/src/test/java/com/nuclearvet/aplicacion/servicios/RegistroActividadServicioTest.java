package com.nuclearvet.aplicacion.servicios;

import com.nuclearvet.aplicacion.mapeadores.UsuarioMapeador;
import com.nuclearvet.dominio.entidades.Usuario;
import com.nuclearvet.dominio.enums.TipoAccion;
import com.nuclearvet.infraestructura.persistencia.RegistroActividadRepositorio;
import com.nuclearvet.infraestructura.persistencia.UsuarioRepositorio;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistroActividadServicioTest {

    @Mock
    private RegistroActividadRepositorio registroActividadRepositorio;

    @Mock
    private UsuarioRepositorio usuarioRepositorio;

    @Mock
    private UsuarioMapeador usuarioMapeador;

    @Mock
    private HttpServletRequest httpServletRequest;

    @InjectMocks
    private RegistroActividadServicio registroActividadServicio;

    @Test
    void registrarActividad_DeberiaGuardarRegistro() {
        Long usuarioId = 1L;
        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);
        
        when(usuarioRepositorio.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(httpServletRequest.getRemoteAddr()).thenReturn("127.0.0.1");
        
        registroActividadServicio.registrarActividad(usuarioId, TipoAccion.CREAR_USUARIO, "Test", httpServletRequest);
        
        verify(registroActividadRepositorio, times(1)).save(any());
    }
}
