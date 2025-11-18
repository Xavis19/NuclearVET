package com.nuclearvet.compartido.excepciones;

/**
 * Excepción lanzada cuando un recurso no es encontrado
 */
public class RecursoNoEncontradoExcepcion extends RuntimeException {
    
    public RecursoNoEncontradoExcepcion(String mensaje) {
        super(mensaje);
    }
    
    public RecursoNoEncontradoExcepcion(String recurso, String campo, Object valor) {
        super(String.format("%s no encontrado con %s: '%s'", recurso, campo, valor));
    }
}
