package com.nuclearvet.dominio.servicios;

import com.nuclearvet.aplicacion.mapeadores.NotificacionMapeador;
import com.nuclearvet.dominio.enumeraciones.PrioridadNotificacion;
import com.nuclearvet.dominio.enumeraciones.TipoNotificacion;
import com.nuclearvet.infraestructura.persistencia.NotificacionRepositorio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificacionServicioTest {

    @Mock
    private NotificacionRepositorio notificacionRepositorio;

    @Mock
    private NotificacionMapeador notificacionMapeador;

    @InjectMocks
    private NotificacionServicio notificacionServicio;

    @Test
    void listarPorUsuario_DeberiaInvocarRepositorio() {
        Long usuarioId = 1L;
        when(notificacionRepositorio.findByUsuarioOrdenadas(usuarioId)).thenReturn(Arrays.asList());
        notificacionServicio.listarPorUsuario(usuarioId);
        verify(notificacionRepositorio, times(1)).findByUsuarioOrdenadas(usuarioId);
    }

    @Test
    void listarNoLeidasPorUsuario_DeberiaInvocarRepositorio() {
        Long usuarioId = 1L;
        when(notificacionRepositorio.findNoLeidasPorUsuario(usuarioId)).thenReturn(Arrays.asList());
        notificacionServicio.listarNoLeidasPorUsuario(usuarioId);
        verify(notificacionRepositorio, times(1)).findNoLeidasPorUsuario(usuarioId);
    }

    @Test
    void listarPorUsuarioYTipo_DeberiaInvocarRepositorio() {
        Long usuarioId = 1L;
        TipoNotificacion tipo = TipoNotificacion.CITA;
        when(notificacionRepositorio.findByUsuarioYTipo(usuarioId, tipo)).thenReturn(Arrays.asList());
        notificacionServicio.listarPorUsuarioYTipo(usuarioId, tipo);
        verify(notificacionRepositorio, times(1)).findByUsuarioYTipo(usuarioId, tipo);
    }

    @Test
    void listarNoLeidasPorUsuarioYPrioridad_DeberiaInvocarRepositorio() {
        Long usuarioId = 1L;
        PrioridadNotificacion prioridad = PrioridadNotificacion.ALTA;
        when(notificacionRepositorio.findNoLeidasPorUsuarioYPrioridad(usuarioId, prioridad)).thenReturn(Arrays.asList());
        notificacionServicio.listarNoLeidasPorUsuarioYPrioridad(usuarioId, prioridad);
        verify(notificacionRepositorio, times(1)).findNoLeidasPorUsuarioYPrioridad(usuarioId, prioridad);
    }

    @Test
    void listarRecientesPorUsuario_DeberiaInvocarRepositorio() {
        Long usuarioId = 1L;
        int dias = 7;
        when(notificacionRepositorio.findRecientesPorUsuario(eq(usuarioId), any())).thenReturn(Arrays.asList());
        notificacionServicio.listarRecientesPorUsuario(usuarioId, dias);
        verify(notificacionRepositorio, times(1)).findRecientesPorUsuario(eq(usuarioId), any());
    }
}
