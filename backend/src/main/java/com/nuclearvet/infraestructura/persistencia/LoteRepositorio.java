package com.nuclearvet.infraestructura.persistencia;

import com.nuclearvet.dominio.entidades.Lote;
import com.nuclearvet.dominio.enumeraciones.EstadoLote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para gestión de lotes de productos
 */
@Repository
public interface LoteRepositorio extends JpaRepository<Lote, Long> {

    // Búsquedas básicas
    Optional<Lote> findByNumeroLote(String numeroLote);
    
    List<Lote> findByProductoId(Long productoId);
    
    List<Lote> findByEstado(EstadoLote estado);
    
    boolean existsByNumeroLote(String numeroLote);

    // Búsquedas por disponibilidad
    @Query("SELECT l FROM Lote l WHERE l.producto.id = :productoId AND l.estado = 'DISPONIBLE' " +
           "AND l.cantidadDisponible > 0 ORDER BY l.fechaVencimiento ASC")
    List<Lote> findLotesDisponiblesPorProducto(@Param("productoId") Long productoId);

    @Query("SELECT l FROM Lote l LEFT JOIN FETCH l.producto WHERE l.id = :id")
    Optional<Lote> findByIdConProducto(@Param("id") Long id);

    // Alertas de vencimiento
    @Query("SELECT l FROM Lote l WHERE l.fechaVencimiento <= :fecha AND l.estado != 'VENCIDO' " +
           "AND l.cantidadDisponible > 0")
    List<Lote> findLotesPorVencer(@Param("fecha") LocalDate fecha);

    @Query("SELECT l FROM Lote l WHERE l.fechaVencimiento < CURRENT_DATE AND l.estado != 'VENCIDO' " +
           "AND l.cantidadDisponible > 0")
    List<Lote> findLotesVencidos();

    @Query("SELECT l FROM Lote l WHERE l.fechaVencimiento BETWEEN CURRENT_DATE AND :fecha " +
           "AND l.estado = 'DISPONIBLE' AND l.cantidadDisponible > 0")
    List<Lote> findLotesProximosVencer(@Param("fecha") LocalDate fecha);

    // Búsquedas por fechas
    @Query("SELECT l FROM Lote l WHERE l.fechaIngreso BETWEEN :fechaInicio AND :fechaFin")
    List<Lote> findByFechaIngresoBetween(@Param("fechaInicio") LocalDate fechaInicio, 
                                          @Param("fechaFin") LocalDate fechaFin);

    @Query("SELECT l FROM Lote l WHERE l.producto.id = :productoId ORDER BY l.fechaVencimiento ASC")
    List<Lote> findByProductoIdOrdenadoPorVencimiento(@Param("productoId") Long productoId);

    @Query("SELECT l FROM Lote l WHERE l.producto.id = :productoId ORDER BY l.fechaVencimiento")
    List<Lote> findByProductoIdOrderByFechaVencimiento(@Param("productoId") Long productoId);

    @Query("SELECT l FROM Lote l WHERE l.estado IN ('DISPONIBLE', 'PROXIMO_VENCER') AND l.cantidadDisponible > 0")
    List<Lote> findLotesDisponibles();

    // Estadísticas
    @Query("SELECT SUM(l.cantidadDisponible) FROM Lote l WHERE l.producto.id = :productoId " +
           "AND l.estado = 'DISPONIBLE'")
    Integer sumarCantidadDisponiblePorProducto(@Param("productoId") Long productoId);

    @Query("SELECT COUNT(l) FROM Lote l WHERE l.estado = :estado")
    Long contarPorEstado(@Param("estado") EstadoLote estado);

    @Query("SELECT l FROM Lote l WHERE l.cantidadDisponible = 0 AND l.estado != 'AGOTADO'")
    List<Lote> findLotesAgotadosSinActualizar();

    @Query("SELECT l FROM Lote l WHERE l.producto.id = :productoId AND l.numeroLote = :numeroLote")
    Optional<Lote> findByProductoIdAndNumeroLote(@Param("productoId") Long productoId, 
                                                   @Param("numeroLote") String numeroLote);
}
