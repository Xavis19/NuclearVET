package com.nuclearvet.infraestructura.persistencia;

import com.nuclearvet.dominio.entidades.Pago;
import com.nuclearvet.dominio.enumeraciones.MetodoPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para gestión de pagos
 */
@Repository
public interface PagoRepositorio extends JpaRepository<Pago, Long> {

    Optional<Pago> findByNumeroPago(String numeroPago);
    
    List<Pago> findByFacturaId(Long facturaId);
    
    List<Pago> findByMetodoPago(MetodoPago metodoPago);

    @Query("SELECT p FROM Pago p WHERE p.factura.id = :facturaId ORDER BY p.fechaPago DESC")
    List<Pago> findByFacturaOrdenados(@Param("facturaId") Long facturaId);

    @Query("SELECT p FROM Pago p WHERE p.fechaPago BETWEEN :inicio AND :fin ORDER BY p.fechaPago DESC")
    List<Pago> findByRangoFechas(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);

    @Query("SELECT SUM(p.monto) FROM Pago p WHERE p.fechaPago BETWEEN :inicio AND :fin")
    BigDecimal calcularTotalPagos(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);

    @Query("SELECT p.metodoPago, SUM(p.monto) FROM Pago p " +
           "WHERE p.fechaPago BETWEEN :inicio AND :fin GROUP BY p.metodoPago")
    List<Object[]> calcularPagosPorMetodo(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);

    @Query("SELECT p FROM Pago p WHERE p.usuarioRegistro.id = :usuarioId ORDER BY p.fechaPago DESC")
    List<Pago> findByUsuarioRegistro(@Param("usuarioId") Long usuarioId);

    @Query("SELECT COUNT(p) FROM Pago p WHERE p.metodoPago = :metodo")
    Long contarPorMetodoPago(@Param("metodo") MetodoPago metodo);

    @Query("SELECT p FROM Pago p WHERE p.factura.propietario.id = :propietarioId ORDER BY p.fechaPago DESC")
    List<Pago> findByPropietarioId(@Param("propietarioId") Long propietarioId);
}
