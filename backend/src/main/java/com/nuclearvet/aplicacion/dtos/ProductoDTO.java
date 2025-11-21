package com.nuclearvet.aplicacion.dtos;

import com.nuclearvet.dominio.enumeraciones.TipoProducto;
import com.nuclearvet.dominio.enumeraciones.UnidadMedida;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para transferir información de productos
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoDTO {
    
    private Long id;
    
    @NotBlank(message = "El código del producto es obligatorio")
    @Size(max = 20, message = "El código no puede exceder 20 caracteres")
    private String codigo;
    
    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(max = 200, message = "El nombre no puede exceder 200 caracteres")
    private String nombre;
    
    private String descripcion;
    
    @NotNull(message = "El tipo de producto es obligatorio")
    private TipoProducto tipoProducto;
    
    @NotNull(message = "La unidad de medida es obligatoria")
    private UnidadMedida unidadMedida;
    
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio de compra debe ser mayor a 0")
    @Digits(integer = 8, fraction = 2, message = "El precio de compra debe tener máximo 8 enteros y 2 decimales")
    private BigDecimal precioCompra;
    
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio de venta debe ser mayor a 0")
    @Digits(integer = 8, fraction = 2, message = "El precio de venta debe tener máximo 8 enteros y 2 decimales")
    private BigDecimal precioVenta;
    
    @NotNull(message = "El stock mínimo es obligatorio")
    @Min(value = 0, message = "El stock mínimo no puede ser negativo")
    private Integer stockMinimo;
    
    private Integer stockActual;
    
    @Size(max = 100, message = "La ubicación no puede exceder 100 caracteres")
    private String ubicacion;
    
    private Boolean requiereRefrigeracion;
    
    private Boolean requiereReceta;
    
    private Boolean activo;
    
    private String observaciones;
    
    @NotNull(message = "La categoría es obligatoria")
    private Long categoriaId;
    
    private String categoriaNombre;
    
    private Long proveedorId;
    
    private String proveedorNombre;
    
    private Boolean stockBajo;
    
    private LocalDateTime fechaCreacion;
    
    private LocalDateTime fechaActualizacion;
}
