package com.nuclearvet.infraestructura.persistencia;

import com.nuclearvet.dominio.entidades.MovimientoInventario;
import com.nuclearvet.dominio.enumeraciones.TipoMovimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para gestión de movimientos de inventario
 */
@Repository
public interface MovimientoInventarioRepositorio extends JpaRepository<MovimientoInventario, Long> {

    // Búsquedas básicas
    Optional<MovimientoInventario> findByNumeroMovimiento(String numeroMovimiento);
    
    List<MovimientoInventario> findByProductoId(Long productoId);
    
    List<MovimientoInventario> findByTipoMovimiento(TipoMovimiento tipoMovimiento);
    
    boolean existsByNumeroMovimiento(String numeroMovimiento);

    // Búsquedas por fechas
    @Query("SELECT m FROM MovimientoInventario m WHERE m.fechaMovimiento BETWEEN :fechaInicio AND :fechaFin " +
           "ORDER BY m.fechaMovimiento DESC")
    List<MovimientoInventario> findByFechaMovimientoBetween(@Param("fechaInicio") LocalDateTime fechaInicio,
                                                              @Param("fechaFin") LocalDateTime fechaFin);

    @Query("SELECT m FROM MovimientoInventario m WHERE m.producto.id = :productoId " +
           "ORDER BY m.fechaMovimiento DESC")
    List<MovimientoInventario> findByProductoIdOrdenadoPorFecha(@Param("productoId") Long productoId);

    @Query("SELECT m FROM MovimientoInventario m LEFT JOIN FETCH m.producto LEFT JOIN FETCH m.usuario " +
           "WHERE m.id = :id")
    Optional<MovimientoInventario> findByIdConRelaciones(@Param("id") Long id);

    // Búsquedas por tipo de movimiento
    @Query("SELECT m FROM MovimientoInventario m WHERE m.producto.id = :productoId " +
           "AND m.tipoMovimiento = :tipo ORDER BY m.fechaMovimiento DESC")
    List<MovimientoInventario> findByProductoIdAndTipoMovimiento(@Param("productoId") Long productoId,
                                                                   @Param("tipo") TipoMovimiento tipo);

    @Query("SELECT m FROM MovimientoInventario m WHERE m.tipoMovimiento IN :tipos " +
           "AND m.fechaMovimiento BETWEEN :fechaInicio AND :fechaFin")
    List<MovimientoInventario> findByTiposAndFecha(@Param("tipos") List<TipoMovimiento> tipos,
                                                     @Param("fechaInicio") LocalDateTime fechaInicio,
                                                     @Param("fechaFin") LocalDateTime fechaFin);

    // Búsquedas por usuario
    @Query("SELECT m FROM MovimientoInventario m WHERE m.usuario.id = :usuarioId " +
           "ORDER BY m.fechaMovimiento DESC")
    List<MovimientoInventario> findByUsuarioId(@Param("usuarioId") Long usuarioId);

    // Búsquedas por lote
    @Query("SELECT m FROM MovimientoInventario m WHERE m.lote.id = :loteId " +
           "ORDER BY m.fechaMovimiento DESC")
    List<MovimientoInventario> findByLoteId(@Param("loteId") Long loteId);

    // Estadísticas
    @Query("SELECT SUM(m.cantidad) FROM MovimientoInventario m WHERE m.producto.id = :productoId " +
           "AND m.tipoMovimiento IN :tiposEntrada AND m.fechaMovimiento BETWEEN :fechaInicio AND :fechaFin")
    Integer sumarEntradasPorProducto(@Param("productoId") Long productoId,
                                      @Param("tiposEntrada") List<TipoMovimiento> tiposEntrada,
                                      @Param("fechaInicio") LocalDateTime fechaInicio,
                                      @Param("fechaFin") LocalDateTime fechaFin);

    @Query("SELECT SUM(m.cantidad) FROM MovimientoInventario m WHERE m.producto.id = :productoId " +
           "AND m.tipoMovimiento IN :tiposSalida AND m.fechaMovimiento BETWEEN :fechaInicio AND :fechaFin")
    Integer sumarSalidasPorProducto(@Param("productoId") Long productoId,
                                     @Param("tiposSalida") List<TipoMovimiento> tiposSalida,
                                     @Param("fechaInicio") LocalDateTime fechaInicio,
                                     @Param("fechaFin") LocalDateTime fechaFin);

    @Query("SELECT COUNT(m) FROM MovimientoInventario m WHERE m.tipoMovimiento = :tipo")
    Long contarPorTipo(@Param("tipo") TipoMovimiento tipo);

    @Query("SELECT m FROM MovimientoInventario m WHERE m.fechaMovimiento >= :fecha " +
           "ORDER BY m.fechaMovimiento DESC")
    List<MovimientoInventario> findMovimientosRecientes(@Param("fecha") LocalDateTime fecha);

    // Búsqueda por documento
    @Query("SELECT m FROM MovimientoInventario m WHERE LOWER(m.numeroDocumento) = LOWER(:numeroDocumento)")
    List<MovimientoInventario> findByNumeroDocumento(@Param("numeroDocumento") String numeroDocumento);
}
