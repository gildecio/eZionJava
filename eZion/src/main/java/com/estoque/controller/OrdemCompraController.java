package com.estoque.controller;

import com.estoque.model.OrdemCompra;
import com.estoque.model.OrdemCompraItem;
import com.estoque.service.OrdemCompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ordens-compra")
public class OrdemCompraController {

    @Autowired
    private OrdemCompraService ordemCompraService;

    @PostMapping
    public ResponseEntity<OrdemCompra> createOrdemCompra(@RequestBody OrdemCompra ordemCompra) {
        OrdemCompra created = ordemCompraService.createOrdemCompra(ordemCompra);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<OrdemCompra>> getAllOrdensCompra() {
        List<OrdemCompra> ordens = ordemCompraService.getAllOrdensCompra();
        return ResponseEntity.ok(ordens);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdemCompra> getOrdemCompraById(@PathVariable Long id) {
        Optional<OrdemCompra> ordem = ordemCompraService.getOrdemCompraById(id);
        return ordem.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/numero/{numero}")
    public ResponseEntity<OrdemCompra> getOrdemCompraByNumero(@PathVariable String numero) {
        Optional<OrdemCompra> ordem = ordemCompraService.getOrdemCompraByNumero(numero);
        return ordem.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<OrdemCompra>> getOrdensByStatus(@PathVariable String status) {
        try {
            OrdemCompra.Status s = OrdemCompra.Status.valueOf(status.toUpperCase());
            List<OrdemCompra> ordens = ordemCompraService.getOrdensByStatus(s);
            return ResponseEntity.ok(ordens);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/fornecedor/{fornecedor}")
    public ResponseEntity<List<OrdemCompra>> getOrdensByFornecedor(@PathVariable String fornecedor) {
        List<OrdemCompra> ordens = ordemCompraService.getOrdensByFornecedor(fornecedor);
        return ResponseEntity.ok(ordens);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrdemCompra> updateOrdemCompra(@PathVariable Long id, @RequestBody OrdemCompra ordemCompraDetails) {
        try {
            OrdemCompra updated = ordemCompraService.updateOrdemCompra(id, ordemCompraDetails);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrdemCompra(@PathVariable Long id) {
        try {
            ordemCompraService.deleteOrdemCompra(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/enviar")
    public ResponseEntity<?> enviarOrdemCompra(@PathVariable Long id) {
        try {
            ordemCompraService.enviarOrdemCompra(id);
            return ResponseEntity.ok().body("Ordem de Compra enviada com sucesso");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/confirmar")
    public ResponseEntity<?> confirmarOrdemCompra(@PathVariable Long id) {
        try {
            ordemCompraService.confirmarOrdemCompra(id);
            return ResponseEntity.ok().body("Ordem de Compra confirmada com sucesso");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/receber")
    public ResponseEntity<?> receberOrdemCompra(@PathVariable Long id, @RequestBody OrdemCompraItem itemRecebido) {
        try {
            ordemCompraService.receberOrdemCompra(id, itemRecebido);
            return ResponseEntity.ok().body("Item recebido com sucesso");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelarOrdemCompra(@PathVariable Long id) {
        try {
            ordemCompraService.cancelarOrdemCompra(id);
            return ResponseEntity.ok().body("Ordem de Compra cancelada com sucesso");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}/itens")
    public ResponseEntity<List<OrdemCompraItem>> getItensOrdemCompra(@PathVariable Long id) {
        List<OrdemCompraItem> itens = ordemCompraService.getItensOrdemCompra(id);
        return ResponseEntity.ok(itens);
    }
}
