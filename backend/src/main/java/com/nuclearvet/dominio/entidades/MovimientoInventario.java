package com.nuclearvet.dominio.entidades;

import com.nuclearvet.dominio.enumeraciones.TipoMovimiento;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad que representa un movimiento de inventario (entradas/salidas)
 */
@Entity
@Table(name = "movimientos_inventario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovimientoInventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String numeroMovimiento;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_movimiento", nullable = false, length = 50)
    private TipoMovimiento tipoMovimiento;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unitario", precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    @Column(name = "valor_total", precision = 12, scale = 2)
    private BigDecimal valorTotal;

    @Column(name = "stock_anterior", nullable = false)
    private Integer stockAnterior;

    @Column(name = "stock_nuevo", nullable = false)
    private Integer stockNuevo;

    @Column(name = "numero_documento", length = 50)
    private String numeroDocumento;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "fecha_movimiento", nullable = false)
    private LocalDateTime fechaMovimiento;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    // Relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lote_id")
    private Lote lote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        if (fechaMovimiento == null) {
            fechaMovimiento = LocalDateTime.now();
        }
        calcularValorTotal();
    }

    @PreUpdate
    protected void onUpdate() {
        calcularValorTotal();
    }

    // Helper methods
    private void calcularValorTotal() {
        if (precioUnitario != null && cantidad != null) {
            this.valorTotal = precioUnitario.multiply(BigDecimal.valueOf(cantidad));
        }
    }

    public boolean esEntrada() {
        return tipoMovimiento == TipoMovimiento.ENTRADA_COMPRA ||
               tipoMovimiento == TipoMovimiento.ENTRADA_DEVOLUCION ||
               tipoMovimiento == TipoMovimiento.ENTRADA_AJUSTE;
    }

    public boolean esSalida() {
        return tipoMovimiento == TipoMovimiento.SALIDA_VENTA ||
               tipoMovimiento == TipoMovimiento.SALIDA_CONSUMO ||
               tipoMovimiento == TipoMovimiento.SALIDA_BAJA ||
               tipoMovimiento == TipoMovimiento.SALIDA_AJUSTE;
    }
}
