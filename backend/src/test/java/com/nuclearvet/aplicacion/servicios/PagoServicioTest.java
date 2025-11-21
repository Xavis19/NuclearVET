package com.nuclearvet.aplicacion.servicios;

import com.nuclearvet.aplicacion.dto.administrativo.PagoDTO;
import com.nuclearvet.aplicacion.mapeadores.PagoMapeador;
import com.nuclearvet.dominio.entidades.Pago;
import com.nuclearvet.dominio.enumeraciones.MetodoPago;
import com.nuclearvet.dominio.servicios.PagoServicio;
import com.nuclearvet.infraestructura.persistencia.PagoRepositorio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PagoServicioTest {

    @Mock
    private PagoRepositorio pagoRepositorio;

    @Mock
    private PagoMapeador pagoMapeador;

    @InjectMocks
    private PagoServicio pagoServicio;

    @Test
    void calcularTotalPagos_DeberiaRetornarMontoCorrect() {
        // Arrange
        LocalDateTime inicio = LocalDateTime.of(2025, 11, 1, 0, 0);
        LocalDateTime fin = LocalDateTime.of(2025, 11, 30, 23, 59);
        BigDecimal totalEsperado = BigDecimal.valueOf(300000);
        
        when(pagoRepositorio.calcularTotalPagos(inicio, fin)).thenReturn(totalEsperado);

        // Act
        BigDecimal resultado = pagoServicio.calcularTotalPagos(inicio, fin);

        // Assert
        assertNotNull(resultado);
        assertEquals(totalEsperado, resultado);
        verify(pagoRepositorio, times(1)).calcularTotalPagos(inicio, fin);
    }

    @Test
    void listarPorMetodoPago_DeberiaInvocarRepositorio() {
        // Arrange
        MetodoPago metodo = MetodoPago.EFECTIVO;
        when(pagoRepositorio.findByMetodoPago(metodo)).thenReturn(Arrays.asList());

        // Act
        pagoServicio.listarPorMetodoPago(metodo);

        // Assert
        verify(pagoRepositorio, times(1)).findByMetodoPago(metodo);
    }
}
