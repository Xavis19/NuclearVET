package com.nuclearvet.infraestructura.persistencia;

import com.nuclearvet.dominio.entidades.CategoriaProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para gestión de categorías de productos
 */
@Repository
public interface CategoriaProductoRepositorio extends JpaRepository<CategoriaProducto, Long> {

    // Búsquedas básicas
    Optional<CategoriaProducto> findByNombre(String nombre);
    
    List<CategoriaProducto> findByActivoTrue();
    
    boolean existsByNombre(String nombre);
    
    boolean existsByNombreAndIdNot(String nombre, Long id);

    // Búsquedas avanzadas
    @Query("SELECT c FROM CategoriaProducto c WHERE LOWER(c.nombre) LIKE LOWER(CONCAT('%', :termino, '%'))")
    List<CategoriaProducto> buscarPorNombre(@Param("termino") String termino);

    @Query("SELECT c FROM CategoriaProducto c LEFT JOIN FETCH c.productos WHERE c.id = :id")
    Optional<CategoriaProducto> findByIdConProductos(@Param("id") Long id);

    @Query("SELECT c FROM CategoriaProducto c WHERE c.activo = true ORDER BY c.nombre")
    List<CategoriaProducto> findAllActivasOrdenadas();

    @Query("SELECT c FROM CategoriaProducto c WHERE SIZE(c.productos) > 0")
    List<CategoriaProducto> findCategoriasConProductos();

    @Query("SELECT COUNT(p) FROM Producto p WHERE p.categoria.id = :categoriaId AND p.activo = true")
    Long contarProductosActivos(@Param("categoriaId") Long categoriaId);
}
