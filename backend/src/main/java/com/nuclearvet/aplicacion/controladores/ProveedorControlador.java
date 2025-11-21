package com.nuclearvet.aplicacion.controladores;

import com.nuclearvet.aplicacion.dtos.ProveedorDTO;
import com.nuclearvet.aplicacion.servicios.ProveedorServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestión de proveedores
 */
@RestController
@RequestMapping("/api/inventario/proveedores")
@RequiredArgsConstructor
@Tag(name = "Proveedores", description = "API para gestión de proveedores")
public class ProveedorControlador {

    private final ProveedorServicio proveedorServicio;

    @PostMapping
    @Operation(summary = "Crear proveedor", description = "Registra un nuevo proveedor en el sistema")
    public ResponseEntity<ProveedorDTO> crear(@Valid @RequestBody ProveedorDTO proveedorDTO) {
        return new ResponseEntity<>(proveedorServicio.crear(proveedorDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar proveedor", description = "Actualiza la información de un proveedor")
    public ResponseEntity<ProveedorDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProveedorDTO proveedorDTO) {
        return ResponseEntity.ok(proveedorServicio.actualizar(id, proveedorDTO));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener proveedor por ID", description = "Obtiene un proveedor por su identificador")
    public ResponseEntity<ProveedorDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(proveedorServicio.obtenerPorId(id));
    }

    @GetMapping("/nit/{nit}")
    @Operation(summary = "Obtener proveedor por NIT", description = "Obtiene un proveedor por su número de NIT")
    public ResponseEntity<ProveedorDTO> obtenerPorNit(@PathVariable String nit) {
        return ResponseEntity.ok(proveedorServicio.obtenerPorNit(nit));
    }

    @GetMapping
    @Operation(summary = "Listar todos los proveedores", description = "Lista todos los proveedores registrados")
    public ResponseEntity<List<ProveedorDTO>> listarTodos() {
        return ResponseEntity.ok(proveedorServicio.listarTodos());
    }

    @GetMapping("/activos")
    @Operation(summary = "Listar proveedores activos", description = "Lista solo los proveedores activos")
    public ResponseEntity<List<ProveedorDTO>> listarActivos() {
        return ResponseEntity.ok(proveedorServicio.listarActivos());
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar proveedores", description = "Busca proveedores por nombre o NIT")
    public ResponseEntity<List<ProveedorDTO>> buscar(@RequestParam String termino) {
        return ResponseEntity.ok(proveedorServicio.buscarPorNombre(termino));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar proveedor", description = "Elimina un proveedor del sistema")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        proveedorServicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/desactivar")
    @Operation(summary = "Desactivar proveedor", description = "Desactiva un proveedor sin eliminarlo")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        proveedorServicio.desactivar(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/activar")
    @Operation(summary = "Activar proveedor", description = "Activa un proveedor previamente desactivado")
    public ResponseEntity<Void> activar(@PathVariable Long id) {
        proveedorServicio.activar(id);
        return ResponseEntity.ok().build();
    }
}
