package com.nuclearvet.aplicacion.servicios;

import com.nuclearvet.aplicacion.mapeadores.CategoriaProductoMapeador;
import com.nuclearvet.infraestructura.persistencia.CategoriaProductoRepositorio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoriaProductoServicioTest {

    @Mock
    private CategoriaProductoRepositorio categoriaProductoRepositorio;

    @Mock
    private CategoriaProductoMapeador categoriaProductoMapeador;

    @InjectMocks
    private CategoriaProductoServicio categoriaProductoServicio;

    @Test
    void listarTodas_DeberiaInvocarRepositorio() {
        when(categoriaProductoRepositorio.findAllActivasOrdenadas()).thenReturn(Arrays.asList());
        categoriaProductoServicio.listarTodas();
        verify(categoriaProductoRepositorio, times(1)).findAllActivasOrdenadas();
    }

    @Test
    void listarActivas_DeberiaInvocarRepositorio() {
        when(categoriaProductoRepositorio.findByActivoTrue()).thenReturn(Arrays.asList());
        categoriaProductoServicio.listarActivas();
        verify(categoriaProductoRepositorio, times(1)).findByActivoTrue();
    }

    @Test
    void buscarPorNombre_DeberiaInvocarRepositorio() {
        String termino = "medicamento";
        when(categoriaProductoRepositorio.buscarPorNombre(termino)).thenReturn(Arrays.asList());
        categoriaProductoServicio.buscarPorNombre(termino);
        verify(categoriaProductoRepositorio, times(1)).buscarPorNombre(termino);
    }
}
