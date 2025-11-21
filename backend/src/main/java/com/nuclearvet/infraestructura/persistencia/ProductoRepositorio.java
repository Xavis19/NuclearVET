package com.nuclearvet.infraestructura.persistencia;

import com.nuclearvet.dominio.entidades.Producto;
import com.nuclearvet.dominio.enumeraciones.TipoProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para gestión de productos del inventario
 */
@Repository
public interface ProductoRepositorio extends JpaRepository<Producto, Long> {

    // Búsquedas básicas
    Optional<Producto> findByCodigo(String codigo);
    
    List<Producto> findByActivoTrue();
    
    boolean existsByCodigo(String codigo);
    
    boolean existsByCodigoAndIdNot(String codigo, Long id);

    // Búsquedas por tipo y categoría
    List<Producto> findByTipoProducto(TipoProducto tipoProducto);
    
    List<Producto> findByCategoriaId(Long categoriaId);
    
    List<Producto> findByProveedorId(Long proveedorId);

    // Búsquedas avanzadas
    @Query("SELECT p FROM Producto p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :termino, '%')) " +
           "OR LOWER(p.codigo) LIKE LOWER(CONCAT('%', :termino, '%'))")
    List<Producto> buscarPorNombreOCodigo(@Param("termino") String termino);

    @Query("SELECT p FROM Producto p WHERE p.activo = true ORDER BY p.nombre")
    List<Producto> findAllActivosOrdenados();

    @Query("SELECT p FROM Producto p LEFT JOIN FETCH p.categoria WHERE p.id = :id")
    Optional<Producto> findByIdConCategoria(@Param("id") Long id);

    @Query("SELECT p FROM Producto p LEFT JOIN FETCH p.categoria LEFT JOIN FETCH p.proveedor WHERE p.id = :id")
    Optional<Producto> findByIdConRelaciones(@Param("id") Long id);

    // Alertas de stock
    @Query("SELECT p FROM Producto p WHERE p.stockActual <= p.stockMinimo AND p.activo = true")
    List<Producto> findProductosStockBajo();

    @Query("SELECT p FROM Producto p WHERE p.stockActual <= p.stockMinimo AND p.activo = true")
    List<Producto> findProductosConStockBajo();

    @Query("SELECT p FROM Producto p WHERE p.stockActual = 0 AND p.activo = true")
    List<Producto> findProductosAgotados();

    @Query("SELECT p FROM Producto p WHERE p.stockActual <= :cantidad AND p.activo = true")
    List<Producto> findProductosConStockMenorQue(@Param("cantidad") Integer cantidad);

    // Reportes
    @Query("SELECT p FROM Producto p WHERE p.tipoProducto = :tipo AND p.activo = true ORDER BY p.nombre")
    List<Producto> findByTipoProductoOrdenados(@Param("tipo") TipoProducto tipo);

    @Query("SELECT p FROM Producto p WHERE p.categoria.id = :categoriaId AND p.activo = true ORDER BY p.nombre")
    List<Producto> findByCategoriaIdOrdenados(@Param("categoriaId") Long categoriaId);

    @Query("SELECT p FROM Producto p WHERE p.requiereRefrigeracion = true AND p.activo = true")
    List<Producto> findProductosRefrigerados();

    @Query("SELECT p FROM Producto p WHERE p.requiereReceta = true AND p.activo = true")
    List<Producto> findProductosConReceta();

    // Estadísticas
    @Query("SELECT COUNT(p) FROM Producto p WHERE p.activo = true")
    Long contarProductosActivos();

    @Query("SELECT SUM(p.stockActual) FROM Producto p WHERE p.activo = true")
    Long sumarStockTotal();

    @Query("SELECT p.tipoProducto, COUNT(p) FROM Producto p WHERE p.activo = true GROUP BY p.tipoProducto")
    List<Object[]> contarPorTipo();
}
