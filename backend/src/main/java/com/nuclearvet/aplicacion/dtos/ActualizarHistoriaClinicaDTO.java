package com.nuclearvet.aplicacion.dtos;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para actualizar Historia Clínica
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActualizarHistoriaClinicaDTO {
    
    @Size(max = 2000, message = "Las alergias no pueden exceder 2000 caracteres")
    private String alergiasConocidas;
    
    @Size(max = 2000, message = "Las enfermedades crónicas no pueden exceder 2000 caracteres")
    private String enfermedadesCronicas;
    
    @Size(max = 2000, message = "Las cirugías previas no pueden exceder 2000 caracteres")
    private String cirugiasPrevias;
    
    @Size(max = 1000, message = "Los medicamentos actuales no pueden exceder 1000 caracteres")
    private String medicamentosActuales;
    
    @Size(max = 2000, message = "Las vacunas no pueden exceder 2000 caracteres")
    private String vacunas;
    
    @Size(max = 3000, message = "Las observaciones no pueden exceder 3000 caracteres")
    private String observacionesGenerales;
}
