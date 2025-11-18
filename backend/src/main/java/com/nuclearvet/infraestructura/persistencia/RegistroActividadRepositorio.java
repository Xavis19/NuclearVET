package com.nuclearvet.infraestructura.persistencia;

import com.nuclearvet.dominio.entidades.RegistroActividad;
import com.nuclearvet.dominio.enums.TipoAccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio para registro de auditoría
 * RF1.5 - Registro de actividad relevante
 */
@Repository
public interface RegistroActividadRepositorio extends JpaRepository<RegistroActividad, Long> {

    List<RegistroActividad> findByUsuarioIdOrderByFechaHoraDesc(Long usuarioId);

    List<RegistroActividad> findByTipoAccion(TipoAccion tipoAccion);

    List<RegistroActividad> findByFechaHoraBetweenOrderByFechaHoraDesc(
            LocalDateTime fechaInicio, 
            LocalDateTime fechaFin
    );

    List<RegistroActividad> findTop100ByOrderByFechaHoraDesc();
}
