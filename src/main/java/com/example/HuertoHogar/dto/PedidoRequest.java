package com.example.HuertoHogar.dto;

import lombok.Data;
import java.util.List;

@Data
public class PedidoRequest {
    private String emailUsuario; 
    private String direccion;
    private boolean usarPuntos; 
    private List<ItemPedido> items;

    @Data
    public static class ItemPedido {
        private Integer productoId;
        private Integer cantidad;
    }
}