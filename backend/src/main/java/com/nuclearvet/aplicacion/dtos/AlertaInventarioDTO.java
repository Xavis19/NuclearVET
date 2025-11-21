package com.nuclearvet.aplicacion.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para transferir información de alertas de inventario
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlertaInventarioDTO {
    
    private Long id;
    
    @NotBlank(message = "El tipo de alerta es obligatorio")
    @Size(max = 100, message = "El tipo no puede exceder 100 caracteres")
    private String tipo;
    
    @NotBlank(message = "El mensaje de la alerta es obligatorio")
    @Size(max = 500, message = "El mensaje no puede exceder 500 caracteres")
    private String mensaje;
    
    @NotBlank(message = "La prioridad es obligatoria")
    @Size(max = 20, message = "La prioridad no puede exceder 20 caracteres")
    private String prioridad;
    
    private Boolean leida;
    
    private LocalDateTime fechaAlerta;
    
    private LocalDateTime fechaLeida;
    
    @NotNull(message = "El producto es obligatorio")
    private Long productoId;
    
    private String productoNombre;
    
    private String productoCodigo;
    
    private Long loteId;
    
    private String loteNumero;
    
    private LocalDateTime fechaCreacion;
}
