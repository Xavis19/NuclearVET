package com.nuclearvet.dominio.entidades;

import com.nuclearvet.dominio.enums.EstadoCita;
import com.nuclearvet.dominio.enums.Prioridad;
import com.nuclearvet.dominio.enums.TipoCita;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Cita
 * RF3.1 - Agendamiento de citas
 */
@Entity
@Table(name = "citas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_cita", nullable = false, unique = true, length = 20)
    private String numeroCita;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "veterinario_id", nullable = false)
    private Usuario veterinario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "propietario_id", nullable = false)
    private Propietario propietario;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cita", nullable = false, length = 50)
    private TipoCita tipoCita;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Column(name = "duracion_estimada")
    @Builder.Default
    private Integer duracionEstimada = 30; // minutos

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 50)
    @Builder.Default
    private EstadoCita estado = EstadoCita.PENDIENTE;

    @Enumerated(EnumType.STRING)
    @Column(name = "prioridad", nullable = false, length = 50)
    @Builder.Default
    private Prioridad prioridad = Prioridad.NORMAL;

    @Column(name = "motivo_consulta", nullable = false, columnDefinition = "TEXT")
    private String motivoConsulta;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "sintomas", columnDefinition = "TEXT")
    private String sintomas;

    @Column(name = "diagnostico", columnDefinition = "TEXT")
    private String diagnostico;

    @Column(name = "tratamiento", columnDefinition = "TEXT")
    private String tratamiento;

    @Column(name = "costo_consulta", precision = 10, scale = 2)
    private BigDecimal costoConsulta;

    @Column(name = "fecha_confirmacion")
    private LocalDateTime fechaConfirmacion;

    @Column(name = "fecha_atencion")
    private LocalDateTime fechaAtencion;

    @Column(name = "fecha_finalizacion")
    private LocalDateTime fechaFinalizacion;

    @Column(name = "motivo_cancelacion", columnDefinition = "TEXT")
    private String motivoCancelacion;

    @Column(name = "recordatorio_enviado")
    @Builder.Default
    private Boolean recordatorioEnviado = false;

    @OneToMany(mappedBy = "cita", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<HistorialCita> historial = new ArrayList<>();

    @CreatedDate
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @LastModifiedDate
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    /**
     * Verifica si la cita está en el pasado
     */
    public boolean esPasada() {
        return fechaHora.isBefore(LocalDateTime.now());
    }

    /**
     * Verifica si la cita es para hoy
     */
    public boolean esHoy() {
        LocalDateTime ahora = LocalDateTime.now();
        return fechaHora.toLocalDate().equals(ahora.toLocalDate());
    }

    /**
     * Verifica si la cita se puede cancelar
     */
    public boolean sePuedeCancelar() {
        return estado == EstadoCita.PENDIENTE || estado == EstadoCita.CONFIRMADA;
    }

    /**
     * Verifica si la cita se puede confirmar
     */
    public boolean sePuedeConfirmar() {
        return estado == EstadoCita.PENDIENTE && !esPasada();
    }

    /**
     * Calcular hora de fin estimada
     */
    public LocalDateTime calcularHoraFin() {
        return fechaHora.plusMinutes(duracionEstimada);
    }

    /**
     * Agregar registro al historial
     */
    public void agregarAlHistorial(HistorialCita registro) {
        historial.add(registro);
        registro.setCita(this);
    }
}
