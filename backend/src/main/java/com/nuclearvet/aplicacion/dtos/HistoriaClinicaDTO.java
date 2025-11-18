package com.nuclearvet.aplicacion.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO de respuesta para Historia Clínica
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoriaClinicaDTO {
    
    private Long id;
    private String numeroHistoria;
    
    // Datos del paciente
    private Long pacienteId;
    private String pacienteNombre;
    private String pacienteCodigo;
    
    // Información médica
    private String alergiasConocidas;
    private String enfermedadesCronicas;
    private String cirugiasPrevias;
    private String medicamentosActuales;
    private String vacunas;
    private String observacionesGenerales;
    
    // Estadísticas
    private Integer totalConsultas;
    private LocalDateTime fechaUltimaConsulta;
    
    // Auditoría
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}
