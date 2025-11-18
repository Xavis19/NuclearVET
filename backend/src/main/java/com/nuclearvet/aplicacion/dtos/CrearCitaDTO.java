package com.nuclearvet.aplicacion.dtos;

import com.nuclearvet.dominio.enums.Prioridad;
import com.nuclearvet.dominio.enums.TipoCita;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para crear una nueva cita
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrearCitaDTO {
    
    @NotNull(message = "El paciente es obligatorio")
    private Long pacienteId;
    
    @NotNull(message = "El veterinario es obligatorio")
    private Long veterinarioId;
    
        @NotNull(message = "La fecha y hora de la cita es obligatoria")
    @Future(message = "La cita debe ser en una fecha futura")
    @Schema(description = "Fecha y hora de la cita", example = "2024-12-01T10:00:00")
    private LocalDateTime fechaHora;

    @NotNull(message = "La duración estimada es obligatoria")
    @Min(value = 15, message = "La duración mínima es 15 minutos")
    @Max(value = 480, message = "La duración máxima es 8 horas")
    @Schema(description = "Duración estimada en minutos", example = "30")
    private Integer duracionEstimada;

    @NotNull(message = "El tipo de cita es obligatorio")
    @Schema(description = "Tipo de cita", example = "CONSULTA")
    private TipoCita tipoCita;

    @NotBlank(message = "El motivo de consulta es obligatorio")
    @Size(max = 500, message = "El motivo no puede exceder 500 caracteres")
    @Schema(description = "Motivo de la consulta", example = "Control de vacunación")
    private String motivoConsulta;

    @Size(max = 1000, message = "Las observaciones no pueden exceder 1000 caracteres")
    @Schema(description = "Observaciones adicionales", example = "Paciente nervioso con extraños")
    private String observaciones;

    @Builder.Default
    @Schema(description = "Prioridad de la cita", example = "NORMAL")
    private Prioridad prioridad = Prioridad.NORMAL;

    @DecimalMin(value = "0.0", message = "El costo debe ser mayor o igual a 0")
    @Schema(description = "Costo de la consulta", example = "50000")
    private BigDecimal costoConsulta;

    @Builder.Default
    @Schema(description = "Enviar recordatorio al propietario", example = "true")
    private Boolean enviarRecordatorio = true;
}
