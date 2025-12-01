package com.example.HuertoHogar.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;
    private Integer precio;
    private Integer precioConDescuento;
    private String categoria;
    
    @Column(length = 1000) 
    private String descripcion;
    
    private String imagen;
    
    private Integer stock;
    private Integer stockCritico;
    
    private String origen; 
    private String unidad; 
    private String etiqueta; 
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private java.util.List<Resena> resenas;
}