package com.nuclearvet.aplicacion.dtos;

import com.nuclearvet.dominio.enumeraciones.EstadoLote;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO para transferir información de lotes
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoteDTO {
    
    private Long id;
    
    @NotBlank(message = "El número de lote es obligatorio")
    @Size(max = 30, message = "El número de lote no puede exceder 30 caracteres")
    private String numeroLote;
    
    @NotNull(message = "La fecha de ingreso es obligatoria")
    private LocalDate fechaIngreso;
    
    private LocalDate fechaFabricacion;
    
    private LocalDate fechaVencimiento;
    
    @NotNull(message = "La cantidad inicial es obligatoria")
    @Min(value = 1, message = "La cantidad inicial debe ser mayor a 0")
    private Integer cantidadInicial;
    
    private Integer cantidadDisponible;
    
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio de compra debe ser mayor a 0")
    @Digits(integer = 8, fraction = 2, message = "El precio debe tener máximo 8 enteros y 2 decimales")
    private BigDecimal precioCompraUnitario;
    
    private EstadoLote estado;
    
    @Size(max = 100, message = "La ubicación física no puede exceder 100 caracteres")
    private String ubicacionFisica;
    
    private String observaciones;
    
    @NotNull(message = "El producto es obligatorio")
    private Long productoId;
    
    private String productoNombre;
    
    private String productoCodigo;
    
    private Integer diasParaVencer;
    
    private Boolean estaVencido;
    
    private LocalDateTime fechaCreacion;
    
    private LocalDateTime fechaActualizacion;
}
