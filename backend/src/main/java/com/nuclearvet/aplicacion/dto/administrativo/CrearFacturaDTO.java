package com.nuclearvet.aplicacion.dto.administrativo;

import com.nuclearvet.dominio.enumeraciones.EstadoFactura;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrearFacturaDTO {

    @NotNull(message = "El ID del propietario es obligatorio")
    private Long propietarioId;

    private Long pacienteId;

    @NotNull(message = "La fecha de emisión es obligatoria")
    private LocalDate fechaEmision;

    @NotNull(message = "La fecha de vencimiento es obligatoria")
    @Future(message = "La fecha de vencimiento debe ser futura")
    private LocalDate fechaVencimiento;

    @NotNull(message = "Los items son obligatorios")
    @Size(min = 1, message = "Debe incluir al menos un item")
    @Valid
    private List<ItemFacturaDTO> items = new ArrayList<>();

    @DecimalMin(value = "0.0", message = "El descuento no puede ser negativo")
    private BigDecimal descuento;

    private String observaciones;
}
