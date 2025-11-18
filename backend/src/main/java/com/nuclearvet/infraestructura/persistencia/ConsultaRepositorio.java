package com.nuclearvet.infraestructura.persistencia;

import com.nuclearvet.dominio.entidades.Consulta;
import com.nuclearvet.dominio.enums.EstadoConsulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para gestión de consultas
 */
@Repository
public interface ConsultaRepositorio extends JpaRepository<Consulta, Long> {

    /**
     * Buscar consulta por número
     */
    Optional<Consulta> findByNumeroConsulta(String numeroConsulta);

    /**
     * Verificar si existe consulta por número
     */
    boolean existsByNumeroConsulta(String numeroConsulta);

    /**
     * Buscar consultas por historia clínica
     */
    List<Consulta> findByHistoriaClinicaIdOrderByFechaConsultaDesc(Long historiaClinicaId);

    /**
     * Buscar consultas por veterinario
     */
    List<Consulta> findByVeterinarioIdOrderByFechaConsultaDesc(Long veterinarioId);

    /**
     * Buscar consultas por cita
     */
    Optional<Consulta> findByCitaId(Long citaId);

    /**
     * Buscar consultas por estado
     */
    List<Consulta> findByEstadoOrderByFechaConsultaDesc(EstadoConsulta estado);

    /**
     * Buscar consultas por rango de fechas
     */
    List<Consulta> findByFechaConsultaBetweenOrderByFechaConsultaDesc(
            LocalDateTime inicio, 
            LocalDateTime fin
    );

    /**
     * Buscar consultas de hoy por veterinario
     */
    @Query("SELECT c FROM Consulta c WHERE c.veterinario.id = :veterinarioId " +
           "AND c.fechaConsulta >= :inicio AND c.fechaConsulta < :fin " +
           "ORDER BY c.fechaConsulta ASC")
    List<Consulta> findConsultasDelDiaPorVeterinario(
            @Param("veterinarioId") Long veterinarioId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin
    );

    /**
     * Buscar últimas consultas de un paciente
     */
    @Query("SELECT c FROM Consulta c WHERE c.historiaClinica.paciente.id = :pacienteId " +
           "ORDER BY c.fechaConsulta DESC")
    List<Consulta> findUltimasConsultasPorPaciente(@Param("pacienteId") Long pacienteId);

    /**
     * Buscar consultas completadas por paciente
     */
    @Query("SELECT c FROM Consulta c WHERE c.historiaClinica.paciente.id = :pacienteId " +
           "AND c.estado = 'COMPLETADA' " +
           "ORDER BY c.fechaConsulta DESC")
    List<Consulta> findConsultasCompletadasPorPaciente(@Param("pacienteId") Long pacienteId);

    /**
     * Buscar consultas con próxima cita programada
     */
    @Query("SELECT c FROM Consulta c WHERE c.proximaCita IS NOT NULL " +
           "AND c.proximaCita > :ahora " +
           "ORDER BY c.proximaCita ASC")
    List<Consulta> findConsultasConProximaCita(@Param("ahora") LocalDateTime ahora);

    /**
     * Contar consultas por estado
     */
    Long countByEstado(EstadoConsulta estado);

    /**
     * Contar consultas por veterinario
     */
    Long countByVeterinarioId(Long veterinarioId);

    /**
     * Contar consultas de un paciente
     */
    @Query("SELECT COUNT(c) FROM Consulta c WHERE c.historiaClinica.paciente.id = :pacienteId")
    Long countByPacienteId(@Param("pacienteId") Long pacienteId);

    /**
     * Buscar consultas recientes (últimos 30 días)
     */
    @Query("SELECT c FROM Consulta c WHERE c.fechaConsulta >= :fecha " +
           "ORDER BY c.fechaConsulta DESC")
    List<Consulta> findConsultasRecientes(@Param("fecha") LocalDateTime fecha);
}
