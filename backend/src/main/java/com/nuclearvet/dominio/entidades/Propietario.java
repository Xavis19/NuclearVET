package com.nuclearvet.dominio.entidades;

import com.nuclearvet.dominio.enums.TipoIdentificacion;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Propietario (dueño de mascotas)
 * RF2.1 - Registro de propietarios
 */
@Entity
@Table(name = "propietarios")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Propietario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_identificacion", nullable = false, length = 50)
    private TipoIdentificacion tipoIdentificacion;

    @Column(name = "numero_identificacion", nullable = false, unique = true, length = 50)
    private String numeroIdentificacion;

    @Column(name = "nombres", nullable = false, length = 100)
    private String nombres;

    @Column(name = "apellidos", nullable = false, length = 100)
    private String apellidos;

    @Column(name = "telefono_principal", nullable = false, length = 20)
    private String telefonoPrincipal;

    @Column(name = "telefono_secundario", length = 20)
    private String telefonoSecundario;

    @Column(name = "correo_electronico", length = 100)
    private String correoElectronico;

    @Column(name = "direccion", length = 200)
    private String direccion;

    @Column(name = "ciudad", length = 100)
    private String ciudad;

    @Column(name = "departamento", length = 100)
    private String departamento;

    @Column(name = "codigo_postal", length = 10)
    private String codigoPostal;

    @Column(name = "ocupacion", length = 100)
    private String ocupacion;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "activo", nullable = false)
    @Builder.Default
    private Boolean activo = true;

    @OneToMany(mappedBy = "propietario", cascade = CascadeType.ALL, orphanRemoval = false)
    @Builder.Default
    private List<Paciente> pacientes = new ArrayList<>();

    @CreatedDate
    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    @LastModifiedDate
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    /**
     * Obtener nombre completo del propietario
     */
    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }

    /**
     * Agregar un paciente a la lista
     */
    public void agregarPaciente(Paciente paciente) {
        pacientes.add(paciente);
        paciente.setPropietario(this);
    }

    /**
     * Remover un paciente de la lista
     */
    public void removerPaciente(Paciente paciente) {
        pacientes.remove(paciente);
        paciente.setPropietario(null);
    }
}
