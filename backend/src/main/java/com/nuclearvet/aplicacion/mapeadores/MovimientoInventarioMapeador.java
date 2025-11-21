package com.nuclearvet.aplicacion.mapeadores;

import com.nuclearvet.aplicacion.dtos.MovimientoInventarioDTO;
import com.nuclearvet.dominio.entidades.MovimientoInventario;
import org.mapstruct.*;

import java.util.List;

/**
 * Mapeador para convertir entre MovimientoInventario y MovimientoInventarioDTO
 */
@Mapper(componentModel = "spring")
public interface MovimientoInventarioMapeador {

    @Mapping(source = "producto.id", target = "productoId")
    @Mapping(source = "producto.nombre", target = "productoNombre")
    @Mapping(source = "producto.codigo", target = "productoCodigo")
    @Mapping(source = "lote.id", target = "loteId")
    @Mapping(source = "lote.numeroLote", target = "loteNumero")
    @Mapping(source = "usuario.id", target = "usuarioId")
    @Mapping(source = "usuario.nombreCompleto", target = "usuarioNombre")
    MovimientoInventarioDTO aDTO(MovimientoInventario movimiento);

    List<MovimientoInventarioDTO> aDTOLista(List<MovimientoInventario> movimientos);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "producto", ignore = true)
    @Mapping(target = "lote", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    MovimientoInventario aEntidad(MovimientoInventarioDTO dto);
}
