package com.nuclearvet.dominio.entidades;

import com.nuclearvet.dominio.enums.TipoArchivo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Entidad ArchivoMedico
 * RF4.3 - Gestión de archivos médicos
 */
@Entity
@Table(name = "archivos_medicos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ArchivoMedico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "historia_clinica_id", nullable = false)
    private HistoriaClinica historiaClinica;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consulta_id")
    private Consulta consulta;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_archivo", nullable = false, length = 50)
    private TipoArchivo tipoArchivo;

    @Column(name = "nombre_archivo", nullable = false, length = 255)
    private String nombreArchivo;

    @Column(name = "ruta_archivo", nullable = false, length = 500)
    private String rutaArchivo;

    @Column(name = "tipo_contenido", length = 100)
    private String tipoContenido;

    @Column(name = "tamano_bytes")
    private Long tamanoBytes;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subido_por", nullable = false)
    private Usuario subidoPor;

    @CreatedDate
    @Column(name = "fecha_subida", nullable = false, updatable = false)
    private LocalDateTime fechaSubida;

    /**
     * Obtener tamaño en formato legible
     */
    public String getTamanoFormateado() {
        if (tamanoBytes == null) return "0 B";
        
        long bytes = tamanoBytes;
        if (bytes < 1024) return bytes + " B";
        
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String[] units = {"B", "KB", "MB", "GB"};
        return String.format("%.1f %s", bytes / Math.pow(1024, exp), units[exp]);
    }

    /**
     * Verificar si es una imagen
     */
    public boolean esImagen() {
        return tipoContenido != null && tipoContenido.startsWith("image/");
    }

    /**
     * Verificar si es PDF
     */
    public boolean esPdf() {
        return tipoContenido != null && tipoContenido.equals("application/pdf");
    }
}
