package com.nuclearvet.dominio.servicios;

import com.nuclearvet.aplicacion.dto.administrativo.ConfiguracionClinicaDTO;
import com.nuclearvet.aplicacion.mapeadores.ConfiguracionClinicaMapeador;
import com.nuclearvet.compartido.excepciones.RecursoNoEncontradoExcepcion;
import com.nuclearvet.dominio.entidades.ConfiguracionClinica;
import com.nuclearvet.infraestructura.persistencia.ConfiguracionClinicaRepositorio;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConfiguracionClinicaServicio {

    private final ConfiguracionClinicaRepositorio configuracionRepositorio;
    private final ConfiguracionClinicaMapeador configuracionMapeador;

    @Transactional(readOnly = true)
    public ConfiguracionClinicaDTO obtenerConfiguracionActual() {
        log.debug("Obteniendo configuración actual de la clínica");
        ConfiguracionClinica config = configuracionRepositorio.findConfiguracionActual()
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("No existe configuración de la clínica"));
        return configuracionMapeador.aDTO(config);
    }

    @Transactional(readOnly = true)
    public ConfiguracionClinicaDTO obtenerPorId(Long id) {
        log.debug("Obteniendo configuración con ID: {}", id);
        ConfiguracionClinica config = buscarConfiguracionPorId(id);
        return configuracionMapeador.aDTO(config);
    }

    @Transactional
    public ConfiguracionClinicaDTO crear(ConfiguracionClinicaDTO dto) {
        log.info("Creando configuración de clínica");

        ConfiguracionClinica config = configuracionMapeador.aEntidad(dto);
        ConfiguracionClinica configGuardada = configuracionRepositorio.save(config);

        log.info("Configuración creada exitosamente con ID: {}", configGuardada.getId());
        return configuracionMapeador.aDTO(configGuardada);
    }

    @Transactional
    public ConfiguracionClinicaDTO actualizar(Long id, ConfiguracionClinicaDTO dto) {
        log.info("Actualizando configuración con ID: {}", id);

        ConfiguracionClinica config = buscarConfiguracionPorId(id);
        configuracionMapeador.actualizarEntidad(dto, config);
        ConfiguracionClinica configActualizada = configuracionRepositorio.save(config);

        log.info("Configuración actualizada exitosamente: {}", id);
        return configuracionMapeador.aDTO(configActualizada);
    }

    @Transactional
    public ConfiguracionClinicaDTO actualizarActual(ConfiguracionClinicaDTO dto) {
        log.info("Actualizando configuración actual");
        
        ConfiguracionClinica config = configuracionRepositorio.findConfiguracionActual()
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("No existe configuración de la clínica"));
        
        configuracionMapeador.actualizarEntidad(dto, config);
        ConfiguracionClinica configActualizada = configuracionRepositorio.save(config);

        log.info("Configuración actual actualizada exitosamente");
        return configuracionMapeador.aDTO(configActualizada);
    }

    private ConfiguracionClinica buscarConfiguracionPorId(Long id) {
        return configuracionRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Configuración con ID " + id + " no encontrada"));
    }
}
