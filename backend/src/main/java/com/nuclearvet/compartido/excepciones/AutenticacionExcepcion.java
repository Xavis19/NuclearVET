package com.nuclearvet.compartido.excepciones;

/**
 * Excepción lanzada cuando falla la autenticación
 */
public class AutenticacionExcepcion extends RuntimeException {
    
    public AutenticacionExcepcion(String mensaje) {
        super(mensaje);
    }
}
