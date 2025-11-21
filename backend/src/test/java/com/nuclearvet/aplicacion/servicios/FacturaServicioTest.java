package com.nuclearvet.aplicacion.servicios;

import com.nuclearvet.aplicacion.dto.administrativo.CrearFacturaDTO;
import com.nuclearvet.aplicacion.dto.administrativo.FacturaDTO;
import com.nuclearvet.aplicacion.dto.administrativo.ItemFacturaDTO;
import com.nuclearvet.aplicacion.mapeadores.FacturaMapeador;
import com.nuclearvet.dominio.entidades.*;
import com.nuclearvet.dominio.enumeraciones.EstadoFactura;
import com.nuclearvet.dominio.servicios.FacturaServicio;
import com.nuclearvet.infraestructura.persistencia.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FacturaServicioTest {

    @Mock
    private FacturaRepositorio facturaRepositorio;

    @Mock
    private FacturaMapeador facturaMapeador;

    @InjectMocks
    private FacturaServicio facturaServicio;

    @Test
    void calcularTotalVentas_DeberiaRetornarMontoCorrect() {
        // Arrange
        LocalDate inicio = LocalDate.of(2025, 11, 1);
        LocalDate fin = LocalDate.of(2025, 11, 30);
        BigDecimal totalEsperado = BigDecimal.valueOf(500000);
        
        when(facturaRepositorio.calcularTotalVentas(inicio, fin)).thenReturn(totalEsperado);

        // Act
        BigDecimal resultado = facturaServicio.calcularTotalVentas(inicio, fin);

        // Assert
        assertNotNull(resultado);
        assertEquals(totalEsperado, resultado);
        verify(facturaRepositorio, times(1)).calcularTotalVentas(inicio, fin);
    }

    @Test
    void calcularCuentasPorCobrar_DeberiaRetornarSaldoPendiente() {
        // Arrange
        BigDecimal cuentasPorCobrar = BigDecimal.valueOf(250000);
        when(facturaRepositorio.calcularTotalCuentasPorCobrar()).thenReturn(cuentasPorCobrar);

        // Act
        BigDecimal resultado = facturaServicio.calcularCuentasPorCobrar();

        // Assert
        assertNotNull(resultado);
        assertEquals(cuentasPorCobrar, resultado);
        verify(facturaRepositorio, times(1)).calcularTotalCuentasPorCobrar();
    }

    @Test
    void listarPorEstado_DeberiaInvocarRepositorio() {
        // Arrange
        EstadoFactura estado = EstadoFactura.PENDIENTE;
        
        when(facturaRepositorio.findByEstadoOrdenadas(estado)).thenReturn(Arrays.asList());

        // Act
        facturaServicio.listarPorEstado(estado);

        // Assert
        verify(facturaRepositorio, times(1)).findByEstadoOrdenadas(estado);
    }
}
