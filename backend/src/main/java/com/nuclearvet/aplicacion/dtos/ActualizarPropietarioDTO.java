package com.nuclearvet.aplicacion.dtos;

import com.nuclearvet.dominio.enums.TipoIdentificacion;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para actualizar un Propietario existente
 * Todos los campos son opcionales para permitir actualizaciones parciales
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActualizarPropietarioDTO {
    
    private TipoIdentificacion tipoIdentificacion;
    
    @Size(max = 50, message = "El número de identificación no puede exceder 50 caracteres")
    private String numeroIdentificacion;
    
    @Size(max = 100, message = "Los nombres no pueden exceder 100 caracteres")
    private String nombres;
    
    @Size(max = 100, message = "Los apellidos no pueden exceder 100 caracteres")
    private String apellidos;
    
    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    private String telefonoPrincipal;
    
    @Size(max = 20, message = "El teléfono secundario no puede exceder 20 caracteres")
    private String telefonoSecundario;
    
    @Email(message = "Debe proporcionar un correo electrónico válido")
    @Size(max = 100, message = "El correo electrónico no puede exceder 100 caracteres")
    private String correoElectronico;
    
    @Size(max = 200, message = "La dirección no puede exceder 200 caracteres")
    private String direccion;
    
    @Size(max = 100, message = "La ciudad no puede exceder 100 caracteres")
    private String ciudad;
    
    @Size(max = 100, message = "El departamento no puede exceder 100 caracteres")
    private String departamento;
    
    @Size(max = 10, message = "El código postal no puede exceder 10 caracteres")
    private String codigoPostal;
    
    @Size(max = 100, message = "La ocupación no puede exceder 100 caracteres")
    private String ocupacion;
    
    private String observaciones;
}
