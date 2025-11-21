package com.nuclearvet.infraestructura.persistencia;

import com.nuclearvet.dominio.entidades.Notificacion;
import com.nuclearvet.dominio.enumeraciones.PrioridadNotificacion;
import com.nuclearvet.dominio.enumeraciones.TipoNotificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para gestión de notificaciones
 */
@Repository
public interface NotificacionRepositorio extends JpaRepository<Notificacion, Long> {

    // Búsquedas básicas
    List<Notificacion> findByUsuarioDestinatarioId(Long usuarioId);
    
    List<Notificacion> findByLeidaFalse();
    
    List<Notificacion> findByTipoNotificacion(TipoNotificacion tipo);
    
    List<Notificacion> findByNivelPrioridad(PrioridadNotificacion prioridad);

    // Búsquedas combinadas
    @Query("SELECT n FROM Notificacion n WHERE n.usuarioDestinatario.id = :usuarioId AND n.leida = false " +
           "ORDER BY n.nivelPrioridad DESC, n.fechaCreacion DESC")
    List<Notificacion> findNoLeidasPorUsuario(@Param("usuarioId") Long usuarioId);

    @Query("SELECT n FROM Notificacion n WHERE n.usuarioDestinatario.id = :usuarioId " +
           "ORDER BY n.fechaCreacion DESC")
    List<Notificacion> findByUsuarioOrdenadas(@Param("usuarioId") Long usuarioId);

    @Query("SELECT n FROM Notificacion n WHERE n.usuarioDestinatario.id = :usuarioId " +
           "AND n.tipoNotificacion = :tipo ORDER BY n.fechaCreacion DESC")
    List<Notificacion> findByUsuarioYTipo(@Param("usuarioId") Long usuarioId, 
                                           @Param("tipo") TipoNotificacion tipo);

    @Query("SELECT n FROM Notificacion n WHERE n.usuarioDestinatario.id = :usuarioId " +
           "AND n.nivelPrioridad = :prioridad AND n.leida = false")
    List<Notificacion> findNoLeidasPorUsuarioYPrioridad(@Param("usuarioId") Long usuarioId,
                                                          @Param("prioridad") PrioridadNotificacion prioridad);

    // Búsquedas por fecha
    @Query("SELECT n FROM Notificacion n WHERE n.usuarioDestinatario.id = :usuarioId " +
           "AND n.fechaCreacion BETWEEN :inicio AND :fin ORDER BY n.fechaCreacion DESC")
    List<Notificacion> findByUsuarioYRangoFechas(@Param("usuarioId") Long usuarioId,
                                                   @Param("inicio") LocalDateTime inicio,
                                                   @Param("fin") LocalDateTime fin);

    @Query("SELECT n FROM Notificacion n WHERE n.usuarioDestinatario.id = :usuarioId " +
           "AND n.fechaCreacion >= :fecha ORDER BY n.fechaCreacion DESC")
    List<Notificacion> findRecientesPorUsuario(@Param("usuarioId") Long usuarioId,
                                                 @Param("fecha") LocalDateTime fecha);

    // Con relaciones
    @Query("SELECT n FROM Notificacion n LEFT JOIN FETCH n.usuarioDestinatario WHERE n.id = :id")
    Optional<Notificacion> findByIdConUsuario(@Param("id") Long id);

    // Contadores
    @Query("SELECT COUNT(n) FROM Notificacion n WHERE n.usuarioDestinatario.id = :usuarioId AND n.leida = false")
    Long contarNoLeidasPorUsuario(@Param("usuarioId") Long usuarioId);

    @Query("SELECT COUNT(n) FROM Notificacion n WHERE n.usuarioDestinatario.id = :usuarioId " +
           "AND n.nivelPrioridad = 'URGENTE' AND n.leida = false")
    Long contarUrgentesNoLeidasPorUsuario(@Param("usuarioId") Long usuarioId);

    @Query("SELECT n.tipoNotificacion, COUNT(n) FROM Notificacion n " +
           "WHERE n.usuarioDestinatario.id = :usuarioId AND n.leida = false GROUP BY n.tipoNotificacion")
    List<Object[]> contarNoLeidasPorTipo(@Param("usuarioId") Long usuarioId);

    // Operaciones masivas
    @Modifying
    @Query("UPDATE Notificacion n SET n.leida = true, n.fechaLeida = CURRENT_TIMESTAMP " +
           "WHERE n.usuarioDestinatario.id = :usuarioId AND n.leida = false")
    int marcarTodasComoLeidasPorUsuario(@Param("usuarioId") Long usuarioId);

    @Modifying
    @Query("UPDATE Notificacion n SET n.leida = true, n.fechaLeida = CURRENT_TIMESTAMP " +
           "WHERE n.usuarioDestinatario.id = :usuarioId AND n.tipoNotificacion = :tipo AND n.leida = false")
    int marcarComoLeidasPorTipo(@Param("usuarioId") Long usuarioId, @Param("tipo") TipoNotificacion tipo);

    // Limpieza
    @Query("SELECT n FROM Notificacion n WHERE n.leida = true " +
           "AND n.fechaLeida < :fechaLimite")
    List<Notificacion> findLeidasAntiguas(@Param("fechaLimite") LocalDateTime fechaLimite);
}
