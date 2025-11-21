package com.nuclearvet.aplicacion.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para transferir información de proveedores
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProveedorDTO {
    
    private Long id;
    
    @NotBlank(message = "El nombre del proveedor es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;
    
    @Size(max = 20, message = "El NIT no puede exceder 20 caracteres")
    private String nit;
    
    @Size(max = 200, message = "La dirección no puede exceder 200 caracteres")
    private String direccion;
    
    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    private String telefono;
    
    @Email(message = "El email debe ser válido")
    @Size(max = 100, message = "El email no puede exceder 100 caracteres")
    private String email;
    
    @Size(max = 100, message = "El nombre del contacto no puede exceder 100 caracteres")
    private String contactoPrincipal;
    
    @Size(max = 20, message = "El teléfono de contacto no puede exceder 20 caracteres")
    private String telefonoContacto;
    
    private Boolean activo;
    
    private String observaciones;
    
    private Integer cantidadProductos;
    
    private LocalDateTime fechaCreacion;
    
    private LocalDateTime fechaActualizacion;
}
