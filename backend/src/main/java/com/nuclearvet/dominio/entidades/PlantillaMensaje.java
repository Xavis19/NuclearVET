package com.nuclearvet.dominio.entidades;

import com.nuclearvet.dominio.enumeraciones.TipoPlantilla;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entidad que representa una plantilla de mensaje configurable
 */
@Entity
@Table(name = "plantillas_mensajes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlantillaMensaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_plantilla", nullable = false, length = 50)
    private TipoPlantilla tipoPlantilla;

    @Column(length = 255)
    private String asunto;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String contenido;

    @Column(name = "variables_disponibles", columnDefinition = "TEXT")
    private String variablesDisponibles;

    @Column(nullable = false)
    private Boolean activo = true;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();
        if (activo == null) {
            activo = true;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }

    /**
     * Renderiza la plantilla reemplazando variables
     * @param variables Mapa de variables key-value
     * @return Contenido renderizado
     */
    public String renderizar(java.util.Map<String, String> variables) {
        String resultado = contenido;
        for (java.util.Map.Entry<String, String> entry : variables.entrySet()) {
            String placeholder = "{{" + entry.getKey() + "}}";
            resultado = resultado.replace(placeholder, entry.getValue() != null ? entry.getValue() : "");
        }
        return resultado;
    }

    /**
     * Renderiza el asunto reemplazando variables
     */
    public String renderizarAsunto(java.util.Map<String, String> variables) {
        if (asunto == null) return "";
        String resultado = asunto;
        for (java.util.Map.Entry<String, String> entry : variables.entrySet()) {
            String placeholder = "{{" + entry.getKey() + "}}";
            resultado = resultado.replace(placeholder, entry.getValue() != null ? entry.getValue() : "");
        }
        return resultado;
    }
}
