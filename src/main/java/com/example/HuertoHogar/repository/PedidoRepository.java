package com.example.HuertoHogar.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.HuertoHogar.model.Pedido;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
    List<Pedido> findByUsuarioEmail(String email);
}