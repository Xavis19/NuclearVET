package com.nuclearvet.dominio.servicios;

import com.nuclearvet.aplicacion.mapeadores.RecordatorioMapeador;
import com.nuclearvet.dominio.enumeraciones.TipoRecordatorio;
import com.nuclearvet.infraestructura.persistencia.RecordatorioRepositorio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecordatorioServicioTest {

    @Mock
    private RecordatorioRepositorio recordatorioRepositorio;

    @Mock
    private RecordatorioMapeador recordatorioMapeador;

    @InjectMocks
    private RecordatorioServicio recordatorioServicio;

    @Test
    void listarTodos_DeberiaInvocarRepositorio() {
        when(recordatorioRepositorio.findAll()).thenReturn(Arrays.asList());
        recordatorioServicio.listarTodos();
        verify(recordatorioRepositorio, times(1)).findAll();
    }

    @Test
    void listarPorPaciente_DeberiaInvocarRepositorio() {
        Long pacienteId = 1L;
        when(recordatorioRepositorio.findByPacienteConRelaciones(pacienteId)).thenReturn(Arrays.asList());
        recordatorioServicio.listarPorPaciente(pacienteId);
        verify(recordatorioRepositorio, times(1)).findByPacienteConRelaciones(pacienteId);
    }

    @Test
    void listarPendientes_DeberiaInvocarRepositorio() {
        when(recordatorioRepositorio.findByEnviadoFalse()).thenReturn(Arrays.asList());
        recordatorioServicio.listarPendientes();
        verify(recordatorioRepositorio, times(1)).findByEnviadoFalse();
    }

    @Test
    void listarPorTipo_DeberiaInvocarRepositorio() {
        TipoRecordatorio tipo = TipoRecordatorio.VACUNA;
        when(recordatorioRepositorio.findByTipoRecordatorio(tipo)).thenReturn(Arrays.asList());
        recordatorioServicio.listarPorTipo(tipo);
        verify(recordatorioRepositorio, times(1)).findByTipoRecordatorio(tipo);
    }

    @Test
    void listarPendientesDeEnvio_DeberiaInvocarRepositorio() {
        when(recordatorioRepositorio.findRecordatoriosPendientesDeEnvio()).thenReturn(Arrays.asList());
        recordatorioServicio.listarPendientesDeEnvio();
        verify(recordatorioRepositorio, times(1)).findRecordatoriosPendientesDeEnvio();
    }
}
