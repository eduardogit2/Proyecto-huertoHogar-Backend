package com.example.HuertoHogar.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.HuertoHogar.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {
}