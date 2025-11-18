package com.nuclearvet.infraestructura.persistencia;

import com.nuclearvet.dominio.entidades.Propietario;
import com.nuclearvet.dominio.enums.TipoIdentificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para entidad Propietario
 */
@Repository
public interface PropietarioRepositorio extends JpaRepository<Propietario, Long> {

    /**
     * Buscar propietario por número de identificación
     */
    Optional<Propietario> findByNumeroIdentificacion(String numeroIdentificacion);

    /**
     * Verificar si existe un propietario con ese número de identificación
     */
    Boolean existsByNumeroIdentificacion(String numeroIdentificacion);

    /**
     * Buscar propietarios activos
     */
    List<Propietario> findByActivoTrue();

    /**
     * Buscar propietarios por tipo de identificación
     */
    List<Propietario> findByTipoIdentificacion(TipoIdentificacion tipoIdentificacion);

    /**
     * Buscar propietarios por nombre o apellido (búsqueda parcial)
     */
    @Query("SELECT p FROM Propietario p WHERE " +
           "LOWER(p.nombres) LIKE LOWER(CONCAT('%', :termino, '%')) OR " +
           "LOWER(p.apellidos) LIKE LOWER(CONCAT('%', :termino, '%'))")
    List<Propietario> buscarPorNombre(@Param("termino") String termino);

    /**
     * Buscar propietarios por ciudad
     */
    List<Propietario> findByCiudad(String ciudad);

    /**
     * Buscar propietarios por correo electrónico
     */
    Optional<Propietario> findByCorreoElectronico(String correoElectronico);

    /**
     * Contar propietarios activos
     */
    @Query("SELECT COUNT(p) FROM Propietario p WHERE p.activo = true")
    Long contarPropietariosActivos();

    /**
     * Buscar propietarios con al menos un paciente
     */
    @Query("SELECT DISTINCT p FROM Propietario p " +
           "LEFT JOIN FETCH p.pacientes " +
           "WHERE SIZE(p.pacientes) > 0")
    List<Propietario> findPropietariosConPacientes();
}
