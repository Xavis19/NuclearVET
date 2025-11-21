package com.nuclearvet.aplicacion.dto.administrativo;

import com.nuclearvet.dominio.enumeraciones.TipoImpuesto;
import com.nuclearvet.dominio.enumeraciones.TipoServicio;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrearServicioDTO {

    @NotBlank(message = "El código es obligatorio")
    @Size(max = 20, message = "El código no puede exceder 20 caracteres")
    private String codigo;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 200, message = "El nombre no puede exceder 200 caracteres")
    private String nombre;

    private String descripcion;

    @NotNull(message = "El tipo de servicio es obligatorio")
    private TipoServicio tipoServicio;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a cero")
    private BigDecimal precio;

    @NotNull(message = "El tipo de impuesto es obligatorio")
    private TipoImpuesto tipoImpuesto;

    @Builder.Default
    private Boolean activo = true;

    @Size(max = 500, message = "Las observaciones no pueden exceder 500 caracteres")
    private String observaciones;

    @Min(value = 1, message = "La duración debe ser al menos 1 minuto")
    private Integer duracionEstimada;

    private Boolean requiereAutorizacion;
}
