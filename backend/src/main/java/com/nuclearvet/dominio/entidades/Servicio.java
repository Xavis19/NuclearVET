package com.nuclearvet.dominio.entidades;

import com.nuclearvet.dominio.enumeraciones.TipoImpuesto;
import com.nuclearvet.dominio.enumeraciones.TipoServicio;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad que representa un servicio ofrecido por la clínica
 */
@Entity
@Table(name = "servicios")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String codigo;

    @Column(nullable = false, length = 200)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoServicio tipoServicio;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoImpuesto tipoImpuesto;

    @Column(nullable = false)
    private Boolean activo = true;

    @Column(length = 500)
    private String observaciones;

    @Column(nullable = false)
    private Integer duracionEstimada = 30; // minutos

    @Column(nullable = false)
    private Boolean requiereAutorizacion = false;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @UpdateTimestamp
    private LocalDateTime fechaActualizacion;

    /**
     * Calcula el valor del impuesto basado en el precio
     */
    public BigDecimal calcularImpuesto() {
        if (tipoImpuesto == null || precio == null) {
            return BigDecimal.ZERO;
        }
        return precio.multiply(BigDecimal.valueOf(tipoImpuesto.getPorcentaje()))
                .divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Calcula el precio total incluyendo impuestos
     */
    public BigDecimal calcularPrecioTotal() {
        if (precio == null) {
            return BigDecimal.ZERO;
        }
        return precio.add(calcularImpuesto());
    }

    /**
     * Verifica si el servicio está disponible
     */
    public boolean estaDisponible() {
        return activo != null && activo;
    }
}
