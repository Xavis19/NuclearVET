package com.nuclearvet.infraestructura.persistencia;

import com.nuclearvet.dominio.entidades.AlertaInventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio para gestión de alertas de inventario
 */
@Repository
public interface AlertaInventarioRepositorio extends JpaRepository<AlertaInventario, Long> {

    // Búsquedas básicas
    List<AlertaInventario> findByProductoId(Long productoId);
    
    List<AlertaInventario> findByLeidaFalse();
    
    List<AlertaInventario> findByPrioridad(String prioridad);
    
    boolean existsByProductoIdAndLeidaFalse(Long productoId);
    
    boolean existsByLoteIdAndLeidaFalse(Long loteId);
    
    Long countByLeidaFalse();

    // Búsquedas por tipo
    @Query("SELECT a FROM AlertaInventario a WHERE a.tipo = :tipo AND a.leida = false " +
           "ORDER BY a.fechaAlerta DESC")
    List<AlertaInventario> findByTipoNoLeidas(@Param("tipo") String tipo);

    @Query("SELECT a FROM AlertaInventario a WHERE a.prioridad = :prioridad AND a.leida = false " +
           "ORDER BY a.fechaAlerta DESC")
    List<AlertaInventario> findByPrioridadNoLeidas(@Param("prioridad") String prioridad);

    @Query("SELECT a FROM AlertaInventario a WHERE a.prioridad = :prioridad AND a.leida = false")
    List<AlertaInventario> findByPrioridadAndLeidaFalse(@Param("prioridad") String prioridad);

    @Query("SELECT a FROM AlertaInventario a WHERE a.leida = false ORDER BY a.prioridad DESC, a.fechaAlerta DESC")
    List<AlertaInventario> findNoLeidas();

    @Query("SELECT a FROM AlertaInventario a WHERE a.fechaAlerta >= :fecha ORDER BY a.fechaAlerta DESC")
    List<AlertaInventario> findAlertasRecientes(@Param("fecha") LocalDateTime fecha);

    @Query("UPDATE AlertaInventario a SET a.leida = true WHERE a.leida = false")
    void marcarTodasComoLeidas();

    // Búsquedas combinadas
    @Query("SELECT a FROM AlertaInventario a WHERE a.producto.id = :productoId AND a.leida = false " +
           "ORDER BY a.fechaAlerta DESC")
    List<AlertaInventario> findByProductoIdNoLeidas(@Param("productoId") Long productoId);

    @Query("SELECT a FROM AlertaInventario a WHERE a.lote.id = :loteId AND a.leida = false " +
           "ORDER BY a.fechaAlerta DESC")
    List<AlertaInventario> findByLoteIdNoLeidas(@Param("loteId") Long loteId);

    // Alertas por fecha
    @Query("SELECT a FROM AlertaInventario a WHERE a.fechaAlerta BETWEEN :fechaInicio AND :fechaFin " +
           "ORDER BY a.fechaAlerta DESC")
    List<AlertaInventario> findByFechaAlertaBetween(@Param("fechaInicio") LocalDateTime fechaInicio,
                                                      @Param("fechaFin") LocalDateTime fechaFin);

    @Query("SELECT a FROM AlertaInventario a WHERE a.fechaAlerta >= :fecha AND a.leida = false " +
           "ORDER BY a.fechaAlerta DESC")
    List<AlertaInventario> findAlertasRecientesNoLeidas(@Param("fecha") LocalDateTime fecha);

    // Con relaciones
    @Query("SELECT a FROM AlertaInventario a LEFT JOIN FETCH a.producto LEFT JOIN FETCH a.lote " +
           "WHERE a.id = :id")
    AlertaInventario findByIdConRelaciones(@Param("id") Long id);

    @Query("SELECT a FROM AlertaInventario a LEFT JOIN FETCH a.producto WHERE a.leida = false " +
           "ORDER BY a.prioridad DESC, a.fechaAlerta DESC")
    List<AlertaInventario> findAllNoLeidasConProducto();

    // Estadísticas
    @Query("SELECT COUNT(a) FROM AlertaInventario a WHERE a.leida = false")
    Long contarNoLeidas();

    @Query("SELECT COUNT(a) FROM AlertaInventario a WHERE a.leida = false AND a.prioridad = 'ALTA'")
    Long contarNoLeidasPrioridadAlta();

    @Query("SELECT a.tipo, COUNT(a) FROM AlertaInventario a WHERE a.leida = false GROUP BY a.tipo")
    List<Object[]> contarPorTipoNoLeidas();

    @Query("SELECT a.prioridad, COUNT(a) FROM AlertaInventario a WHERE a.leida = false GROUP BY a.prioridad")
    List<Object[]> contarPorPrioridadNoLeidas();

    // Limpieza
    @Query("SELECT a FROM AlertaInventario a WHERE a.leida = true AND a.fechaLeida < :fechaLimite")
    List<AlertaInventario> findAlertasLeidasAntiguas(@Param("fechaLimite") LocalDateTime fechaLimite);

    @Query("DELETE FROM AlertaInventario a WHERE a.producto.id = :productoId AND a.leida = true")
    void eliminarLeidasPorProducto(@Param("productoId") Long productoId);
}
