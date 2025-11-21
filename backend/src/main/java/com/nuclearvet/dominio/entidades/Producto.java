package com.nuclearvet.dominio.entidades;

import com.nuclearvet.dominio.enumeraciones.TipoProducto;
import com.nuclearvet.dominio.enumeraciones.UnidadMedida;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa un producto en el inventario
 */
@Entity
@Table(name = "productos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Producto {

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
    @Column(name = "tipo_producto", nullable = false, length = 50)
    private TipoProducto tipoProducto;

    @Enumerated(EnumType.STRING)
    @Column(name = "unidad_medida", nullable = false, length = 30)
    private UnidadMedida unidadMedida;

    @Column(name = "precio_compra", precision = 10, scale = 2)
    private BigDecimal precioCompra;

    @Column(name = "precio_venta", precision = 10, scale = 2)
    private BigDecimal precioVenta;

    @Column(name = "stock_minimo", nullable = false)
    private Integer stockMinimo = 0;

    @Column(name = "stock_actual", nullable = false)
    private Integer stockActual = 0;

    @Column(name = "ubicacion", length = 100)
    private String ubicacion;

    @Column(name = "requiere_refrigeracion")
    private Boolean requiereRefrigeracion = false;

    @Column(name = "requiere_receta")
    private Boolean requiereReceta = false;

    @Column(name = "activo", nullable = false)
    @Builder.Default
    private Boolean activo = true;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    // Relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private CategoriaProducto categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proveedor_id")
    private Proveedor proveedor;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Lote> lotes = new ArrayList<>();

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL)
    @Builder.Default
    private List<MovimientoInventario> movimientos = new ArrayList<>();

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL)
    @Builder.Default
    private List<AlertaInventario> alertas = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();
        if (activo == null) {
            activo = true;
        }
        if (stockMinimo == null) {
            stockMinimo = 0;
        }
        if (stockActual == null) {
            stockActual = 0;
        }
        if (requiereRefrigeracion == null) {
            requiereRefrigeracion = false;
        }
        if (requiereReceta == null) {
            requiereReceta = false;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }

    // Helper methods
    public boolean stockBajo() {
        return stockActual <= stockMinimo;
    }

    public void actualizarStock(int cantidad) {
        this.stockActual += cantidad;
    }

    public boolean tieneStock(int cantidad) {
        return stockActual >= cantidad;
    }
}
