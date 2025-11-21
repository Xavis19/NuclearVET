package com.nuclearvet.aplicacion.dtos;

import com.nuclearvet.dominio.enumeraciones.TipoMovimiento;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para crear un movimiento de salida de inventario
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CrearMovimientoSalidaDTO {
    
    @NotNull(message = "El producto es obligatorio")
    private Long productoId;
    
    private Long loteId; // Opcional, se puede seleccionar automáticamente
    
    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    private Integer cantidad;
    
    @NotNull(message = "El tipo de salida es obligatorio")
    private TipoMovimiento tipoSalida; // SALIDA_VENTA, SALIDA_CONSUMO, SALIDA_BAJA, SALIDA_AJUSTE
    
    private String numeroDocumento;
    
    private String observaciones;
}
