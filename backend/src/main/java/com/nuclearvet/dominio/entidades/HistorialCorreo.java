package com.nuclearvet.dominio.entidades;

import com.nuclearvet.dominio.enumeraciones.EstadoCorreo;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entidad que representa el historial de correos enviados
 */
@Entity
@Table(name = "historial_correos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistorialCorreo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "destinatario_email", nullable = false, length = 255)
    private String destinatarioEmail;

    @Column(nullable = false, length = 255)
    private String asunto;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String contenido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plantilla_id")
    private PlantillaMensaje plantilla;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    @Builder.Default
    private EstadoCorreo estado = EstadoCorreo.PENDIENTE;

    @Column(name = "error_mensaje", columnDefinition = "TEXT")
    private String errorMensaje;

    @Column(name = "fecha_envio")
    private LocalDateTime fechaEnvio;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        if (estado == null) {
            estado = EstadoCorreo.PENDIENTE;
        }
    }

    /**
     * Marca el correo como enviado
     */
    public void marcarComoEnviado() {
        this.estado = EstadoCorreo.ENVIADO;
        this.fechaEnvio = LocalDateTime.now();
        this.errorMensaje = null;
    }

    /**
     * Marca el correo con error
     */
    public void marcarComoError(String mensajeError) {
        this.estado = EstadoCorreo.ERROR;
        this.errorMensaje = mensajeError;
        this.fechaEnvio = LocalDateTime.now();
    }

    /**
     * Marca el correo como reintentando
     */
    public void marcarComoReintentando() {
        this.estado = EstadoCorreo.REINTENTANDO;
    }

    /**
     * Verifica si el correo fue enviado exitosamente
     */
    public boolean fueEnviado() {
        return estado == EstadoCorreo.ENVIADO;
    }

    /**
     * Verifica si el correo tiene error
     */
    public boolean tieneError() {
        return estado == EstadoCorreo.ERROR;
    }
}
