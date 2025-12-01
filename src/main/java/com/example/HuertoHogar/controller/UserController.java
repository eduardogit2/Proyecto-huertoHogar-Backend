package com.example.HuertoHogar.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.HuertoHogar.model.User;
import com.example.HuertoHogar.repository.UserRepository;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder; 

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Integer id, @RequestBody User usuarioActualizado) {
        return repository.findById(id).map(user -> {
            user.setNombre(usuarioActualizado.getNombre());
            user.setApellidos(usuarioActualizado.getApellidos());
            user.setRut(usuarioActualizado.getRut());
            user.setEmail(usuarioActualizado.getEmail());
            
            if (usuarioActualizado.getPassword() != null && !usuarioActualizado.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(usuarioActualizado.getPassword()));
            }
            
            user.setRole(usuarioActualizado.getRole());

            return ResponseEntity.ok(repository.save(user));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}