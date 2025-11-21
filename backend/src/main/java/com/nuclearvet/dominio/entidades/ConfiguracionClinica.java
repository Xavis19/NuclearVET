package com.nuclearvet.dominio.entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalTime;
import java.time.LocalDateTime;

/**
 * Entidad que representa la configuración general de la clínica
 */
@Entity
@Table(name = "configuracion_clinica")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfiguracionClinica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String nombreClinica;

    @Column(nullable = false, length = 20)
    private String nit;

    @Column(nullable = false, length = 500)
    private String direccion;

    @Column(nullable = false, length = 100)
    private String ciudad;

    @Column(nullable = false, length = 100)
    private String departamento;

    @Column(nullable = false, length = 20)
    private String telefono;

    @Column(length = 20)
    private String celular;

    @Column(nullable = false, length = 150)
    private String email;

    @Column(length = 200)
    private String sitioWeb;

    @Column(length = 500)
    private String logo;

    @Column(nullable = false)
    private LocalTime horarioApertura;

    @Column(nullable = false)
    private LocalTime horarioCierre;

    @Column(nullable = false)
    private Boolean atencionDomingos = false;

    @Column(nullable = false)
    private Boolean atencionFestivos = false;

    @Column(nullable = false)
    private Integer duracionCitaDefecto = 30; // minutos

    @Column(nullable = false)
    private Integer diasValidezRecordatorio = 7;

    @Column(columnDefinition = "TEXT")
    private String mensajeBienvenida;

    @Column(columnDefinition = "TEXT")
    private String terminosCondiciones;

    @Column(nullable = false)
    private Boolean notificacionesEmail = true;

    @Column(nullable = false)
    private Boolean notificacionesSMS = false;

    @Column(nullable = false)
    private Boolean notificacionesWhatsApp = false;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @UpdateTimestamp
    private LocalDateTime fechaActualizacion;

    /**
     * Obtiene el horario completo como String
     */
    public String getHorarioCompleto() {
        if (horarioApertura == null || horarioCierre == null) {
            return "No configurado";
        }
        return String.format("%s - %s", horarioApertura, horarioCierre);
    }

    /**
     * Verifica si la clínica está abierta en un día específico
     */
    public boolean estaAbierta(java.time.DayOfWeek dia) {
        if (dia == java.time.DayOfWeek.SUNDAY) {
            return atencionDomingos != null && atencionDomingos;
        }
        return true; // Asumimos que está abierta de lunes a sábado
    }
}
