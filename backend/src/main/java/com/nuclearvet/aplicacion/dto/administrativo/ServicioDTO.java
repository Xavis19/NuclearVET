package com.nuclearvet.aplicacion.dto.administrativo;

import com.nuclearvet.dominio.enumeraciones.TipoImpuesto;
import com.nuclearvet.dominio.enumeraciones.TipoServicio;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServicioDTO {

    private Long id;
    private String codigo;
    private String nombre;
    private String descripcion;
    private TipoServicio tipoServicio;
    private BigDecimal precio;
    private TipoImpuesto tipoImpuesto;
    private Boolean activo;
    private String observaciones;
    private Integer duracionEstimada;
    private Boolean requiereAutorizacion;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private BigDecimal precioTotal;
}
