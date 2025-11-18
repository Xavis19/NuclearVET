package com.nuclearvet.aplicacion.dtos;

import com.nuclearvet.dominio.enums.Especie;
import com.nuclearvet.dominio.enums.Sexo;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO para crear un nuevo Paciente
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrearPacienteDTO {
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;
    
    @NotNull(message = "La especie es obligatoria")
    private Especie especie;
    
    @Size(max = 100, message = "La raza no puede exceder 100 caracteres")
    private String raza;
    
    @Past(message = "La fecha de nacimiento debe ser una fecha pasada")
    private LocalDate fechaNacimiento;
    
    @NotNull(message = "El sexo es obligatorio")
    private Sexo sexo;
    
    @Size(max = 100, message = "El color no puede exceder 100 caracteres")
    private String color;
    
    @DecimalMin(value = "0.01", message = "El peso debe ser mayor a 0")
    @DecimalMax(value = "999.99", message = "El peso no puede exceder 999.99 kg")
    private BigDecimal peso;
    
    @Size(max = 50, message = "El microchip no puede exceder 50 caracteres")
    private String microchip;
    
    private Boolean esterilizado;
    
    private String alergias;
    
    private String observaciones;
    
    private String fotoUrl;
    
    @NotNull(message = "El ID del propietario es obligatorio")
    private Long propietarioId;
    
    private Long veterinarioAsignadoId;
}
