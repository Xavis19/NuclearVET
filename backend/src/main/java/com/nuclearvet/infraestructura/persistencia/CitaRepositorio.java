package com.nuclearvet.infraestructura.persistencia;

import com.nuclearvet.dominio.entidades.Cita;
import com.nuclearvet.dominio.enums.EstadoCita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para gestión de citas
 */
@Repository
public interface CitaRepositorio extends JpaRepository<Cita, Long> {

    /**
     * Buscar cita por número
     */
    Optional<Cita> findByNumeroCita(String numeroCita);

    /**
     * Verificar si existe cita por número
     */
    boolean existsByNumeroCita(String numeroCita);

    /**
     * Buscar citas por paciente
     */
    List<Cita> findByPacienteId(Long pacienteId);

    /**
     * Buscar citas por veterinario
     */
    List<Cita> findByVeterinarioId(Long veterinarioId);

    /**
     * Buscar citas por propietario
     */
    List<Cita> findByPropietarioId(Long propietarioId);

    /**
     * Buscar citas por estado
     */
    List<Cita> findByEstado(EstadoCita estado);

    /**
     * Buscar citas por rango de fechas
     */
    List<Cita> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin);

    /**
     * Buscar citas pendientes de un veterinario
     */
    @Query("SELECT c FROM Cita c WHERE c.veterinario.id = :veterinarioId " +
           "AND c.estado IN ('PENDIENTE', 'CONFIRMADA') " +
           "ORDER BY c.fechaHora ASC")
    List<Cita> findCitasPendientesPorVeterinario(@Param("veterinarioId") Long veterinarioId);

    /**
     * Buscar citas del día para un veterinario
     */
    @Query("SELECT c FROM Cita c WHERE c.veterinario.id = :veterinarioId " +
           "AND c.fechaHora >= :inicio AND c.fechaHora < :fin " +
           "ORDER BY c.fechaHora ASC")
    List<Cita> findCitasDelDiaPorVeterinario(
            @Param("veterinarioId") Long veterinarioId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin
    );

    /**
     * Verificar solapamiento de citas para un veterinario
     */
    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END " +
            "FROM citas c " +
            "WHERE c.veterinario_id = :veterinarioId " +
            "AND c.estado NOT IN ('CANCELADA', 'COMPLETADA') " +
            "AND c.fecha_hora < :fin " +
            "AND (c.fecha_hora + (c.duracion_estimada || ' minutes')::interval) > :inicio",
            nativeQuery = true)
    boolean existeSolapamiento(
            @Param("veterinarioId") Long veterinarioId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin
    );

    /**
     * Contar citas por estado
     */
    Long countByEstado(EstadoCita estado);

    /**
     * Buscar citas próximas (24 horas)
     */
    @Query("SELECT c FROM Cita c WHERE c.fechaHora BETWEEN :ahora AND :limite " +
           "AND c.estado IN ('PENDIENTE', 'CONFIRMADA') " +
           "ORDER BY c.fechaHora ASC")
    List<Cita> findCitasProximas(
            @Param("ahora") LocalDateTime ahora,
            @Param("limite") LocalDateTime limite
    );

    /**
     * Buscar citas sin recordatorio enviado
     */
    @Query("SELECT c FROM Cita c WHERE c.recordatorioEnviado = false " +
           "AND c.fechaHora > :ahora " +
           "AND c.estado IN ('PENDIENTE', 'CONFIRMADA')")
    List<Cita> findCitasSinRecordatorio(@Param("ahora") LocalDateTime ahora);
}
