package com.nuclearvet.infraestructura.persistencia;

import com.nuclearvet.dominio.entidades.HistoriaClinica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para gestión de historias clínicas
 */
@Repository
public interface HistoriaClinicaRepositorio extends JpaRepository<HistoriaClinica, Long> {

    /**
     * Buscar historia clínica por número
     */
    Optional<HistoriaClinica> findByNumeroHistoria(String numeroHistoria);

    /**
     * Verificar si existe historia por número
     */
    boolean existsByNumeroHistoria(String numeroHistoria);

    /**
     * Buscar historia clínica por paciente
     */
    Optional<HistoriaClinica> findByPacienteId(Long pacienteId);

    /**
     * Buscar historia clínica por paciente con consultas
     */
    @Query("SELECT h FROM HistoriaClinica h LEFT JOIN FETCH h.consultas WHERE h.paciente.id = :pacienteId")
    Optional<HistoriaClinica> findByPacienteIdWithConsultas(@Param("pacienteId") Long pacienteId);

    /**
     * Buscar historia clínica por paciente con archivos
     */
    @Query("SELECT h FROM HistoriaClinica h LEFT JOIN FETCH h.archivosMedicos WHERE h.paciente.id = :pacienteId")
    Optional<HistoriaClinica> findByPacienteIdWithArchivos(@Param("pacienteId") Long pacienteId);

    /**
     * Contar historias clínicas
     */
    @Query("SELECT COUNT(h) FROM HistoriaClinica h")
    Long contarTotal();

    /**
     * Buscar historias con alergias conocidas
     */
    @Query("SELECT h FROM HistoriaClinica h WHERE h.alergiasConocidas IS NOT NULL AND h.alergiasConocidas != ''")
    java.util.List<HistoriaClinica> findHistoriasConAlergias();

    /**
     * Buscar historias con enfermedades crónicas
     */
    @Query("SELECT h FROM HistoriaClinica h WHERE h.enfermedadesCronicas IS NOT NULL AND h.enfermedadesCronicas != ''")
    java.util.List<HistoriaClinica> findHistoriasConEnfermedadesCronicas();
}
