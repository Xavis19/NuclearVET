package com.nuclearvet.infraestructura.persistencia;

import com.nuclearvet.dominio.entidades.ArchivoMedico;
import com.nuclearvet.dominio.enums.TipoArchivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio para gestión de archivos médicos
 */
@Repository
public interface ArchivoMedicoRepositorio extends JpaRepository<ArchivoMedico, Long> {

    /**
     * Buscar archivos por historia clínica
     */
    List<ArchivoMedico> findByHistoriaClinicaIdOrderByFechaSubidaDesc(Long historiaClinicaId);

    /**
     * Buscar archivos por consulta
     */
    List<ArchivoMedico> findByConsultaIdOrderByFechaSubidaDesc(Long consultaId);

    /**
     * Buscar archivos por tipo
     */
    List<ArchivoMedico> findByTipoArchivoOrderByFechaSubidaDesc(TipoArchivo tipoArchivo);

    /**
     * Buscar archivos subidos por un usuario
     */
    List<ArchivoMedico> findBySubidoPorIdOrderByFechaSubidaDesc(Long usuarioId);

    /**
     * Buscar archivos por rango de fechas
     */
    List<ArchivoMedico> findByFechaSubidaBetweenOrderByFechaSubidaDesc(
            LocalDateTime inicio, 
            LocalDateTime fin
    );

    /**
     * Buscar radiografías de un paciente
     */
    @Query("SELECT a FROM ArchivoMedico a WHERE a.historiaClinica.paciente.id = :pacienteId " +
           "AND a.tipoArchivo = 'RADIOGRAFIA' " +
           "ORDER BY a.fechaSubida DESC")
    List<ArchivoMedico> findRadiografiasPorPaciente(@Param("pacienteId") Long pacienteId);

    /**
     * Buscar exámenes de laboratorio de un paciente
     */
    @Query("SELECT a FROM ArchivoMedico a WHERE a.historiaClinica.paciente.id = :pacienteId " +
           "AND a.tipoArchivo IN ('EXAMEN_SANGRE', 'EXAMEN_ORINA') " +
           "ORDER BY a.fechaSubida DESC")
    List<ArchivoMedico> findExamenesLaboratorioPorPaciente(@Param("pacienteId") Long pacienteId);

    /**
     * Contar archivos por historia clínica
     */
    Long countByHistoriaClinicaId(Long historiaClinicaId);

    /**
     * Contar archivos por tipo
     */
    Long countByTipoArchivo(TipoArchivo tipoArchivo);

    /**
     * Buscar archivos recientes (últimos 30 días)
     */
    @Query("SELECT a FROM ArchivoMedico a WHERE a.fechaSubida >= :fecha " +
           "ORDER BY a.fechaSubida DESC")
    List<ArchivoMedico> findArchivosRecientes(@Param("fecha") LocalDateTime fecha);

    /**
     * Buscar archivos por nombre
     */
    List<ArchivoMedico> findByNombreArchivoContainingIgnoreCaseOrderByFechaSubidaDesc(String nombreArchivo);
}
