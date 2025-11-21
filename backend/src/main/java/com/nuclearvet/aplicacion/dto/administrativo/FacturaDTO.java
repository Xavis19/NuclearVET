package com.nuclearvet.aplicacion.dto.administrativo;

import com.nuclearvet.dominio.enumeraciones.EstadoFactura;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FacturaDTO {

    private Long id;
    private String numeroFactura;
    private Long propietarioId;
    private String propietarioNombre;
    private Long pacienteId;
    private String pacienteNombre;
    private LocalDate fechaEmision;
    private LocalDate fechaVencimiento;
    private EstadoFactura estado;
    private BigDecimal subtotal;
    private BigDecimal impuestos;
    private BigDecimal descuento;
    private BigDecimal total;
    private BigDecimal saldoPendiente;
    private List<ItemFacturaDTO> items;
    private String observaciones;
    private Long usuarioCreadorId;
    private String usuarioCreadorNombre;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}
