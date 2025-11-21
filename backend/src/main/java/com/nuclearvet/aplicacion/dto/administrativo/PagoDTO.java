package com.nuclearvet.aplicacion.dto.administrativo;

import com.nuclearvet.dominio.enumeraciones.MetodoPago;
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
public class PagoDTO {

    private Long id;
    private String numeroPago;
    private Long facturaId;
    private String facturaNumero;
    private LocalDateTime fechaPago;
    private BigDecimal monto;
    private MetodoPago metodoPago;
    private String referencia;
    private String observaciones;
    private Long usuarioRegistroId;
    private String usuarioRegistroNombre;
    private LocalDateTime fechaCreacion;
}
