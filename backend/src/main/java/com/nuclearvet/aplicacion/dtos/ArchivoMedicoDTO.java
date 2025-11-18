package com.nuclearvet.aplicacion.dtos;

import com.nuclearvet.dominio.enums.TipoArchivo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO de respuesta para Archivo Médico
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArchivoMedicoDTO {
    
    private Long id;
    
    // Historia clínica
    private Long historiaClinicaId;
    private String numeroHistoria;
    
    // Consulta relacionada
    private Long consultaId;
    private String numeroConsulta;
    
    // Datos del archivo
    private TipoArchivo tipoArchivo;
    private String nombreArchivo;
    private String rutaArchivo;
    private String tipoContenido;
    private Long tamanoBytes;
    private String tamanoFormateado;
    private String descripcion;
    
    // Usuario que subió
    private Long subidoPorId;
    private String subidoPorNombre;
    
    // Auditoría
    private LocalDateTime fechaSubida;
}
