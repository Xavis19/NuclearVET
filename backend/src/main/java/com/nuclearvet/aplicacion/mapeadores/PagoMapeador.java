package com.nuclearvet.aplicacion.mapeadores;

import com.nuclearvet.aplicacion.dto.administrativo.PagoDTO;
import com.nuclearvet.aplicacion.dto.administrativo.RegistrarPagoDTO;
import com.nuclearvet.dominio.entidades.Pago;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PagoMapeador {

    @Mapping(source = "factura.id", target = "facturaId")
    @Mapping(source = "factura.numeroFactura", target = "facturaNumero")
    @Mapping(source = "usuarioRegistro.id", target = "usuarioRegistroId")
    @Mapping(source = "usuarioRegistro.nombreCompleto", target = "usuarioRegistroNombre")
    PagoDTO aDTO(Pago pago);

    @Mapping(source = "facturaId", target = "factura.id")
    @Mapping(target = "numeroPago", ignore = true)
    Pago aEntidad(RegistrarPagoDTO dto);
}
