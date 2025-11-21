package com.nuclearvet.aplicacion.servicios;

import com.nuclearvet.aplicacion.mapeadores.CitaMapeador;
import com.nuclearvet.dominio.enums.EstadoCita;
import com.nuclearvet.infraestructura.persistencia.CitaRepositorio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CitaServicioTest {

    @Mock
    private CitaRepositorio citaRepositorio;

    @Mock
    private CitaMapeador citaMapeador;

    @InjectMocks
    private CitaServicio citaServicio;

    @Test
    void listarCitas_DeberiaInvocarRepositorio() {
        when(citaRepositorio.findAll()).thenReturn(Arrays.asList());
        citaServicio.listarCitas();
        verify(citaRepositorio, times(1)).findAll();
    }

    @Test
    void listarCitasPorEstado_DeberiaInvocarRepositorio() {
        EstadoCita estado = EstadoCita.CONFIRMADA;
        when(citaRepositorio.findByEstado(estado)).thenReturn(Arrays.asList());
        citaServicio.listarCitasPorEstado(estado);
        verify(citaRepositorio, times(1)).findByEstado(estado);
    }

    @Test
    void listarCitasPorPaciente_DeberiaInvocarRepositorio() {
        Long pacienteId = 1L;
        when(citaRepositorio.findByPacienteId(pacienteId)).thenReturn(Arrays.asList());
        citaServicio.listarCitasPorPaciente(pacienteId);
        verify(citaRepositorio, times(1)).findByPacienteId(pacienteId);
    }

    @Test
    void listarCitasPorVeterinario_DeberiaInvocarRepositorio() {
        Long veterinarioId = 1L;
        when(citaRepositorio.findByVeterinarioId(veterinarioId)).thenReturn(Arrays.asList());
        citaServicio.listarCitasPorVeterinario(veterinarioId);
        verify(citaRepositorio, times(1)).findByVeterinarioId(veterinarioId);
    }

    @Test
    void listarCitasPorFecha_DeberiaInvocarRepositorio() {
        LocalDate fecha = LocalDate.now();
        when(citaRepositorio.findByFechaHoraBetween(any(), any())).thenReturn(Arrays.asList());
        citaServicio.listarCitasPorFecha(fecha);
        verify(citaRepositorio, times(1)).findByFechaHoraBetween(any(), any());
    }
}
