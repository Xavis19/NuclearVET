package com.nuclearvet.aplicacion.dto.administrativo;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfiguracionClinicaDTO {

    private Long id;

    @NotBlank(message = "El nombre de la clínica es obligatorio")
    @Size(max = 200, message = "El nombre no puede exceder 200 caracteres")
    private String nombreClinica;

    @NotBlank(message = "El NIT es obligatorio")
    @Size(max = 20, message = "El NIT no puede exceder 20 caracteres")
    private String nit;

    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 500, message = "La dirección no puede exceder 500 caracteres")
    private String direccion;

    @NotBlank(message = "La ciudad es obligatoria")
    private String ciudad;

    @NotBlank(message = "El departamento es obligatorio")
    private String departamento;

    @NotBlank(message = "El teléfono es obligatorio")
    private String telefono;

    private String celular;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe ser válido")
    private String email;

    private String sitioWeb;
    private String logo;

    @NotNull(message = "El horario de apertura es obligatorio")
    private LocalTime horarioApertura;

    @NotNull(message = "El horario de cierre es obligatorio")
    private LocalTime horarioCierre;

    private Boolean atencionDomingos;
    private Boolean atencionFestivos;
    private Integer duracionCitaDefecto;
    private Integer diasValidezRecordatorio;
    private String mensajeBienvenida;
    private String terminosCondiciones;
    private Boolean notificacionesEmail;
    private Boolean notificacionesSMS;
    private Boolean notificacionesWhatsApp;
}
