package com.nuclearvet.infraestructura.persistencia;

import com.nuclearvet.dominio.entidades.Servicio;
import com.nuclearvet.dominio.enumeraciones.TipoServicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para gestión de servicios
 */
@Repository
public interface ServicioRepositorio extends JpaRepository<Servicio, Long> {

    Optional<Servicio> findByCodigo(String codigo);
    
    List<Servicio> findByActivoTrue();
    
    List<Servicio> findByTipoServicio(TipoServicio tipo);

    @Query("SELECT s FROM Servicio s WHERE s.activo = true AND s.tipoServicio = :tipo")
    List<Servicio> findActivosPorTipo(@Param("tipo") TipoServicio tipo);

    @Query("SELECT s FROM Servicio s WHERE LOWER(s.nombre) LIKE LOWER(CONCAT('%', :termino, '%')) " +
           "OR LOWER(s.codigo) LIKE LOWER(CONCAT('%', :termino, '%'))")
    List<Servicio> buscarPorNombreOCodigo(@Param("termino") String termino);

    @Query("SELECT s FROM Servicio s WHERE s.precio BETWEEN :precioMin AND :precioMax ORDER BY s.precio")
    List<Servicio> findByRangoPrecio(@Param("precioMin") BigDecimal precioMin, 
                                      @Param("precioMax") BigDecimal precioMax);

    @Query("SELECT s FROM Servicio s WHERE s.activo = true ORDER BY s.nombre")
    List<Servicio> findAllActivosOrdenados();

    @Query("SELECT s.tipoServicio, COUNT(s) FROM Servicio s WHERE s.activo = true GROUP BY s.tipoServicio")
    List<Object[]> contarPorTipo();

    @Query("SELECT COUNT(s) FROM Servicio s WHERE s.activo = true")
    Long contarActivos();

    @Query("SELECT s FROM Servicio s WHERE s.requiereAutorizacion = true AND s.activo = true")
    List<Servicio> findQueRequierenAutorizacion();
}
