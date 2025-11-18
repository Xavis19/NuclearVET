package com.nuclearvet.aplicacion.dtos;

import com.nuclearvet.dominio.enums.EstadoCita;
import com.nuclearvet.dominio.enums.Prioridad;
import com.nuclearvet.dominio.enums.TipoCita;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de respuesta para Cita
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CitaDTO {
    
    private Long id;
    private String numeroCita;
    
    // Datos del paciente
    private Long pacienteId;
    private String pacienteNombre;
    private String pacienteCodigo;
    
    // Datos del propietario
    private Long propietarioId;
    private String propietarioNombre;
    private String propietarioTelefono;
    
    // Datos del veterinario
    private Long veterinarioId;
    private String veterinarioNombre;
    
    // Datos de la cita
    private LocalDateTime fechaHora;
    private Integer duracionEstimada;
    private TipoCita tipoCita;
    private String motivoConsulta;
    private String observaciones;
    private EstadoCita estado;
    private Prioridad prioridad;
    private BigDecimal costoConsulta;
    
    // Recordatorios
    private Boolean recordatorioEnviado;
    
    // Auditoría
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;

}
