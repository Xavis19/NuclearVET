package com.nuclearvet.infraestructura.persistencia;

import com.nuclearvet.dominio.entidades.PlantillaMensaje;
import com.nuclearvet.dominio.enumeraciones.TipoPlantilla;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para gestión de plantillas de mensajes
 */
@Repository
public interface PlantillaMensajeRepositorio extends JpaRepository<PlantillaMensaje, Long> {

    // Búsquedas básicas
    Optional<PlantillaMensaje> findByNombre(String nombre);
    
    List<PlantillaMensaje> findByActivoTrue();
    
    List<PlantillaMensaje> findByTipoPlantilla(TipoPlantilla tipoPlantilla);
    
    boolean existsByNombre(String nombre);
    
    boolean existsByNombreAndIdNot(String nombre, Long id);

    // Búsquedas avanzadas
    @Query("SELECT p FROM PlantillaMensaje p WHERE p.tipoPlantilla = :tipo AND p.activo = true")
    List<PlantillaMensaje> findByTipoPlantillaAndActivoTrue(@Param("tipo") TipoPlantilla tipo);

    @Query("SELECT p FROM PlantillaMensaje p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :termino, '%')) " +
           "OR LOWER(p.asunto) LIKE LOWER(CONCAT('%', :termino, '%'))")
    List<PlantillaMensaje> buscarPorNombreOAsunto(@Param("termino") String termino);

    @Query("SELECT p FROM PlantillaMensaje p WHERE p.activo = true ORDER BY p.nombre")
    List<PlantillaMensaje> findAllActivasOrdenadas();

    @Query("SELECT p FROM PlantillaMensaje p WHERE p.contenido LIKE %:variable%")
    List<PlantillaMensaje> findByVariableDisponible(@Param("variable") String variable);

    // Estadísticas
    @Query("SELECT p.tipoPlantilla, COUNT(p) FROM PlantillaMensaje p GROUP BY p.tipoPlantilla")
    List<Object[]> contarPorTipo();

    @Query("SELECT COUNT(p) FROM PlantillaMensaje p WHERE p.activo = true")
    Long contarActivas();
}
