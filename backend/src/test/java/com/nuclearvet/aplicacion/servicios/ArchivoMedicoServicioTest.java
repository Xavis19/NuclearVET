package com.nuclearvet.aplicacion.servicios;

import com.nuclearvet.aplicacion.mapeadores.ArchivoMedicoMapeador;
import com.nuclearvet.dominio.enums.TipoArchivo;
import com.nuclearvet.infraestructura.persistencia.ArchivoMedicoRepositorio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArchivoMedicoServicioTest {

    @Mock
    private ArchivoMedicoRepositorio archivoMedicoRepositorio;

    @Mock
    private com.nuclearvet.infraestructura.persistencia.HistoriaClinicaRepositorio historiaClinicaRepositorio;

    @Mock
    private com.nuclearvet.infraestructura.persistencia.ConsultaRepositorio consultaRepositorio;

    @Mock
    private ArchivoMedicoMapeador archivoMedicoMapeador;

    @InjectMocks
    private ArchivoMedicoServicio archivoMedicoServicio;

    @Test
    void listarPorHistoriaClinica_DeberiaInvocarRepositorio() {
        Long historiaClinicaId = 1L;
        when(historiaClinicaRepositorio.existsById(historiaClinicaId)).thenReturn(true);
        when(archivoMedicoRepositorio.findByHistoriaClinicaIdOrderByFechaSubidaDesc(historiaClinicaId)).thenReturn(Arrays.asList());
        archivoMedicoServicio.listarPorHistoriaClinica(historiaClinicaId);
        verify(archivoMedicoRepositorio, times(1)).findByHistoriaClinicaIdOrderByFechaSubidaDesc(historiaClinicaId);
    }

    @Test
    void listarPorConsulta_DeberiaInvocarRepositorio() {
        Long consultaId = 1L;
        when(consultaRepositorio.existsById(consultaId)).thenReturn(true);
        when(archivoMedicoRepositorio.findByConsultaIdOrderByFechaSubidaDesc(consultaId)).thenReturn(Arrays.asList());
        archivoMedicoServicio.listarPorConsulta(consultaId);
        verify(archivoMedicoRepositorio, times(1)).findByConsultaIdOrderByFechaSubidaDesc(consultaId);
    }

    @Test
    void listarPorTipo_DeberiaInvocarRepositorio() {
        TipoArchivo tipoArchivo = TipoArchivo.RADIOGRAFIA;
        when(archivoMedicoRepositorio.findByTipoArchivoOrderByFechaSubidaDesc(tipoArchivo)).thenReturn(Arrays.asList());
        archivoMedicoServicio.listarPorTipo(tipoArchivo);
        verify(archivoMedicoRepositorio, times(1)).findByTipoArchivoOrderByFechaSubidaDesc(tipoArchivo);
    }

    @Test
    void listarArchivosRecientes_DeberiaInvocarRepositorio() {
        when(archivoMedicoRepositorio.findArchivosRecientes(any())).thenReturn(Arrays.asList());
        archivoMedicoServicio.listarArchivosRecientes();
        verify(archivoMedicoRepositorio, times(1)).findArchivosRecientes(any());
    }
}
