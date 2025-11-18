package com.nuclearvet.dominio.entidades;

import com.nuclearvet.dominio.enums.TipoAccion;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidad para registro de auditoría del sistema
 * RF1.5 - Registro de actividad relevante
 */
@Entity
@Table(name = "registro_actividad")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistroActividad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_accion", nullable = false, length = 50)
    private TipoAccion tipoAccion;

    @Column(name = "descripcion", length = 500)
    private String descripcion;

    @Column(name = "ip_origen", length = 45)
    private String ipOrigen;

    @Column(name = "fecha_hora")
    private LocalDateTime fechaHora;

    @PrePersist
    protected void alCrear() {
        fechaHora = LocalDateTime.now();
    }
}
