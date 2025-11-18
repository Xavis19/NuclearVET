package com.nuclearvet.aplicacion.dtos;

import com.nuclearvet.dominio.enums.Especie;
import com.nuclearvet.dominio.enums.EstadoPaciente;
import com.nuclearvet.dominio.enums.Sexo;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO para actualizar un Paciente existente
 * Todos los campos son opcionales para permitir actualizaciones parciales
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActualizarPacienteDTO {
    
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;
    
    private Especie especie;
    
    @Size(max = 100, message = "La raza no puede exceder 100 caracteres")
    private String raza;
    
    @Past(message = "La fecha de nacimiento debe ser una fecha pasada")
    private LocalDate fechaNacimiento;
    
    private Sexo sexo;
    
    @Size(max = 100, message = "El color no puede exceder 100 caracteres")
    private String color;
    
    @DecimalMin(value = "0.01", message = "El peso debe ser mayor a 0")
    @DecimalMax(value = "999.99", message = "El peso no puede exceder 999.99 kg")
    private BigDecimal peso;
    
    @Size(max = 50, message = "El microchip no puede exceder 50 caracteres")
    private String microchip;
    
    private Boolean esterilizado;
    
    private EstadoPaciente estado;
    
    private String alergias;
    
    private String observaciones;
    
    private String fotoUrl;
    
    private Long veterinarioAsignadoId;
}
