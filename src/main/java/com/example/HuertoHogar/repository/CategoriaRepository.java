package com.example.HuertoHogar.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.HuertoHogar.model.Categoria;

import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    Optional<Categoria> findByNombre(String nombre);
}