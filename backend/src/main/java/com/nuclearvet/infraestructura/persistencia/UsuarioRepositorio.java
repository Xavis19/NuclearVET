package com.nuclearvet.infraestructura.persistencia;

import com.nuclearvet.dominio.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para gestión de usuarios
 * RF1.1 - Gestión de usuarios
 */
@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByCorreoElectronico(String correoElectronico);

    Optional<Usuario> findByNumeroDocumento(String numeroDocumento);

    List<Usuario> findByActivo(Boolean activo);

    List<Usuario> findByRolNombre(String nombreRol);

    @Query("SELECT u FROM Usuario u WHERE u.nombreCompleto LIKE %:nombre% AND u.activo = true")
    List<Usuario> buscarPorNombre(String nombre);

    boolean existsByCorreoElectronico(String correoElectronico);

    boolean existsByNumeroDocumento(String numeroDocumento);

    default Optional<Usuario> findByEmail(String email) {
        return findByCorreoElectronico(email);
    }
}
