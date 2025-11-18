package com.nuclearvet.dominio.entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad HistoriaClinica
 * RF4.1 - Gestión de historias clínicas
 */
@Entity
@Table(name = "historias_clinicas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class HistoriaClinica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_historia", nullable = false, unique = true, length = 20)
    private String numeroHistoria;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false, unique = true)
    private Paciente paciente;

    @Column(name = "alergias_conocidas", columnDefinition = "TEXT")
    private String alergiasConocidas;

    @Column(name = "enfermedades_cronicas", columnDefinition = "TEXT")
    private String enfermedadesCronicas;

    @Column(name = "cirugias_previas", columnDefinition = "TEXT")
    private String cirugiasPrevias;

    @Column(name = "medicamentos_actuales", columnDefinition = "TEXT")
    private String medicamentosActuales;

    @Column(name = "vacunas", columnDefinition = "TEXT")
    private String vacunas;

    @Column(name = "observaciones_generales", columnDefinition = "TEXT")
    private String observacionesGenerales;

    @OneToMany(mappedBy = "historiaClinica", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Consulta> consultas = new ArrayList<>();

    @OneToMany(mappedBy = "historiaClinica", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ArchivoMedico> archivosMedicos = new ArrayList<>();

    @CreatedDate
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @LastModifiedDate
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    /**
     * Agregar consulta a la historia
     */
    public void agregarConsulta(Consulta consulta) {
        consultas.add(consulta);
        consulta.setHistoriaClinica(this);
    }

    /**
     * Agregar archivo médico
     */
    public void agregarArchivo(ArchivoMedico archivo) {
        archivosMedicos.add(archivo);
        archivo.setHistoriaClinica(this);
    }

    /**
     * Obtener última consulta
     */
    public Consulta getUltimaConsulta() {
        if (consultas == null || consultas.isEmpty()) {
            return null;
        }
        return consultas.stream()
                .max((c1, c2) -> c1.getFechaConsulta().compareTo(c2.getFechaConsulta()))
                .orElse(null);
    }

    /**
     * Contar consultas
     */
    public int contarConsultas() {
        return consultas != null ? consultas.size() : 0;
    }
}
