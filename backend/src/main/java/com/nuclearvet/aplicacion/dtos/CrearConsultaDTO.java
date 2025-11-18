package com.nuclearvet.aplicacion.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para crear una nueva consulta
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrearConsultaDTO {
    
    @NotNull(message = "La historia clínica es obligatoria")
    @Schema(description = "ID de la historia clínica", example = "1")
    private Long historiaClinicaId;
    
    @NotNull(message = "El veterinario es obligatorio")
    @Schema(description = "ID del veterinario", example = "2")
    private Long veterinarioId;
    
    @Schema(description = "ID de la cita relacionada (opcional)", example = "5")
    private Long citaId;
    
    @NotNull(message = "La fecha de consulta es obligatoria")
    @Schema(description = "Fecha y hora de la consulta", example = "2025-11-16T10:30:00")
    private LocalDateTime fechaConsulta;
    
    @NotBlank(message = "El motivo de consulta es obligatorio")
    @Size(max = 1000, message = "El motivo no puede exceder 1000 caracteres")
    @Schema(description = "Motivo de la consulta", example = "Control de vacunación")
    private String motivoConsulta;
    
    @Size(max = 2000, message = "Los síntomas no pueden exceder 2000 caracteres")
    @Schema(description = "Síntomas presentados", example = "Vómitos y diarrea")
    private String sintomas;
    
    @DecimalMin(value = "30.0", message = "La temperatura debe ser mayor a 30°C")
    @DecimalMax(value = "45.0", message = "La temperatura debe ser menor a 45°C")
    @Schema(description = "Temperatura en grados Celsius", example = "38.5")
    private BigDecimal temperatura;
    
    @DecimalMin(value = "0.1", message = "El peso debe ser mayor a 0")
    @Schema(description = "Peso en kilogramos", example = "12.5")
    private BigDecimal peso;
    
    @Min(value = 40, message = "La frecuencia cardíaca debe ser mayor a 40")
    @Max(value = 250, message = "La frecuencia cardíaca debe ser menor a 250")
    @Schema(description = "Frecuencia cardíaca (latidos por minuto)", example = "120")
    private Integer frecuenciaCardiaca;
    
    @Min(value = 10, message = "La frecuencia respiratoria debe ser mayor a 10")
    @Max(value = 100, message = "La frecuencia respiratoria debe ser menor a 100")
    @Schema(description = "Frecuencia respiratoria (respiraciones por minuto)", example = "30")
    private Integer frecuenciaRespiratoria;
    
    @Size(max = 3000, message = "El examen físico no puede exceder 3000 caracteres")
    @Schema(description = "Resultados del examen físico")
    private String examenFisico;
    
    @NotBlank(message = "El diagnóstico es obligatorio")
    @Size(max = 2000, message = "El diagnóstico no puede exceder 2000 caracteres")
    @Schema(description = "Diagnóstico del veterinario", example = "Gastroenteritis aguda")
    private String diagnostico;
    
    @Size(max = 3000, message = "El tratamiento no puede exceder 3000 caracteres")
    @Schema(description = "Tratamiento prescrito")
    private String tratamiento;
    
    @Size(max = 2000, message = "Los medicamentos no pueden exceder 2000 caracteres")
    @Schema(description = "Medicamentos recetados")
    private String medicamentos;
    
    @Size(max = 1000, message = "Los exámenes solicitados no pueden exceder 1000 caracteres")
    @Schema(description = "Exámenes de laboratorio o imagen solicitados")
    private String examenesSolicitados;
    
    @Size(max = 2000, message = "Las recomendaciones no pueden exceder 2000 caracteres")
    @Schema(description = "Recomendaciones para el propietario")
    private String recomendaciones;
    
    @Future(message = "La próxima cita debe ser en el futuro")
    @Schema(description = "Fecha y hora de la próxima cita de control")
    private LocalDateTime proximaCita;
    
    @Size(max = 1000, message = "Las observaciones no pueden exceder 1000 caracteres")
    @Schema(description = "Observaciones adicionales")
    private String observaciones;
}
