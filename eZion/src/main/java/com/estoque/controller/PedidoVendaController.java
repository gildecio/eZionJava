package com.estoque.controller;

import com.estoque.model.PedidoVenda;
import com.estoque.model.PedidoVendaItem;
import com.estoque.service.PedidoVendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pedidos-venda")
public class PedidoVendaController {

    @Autowired
    private PedidoVendaService pedidoVendaService;

    @GetMapping
    public List<PedidoVenda> getAllPedidosVenda() {
        return pedidoVendaService.getAllPedidosVenda();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoVenda> getPedidoVendaById(@PathVariable Long id) {
        return pedidoVendaService.getPedidoVendaById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public PedidoVenda createPedidoVenda(@RequestBody PedidoVenda pedidoVenda) {
        return pedidoVendaService.createPedidoVenda(pedidoVenda);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PedidoVenda> updatePedidoVenda(@PathVariable Long id, @RequestBody PedidoVenda pedidoVendaDetails) {
        try {
            PedidoVenda updated = pedidoVendaService.updatePedidoVenda(id, pedidoVendaDetails);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePedidoVenda(@PathVariable Long id) {
        try {
            pedidoVendaService.deletePedidoVenda(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/status/{status}")
    public List<PedidoVenda> getPedidoVendaByStatus(@PathVariable String status) {
        try {
            PedidoVenda.Status statusEnum = PedidoVenda.Status.valueOf(status.toUpperCase());
            return pedidoVendaService.getPedidoVendaByStatus(statusEnum);
        } catch (IllegalArgumentException e) {
            return List.of();
        }
    }

    @GetMapping("/cliente/{cliente}")
    public List<PedidoVenda> getPedidoVendaByCliente(@PathVariable String cliente) {
        return pedidoVendaService.getPedidoVendaByCliente(cliente);
    }

    @GetMapping("/{id}/itens")
    public List<PedidoVendaItem> getItensPedidoVenda(@PathVariable Long id) {
        return pedidoVendaService.getItensPedidoVenda(id);
    }

    @PostMapping("/{id}/confirmar")
    public ResponseEntity<?> confirmarPedidoVenda(@PathVariable Long id) {
        try {
            pedidoVendaService.confirmarPedidoVenda(id);
            return ResponseEntity.ok().body(Map.of("message", "Pedido confirmado com sucesso"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{id}/separar")
    public ResponseEntity<?> marcarComoSeparado(@PathVariable Long id) {
        try {
            pedidoVendaService.marcarComoSeparado(id);
            return ResponseEntity.ok().body(Map.of("message", "Pedido marcado como separado"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{id}/expedir")
    public ResponseEntity<?> expedir(@PathVariable Long id, @RequestBody(required = false) Map<String, Object> request) {
        try {
            var localDestino = new com.estoque.model.Local();
            localDestino.setId(2L); // Local de envio padrão
            
            pedidoVendaService.expedir(id, localDestino);
            return ResponseEntity.ok().body(Map.of("message", "Pedido expedido com sucesso"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{id}/confirmar-entrega")
    public ResponseEntity<?> confirmarEntrega(@PathVariable Long id, @RequestBody(required = false) Map<String, Object> request) {
        try {
            Date dataEntrega = null;
            if (request != null && request.containsKey("dataEntrega")) {
                dataEntrega = new Date((Long) request.get("dataEntrega"));
            }
            
            pedidoVendaService.confirmarEntrega(id, dataEntrega);
            return ResponseEntity.ok().body(Map.of("message", "Entrega confirmada com sucesso"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelarPedidoVenda(@PathVariable Long id) {
        try {
            pedidoVendaService.cancelarPedidoVenda(id);
            return ResponseEntity.ok().body(Map.of("message", "Pedido cancelado com sucesso"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
