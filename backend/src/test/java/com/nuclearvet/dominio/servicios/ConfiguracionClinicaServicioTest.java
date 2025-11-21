package com.nuclearvet.dominio.servicios;

import com.nuclearvet.aplicacion.mapeadores.ConfiguracionClinicaMapeador;
import com.nuclearvet.dominio.entidades.ConfiguracionClinica;
import com.nuclearvet.infraestructura.persistencia.ConfiguracionClinicaRepositorio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConfiguracionClinicaServicioTest {

    @Mock
    private ConfiguracionClinicaRepositorio configuracionRepositorio;

    @Mock
    private ConfiguracionClinicaMapeador configuracionMapeador;

    @InjectMocks
    private ConfiguracionClinicaServicio configuracionClinicaServicio;

    @Test
    void obtenerConfiguracionActual_DeberiaInvocarRepositorio() {
        ConfiguracionClinica config = new ConfiguracionClinica();
        when(configuracionRepositorio.findConfiguracionActual()).thenReturn(Optional.of(config));
        configuracionClinicaServicio.obtenerConfiguracionActual();
        verify(configuracionRepositorio, times(1)).findConfiguracionActual();
    }

    @Test
    void obtenerPorId_DeberiaInvocarRepositorio() {
        Long id = 1L;
        ConfiguracionClinica config = new ConfiguracionClinica();
        when(configuracionRepositorio.findById(id)).thenReturn(Optional.of(config));
        configuracionClinicaServicio.obtenerPorId(id);
        verify(configuracionRepositorio, times(1)).findById(id);
    }
}
