package com.nuclearvet.infraestructura.persistencia;

import com.nuclearvet.dominio.entidades.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para gestión de proveedores
 */
@Repository
public interface ProveedorRepositorio extends JpaRepository<Proveedor, Long> {

    // Búsquedas básicas
    Optional<Proveedor> findByNit(String nit);
    
    List<Proveedor> findByActivoTrue();
    
    boolean existsByNit(String nit);
    
    boolean existsByNitAndIdNot(String nit, Long id);

    // Búsquedas avanzadas
    @Query("SELECT p FROM Proveedor p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :termino, '%')) " +
           "OR LOWER(p.nit) LIKE LOWER(CONCAT('%', :termino, '%'))")
    List<Proveedor> buscarPorNombreONit(@Param("termino") String termino);

    @Query("SELECT p FROM Proveedor p WHERE p.activo = true ORDER BY p.nombre")
    List<Proveedor> findAllActivosOrdenados();

    @Query("SELECT p FROM Proveedor p LEFT JOIN FETCH p.productos WHERE p.id = :id")
    Optional<Proveedor> findByIdConProductos(@Param("id") Long id);

    @Query("SELECT p FROM Proveedor p WHERE SIZE(p.productos) > 0")
    List<Proveedor> findProveedoresConProductos();

    @Query("SELECT COUNT(pr) FROM Producto pr WHERE pr.proveedor.id = :proveedorId AND pr.activo = true")
    Long contarProductosActivos(@Param("proveedorId") Long proveedorId);

    @Query("SELECT p FROM Proveedor p WHERE LOWER(p.email) = LOWER(:email)")
    Optional<Proveedor> findByEmail(@Param("email") String email);
}
