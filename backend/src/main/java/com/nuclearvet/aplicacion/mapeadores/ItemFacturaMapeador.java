package com.nuclearvet.aplicacion.mapeadores;

import com.nuclearvet.aplicacion.dto.administrativo.ItemFacturaDTO;
import com.nuclearvet.dominio.entidades.ItemFactura;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ItemFacturaMapeador {

    @Mapping(source = "servicio.id", target = "servicioId")
    @Mapping(source = "producto.id", target = "productoId")
    ItemFacturaDTO aDTO(ItemFactura item);

    @Mapping(source = "servicioId", target = "servicio.id")
    @Mapping(source = "productoId", target = "producto.id")
    ItemFactura aEntidad(ItemFacturaDTO dto);
}
