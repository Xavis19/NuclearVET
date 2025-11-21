package com.nuclearvet.aplicacion.dto.notificaciones;

import com.nuclearvet.dominio.enumeraciones.TipoPlantilla;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para respuesta de plantilla de mensaje
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlantillaMensajeDTO {

    private Long id;
    private String nombre;
    private TipoPlantilla tipoPlantilla;
    private String asunto;
    private String contenido;
    private String variablesDisponibles;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}
