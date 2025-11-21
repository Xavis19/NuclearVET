package com.nuclearvet.aplicacion.controladores;

import com.nuclearvet.aplicacion.dtos.CategoriaProductoDTO;
import com.nuclearvet.aplicacion.servicios.CategoriaProductoServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestión de categorías de productos
 */
@RestController
@RequestMapping("/api/inventario/categorias")
@RequiredArgsConstructor
@Tag(name = "Categorías de Productos", description = "API para gestión de categorías de productos")
public class CategoriaProductoControlador {

    private final CategoriaProductoServicio categoriaServicio;

    @PostMapping
    @Operation(summary = "Crear categoría", description = "Crea una nueva categoría de productos")
    public ResponseEntity<CategoriaProductoDTO> crear(@Valid @RequestBody CategoriaProductoDTO categoriaDTO) {
        return new ResponseEntity<>(categoriaServicio.crear(categoriaDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar categoría", description = "Actualiza una categoría existente")
    public ResponseEntity<CategoriaProductoDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody CategoriaProductoDTO categoriaDTO) {
        return ResponseEntity.ok(categoriaServicio.actualizar(id, categoriaDTO));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener categoría por ID", description = "Obtiene una categoría por su identificador")
    public ResponseEntity<CategoriaProductoDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(categoriaServicio.obtenerPorId(id));
    }

    @GetMapping
    @Operation(summary = "Listar todas las categorías", description = "Lista todas las categorías de productos")
    public ResponseEntity<List<CategoriaProductoDTO>> listarTodas() {
        return ResponseEntity.ok(categoriaServicio.listarTodas());
    }

    @GetMapping("/activas")
    @Operation(summary = "Listar categorías activas", description = "Lista solo las categorías activas")
    public ResponseEntity<List<CategoriaProductoDTO>> listarActivas() {
        return ResponseEntity.ok(categoriaServicio.listarActivas());
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar categorías", description = "Busca categorías por nombre")
    public ResponseEntity<List<CategoriaProductoDTO>> buscar(@RequestParam String termino) {
        return ResponseEntity.ok(categoriaServicio.buscarPorNombre(termino));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar categoría", description = "Elimina una categoría del sistema")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        categoriaServicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/desactivar")
    @Operation(summary = "Desactivar categoría", description = "Desactiva una categoría sin eliminarla")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        categoriaServicio.desactivar(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/activar")
    @Operation(summary = "Activar categoría", description = "Activa una categoría previamente desactivada")
    public ResponseEntity<Void> activar(@PathVariable Long id) {
        categoriaServicio.activar(id);
        return ResponseEntity.ok().build();
    }
}
