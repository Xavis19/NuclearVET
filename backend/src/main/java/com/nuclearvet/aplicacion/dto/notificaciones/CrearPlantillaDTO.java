package com.nuclearvet.aplicacion.dto.notificaciones;

import com.nuclearvet.dominio.enumeraciones.TipoPlantilla;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para crear plantilla de mensaje
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrearPlantillaDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;

    @NotNull(message = "El tipo de plantilla es obligatorio")
    private TipoPlantilla tipoPlantilla;

    @NotBlank(message = "El asunto es obligatorio")
    @Size(max = 200, message = "El asunto no puede exceder 200 caracteres")
    private String asunto;

    @NotBlank(message = "El contenido es obligatorio")
    private String contenido;

    @Size(max = 500, message = "Las variables disponibles no pueden exceder 500 caracteres")
    private String variablesDisponibles;

    @Builder.Default
    private Boolean activo = true;
}
