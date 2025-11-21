package com.nuclearvet.aplicacion.mapeadores;

import com.nuclearvet.aplicacion.dto.administrativo.ConfiguracionClinicaDTO;
import com.nuclearvet.dominio.entidades.ConfiguracionClinica;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ConfiguracionClinicaMapeador {

    ConfiguracionClinicaDTO aDTO(ConfiguracionClinica config);

    ConfiguracionClinica aEntidad(ConfiguracionClinicaDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void actualizarEntidad(ConfiguracionClinicaDTO dto, @MappingTarget ConfiguracionClinica config);
}
