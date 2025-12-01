package com.example.HuertoHogar.controller;

import com.example.HuertoHogar.model.Producto;
import com.example.HuertoHogar.model.Resena;
import com.example.HuertoHogar.repository.ProductoRepository;
import com.example.HuertoHogar.repository.ResenaRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/productos")
@CrossOrigin(origins = "http://localhost:5173") 
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoRepository repository;
    private final ResenaRepository resenaRepository;

    @GetMapping
    public List<Producto> getAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> getById(@PathVariable Integer id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Producto create(@RequestBody Producto producto) {
        return repository.save(producto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> update(@PathVariable Integer id, @RequestBody Producto productoEditado) {
        return repository.findById(id).map(prod -> {
            prod.setNombre(productoEditado.getNombre());
            prod.setPrecio(productoEditado.getPrecio());
            prod.setStock(productoEditado.getStock());
            prod.setCategoria(productoEditado.getCategoria());
            prod.setDescripcion(productoEditado.getDescripcion());
            prod.setImagen(productoEditado.getImagen());
            prod.setOrigen(productoEditado.getOrigen());
            prod.setUnidad(productoEditado.getUnidad());
            return ResponseEntity.ok(repository.save(prod));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    @PostMapping("/{id}/resenas")
    public ResponseEntity<Producto> addResena(@PathVariable Integer id, @RequestBody Resena resena) {
        return repository.findById(id).map(prod -> {
            resena.setProducto(prod); 
            resenaRepository.save(resena); 
            return ResponseEntity.ok(prod); 
        }).orElse(ResponseEntity.notFound().build());
    }
}