package com.nuclearvet.infraestructura.configuracion;

import com.nuclearvet.infraestructura.seguridad.JwtFiltroAutenticacion;
import com.nuclearvet.infraestructura.seguridad.DetallesUsuarioServicioImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuración de seguridad de la aplicación
 * Maneja autenticación JWT y autorización por roles
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SeguridadConfiguracion {

    private final JwtFiltroAutenticacion jwtFiltroAutenticacion;
    private final DetallesUsuarioServicioImpl detallesUsuarioServicio;

    @Bean
    public SecurityFilterChain cadenaFiltrosSeguridad(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configure(http))
                .authorizeHttpRequests(auth -> auth
                        // Endpoints públicos
                        .requestMatchers(
                                "/api/autenticacion/**",
                                "/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        ).permitAll()
                        
                        // Endpoints de usuarios - según rol
                        .requestMatchers(HttpMethod.GET, "/api/usuarios/**")
                            .hasAnyAuthority("ADMINISTRADOR", "VETERINARIO", "ASISTENTE")
                        .requestMatchers(HttpMethod.POST, "/api/usuarios/**")
                            .hasAuthority("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PUT, "/api/usuarios/**")
                            .hasAnyAuthority("ADMINISTRADOR", "VETERINARIO")
                        .requestMatchers(HttpMethod.DELETE, "/api/usuarios/**")
                            .hasAuthority("ADMINISTRADOR")
                        
                        // Endpoints de pacientes
                        .requestMatchers("/api/pacientes/**")
                            .hasAnyAuthority("ADMINISTRADOR", "VETERINARIO", "ASISTENTE", "CLIENTE")
                        
                        // Endpoints de citas
                        .requestMatchers(HttpMethod.GET, "/api/citas/**")
                            .hasAnyAuthority("ADMINISTRADOR", "VETERINARIO", "ASISTENTE", "CLIENTE")
                        .requestMatchers(HttpMethod.POST, "/api/citas/**")
                            .hasAnyAuthority("ADMINISTRADOR", "VETERINARIO", "ASISTENTE", "CLIENTE")
                        .requestMatchers(HttpMethod.PUT, "/api/citas/**")
                            .hasAnyAuthority("ADMINISTRADOR", "VETERINARIO", "ASISTENTE")
                        .requestMatchers(HttpMethod.DELETE, "/api/citas/**")
                            .hasAnyAuthority("ADMINISTRADOR", "VETERINARIO")
                        
                        // Endpoints de inventario
                        .requestMatchers("/api/inventario/**")
                            .hasAnyAuthority("ADMINISTRADOR", "VETERINARIO", "ASISTENTE")
                        
                        // Endpoints administrativos
                        .requestMatchers("/api/administrativo/**")
                            .hasAnyAuthority("ADMINISTRADOR")
                        
                        // Cualquier otra petición requiere autenticación
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(proveedorAutenticacion())
                .addFilterBefore(jwtFiltroAutenticacion, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider proveedorAutenticacion() {
        DaoAuthenticationProvider proveedorAutenticacion = new DaoAuthenticationProvider();
        proveedorAutenticacion.setUserDetailsService(detallesUsuarioServicio);
        proveedorAutenticacion.setPasswordEncoder(codificadorContrasena());
        return proveedorAutenticacion;
    }

    @Bean
    public AuthenticationManager administradorAutenticacion(AuthenticationConfiguration configuracion) throws Exception {
        return configuracion.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder codificadorContrasena() {
        return new BCryptPasswordEncoder();
    }
}
