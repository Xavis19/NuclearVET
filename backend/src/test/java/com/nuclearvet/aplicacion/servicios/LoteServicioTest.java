package com.nuclearvet.aplicacion.servicios;

import com.nuclearvet.aplicacion.mapeadores.LoteMapeador;
import com.nuclearvet.dominio.enumeraciones.EstadoLote;
import com.nuclearvet.infraestructura.persistencia.LoteRepositorio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoteServicioTest {

    @Mock
    private LoteRepositorio loteRepositorio;

    @Mock
    private LoteMapeador loteMapeador;

    @Mock
    private AlertaInventarioServicio alertaServicio;

    @InjectMocks
    private LoteServicio loteServicio;

    @Test
    void listarDisponibles_DeberiaInvocarRepositorio() {
        when(loteRepositorio.findLotesDisponibles()).thenReturn(Arrays.asList());
        loteServicio.listarDisponibles();
        verify(loteRepositorio, times(1)).findLotesDisponibles();
    }

    @Test
    void listarProximosVencer_DeberiaInvocarRepositorio() {
        int dias = 30;
        when(loteRepositorio.findLotesProximosVencer(any(LocalDate.class))).thenReturn(Arrays.asList());
        loteServicio.listarProximosVencer(dias);
        verify(loteRepositorio, times(1)).findLotesProximosVencer(any(LocalDate.class));
    }

    @Test
    void listarVencidos_DeberiaInvocarRepositorio() {
        when(loteRepositorio.findLotesVencidos()).thenReturn(Arrays.asList());
        loteServicio.listarVencidos();
        verify(loteRepositorio, times(1)).findLotesVencidos();
    }

    @Test
    void listarPorEstado_DeberiaInvocarRepositorio() {
        EstadoLote estado = EstadoLote.DISPONIBLE;
        when(loteRepositorio.findByEstado(estado)).thenReturn(Arrays.asList());
        loteServicio.listarPorEstado(estado);
        verify(loteRepositorio, times(1)).findByEstado(estado);
    }

    @Test
    void listarPorProducto_DeberiaInvocarRepositorio() {
        Long productoId = 1L;
        when(loteRepositorio.findByProductoIdOrderByFechaVencimiento(productoId)).thenReturn(Arrays.asList());
        loteServicio.listarPorProducto(productoId);
        verify(loteRepositorio, times(1)).findByProductoIdOrderByFechaVencimiento(productoId);
    }
}
