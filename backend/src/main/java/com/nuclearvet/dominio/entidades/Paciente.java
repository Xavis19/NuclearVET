package com.nuclearvet.dominio.entidades;

import com.nuclearvet.dominio.enums.Especie;
import com.nuclearvet.dominio.enums.EstadoPaciente;
import com.nuclearvet.dominio.enums.Sexo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

/**
 * Entidad Paciente (mascota)
 * RF2.2 - Registro de pacientes
 */
@Entity
@Table(name = "pacientes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo", nullable = false, unique = true, length = 20)
    private String codigo;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(name = "especie", nullable = false, length = 50)
    private Especie especie;

    @Column(name = "raza", length = 100)
    private String raza;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Enumerated(EnumType.STRING)
    @Column(name = "sexo", nullable = false, length = 20)
    private Sexo sexo;

    @Column(name = "color", length = 100)
    private String color;

    @Column(name = "peso", precision = 6, scale = 2)
    private BigDecimal peso;

    @Column(name = "microchip", unique = true, length = 50)
    private String microchip;

    @Column(name = "esterilizado")
    @Builder.Default
    private Boolean esterilizado = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 50)
    @Builder.Default
    private EstadoPaciente estado = EstadoPaciente.ACTIVO;

    @Column(name = "alergias", columnDefinition = "TEXT")
    private String alergias;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "foto_url", length = 500)
    private String fotoUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "propietario_id", nullable = false)
    private Propietario propietario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "veterinario_asignado_id")
    private Usuario veterinarioAsignado;

    @CreatedDate
    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    @LastModifiedDate
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    /**
     * Calcular edad del paciente en años
     */
    public Integer calcularEdad() {
        if (fechaNacimiento == null) {
            return null;
        }
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }

    /**
     * Calcular edad en meses
     */
    public Integer calcularEdadEnMeses() {
        if (fechaNacimiento == null) {
            return null;
        }
        Period periodo = Period.between(fechaNacimiento, LocalDate.now());
        return (periodo.getYears() * 12) + periodo.getMonths();
    }

    /**
     * Verificar si es cachorro/cachorra (menor a 1 año)
     */
    public Boolean esCachorro() {
        Integer edad = calcularEdad();
        return edad != null && edad < 1;
    }

    /**
     * Verificar si es senior (mayor a 7 años)
     */
    public Boolean esSenior() {
        Integer edad = calcularEdad();
        return edad != null && edad >= 7;
    }

    /**
     * Obtener descripción completa del paciente
     */
    public String getDescripcionCompleta() {
        return String.format("%s - %s %s (%s)", 
            codigo, 
            nombre, 
            especie.getDescripcion(), 
            raza != null ? raza : "Sin raza");
    }
}
