package com.nuclearvet.aplicacion.dto.notificaciones;

import com.nuclearvet.dominio.enumeraciones.TipoPlantilla;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para actualizar plantilla de mensaje
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActualizarPlantillaDTO {

    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;

    private TipoPlantilla tipoPlantilla;

    @Size(max = 200, message = "El asunto no puede exceder 200 caracteres")
    private String asunto;

    private String contenido;

    @Size(max = 500, message = "Las variables disponibles no pueden exceder 500 caracteres")
    private String variablesDisponibles;

    private Boolean activo;
}
