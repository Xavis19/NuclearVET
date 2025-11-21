package com.nuclearvet.dominio.entidades;

import com.nuclearvet.dominio.enumeraciones.TipoRecordatorio;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Entidad que representa un recordatorio programado
 */
@Entity
@Table(name = "recordatorios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recordatorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_recordatorio", nullable = false, length = 50)
    private TipoRecordatorio tipoRecordatorio;

    @Column(nullable = false, length = 255)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "fecha_programada", nullable = false)
    private LocalDateTime fechaProgramada;

    @Column(nullable = false)
    @Builder.Default
    private Boolean enviado = false;

    @Column(name = "fecha_envio")
    private LocalDateTime fechaEnvio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cita_id")
    private Cita cita;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        if (enviado == null) {
            enviado = false;
        }
    }

    /**
     * Marca el recordatorio como enviado
     */
    public void marcarComoEnviado() {
        this.enviado = true;
        this.fechaEnvio = LocalDateTime.now();
    }

    /**
     * Verifica si el recordatorio debe ser enviado
     */
    public boolean debeSerEnviado() {
        return !enviado && fechaProgramada.isBefore(LocalDateTime.now());
    }

    /**
     * Obtiene los días que faltan para el recordatorio
     */
    public long diasParaRecordatorio() {
        return ChronoUnit.DAYS.between(LocalDateTime.now(), fechaProgramada);
    }

    /**
     * Verifica si el recordatorio está pendiente
     */
    public boolean estaPendiente() {
        return !enviado && fechaProgramada.isAfter(LocalDateTime.now());
    }

    /**
     * Verifica si el recordatorio está vencido
     */
    public boolean estaVencido() {
        return !enviado && fechaProgramada.isBefore(LocalDateTime.now());
    }
}
