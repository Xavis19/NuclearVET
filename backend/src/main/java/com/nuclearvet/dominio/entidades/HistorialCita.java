package com.nuclearvet.dominio.entidades;

import com.nuclearvet.dominio.enums.EstadoCita;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Entidad HistorialCita
 * Registro de cambios de estado de una cita
 */
@Entity
@Table(name = "historial_citas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class HistorialCita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cita_id", nullable = false)
    private Cita cita;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_anterior", length = 50)
    private EstadoCita estadoAnterior;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_nuevo", nullable = false, length = 50)
    private EstadoCita estadoNuevo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "comentario", columnDefinition = "TEXT")
    private String comentario;

    @CreatedDate
    @Column(name = "fecha_cambio", nullable = false, updatable = false)
    private LocalDateTime fechaCambio;

    /**
     * Obtener descripción del cambio
     */
    public String getDescripcionCambio() {
        if (estadoAnterior == null) {
            return "Cita creada con estado: " + estadoNuevo.getDescripcion();
        }
        return String.format("Estado cambiado de %s a %s",
                estadoAnterior.getDescripcion(),
                estadoNuevo.getDescripcion());
    }
}
