package com.example.HuertoHogar.controller;

import com.example.HuertoHogar.dto.PedidoRequest;
import com.example.HuertoHogar.model.*;
import com.example.HuertoHogar.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/pedidos")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;
    private final UserRepository userRepository;
    private final DetallePedidoRepository detalleRepository;

    @GetMapping
    public List<Pedido> getAllPedidos() {
        return pedidoRepository.findAll();
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> crearPedido(@RequestBody PedidoRequest request) {
        User usuario = userRepository.findByEmail(request.getEmailUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Pedido nuevoPedido = Pedido.builder()
                .fecha(LocalDateTime.now())
                .estado("En preparación")
                .direccionEnvio(request.getDireccion())
                .usuario(usuario)
                .total(0.0)
                .build();
        
        Pedido pedidoGuardado = pedidoRepository.save(nuevoPedido);

        double totalCalculado = 0;

        for (PedidoRequest.ItemPedido item : request.getItems()) {
            Producto producto = productoRepository.findById(item.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            if (producto.getStock() < item.getCantidad()) {
                throw new RuntimeException("Sin stock para: " + producto.getNombre());
            }

            producto.setStock(producto.getStock() - item.getCantidad());
            productoRepository.save(producto);

            double precioFinalItem = (producto.getPrecioConDescuento() != null && producto.getPrecioConDescuento() > 0) 
                                     ? producto.getPrecioConDescuento() 
                                     : producto.getPrecio();

            DetallePedido detalle = DetallePedido.builder()
                    .pedido(pedidoGuardado)
                    .producto(producto)
                    .cantidad(item.getCantidad())
                    .precioUnitario(precioFinalItem)
                    .build();

            detalleRepository.save(detalle);
            totalCalculado += (precioFinalItem * item.getCantidad());
        }

        double descuentoPuntos = 0;
        int puntosAUsar = 0;

        if (request.isUsarPuntos() && usuario.getPuntos() != null && usuario.getPuntos() > 0) {
            if (usuario.getPuntos() >= totalCalculado) {
                descuentoPuntos = totalCalculado;
                puntosAUsar = (int) totalCalculado;
            } else {
                descuentoPuntos = usuario.getPuntos();
                puntosAUsar = usuario.getPuntos();
            }
            usuario.setPuntos(usuario.getPuntos() - puntosAUsar);
        }

        double totalFinal = Math.max(0, totalCalculado - descuentoPuntos);
        
        int puntosGanados = (int) (totalFinal / 1000); 

        if (usuario.getPuntos() == null) usuario.setPuntos(0);
        usuario.setPuntos(usuario.getPuntos() + puntosGanados);

        userRepository.save(usuario);
        pedidoGuardado.setTotal(totalFinal);
        pedidoRepository.save(pedidoGuardado);

        Pedido pedidoCompleto = pedidoRepository.findById(pedidoGuardado.getId()).orElse(pedidoGuardado);
        return ResponseEntity.ok(pedidoCompleto);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> updateEstado(@PathVariable Integer id, @RequestBody Map<String, String> payload) {
        String nuevoEstado = payload.get("estado");
        return pedidoRepository.findById(id).map(pedido -> {
            pedido.setEstado(nuevoEstado);
            return ResponseEntity.ok(pedidoRepository.save(pedido));
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/mis-pedidos")
    public List<Pedido> getMisPedidos() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return pedidoRepository.findByUsuarioEmail(email);
    }
}