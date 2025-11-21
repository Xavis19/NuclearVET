package com.nuclearvet.aplicacion.servicios;

import com.nuclearvet.aplicacion.mapeadores.ProductoMapeador;
import com.nuclearvet.dominio.enumeraciones.TipoProducto;
import com.nuclearvet.infraestructura.persistencia.ProductoRepositorio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServicioTest {

    @Mock
    private ProductoRepositorio productoRepositorio;

    @Mock
    private ProductoMapeador productoMapeador;

    @InjectMocks
    private ProductoServicio productoServicio;

    @Test
    void listarTodos_DeberiaInvocarRepositorio() {
        when(productoRepositorio.findAll()).thenReturn(Arrays.asList());
        productoServicio.listarTodos();
        verify(productoRepositorio, times(1)).findAll();
    }

    @Test
    void listarActivos_DeberiaInvocarRepositorio() {
        when(productoRepositorio.findAllActivosOrdenados()).thenReturn(Arrays.asList());
        productoServicio.listarActivos();
        verify(productoRepositorio, times(1)).findAllActivosOrdenados();
    }

    @Test
    void listarPorTipo_DeberiaInvocarRepositorio() {
        TipoProducto tipo = TipoProducto.MEDICAMENTO;
        when(productoRepositorio.findByTipoProductoOrdenados(tipo)).thenReturn(Arrays.asList());
        productoServicio.listarPorTipo(tipo);
        verify(productoRepositorio, times(1)).findByTipoProductoOrdenados(tipo);
    }

    @Test
    void listarProductosStockBajo_DeberiaInvocarRepositorio() {
        when(productoRepositorio.findProductosStockBajo()).thenReturn(Arrays.asList());
        productoServicio.listarProductosStockBajo();
        verify(productoRepositorio, times(1)).findProductosStockBajo();
    }

    @Test
    void listarProductosAgotados_DeberiaInvocarRepositorio() {
        when(productoRepositorio.findProductosAgotados()).thenReturn(Arrays.asList());
        productoServicio.listarProductosAgotados();
        verify(productoRepositorio, times(1)).findProductosAgotados();
    }
}
