package com.nuclearvet.dominio.entidades;

import com.nuclearvet.dominio.enumeraciones.PrioridadNotificacion;
import com.nuclearvet.dominio.enumeraciones.TipoNotificacion;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entidad que representa una notificación interna del sistema
 */
@Entity
@Table(name = "notificaciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_destinatario_id", nullable = false)
    private Usuario usuarioDestinatario;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_notificacion", nullable = false, length = 50)
    private TipoNotificacion tipoNotificacion;

    @Column(nullable = false, length = 255)
    private String titulo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String mensaje;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_prioridad", length = 20)
    @Builder.Default
    private PrioridadNotificacion nivelPrioridad = PrioridadNotificacion.NORMAL;

    @Column(nullable = false)
    @Builder.Default
    private Boolean leida = false;

    @Column(name = "fecha_leida")
    private LocalDateTime fechaLeida;

    @Column(length = 500)
    private String enlace;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        if (leida == null) {
            leida = false;
        }
        if (nivelPrioridad == null) {
            nivelPrioridad = PrioridadNotificacion.NORMAL;
        }
    }

    /**
     * Marca la notificación como leída
     */
    public void marcarComoLeida() {
        this.leida = true;
        this.fechaLeida = LocalDateTime.now();
    }

    /**
     * Marca la notificación como no leída
     */
    public void marcarComoNoLeida() {
        this.leida = false;
        this.fechaLeida = null;
    }

    /**
     * Verifica si la notificación es urgente
     */
    public boolean esUrgente() {
        return nivelPrioridad == PrioridadNotificacion.URGENTE || 
               nivelPrioridad == PrioridadNotificacion.ALTA;
    }
}
