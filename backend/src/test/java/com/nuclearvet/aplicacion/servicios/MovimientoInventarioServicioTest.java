package com.nuclearvet.aplicacion.servicios;

import com.nuclearvet.aplicacion.mapeadores.MovimientoInventarioMapeador;
import com.nuclearvet.dominio.enumeraciones.TipoMovimiento;
import com.nuclearvet.infraestructura.persistencia.MovimientoInventarioRepositorio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovimientoInventarioServicioTest {

    @Mock
    private MovimientoInventarioRepositorio movimientoInventarioRepositorio;

    @Mock
    private MovimientoInventarioMapeador movimientoInventarioMapeador;

    @InjectMocks
    private MovimientoInventarioServicio movimientoInventarioServicio;

    @Test
    void listarPorProducto_DeberiaInvocarRepositorio() {
        Long productoId = 1L;
        when(movimientoInventarioRepositorio.findByProductoIdOrdenadoPorFecha(productoId)).thenReturn(Arrays.asList());
        movimientoInventarioServicio.listarPorProducto(productoId);
        verify(movimientoInventarioRepositorio, times(1)).findByProductoIdOrdenadoPorFecha(productoId);
    }

    @Test
    void listarPorTipo_DeberiaInvocarRepositorio() {
        TipoMovimiento tipo = TipoMovimiento.ENTRADA_COMPRA;
        when(movimientoInventarioRepositorio.findByTipoMovimiento(tipo)).thenReturn(Arrays.asList());
        movimientoInventarioServicio.listarPorTipo(tipo);
        verify(movimientoInventarioRepositorio, times(1)).findByTipoMovimiento(tipo);
    }

    @Test
    void listarPorFechas_DeberiaInvocarRepositorio() {
        when(movimientoInventarioRepositorio.findByFechaMovimientoBetween(any(), any())).thenReturn(Arrays.asList());
        movimientoInventarioServicio.listarPorFechas(any(), any());
        verify(movimientoInventarioRepositorio, times(1)).findByFechaMovimientoBetween(any(), any());
    }

    @Test
    void listarRecientes_DeberiaInvocarRepositorio() {
        int dias = 7;
        when(movimientoInventarioRepositorio.findMovimientosRecientes(any())).thenReturn(Arrays.asList());
        movimientoInventarioServicio.listarRecientes(dias);
        verify(movimientoInventarioRepositorio, times(1)).findMovimientosRecientes(any());
    }
}
