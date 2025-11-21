package com.nuclearvet.aplicacion.mapeadores;

import com.nuclearvet.aplicacion.dtos.AlertaInventarioDTO;
import com.nuclearvet.dominio.entidades.AlertaInventario;
import org.mapstruct.*;

import java.util.List;

/**
 * Mapeador para convertir entre AlertaInventario y AlertaInventarioDTO
 */
@Mapper(componentModel = "spring")
public interface AlertaInventarioMapeador {

    @Mapping(source = "producto.id", target = "productoId")
    @Mapping(source = "producto.nombre", target = "productoNombre")
    @Mapping(source = "producto.codigo", target = "productoCodigo")
    @Mapping(source = "lote.id", target = "loteId")
    @Mapping(source = "lote.numeroLote", target = "loteNumero")
    AlertaInventarioDTO aDTO(AlertaInventario alerta);

    List<AlertaInventarioDTO> aDTOLista(List<AlertaInventario> alertas);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "producto", ignore = true)
    @Mapping(target = "lote", ignore = true)
    AlertaInventario aEntidad(AlertaInventarioDTO dto);
}
