package com.nuclearvet.aplicacion.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para crear un movimiento de entrada de inventario
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CrearMovimientoEntradaDTO {
    
    @NotNull(message = "El producto es obligatorio")
    private Long productoId;
    
    @NotNull(message = "El lote es obligatorio")
    private Long loteId;
    
    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    private Integer cantidad;
    
    private String numeroDocumento;
    
    private String observaciones;
}
