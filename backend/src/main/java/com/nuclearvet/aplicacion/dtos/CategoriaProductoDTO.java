package com.nuclearvet.aplicacion.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para transferir información de categorías de productos
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoriaProductoDTO {
    
    private Long id;
    
    @NotBlank(message = "El nombre de la categoría es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;
    
    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    private String descripcion;
    
    private Boolean activo;
    
    private Integer cantidadProductos;
    
    private LocalDateTime fechaCreacion;
    
    private LocalDateTime fechaActualizacion;
}
