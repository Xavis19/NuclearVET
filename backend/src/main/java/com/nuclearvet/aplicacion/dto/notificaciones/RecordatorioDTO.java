package com.nuclearvet.aplicacion.dto.notificaciones;

import com.nuclearvet.dominio.enumeraciones.TipoRecordatorio;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para respuesta de recordatorio
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordatorioDTO {

    private Long id;
    private Long pacienteId;
    private String pacienteNombre;
    private Long propietarioId;
    private String propietarioNombre;
    private TipoRecordatorio tipoRecordatorio;
    private String titulo;
    private String descripcion;
    private LocalDateTime fechaProgramada;
    private Boolean enviado;
    private LocalDateTime fechaEnvio;
    private Long citaId;
    private LocalDateTime fechaCreacion;
    private Long diasParaRecordatorio;
}
