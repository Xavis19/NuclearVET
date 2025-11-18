package com.nuclearvet.infraestructura.persistencia;

import com.nuclearvet.dominio.entidades.Paciente;
import com.nuclearvet.dominio.enums.Especie;
import com.nuclearvet.dominio.enums.EstadoPaciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para entidad Paciente
 */
@Repository
public interface PacienteRepositorio extends JpaRepository<Paciente, Long> {

    /**
     * Buscar paciente por código único
     */
    Optional<Paciente> findByCodigo(String codigo);

    /**
     * Verificar si existe un paciente con ese código
     */
    Boolean existsByCodigo(String codigo);

    /**
     * Buscar pacientes por propietario
     */
    List<Paciente> findByPropietarioId(Long propietarioId);

    /**
     * Buscar pacientes activos de un propietario
     */
    List<Paciente> findByPropietarioIdAndEstado(Long propietarioId, EstadoPaciente estado);

    /**
     * Buscar pacientes por especie
     */
    List<Paciente> findByEspecie(Especie especie);

    /**
     * Buscar pacientes por estado
     */
    List<Paciente> findByEstado(EstadoPaciente estado);

    /**
     * Buscar pacientes activos
     */
    @Query("SELECT p FROM Paciente p WHERE p.estado = 'ACTIVO'")
    List<Paciente> findPacientesActivos();

    /**
     * Buscar pacientes por veterinario asignado
     */
    List<Paciente> findByVeterinarioAsignadoId(Long veterinarioId);

    /**
     * Buscar pacientes por nombre (búsqueda parcial)
     */
    @Query("SELECT p FROM Paciente p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Paciente> buscarPorNombre(@Param("nombre") String nombre);

    /**
     * Buscar paciente por microchip
     */
    Optional<Paciente> findByMicrochip(String microchip);

    /**
     * Verificar si existe un paciente con ese microchip
     */
    Boolean existsByMicrochip(String microchip);

    /**
     * Buscar pacientes nacidos después de una fecha (cachorros)
     */
    List<Paciente> findByFechaNacimientoAfter(LocalDate fecha);

    /**
     * Buscar pacientes por raza
     */
    List<Paciente> findByRaza(String raza);

    /**
     * Contar pacientes activos
     */
    @Query("SELECT COUNT(p) FROM Paciente p WHERE p.estado = 'ACTIVO'")
    Long contarPacientesActivos();

    /**
     * Contar pacientes por especie
     */
    @Query("SELECT COUNT(p) FROM Paciente p WHERE p.especie = :especie AND p.estado = 'ACTIVO'")
    Long contarPorEspecie(@Param("especie") Especie especie);

    /**
     * Buscar pacientes en tratamiento u observación
     */
    @Query("SELECT p FROM Paciente p WHERE p.estado IN ('EN_TRATAMIENTO', 'EN_OBSERVACION')")
    List<Paciente> findPacientesEnAtencion();
}
