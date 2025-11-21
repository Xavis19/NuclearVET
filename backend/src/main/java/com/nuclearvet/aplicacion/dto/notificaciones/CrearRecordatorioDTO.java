package com.nuclearvet.aplicacion.dto.notificaciones;

import com.nuclearvet.dominio.enumeraciones.TipoRecordatorio;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para crear recordatorio
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrearRecordatorioDTO {

    @NotNull(message = "El ID del paciente es obligatorio")
    private Long pacienteId;

    @NotNull(message = "El tipo de recordatorio es obligatorio")
    private TipoRecordatorio tipoRecordatorio;

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 200, message = "El título no puede exceder 200 caracteres")
    private String titulo;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    @NotNull(message = "La fecha programada es obligatoria")
    @Future(message = "La fecha programada debe ser futura")
    private LocalDateTime fechaProgramada;

    private Long citaId;
}
