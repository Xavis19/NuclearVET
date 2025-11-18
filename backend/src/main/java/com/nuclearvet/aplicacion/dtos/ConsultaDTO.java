package com.nuclearvet.aplicacion.dtos;

import com.nuclearvet.dominio.enums.EstadoConsulta;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de respuesta para Consulta
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsultaDTO {
    
    private Long id;
    private String numeroConsulta;
    
    // Historia clínica
    private Long historiaClinicaId;
    private String numeroHistoria;
    
    // Paciente
    private Long pacienteId;
    private String pacienteNombre;
    private String pacienteCodigo;
    
    // Veterinario
    private Long veterinarioId;
    private String veterinarioNombre;
    
    // Cita relacionada
    private Long citaId;
    private String numeroCita;
    
    // Datos de la consulta
    private LocalDateTime fechaConsulta;
    private String motivoConsulta;
    private String sintomas;
    
    // Signos vitales
    private BigDecimal temperatura;
    private BigDecimal peso;
    private Integer frecuenciaCardiaca;
    private Integer frecuenciaRespiratoria;
    
    // Evaluación médica
    private String examenFisico;
    private String diagnostico;
    private String tratamiento;
    private String medicamentos;
    private String examenesSolicitados;
    private String recomendaciones;
    
    // Seguimiento
    private LocalDateTime proximaCita;
    private EstadoConsulta estado;
    private String observaciones;
    
    // Auditoría
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}
