package com.example.HuertoHogar.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "resenas")
public class Resena {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String usuario; 
    private Integer calificacion;
    
    @Column(length = 1000)
    private String texto;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    @JsonIgnore 
    private Producto producto;
}