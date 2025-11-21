package com.nuclearvet.dominio.entidades;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Entidad que representa una alerta de inventario (stock bajo, próximo a vencer, etc.)
 */
@Entity
@Table(name = "alertas_inventario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlertaInventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String tipo; // STOCK_BAJO, PROXIMO_VENCER, VENCIDO, AGOTADO

    @Column(nullable = false, length = 500)
    private String mensaje;

    @Column(nullable = false, length = 20)
    private String prioridad; // ALTA, MEDIA, BAJA

    @Column(nullable = false)
    @Builder.Default
    private Boolean leida = false;

    @Column(name = "fecha_alerta", nullable = false)
    private LocalDateTime fechaAlerta;

    @Column(name = "fecha_leida")
    private LocalDateTime fechaLeida;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    // Relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lote_id")
    private Lote lote;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        if (fechaAlerta == null) {
            fechaAlerta = LocalDateTime.now();
        }
        if (leida == null) {
            leida = false;
        }
    }

    // Helper methods
    public void marcarComoLeida() {
        this.leida = true;
        this.fechaLeida = LocalDateTime.now();
    }

    public boolean esAlta() {
        return "ALTA".equals(prioridad);
    }

    public boolean noLeida() {
        return !leida;
    }
}
