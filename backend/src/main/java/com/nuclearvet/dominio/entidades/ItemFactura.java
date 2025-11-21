package com.nuclearvet.dominio.entidades;

import com.nuclearvet.dominio.enumeraciones.TipoImpuesto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Entidad que representa un item de una factura
 */
@Entity
@Table(name = "items_factura")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemFactura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "factura_id", nullable = false)
    private Factura factura;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "servicio_id")
    private Servicio servicio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id")
    private Producto producto;

    @Column(nullable = false, length = 200)
    private String descripcion;

    @Column(nullable = false)
    private Integer cantidad = 1;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precioUnitario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoImpuesto tipoImpuesto;

    @Column(precision = 12, scale = 2)
    private BigDecimal descuento = BigDecimal.ZERO;

    @Column(length = 500)
    private String observaciones;

    /**
     * Calcula el subtotal del item (cantidad * precio - descuento)
     */
    public BigDecimal calcularSubtotal() {
        if (cantidad == null || precioUnitario == null) {
            return BigDecimal.ZERO;
        }
        BigDecimal subtotal = precioUnitario.multiply(BigDecimal.valueOf(cantidad));
        if (descuento != null && descuento.compareTo(BigDecimal.ZERO) > 0) {
            subtotal = subtotal.subtract(descuento);
        }
        return subtotal;
    }

    /**
     * Calcula el impuesto del item
     */
    public BigDecimal calcularImpuesto() {
        if (tipoImpuesto == null) {
            return BigDecimal.ZERO;
        }
        BigDecimal subtotal = calcularSubtotal();
        return subtotal.multiply(BigDecimal.valueOf(tipoImpuesto.getPorcentaje()))
                .divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Calcula el total del item (subtotal + impuesto)
     */
    public BigDecimal calcularTotal() {
        return calcularSubtotal().add(calcularImpuesto());
    }
}
