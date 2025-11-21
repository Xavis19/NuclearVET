package com.nuclearvet.aplicacion.servicios;

import com.nuclearvet.aplicacion.dtos.PropietarioDTO;
import com.nuclearvet.aplicacion.mapeadores.PropietarioMapeador;
import com.nuclearvet.dominio.entidades.Propietario;
import com.nuclearvet.infraestructura.persistencia.PropietarioRepositorio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PropietarioServicioTest {

    @Mock
    private PropietarioRepositorio propietarioRepositorio;

    @Mock
    private PropietarioMapeador propietarioMapeador;

    @InjectMocks
    private PropietarioServicio propietarioServicio;

    @Test
    void listarPropietarios_DeberiaInvocarRepositorio() {
        when(propietarioRepositorio.findAll()).thenReturn(Arrays.asList());
        propietarioServicio.listarPropietarios();
        verify(propietarioRepositorio, times(1)).findAll();
    }

    @Test
    void listarPropietariosActivos_DeberiaInvocarRepositorio() {
        when(propietarioRepositorio.findByActivoTrue()).thenReturn(Arrays.asList());
        propietarioServicio.listarPropietariosActivos();
        verify(propietarioRepositorio, times(1)).findByActivoTrue();
    }

    @Test
    void buscarPropietariosPorNombre_DeberiaInvocarRepositorio() {
        String nombre = "Juan";
        when(propietarioRepositorio.buscarPorNombre(nombre)).thenReturn(Arrays.asList());
        propietarioServicio.buscarPropietariosPorNombre(nombre);
        verify(propietarioRepositorio, times(1)).buscarPorNombre(nombre);
    }

    @Test
    void obtenerPropietarioPorIdentificacion_DeberiaRetornarPropietario() {
        String identificacion = "123456789";
        Propietario propietario = new Propietario();
        PropietarioDTO dto = new PropietarioDTO();
        
        when(propietarioRepositorio.findByNumeroIdentificacion(identificacion)).thenReturn(Optional.of(propietario));
        when(propietarioMapeador.aDTO(propietario)).thenReturn(dto);
        
        propietarioServicio.obtenerPropietarioPorIdentificacion(identificacion);
        
        verify(propietarioRepositorio, times(1)).findByNumeroIdentificacion(identificacion);
        verify(propietarioMapeador, times(1)).aDTO(propietario);
    }

    @Test
    void contarPropietariosActivos_DeberiaInvocarRepositorio() {
        when(propietarioRepositorio.contarPropietariosActivos()).thenReturn(10L);
        propietarioServicio.contarPropietariosActivos();
        verify(propietarioRepositorio, times(1)).contarPropietariosActivos();
    }
}
