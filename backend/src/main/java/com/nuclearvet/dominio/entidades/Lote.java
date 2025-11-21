package com.nuclearvet.dominio.entidades;

import com.nuclearvet.dominio.enumeraciones.EstadoLote;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Entidad que representa un lote de productos con control de vencimiento
 */
@Entity
@Table(name = "lotes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String numeroLote;

    @Column(name = "fecha_ingreso", nullable = false)
    private LocalDate fechaIngreso;

    @Column(name = "fecha_fabricacion")
    private LocalDate fechaFabricacion;

    @Column(name = "fecha_vencimiento")
    private LocalDate fechaVencimiento;

    @Column(name = "cantidad_inicial", nullable = false)
    private Integer cantidadInicial;

    @Column(name = "cantidad_disponible", nullable = false)
    private Integer cantidadDisponible;

    @Column(name = "precio_compra_unitario", precision = 10, scale = 2)
    private BigDecimal precioCompraUnitario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Builder.Default
    private EstadoLote estado = EstadoLote.DISPONIBLE;

    @Column(name = "ubicacion_fisica", length = 100)
    private String ubicacionFisica;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    // Relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();
        cantidadDisponible = cantidadInicial;
        if (estado == null) {
            estado = EstadoLote.DISPONIBLE;
        }
        actualizarEstado();
    }

    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
        actualizarEstado();
    }

    // Helper methods
    public void actualizarEstado() {
        if (cantidadDisponible == 0) {
            this.estado = EstadoLote.AGOTADO;
        } else if (fechaVencimiento != null) {
            LocalDate hoy = LocalDate.now();
            if (hoy.isAfter(fechaVencimiento)) {
                this.estado = EstadoLote.VENCIDO;
            } else {
                long diasParaVencer = ChronoUnit.DAYS.between(hoy, fechaVencimiento);
                if (diasParaVencer <= 30) {
                    this.estado = EstadoLote.PROXIMO_VENCER;
                } else if (!estado.equals(EstadoLote.BLOQUEADO)) {
                    this.estado = EstadoLote.DISPONIBLE;
                }
            }
        }
    }

    public boolean estaVencido() {
        return fechaVencimiento != null && LocalDate.now().isAfter(fechaVencimiento);
    }

    public boolean proximoVencer(int dias) {
        if (fechaVencimiento == null) {
            return false;
        }
        long diasParaVencer = ChronoUnit.DAYS.between(LocalDate.now(), fechaVencimiento);
        return diasParaVencer > 0 && diasParaVencer <= dias;
    }

    public long diasParaVencer() {
        if (fechaVencimiento == null) {
            return Long.MAX_VALUE;
        }
        return ChronoUnit.DAYS.between(LocalDate.now(), fechaVencimiento);
    }

    public void descontarCantidad(int cantidad) {
        this.cantidadDisponible -= cantidad;
        actualizarEstado();
    }

    public void agregarCantidad(int cantidad) {
        this.cantidadDisponible += cantidad;
        actualizarEstado();
    }

    public boolean tieneDisponible(int cantidad) {
        return cantidadDisponible >= cantidad && 
               !estado.equals(EstadoLote.VENCIDO) && 
               !estado.equals(EstadoLote.BLOQUEADO);
    }
}
