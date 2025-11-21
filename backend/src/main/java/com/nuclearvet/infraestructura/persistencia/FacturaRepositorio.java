package com.nuclearvet.infraestructura.persistencia;

import com.nuclearvet.dominio.entidades.Factura;
import com.nuclearvet.dominio.enumeraciones.EstadoFactura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para gestión de facturas
 */
@Repository
public interface FacturaRepositorio extends JpaRepository<Factura, Long> {

    Optional<Factura> findByNumeroFactura(String numeroFactura);
    
    List<Factura> findByEstado(EstadoFactura estado);
    
    List<Factura> findByPropietarioId(Long propietarioId);

    @Query("SELECT f FROM Factura f LEFT JOIN FETCH f.items LEFT JOIN FETCH f.propietario WHERE f.id = :id")
    Optional<Factura> findByIdConItems(@Param("id") Long id);

    @Query("SELECT f FROM Factura f WHERE f.propietario.id = :propietarioId ORDER BY f.fechaEmision DESC")
    List<Factura> findByPropietarioOrdenadas(@Param("propietarioId") Long propietarioId);

    @Query("SELECT f FROM Factura f WHERE f.estado = :estado ORDER BY f.fechaEmision DESC")
    List<Factura> findByEstadoOrdenadas(@Param("estado") EstadoFactura estado);

    @Query("SELECT f FROM Factura f WHERE f.fechaEmision BETWEEN :inicio AND :fin ORDER BY f.fechaEmision DESC")
    List<Factura> findByRangoFechas(@Param("inicio") LocalDate inicio, @Param("fin") LocalDate fin);

    @Query("SELECT f FROM Factura f WHERE f.estado = 'PENDIENTE' AND f.fechaVencimiento < CURRENT_DATE")
    List<Factura> findFacturasVencidas();

    @Query("SELECT f FROM Factura f WHERE f.saldoPendiente > 0 ORDER BY f.fechaVencimiento")
    List<Factura> findConSaldoPendiente();

    @Query("SELECT f FROM Factura f WHERE f.paciente.id = :pacienteId ORDER BY f.fechaEmision DESC")
    List<Factura> findByPacienteId(@Param("pacienteId") Long pacienteId);

    @Query("SELECT SUM(f.total) FROM Factura f WHERE f.estado = 'PAGADA' " +
           "AND f.fechaEmision BETWEEN :inicio AND :fin")
    BigDecimal calcularTotalVentas(@Param("inicio") LocalDate inicio, @Param("fin") LocalDate fin);

    @Query("SELECT SUM(f.saldoPendiente) FROM Factura f WHERE f.saldoPendiente > 0")
    BigDecimal calcularTotalCuentasPorCobrar();

    @Query("SELECT COUNT(f) FROM Factura f WHERE f.estado = :estado")
    Long contarPorEstado(@Param("estado") EstadoFactura estado);

    @Query("SELECT f.estado, COUNT(f) FROM Factura f GROUP BY f.estado")
    List<Object[]> contarPorEstados();

    @Query("SELECT f FROM Factura f WHERE f.usuarioCreador.id = :usuarioId ORDER BY f.fechaCreacion DESC")
    List<Factura> findByUsuarioCreador(@Param("usuarioId") Long usuarioId);
}
