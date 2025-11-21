package com.nuclearvet.aplicacion.dtos;

import com.nuclearvet.dominio.enumeraciones.TipoMovimiento;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para transferir información de movimientos de inventario
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovimientoInventarioDTO {
    
    private Long id;
    
    private String numeroMovimiento;
    
    @NotNull(message = "El tipo de movimiento es obligatorio")
    private TipoMovimiento tipoMovimiento;
    
    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    private Integer cantidad;
    
    @DecimalMin(value = "0.0", message = "El precio unitario no puede ser negativo")
    @Digits(integer = 8, fraction = 2, message = "El precio debe tener máximo 8 enteros y 2 decimales")
    private BigDecimal precioUnitario;
    
    private BigDecimal valorTotal;
    
    private Integer stockAnterior;
    
    private Integer stockNuevo;
    
    @Size(max = 50, message = "El número de documento no puede exceder 50 caracteres")
    private String numeroDocumento;
    
    private String observaciones;
    
    private LocalDateTime fechaMovimiento;
    
    @NotNull(message = "El producto es obligatorio")
    private Long productoId;
    
    private String productoNombre;
    
    private String productoCodigo;
    
    private Long loteId;
    
    private String loteNumero;
    
    private Long usuarioId;
    
    private String usuarioNombre;
    
    private LocalDateTime fechaCreacion;
}
