package com.nuclearvet.infraestructura.persistencia;

import com.nuclearvet.dominio.entidades.Recordatorio;
import com.nuclearvet.dominio.enumeraciones.TipoRecordatorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para gestión de recordatorios
 */
@Repository
public interface RecordatorioRepositorio extends JpaRepository<Recordatorio, Long> {

    // Búsquedas básicas
    List<Recordatorio> findByPacienteId(Long pacienteId);
    
    List<Recordatorio> findByEnviadoFalse();
    
    List<Recordatorio> findByTipoRecordatorio(TipoRecordatorio tipo);
    
    List<Recordatorio> findByCitaId(Long citaId);

    // Recordatorios pendientes
    @Query("SELECT r FROM Recordatorio r WHERE r.enviado = false " +
           "AND r.fechaProgramada <= CURRENT_TIMESTAMP ORDER BY r.fechaProgramada")
    List<Recordatorio> findRecordatoriosPendientesDeEnvio();

    @Query("SELECT r FROM Recordatorio r WHERE r.paciente.id = :pacienteId AND r.enviado = false " +
           "ORDER BY r.fechaProgramada")
    List<Recordatorio> findPendientesPorPaciente(@Param("pacienteId") Long pacienteId);

    @Query("SELECT r FROM Recordatorio r WHERE r.tipoRecordatorio = :tipo AND r.enviado = false " +
           "ORDER BY r.fechaProgramada")
    List<Recordatorio> findPendientesPorTipo(@Param("tipo") TipoRecordatorio tipo);

    // Búsquedas por fecha
    @Query("SELECT r FROM Recordatorio r WHERE r.fechaProgramada BETWEEN :inicio AND :fin " +
           "ORDER BY r.fechaProgramada")
    List<Recordatorio> findByRangoFechas(@Param("inicio") LocalDateTime inicio,
                                          @Param("fin") LocalDateTime fin);

    @Query("SELECT r FROM Recordatorio r WHERE r.paciente.id = :pacienteId " +
           "AND r.fechaProgramada BETWEEN :inicio AND :fin ORDER BY r.fechaProgramada")
    List<Recordatorio> findByPacienteYRangoFechas(@Param("pacienteId") Long pacienteId,
                                                    @Param("inicio") LocalDateTime inicio,
                                                    @Param("fin") LocalDateTime fin);

    @Query("SELECT r FROM Recordatorio r WHERE r.fechaProgramada >= :fecha AND r.enviado = false " +
           "ORDER BY r.fechaProgramada")
    List<Recordatorio> findProximosRecordatorios(@Param("fecha") LocalDateTime fecha);

    // Próximos a vencer
    @Query("SELECT r FROM Recordatorio r WHERE r.enviado = false " +
           "AND r.fechaProgramada BETWEEN CURRENT_TIMESTAMP AND :fechaLimite " +
           "ORDER BY r.fechaProgramada")
    List<Recordatorio> findProximosAEnviar(@Param("fechaLimite") LocalDateTime fechaLimite);

    // Con relaciones
    @Query("SELECT r FROM Recordatorio r LEFT JOIN FETCH r.paciente " +
           "LEFT JOIN FETCH r.cita WHERE r.id = :id")
    Optional<Recordatorio> findByIdConRelaciones(@Param("id") Long id);

    @Query("SELECT r FROM Recordatorio r LEFT JOIN FETCH r.paciente " +
           "WHERE r.paciente.id = :pacienteId ORDER BY r.fechaProgramada DESC")
    List<Recordatorio> findByPacienteConRelaciones(@Param("pacienteId") Long pacienteId);

    // Estadísticas
    @Query("SELECT r.tipoRecordatorio, COUNT(r) FROM Recordatorio r " +
           "WHERE r.enviado = false GROUP BY r.tipoRecordatorio")
    List<Object[]> contarPendientesPorTipo();

    @Query("SELECT COUNT(r) FROM Recordatorio r WHERE r.enviado = false " +
           "AND r.fechaProgramada <= CURRENT_TIMESTAMP")
    Long contarVencidos();

    @Query("SELECT COUNT(r) FROM Recordatorio r WHERE r.paciente.id = :pacienteId AND r.enviado = false")
    Long contarPendientesPorPaciente(@Param("pacienteId") Long pacienteId);

    // Por propietario
    @Query("SELECT r FROM Recordatorio r WHERE r.paciente.propietario.id = :propietarioId " +
           "AND r.enviado = false ORDER BY r.fechaProgramada")
    List<Recordatorio> findPendientesPorPropietario(@Param("propietarioId") Long propietarioId);

    // Limpieza
    @Query("SELECT r FROM Recordatorio r WHERE r.enviado = true " +
           "AND r.fechaEnvio < :fechaLimite")
    List<Recordatorio> findEnviadosAntiguos(@Param("fechaLimite") LocalDateTime fechaLimite);
}
