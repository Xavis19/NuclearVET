package com.nuclearvet.aplicacion.servicios;

import com.nuclearvet.aplicacion.mapeadores.ConsultaMapeador;
import com.nuclearvet.dominio.enums.EstadoConsulta;
import com.nuclearvet.infraestructura.persistencia.ConsultaRepositorio;
import com.nuclearvet.infraestructura.persistencia.HistoriaClinicaRepositorio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsultaServicioTest {

    @Mock
    private ConsultaRepositorio consultaRepositorio;

    @Mock
    private HistoriaClinicaRepositorio historiaClinicaRepositorio;

    @Mock
    private ConsultaMapeador consultaMapeador;

    @InjectMocks
    private ConsultaServicio consultaServicio;

    @Test
    void listarPorHistoriaClinica_DeberiaInvocarRepositorio() {
        Long historiaClinicaId = 1L;
        when(historiaClinicaRepositorio.existsById(historiaClinicaId)).thenReturn(true);
        when(consultaRepositorio.findByHistoriaClinicaIdOrderByFechaConsultaDesc(historiaClinicaId)).thenReturn(Arrays.asList());
        consultaServicio.listarPorHistoriaClinica(historiaClinicaId);
        verify(consultaRepositorio, times(1)).findByHistoriaClinicaIdOrderByFechaConsultaDesc(historiaClinicaId);
    }

    @Test
    void listarPorPaciente_DeberiaInvocarRepositorio() {
        Long pacienteId = 1L;
        when(consultaRepositorio.findUltimasConsultasPorPaciente(pacienteId)).thenReturn(Arrays.asList());
        consultaServicio.listarPorPaciente(pacienteId);
        verify(consultaRepositorio, times(1)).findUltimasConsultasPorPaciente(pacienteId);
    }

    @Test
    void listarPorEstado_DeberiaInvocarRepositorio() {
        EstadoConsulta estado = EstadoConsulta.COMPLETADA;
        when(consultaRepositorio.findByEstadoOrderByFechaConsultaDesc(estado)).thenReturn(Arrays.asList());
        consultaServicio.listarPorEstado(estado);
        verify(consultaRepositorio, times(1)).findByEstadoOrderByFechaConsultaDesc(estado);
    }

    @Test
    void listarConsultasDelDia_DeberiaInvocarRepositorio() {
        Long veterinarioId = 1L;
        when(consultaRepositorio.findConsultasDelDiaPorVeterinario(eq(veterinarioId), any(), any())).thenReturn(Arrays.asList());
        consultaServicio.listarConsultasDelDia(veterinarioId);
        verify(consultaRepositorio, times(1)).findConsultasDelDiaPorVeterinario(eq(veterinarioId), any(), any());
    }

    @Test
    void listarConsultasRecientes_DeberiaInvocarRepositorio() {
        when(consultaRepositorio.findConsultasRecientes(any())).thenReturn(Arrays.asList());
        consultaServicio.listarConsultasRecientes();
        verify(consultaRepositorio, times(1)).findConsultasRecientes(any());
    }
}
