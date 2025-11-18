package com.nuclearvet.aplicacion.servicios;

import com.nuclearvet.aplicacion.dtos.LoginDTO;
import com.nuclearvet.aplicacion.dtos.RecuperarContrasenaDTO;
import com.nuclearvet.aplicacion.dtos.TokenDTO;
import com.nuclearvet.compartido.excepciones.AutenticacionExcepcion;
import com.nuclearvet.compartido.excepciones.RecursoNoEncontradoExcepcion;
import com.nuclearvet.dominio.entidades.Usuario;
import com.nuclearvet.dominio.enums.TipoAccion;
import com.nuclearvet.infraestructura.persistencia.UsuarioRepositorio;
import com.nuclearvet.infraestructura.seguridad.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

/**
 * Servicio para autenticación y seguridad
 * RF1.3 - Inicio de sesión seguro
 * RF1.4 - Recuperación de acceso
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AutenticacionServicio {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UsuarioRepositorio usuarioRepositorio;
    private final PasswordEncoder passwordEncoder;
    private final RegistroActividadServicio registroActividadServicio;
    private final JavaMailSender mailSender;

    @Value("${jwt.expiracion}")
    private long jwtExpiracion;

    @Value("${spring.mail.username:noreply@nuclearvet.com}")
    private String emailFrom;

    /**
     * Iniciar sesión y generar token JWT
     * RF1.3 - Inicio de sesión seguro
     */
    @Transactional
    public TokenDTO iniciarSesion(LoginDTO loginDTO, HttpServletRequest request) {
        try {
            // Autenticar usuario
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getCorreoElectronico(),
                            loginDTO.getContrasena()
                    )
            );

            // Obtener detalles del usuario
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Buscar usuario en la base de datos
            Usuario usuario = usuarioRepositorio.findByCorreoElectronico(userDetails.getUsername())
                    .orElseThrow(() -> new AutenticacionExcepcion("Usuario no encontrado"));

            // Verificar que el usuario esté activo
            if (!usuario.getActivo()) {
                throw new AutenticacionExcepcion("El usuario está desactivado");
            }

            // Generar token JWT
            String token = jwtUtil.generarToken(userDetails);

            // Registrar actividad
            registroActividadServicio.registrarActividad(
                    usuario.getId(),
                    TipoAccion.INICIO_SESION,
                    "Inicio de sesión exitoso",
                    request
            );

            log.info("Inicio de sesión exitoso para: {}", usuario.getCorreoElectronico());

            // Crear respuesta con token
            return TokenDTO.builder()
                    .token(token)
                    .tipo("Bearer")
                    .usuarioId(usuario.getId())
                    .nombreCompleto(usuario.getNombreCompleto())
                    .correoElectronico(usuario.getCorreoElectronico())
                    .rol(usuario.getRol().getNombre())
                    .permisos(Collections.singletonList(usuario.getRol().getNombre()))
                    .expiracionMs(jwtExpiracion)
                    .build();

        } catch (Exception e) {
            log.error("Error en inicio de sesión: {}", e.getMessage());
            throw new AutenticacionExcepcion("Credenciales inválidas");
        }
    }

    /**
     * Solicitar recuperación de contraseña
     * RF1.4 - Recuperación de acceso
     */
    @Transactional
    public void solicitarRecuperacionContrasena(RecuperarContrasenaDTO dto, HttpServletRequest request) {
        Usuario usuario = usuarioRepositorio.findByCorreoElectronico(dto.getCorreoElectronico())
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion(
                        "Usuario", "correo electrónico", dto.getCorreoElectronico()));

        // Generar token de recuperación
        String tokenRecuperacion = UUID.randomUUID().toString();
        usuario.setTokenRecuperacion(tokenRecuperacion);
        usuario.setFechaExpiracionToken(LocalDateTime.now().plusHours(24)); // Válido por 24 horas

        usuarioRepositorio.save(usuario);

        // Enviar correo con token
        enviarCorreoRecuperacion(usuario.getCorreoElectronico(), tokenRecuperacion);

        // Registrar actividad
        registroActividadServicio.registrarActividad(
                usuario.getId(),
                TipoAccion.RECUPERAR_CONTRASENA,
                "Solicitud de recuperación de contraseña",
                request
        );

        log.info("Solicitud de recuperación de contraseña para: {}", usuario.getCorreoElectronico());
    }

    /**
     * Restablecer contraseña con token
     * RF1.4 - Recuperación de acceso
     */
    @Transactional
    public void restablecerContrasena(String token, String nuevaContrasena, HttpServletRequest request) {
        Usuario usuario = usuarioRepositorio.findAll().stream()
                .filter(u -> token.equals(u.getTokenRecuperacion()))
                .findFirst()
                .orElseThrow(() -> new AutenticacionExcepcion("Token de recuperación inválido"));

        // Verificar que el token no haya expirado
        if (usuario.getFechaExpiracionToken() == null ||
                LocalDateTime.now().isAfter(usuario.getFechaExpiracionToken())) {
            throw new AutenticacionExcepcion("El token de recuperación ha expirado");
        }

        // Actualizar contraseña
        usuario.setContrasena(passwordEncoder.encode(nuevaContrasena));
        usuario.setTokenRecuperacion(null);
        usuario.setFechaExpiracionToken(null);

        usuarioRepositorio.save(usuario);

        // Registrar actividad
        registroActividadServicio.registrarActividad(
                usuario.getId(),
                TipoAccion.RECUPERAR_CONTRASENA,
                "Contraseña restablecida exitosamente",
                request
        );

        log.info("Contraseña restablecida para: {}", usuario.getCorreoElectronico());
    }

    /**
     * Validar token JWT
     */
    public boolean validarToken(String token) {
        try {
            String correoElectronico = jwtUtil.extraerCorreoElectronico(token);
            Usuario usuario = usuarioRepositorio.findByCorreoElectronico(correoElectronico)
                    .orElse(null);
            return usuario != null && usuario.getActivo();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Cerrar sesión (invalida el token en el cliente)
     */
    @Transactional
    public void cerrarSesion(String correoElectronico, HttpServletRequest request) {
        Usuario usuario = usuarioRepositorio.findByCorreoElectronico(correoElectronico)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Usuario", "correo", correoElectronico));

        // Registrar actividad
        registroActividadServicio.registrarActividad(
                usuario.getId(),
                TipoAccion.CIERRE_SESION,
                "Cierre de sesión",
                request
        );

        log.info("Cierre de sesión para: {}", correoElectronico);
    }

    /**
     * Enviar correo de recuperación de contraseña
     */
    private void enviarCorreoRecuperacion(String destinatario, String token) {
        try {
            SimpleMailMessage mensaje = new SimpleMailMessage();
            mensaje.setFrom(emailFrom);
            mensaje.setTo(destinatario);
            mensaje.setSubject("Recuperación de Contraseña - NuclearVET");
            mensaje.setText(String.format(
                    "Hola,\n\n" +
                            "Has solicitado recuperar tu contraseña en NuclearVET.\n\n" +
                            "Tu token de recuperación es: %s\n\n" +
                            "Este token es válido por 24 horas.\n\n" +
                            "Si no solicitaste esta recuperación, ignora este correo.\n\n" +
                            "Saludos,\n" +
                            "Equipo NuclearVET",
                    token
            ));

            mailSender.send(mensaje);
            log.info("Correo de recuperación enviado a: {}", destinatario);
        } catch (Exception e) {
            log.error("Error al enviar correo de recuperación: {}", e.getMessage());
            // No lanzamos excepción para no bloquear el proceso
        }
    }
}
