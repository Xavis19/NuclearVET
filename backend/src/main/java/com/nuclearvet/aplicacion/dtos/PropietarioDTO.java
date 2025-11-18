package com.nuclearvet.aplicacion.dtos;

import com.nuclearvet.dominio.enums.TipoIdentificacion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO de respuesta para Propietario
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropietarioDTO {
    
    private Long id;
    private TipoIdentificacion tipoIdentificacion;
    private String numeroIdentificacion;
    private String nombres;
    private String apellidos;
    private String nombreCompleto;
    private String telefonoPrincipal;
    private String telefonoSecundario;
    private String correoElectronico;
    private String direccion;
    private String ciudad;
    private String departamento;
    private String codigoPostal;
    private String ocupacion;
    private String observaciones;
    private Boolean activo;
    private Integer cantidadPacientes;
    private LocalDateTime fechaRegistro;
    private LocalDateTime fechaActualizacion;
}
