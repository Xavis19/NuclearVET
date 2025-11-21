package com.nuclearvet.aplicacion.servicios;

import com.nuclearvet.aplicacion.mapeadores.AlertaInventarioMapeador;
import com.nuclearvet.infraestructura.persistencia.AlertaInventarioRepositorio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlertaInventarioServicioTest {

    @Mock
    private AlertaInventarioRepositorio alertaInventarioRepositorio;

    @Mock
    private AlertaInventarioMapeador alertaInventarioMapeador;

    @InjectMocks
    private AlertaInventarioServicio alertaInventarioServicio;

    @Test
    void listarNoLeidas_DeberiaInvocarRepositorio() {
        when(alertaInventarioRepositorio.findNoLeidas()).thenReturn(Arrays.asList());
        alertaInventarioServicio.listarNoLeidas();
        verify(alertaInventarioRepositorio, times(1)).findNoLeidas();
    }

    @Test
    void listarPorPrioridad_DeberiaInvocarRepositorio() {
        String prioridad = "ALTA";
        when(alertaInventarioRepositorio.findByPrioridadAndLeidaFalse(prioridad)).thenReturn(Arrays.asList());
        alertaInventarioServicio.listarPorPrioridad(prioridad);
        verify(alertaInventarioRepositorio, times(1)).findByPrioridadAndLeidaFalse(prioridad);
    }

    @Test
    void listarPorProducto_DeberiaInvocarRepositorio() {
        Long productoId = 1L;
        when(alertaInventarioRepositorio.findByProductoId(productoId)).thenReturn(Arrays.asList());
        alertaInventarioServicio.listarPorProducto(productoId);
        verify(alertaInventarioRepositorio, times(1)).findByProductoId(productoId);
    }

    @Test
    void listarRecientes_DeberiaInvocarRepositorio() {
        int dias = 7;
        when(alertaInventarioRepositorio.findAlertasRecientes(any())).thenReturn(Arrays.asList());
        alertaInventarioServicio.listarRecientes(dias);
        verify(alertaInventarioRepositorio, times(1)).findAlertasRecientes(any());
    }
}
