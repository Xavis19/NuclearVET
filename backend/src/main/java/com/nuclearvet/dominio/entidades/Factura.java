package com.nuclearvet.dominio.entidades;

import com.nuclearvet.dominio.enumeraciones.EstadoFactura;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa una factura de servicios
 */
@Entity
@Table(name = "facturas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String numeroFactura;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "propietario_id", nullable = false)
    private Propietario propietario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    @Column(nullable = false)
    private LocalDate fechaEmision;

    @Column(nullable = false)
    private LocalDate fechaVencimiento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoFactura estado;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal subtotal = BigDecimal.ZERO;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal impuestos = BigDecimal.ZERO;

    @Column(precision = 12, scale = 2)
    private BigDecimal descuento = BigDecimal.ZERO;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal total = BigDecimal.ZERO;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal saldoPendiente = BigDecimal.ZERO;

    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ItemFactura> items = new ArrayList<>();

    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Pago> pagos = new ArrayList<>();

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_creador_id", nullable = false)
    private Usuario usuarioCreador;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @UpdateTimestamp
    private LocalDateTime fechaActualizacion;

    /**
     * Agrega un item a la factura
     */
    public void agregarItem(ItemFactura item) {
        items.add(item);
        item.setFactura(this);
        recalcularTotales();
    }

    /**
     * Recalcula los totales de la factura
     */
    public void recalcularTotales() {
        this.subtotal = items.stream()
                .map(ItemFactura::calcularSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.impuestos = items.stream()
                .map(ItemFactura::calcularImpuesto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (descuento == null) {
            descuento = BigDecimal.ZERO;
        }

        this.total = subtotal.add(impuestos).subtract(descuento);
        recalcularSaldoPendiente();
    }

    /**
     * Recalcula el saldo pendiente basado en los pagos
     */
    public void recalcularSaldoPendiente() {
        BigDecimal totalPagado = pagos.stream()
                .map(Pago::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.saldoPendiente = total.subtract(totalPagado);
    }

    /**
     * Verifica si la factura está completamente pagada
     */
    public boolean estaPagada() {
        return saldoPendiente != null && saldoPendiente.compareTo(BigDecimal.ZERO) <= 0;
    }

    /**
     * Verifica si la factura está vencida
     */
    public boolean estaVencida() {
        return fechaVencimiento != null && 
               LocalDate.now().isAfter(fechaVencimiento) && 
               !estaPagada();
    }

    /**
     * Actualiza el estado de la factura basado en pagos
     */
    public void actualizarEstado() {
        if (estaPagada()) {
            this.estado = EstadoFactura.PAGADA;
        } else if (saldoPendiente.compareTo(total) < 0) {
            this.estado = EstadoFactura.PAGADA_PARCIAL;
        } else if (estaVencida()) {
            this.estado = EstadoFactura.VENCIDA;
        } else {
            this.estado = EstadoFactura.PENDIENTE;
        }
    }
}
