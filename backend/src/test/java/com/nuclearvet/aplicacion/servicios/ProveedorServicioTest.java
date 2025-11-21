package com.nuclearvet.aplicacion.servicios;

import com.nuclearvet.aplicacion.mapeadores.ProveedorMapeador;
import com.nuclearvet.infraestructura.persistencia.ProveedorRepositorio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProveedorServicioTest {

    @Mock
    private ProveedorRepositorio proveedorRepositorio;

    @Mock
    private ProveedorMapeador proveedorMapeador;

    @InjectMocks
    private ProveedorServicio proveedorServicio;

    @Test
    void listarTodos_DeberiaInvocarRepositorio() {
        when(proveedorRepositorio.findAllActivosOrdenados()).thenReturn(Arrays.asList());
        proveedorServicio.listarTodos();
        verify(proveedorRepositorio, times(1)).findAllActivosOrdenados();
    }

    @Test
    void listarActivos_DeberiaInvocarRepositorio() {
        when(proveedorRepositorio.findByActivoTrue()).thenReturn(Arrays.asList());
        proveedorServicio.listarActivos();
        verify(proveedorRepositorio, times(1)).findByActivoTrue();
    }

    @Test
    void buscarPorNombre_DeberiaInvocarRepositorio() {
        String termino = "Proveedor A";
        when(proveedorRepositorio.buscarPorNombreONit(termino)).thenReturn(Arrays.asList());
        proveedorServicio.buscarPorNombre(termino);
        verify(proveedorRepositorio, times(1)).buscarPorNombreONit(termino);
    }
}
