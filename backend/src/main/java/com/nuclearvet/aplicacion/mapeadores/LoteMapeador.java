package com.nuclearvet.aplicacion.mapeadores;

import com.nuclearvet.aplicacion.dtos.LoteDTO;
import com.nuclearvet.dominio.entidades.Lote;
import org.mapstruct.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Mapeador para convertir entre Lote y LoteDTO
 */
@Mapper(componentModel = "spring")
public interface LoteMapeador {

    @Mapping(source = "producto.id", target = "productoId")
    @Mapping(source = "producto.nombre", target = "productoNombre")
    @Mapping(source = "producto.codigo", target = "productoCodigo")
    @Mapping(target = "diasParaVencer", expression = "java(calcularDiasParaVencer(lote))")
    @Mapping(target = "estaVencido", expression = "java(lote.estaVencido())")
    LoteDTO aDTO(Lote lote);

    List<LoteDTO> aDTOLista(List<Lote> lotes);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "fechaActualizacion", ignore = true)
    @Mapping(target = "producto", ignore = true)
    @Mapping(target = "estado", ignore = true)
    Lote aEntidad(LoteDTO loteDTO);

    default Integer calcularDiasParaVencer(Lote lote) {
        if (lote.getFechaVencimiento() == null) {
            return null;
        }
        long dias = ChronoUnit.DAYS.between(LocalDate.now(), lote.getFechaVencimiento());
        return (int) dias;
    }
}
