package com.nuclearvet.aplicacion.servicios;

import com.nuclearvet.aplicacion.mapeadores.PacienteMapeador;
import com.nuclearvet.infraestructura.persistencia.PacienteRepositorio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PacienteServicioTest {

    @Mock
    private PacienteRepositorio pacienteRepositorio;

    @Mock
    private PacienteMapeador pacienteMapeador;

    @InjectMocks
    private PacienteServicio pacienteServicio;

    @Test
    void listarPacientes_DeberiaInvocarRepositorio() {
        when(pacienteRepositorio.findAll()).thenReturn(Arrays.asList());
        pacienteServicio.listarPacientes();
        verify(pacienteRepositorio, times(1)).findAll();
    }

    @Test
    void listarPacientesActivos_DeberiaInvocarRepositorio() {
        when(pacienteRepositorio.findPacientesActivos()).thenReturn(Arrays.asList());
        pacienteServicio.listarPacientesActivos();
        verify(pacienteRepositorio, times(1)).findPacientesActivos();
    }

    @Test
    void listarPacientesPorPropietario_DeberiaInvocarRepositorio() {
        Long propietarioId = 1L;
        when(pacienteRepositorio.findByPropietarioId(propietarioId)).thenReturn(Arrays.asList());
        pacienteServicio.listarPacientesPorPropietario(propietarioId);
        verify(pacienteRepositorio, times(1)).findByPropietarioId(propietarioId);
    }

    @Test
    void buscarPacientesPorNombre_DeberiaInvocarRepositorio() {
        String nombre = "Max";
        when(pacienteRepositorio.buscarPorNombre(nombre)).thenReturn(Arrays.asList());
        pacienteServicio.buscarPacientesPorNombre(nombre);
        verify(pacienteRepositorio, times(1)).buscarPorNombre(nombre);
    }
}
