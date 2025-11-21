package com.nuclearvet.infraestructura.persistencia;

import com.nuclearvet.dominio.entidades.ConfiguracionClinica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para gestión de configuración de la clínica
 */
@Repository
public interface ConfiguracionClinicaRepositorio extends JpaRepository<ConfiguracionClinica, Long> {

    @Query("SELECT c FROM ConfiguracionClinica c ORDER BY c.id DESC")
    Optional<ConfiguracionClinica> findConfiguracionActual();

    Optional<ConfiguracionClinica> findByNit(String nit);
}
