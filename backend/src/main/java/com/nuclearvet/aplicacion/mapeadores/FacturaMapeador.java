package com.nuclearvet.aplicacion.mapeadores;

import com.nuclearvet.aplicacion.dto.administrativo.CrearFacturaDTO;
import com.nuclearvet.aplicacion.dto.administrativo.FacturaDTO;
import com.nuclearvet.dominio.entidades.Factura;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {ItemFacturaMapeador.class})
public interface FacturaMapeador {

    @Mapping(source = "propietario.id", target = "propietarioId")
    @Mapping(source = "propietario.nombreCompleto", target = "propietarioNombre")
    @Mapping(source = "paciente.id", target = "pacienteId")
    @Mapping(source = "paciente.nombre", target = "pacienteNombre")
    @Mapping(source = "usuarioCreador.id", target = "usuarioCreadorId")
    @Mapping(source = "usuarioCreador.nombreCompleto", target = "usuarioCreadorNombre")
    FacturaDTO aDTO(Factura factura);

    @Mapping(source = "propietarioId", target = "propietario.id")
    @Mapping(source = "pacienteId", target = "paciente.id")
    @Mapping(target = "numeroFactura", ignore = true)
    @Mapping(target = "estado", constant = "PENDIENTE")
    Factura aEntidad(CrearFacturaDTO dto);
}
