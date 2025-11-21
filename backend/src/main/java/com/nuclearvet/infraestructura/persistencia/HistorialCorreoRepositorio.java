package com.nuclearvet.infraestructura.persistencia;

import com.nuclearvet.dominio.entidades.HistorialCorreo;
import com.nuclearvet.dominio.enumeraciones.EstadoCorreo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para gestión de historial de correos
 */
@Repository
public interface HistorialCorreoRepositorio extends JpaRepository<HistorialCorreo, Long> {

    // Búsquedas básicas
    List<HistorialCorreo> findByDestinatarioEmail(String email);
    
    List<HistorialCorreo> findByEstado(EstadoCorreo estado);
    
    List<HistorialCorreo> findByPlantillaId(Long plantillaId);

    // Pendientes y errores
    @Query("SELECT h FROM HistorialCorreo h WHERE h.estado = 'PENDIENTE' ORDER BY h.fechaCreacion")
    List<HistorialCorreo> findPendientesDeEnvio();

    @Query("SELECT h FROM HistorialCorreo h WHERE h.estado = 'ERROR' ORDER BY h.fechaCreacion DESC")
    List<HistorialCorreo> findConError();

    @Query("SELECT h FROM HistorialCorreo h WHERE h.estado = 'REINTENTANDO' ORDER BY h.fechaCreacion")
    List<HistorialCorreo> findReintentando();

    // Búsquedas por fecha
    @Query("SELECT h FROM HistorialCorreo h WHERE h.fechaEnvio BETWEEN :inicio AND :fin " +
           "ORDER BY h.fechaEnvio DESC")
    List<HistorialCorreo> findByRangoFechasEnvio(@Param("inicio") LocalDateTime inicio,
                                                   @Param("fin") LocalDateTime fin);

    @Query("SELECT h FROM HistorialCorreo h WHERE h.destinatarioEmail = :email " +
           "ORDER BY h.fechaCreacion DESC")
    List<HistorialCorreo> findByDestinatarioOrdenado(@Param("email") String email);

    @Query("SELECT h FROM HistorialCorreo h WHERE h.fechaCreacion >= :fecha " +
           "ORDER BY h.fechaCreacion DESC")
    List<HistorialCorreo> findRecientes(@Param("fecha") LocalDateTime fecha);

    // Con relaciones
    @Query("SELECT h FROM HistorialCorreo h LEFT JOIN FETCH h.plantilla WHERE h.id = :id")
    Optional<HistorialCorreo> findByIdConPlantilla(@Param("id") Long id);

    @Query("SELECT h FROM HistorialCorreo h LEFT JOIN FETCH h.plantilla " +
           "WHERE h.destinatarioEmail = :email ORDER BY h.fechaCreacion DESC")
    List<HistorialCorreo> findByDestinatarioConPlantilla(@Param("email") String email);

    // Estadísticas
    @Query("SELECT h.estado, COUNT(h) FROM HistorialCorreo h GROUP BY h.estado")
    List<Object[]> contarPorEstado();

    @Query("SELECT COUNT(h) FROM HistorialCorreo h WHERE h.estado = 'ENVIADO'")
    Long contarEnviados();

    @Query("SELECT COUNT(h) FROM HistorialCorreo h WHERE h.estado = 'ERROR'")
    Long contarConError();

    @Query("SELECT COUNT(h) FROM HistorialCorreo h WHERE h.estado = 'PENDIENTE'")
    Long contarPendientes();

    @Query("SELECT COUNT(h) FROM HistorialCorreo h WHERE h.destinatarioEmail = :email " +
           "AND h.estado = 'ENVIADO'")
    Long contarEnviadosPorDestinatario(@Param("email") String email);

    // Por plantilla
    @Query("SELECT h FROM HistorialCorreo h WHERE h.plantilla.id = :plantillaId " +
           "ORDER BY h.fechaCreacion DESC")
    List<HistorialCorreo> findByPlantillaOrdenados(@Param("plantillaId") Long plantillaId);

    @Query("SELECT COUNT(h) FROM HistorialCorreo h WHERE h.plantilla.id = :plantillaId")
    Long contarPorPlantilla(@Param("plantillaId") Long plantillaId);

    // Últimos envíos
    @Query("SELECT h FROM HistorialCorreo h ORDER BY h.fechaCreacion DESC")
    List<HistorialCorreo> findUltimosEnvios();

    @Query("SELECT h FROM HistorialCorreo h WHERE h.estado = 'ENVIADO' " +
           "ORDER BY h.fechaEnvio DESC")
    List<HistorialCorreo> findUltimosEnviosExitosos();

    // Limpieza
    @Query("SELECT h FROM HistorialCorreo h WHERE h.estado = 'ENVIADO' " +
           "AND h.fechaEnvio < :fechaLimite")
    List<HistorialCorreo> findEnviadosAntiguos(@Param("fechaLimite") LocalDateTime fechaLimite);
}
