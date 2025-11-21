package com.nuclearvet.aplicacion.servicios;

import com.nuclearvet.aplicacion.dtos.HistoriaClinicaDTO;
import com.nuclearvet.aplicacion.mapeadores.HistoriaClinicaMapeador;
import com.nuclearvet.dominio.entidades.HistoriaClinica;
import com.nuclearvet.infraestructura.persistencia.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HistoriaClinicaServicioTest {

    @Mock
    private HistoriaClinicaRepositorio historiaClinicaRepositorio;

    @Mock
    private PacienteRepositorio pacienteRepositorio;

    @Mock
    private HistoriaClinicaMapeador historiaClinicaMapeador;

    @InjectMocks
    private HistoriaClinicaServicio historiaClinicaServicio;

    @Test
    void obtenerPorPaciente_DeberiaRetornarHistoria() {
        Long pacienteId = 1L;
        HistoriaClinica historia = new HistoriaClinica();
        HistoriaClinicaDTO dto = new HistoriaClinicaDTO();
        
        when(historiaClinicaRepositorio.findByPacienteId(pacienteId)).thenReturn(Optional.of(historia));
        when(historiaClinicaMapeador.aDTO(historia)).thenReturn(dto);
        
        historiaClinicaServicio.obtenerPorPaciente(pacienteId);
        
        verify(historiaClinicaRepositorio, times(1)).findByPacienteId(pacienteId);
        verify(historiaClinicaMapeador, times(1)).aDTO(historia);
    }

    @Test
    void obtenerPorNumero_DeberiaRetornarHistoria() {
        String numeroHistoria = "HC-001";
        HistoriaClinica historia = new HistoriaClinica();
        HistoriaClinicaDTO dto = new HistoriaClinicaDTO();
        
        when(historiaClinicaRepositorio.findByNumeroHistoria(numeroHistoria)).thenReturn(Optional.of(historia));
        when(historiaClinicaMapeador.aDTO(historia)).thenReturn(dto);
        
        historiaClinicaServicio.obtenerPorNumero(numeroHistoria);
        
        verify(historiaClinicaRepositorio, times(1)).findByNumeroHistoria(numeroHistoria);
        verify(historiaClinicaMapeador, times(1)).aDTO(historia);
    }

    @Test
    void obtenerPorPacienteConConsultas_DeberiaRetornarHistoria() {
        Long pacienteId = 1L;
        HistoriaClinica historia = new HistoriaClinica();
        HistoriaClinicaDTO dto = new HistoriaClinicaDTO();
        
        when(historiaClinicaRepositorio.findByPacienteIdWithConsultas(pacienteId)).thenReturn(Optional.of(historia));
        when(historiaClinicaMapeador.aDTO(historia)).thenReturn(dto);
        
        historiaClinicaServicio.obtenerPorPacienteConConsultas(pacienteId);
        
        verify(historiaClinicaRepositorio, times(1)).findByPacienteIdWithConsultas(pacienteId);
        verify(historiaClinicaMapeador, times(1)).aDTO(historia);
    }
}
