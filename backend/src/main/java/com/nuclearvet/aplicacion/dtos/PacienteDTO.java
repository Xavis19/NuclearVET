package com.nuclearvet.aplicacion.dtos;

import com.nuclearvet.dominio.enums.Especie;
import com.nuclearvet.dominio.enums.EstadoPaciente;
import com.nuclearvet.dominio.enums.Sexo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO de respuesta para Paciente
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PacienteDTO {
    
    private Long id;
    private String codigo;
    private String nombre;
    private Especie especie;
    private String raza;
    private LocalDate fechaNacimiento;
    private Integer edadAnios;
    private Integer edadMeses;
    private Sexo sexo;
    private String color;
    private BigDecimal peso;
    private String microchip;
    private Boolean esterilizado;
    private EstadoPaciente estado;
    private String alergias;
    private String observaciones;
    private String fotoUrl;
    
    // Datos del propietario
    private Long propietarioId;
    private String propietarioNombre;
    private String propietarioTelefono;
    
    // Datos del veterinario asignado
    private Long veterinarioAsignadoId;
    private String veterinarioAsignadoNombre;
    
    private LocalDateTime fechaRegistro;
    private LocalDateTime fechaActualizacion;
}
