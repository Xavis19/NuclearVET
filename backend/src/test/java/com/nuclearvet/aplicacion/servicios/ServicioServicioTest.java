package com.nuclearvet.aplicacion.servicios;

import com.nuclearvet.aplicacion.dto.administrativo.ServicioDTO;
import com.nuclearvet.aplicacion.mapeadores.ServicioMapeador;
import com.nuclearvet.dominio.entidades.Servicio;
import com.nuclearvet.dominio.enumeraciones.TipoServicio;
import com.nuclearvet.dominio.servicios.ServicioServicio;
import com.nuclearvet.infraestructura.persistencia.ServicioRepositorio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServicioServicioTest {

    @Mock
    private ServicioRepositorio servicioRepositorio;

    @Mock
    private ServicioMapeador servicioMapeador;

    @InjectMocks
    private ServicioServicio servicioServicio;

    @Test
    void listarActivos_DeberiaInvocarRepositorio() {
        // Arrange
        when(servicioRepositorio.findAllActivosOrdenados()).thenReturn(Arrays.asList());

        // Act
        servicioServicio.listarActivos();

        // Assert
        verify(servicioRepositorio, times(1)).findAllActivosOrdenados();
    }

    @Test
    void listarPorTipo_DeberiaInvocarRepositorio() {
        // Arrange
        TipoServicio tipo = TipoServicio.CONSULTA;
        when(servicioRepositorio.findActivosPorTipo(tipo)).thenReturn(Arrays.asList());

        // Act
        servicioServicio.listarPorTipo(tipo);

        // Assert
        verify(servicioRepositorio, times(1)).findActivosPorTipo(tipo);
    }

    @Test
    void obtenerPorCodigo_DeberiaRetornarServicio() {
        // Arrange
        String codigo = "CONS-001";
        Servicio servicio = Servicio.builder()
                .id(1L)
                .codigo(codigo)
                .nombre("Consulta")
                .precio(BigDecimal.valueOf(50000))
                .build();
        
        when(servicioRepositorio.findByCodigo(codigo)).thenReturn(Optional.of(servicio));
        
        ServicioDTO servicioDTO = new ServicioDTO();
        servicioDTO.setCodigo(codigo);
        when(servicioMapeador.aDTO(any(Servicio.class))).thenReturn(servicioDTO);

        // Act
        ServicioDTO resultado = servicioServicio.obtenerPorCodigo(codigo);

        // Assert
        assertNotNull(resultado);
        assertEquals(codigo, resultado.getCodigo());
        verify(servicioRepositorio, times(1)).findByCodigo(codigo);
    }
}
