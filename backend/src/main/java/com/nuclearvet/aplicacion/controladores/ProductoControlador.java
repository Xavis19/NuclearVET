package com.nuclearvet.aplicacion.controladores;

import com.nuclearvet.aplicacion.dtos.ProductoDTO;
import com.nuclearvet.aplicacion.servicios.ProductoServicio;
import com.nuclearvet.dominio.enumeraciones.TipoProducto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestión de productos del inventario
 */
@RestController
@RequestMapping("/api/inventario/productos")
@RequiredArgsConstructor
@Tag(name = "Productos", description = "API para gestión de productos del inventario")
public class ProductoControlador {

    private final ProductoServicio productoServicio;

    @PostMapping
    @Operation(summary = "Crear producto", description = "Crea un nuevo producto en el inventario")
    public ResponseEntity<ProductoDTO> crear(@Valid @RequestBody ProductoDTO productoDTO) {
        return new ResponseEntity<>(productoServicio.crear(productoDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto", description = "Actualiza un producto existente")
    public ResponseEntity<ProductoDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProductoDTO productoDTO) {
        return ResponseEntity.ok(productoServicio.actualizar(id, productoDTO));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por ID", description = "Obtiene un producto por su identificador")
    public ResponseEntity<ProductoDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(productoServicio.obtenerPorId(id));
    }

    @GetMapping("/codigo/{codigo}")
    @Operation(summary = "Obtener producto por código", description = "Obtiene un producto por su código único")
    public ResponseEntity<ProductoDTO> obtenerPorCodigo(@PathVariable String codigo) {
        return ResponseEntity.ok(productoServicio.obtenerPorCodigo(codigo));
    }

    @GetMapping
    @Operation(summary = "Listar todos los productos", description = "Lista todos los productos del inventario")
    public ResponseEntity<List<ProductoDTO>> listarTodos() {
        return ResponseEntity.ok(productoServicio.listarTodos());
    }

    @GetMapping("/activos")
    @Operation(summary = "Listar productos activos", description = "Lista solo los productos activos")
    public ResponseEntity<List<ProductoDTO>> listarActivos() {
        return ResponseEntity.ok(productoServicio.listarActivos());
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar productos", description = "Busca productos por nombre o código")
    public ResponseEntity<List<ProductoDTO>> buscar(@RequestParam String termino) {
        return ResponseEntity.ok(productoServicio.buscarPorNombreOCodigo(termino));
    }

    @GetMapping("/categoria/{categoriaId}")
    @Operation(summary = "Listar por categoría", description = "Lista productos de una categoría específica")
    public ResponseEntity<List<ProductoDTO>> listarPorCategoria(@PathVariable Long categoriaId) {
        return ResponseEntity.ok(productoServicio.listarPorCategoria(categoriaId));
    }

    @GetMapping("/tipo/{tipo}")
    @Operation(summary = "Listar por tipo", description = "Lista productos de un tipo específico")
    public ResponseEntity<List<ProductoDTO>> listarPorTipo(@PathVariable TipoProducto tipo) {
        return ResponseEntity.ok(productoServicio.listarPorTipo(tipo));
    }

    @GetMapping("/stock-bajo")
    @Operation(summary = "Productos con stock bajo", description = "Lista productos con stock por debajo del mínimo")
    public ResponseEntity<List<ProductoDTO>> listarStockBajo() {
        return ResponseEntity.ok(productoServicio.listarProductosStockBajo());
    }

    @GetMapping("/agotados")
    @Operation(summary = "Productos agotados", description = "Lista productos sin stock disponible")
    public ResponseEntity<List<ProductoDTO>> listarAgotados() {
        return ResponseEntity.ok(productoServicio.listarProductosAgotados());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar producto", description = "Elimina un producto del sistema")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoServicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/activar")
    @Operation(summary = "Activar producto", description = "Activa un producto previamente inactivo")
    public ResponseEntity<Void> activar(@PathVariable Long id) {
        productoServicio.activar(id);
        return ResponseEntity.ok().build();
    }
}
