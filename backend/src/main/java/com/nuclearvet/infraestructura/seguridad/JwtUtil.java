package com.nuclearvet.infraestructura.seguridad;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Utilidad para gestión de tokens JWT
 * RF1.3 - Inicio de sesión seguro
 */
@Component
public class JwtUtil {

    @Value("${jwt.secreto}")
    private String secreto;

    @Value("${jwt.expiracion}")
    private long tiempoExpiracion;

    public String extraerCorreoElectronico(String token) {
        return extraerClaim(token, Claims::getSubject);
    }

    public Date extraerFechaExpiracion(String token) {
        return extraerClaim(token, Claims::getExpiration);
    }

    public <T> T extraerClaim(String token, Function<Claims, T> resolvedor) {
        final Claims claims = extraerTodosClaims(token);
        return resolvedor.apply(claims);
    }

    private Claims extraerTodosClaims(String token) {
        return Jwts
                .parser()
                .setSigningKey(obtenerClaveSecreto())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean esTokenExpirado(String token) {
        return extraerFechaExpiracion(token).before(new Date());
    }

    public String generarToken(UserDetails detallesUsuario) {
        Map<String, Object> claims = new HashMap<>();
        return crearToken(claims, detallesUsuario.getUsername());
    }

    public String generarToken(UserDetails detallesUsuario, Map<String, Object> claimsAdicionales) {
        return crearToken(claimsAdicionales, detallesUsuario.getUsername());
    }

    private String crearToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tiempoExpiracion))
                .signWith(obtenerClaveSecreto(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Boolean validarToken(String token, UserDetails detallesUsuario) {
        final String correo = extraerCorreoElectronico(token);
        return (correo.equals(detallesUsuario.getUsername()) && !esTokenExpirado(token));
    }

    private Key obtenerClaveSecreto() {
        byte[] bytesSecreto = Decoders.BASE64.decode(secreto);
        return Keys.hmacShaKeyFor(bytesSecreto);
    }
}
