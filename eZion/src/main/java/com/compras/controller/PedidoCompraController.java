package com.compras.controller;

import com.compras.model.PedidoCompra;
import com.compras.model.StatusPedidoCompra;
import com.compras.service.PedidoCompraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos-compra")
@RequiredArgsConstructor
@Tag(name = "Pedidos de Compra", description = "API para gerenciamento de pedidos de compra")
public class PedidoCompraController {

    private final PedidoCompraService pedidoCompraService;

    @PostMapping
    @Operation(summary = "Criar novo pedido de compra")
    public ResponseEntity<PedidoCompra> criar(@Valid @RequestBody PedidoCompra pedidoCompra) {
        try {
            PedidoCompra novoPedido = pedidoCompraService.criar(pedidoCompra);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoPedido);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar pedido de compra")
    public ResponseEntity<PedidoCompra> atualizar(@PathVariable Long id, @Valid @RequestBody PedidoCompra pedidoCompra) {
        try {
            PedidoCompra pedidoAtualizado = pedidoCompraService.atualizar(id, pedidoCompra);
            return ResponseEntity.ok(pedidoAtualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/aprovar")
    @Operation(summary = "Aprovar pedido de compra")
    public ResponseEntity<PedidoCompra> aprovar(@PathVariable Long id) {
        try {
            PedidoCompra pedidoAprovado = pedidoCompraService.aprovar(id);
            return ResponseEntity.ok(pedidoAprovado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/rejeitar")
    @Operation(summary = "Rejeitar pedido de compra")
    public ResponseEntity<PedidoCompra> rejeitar(@PathVariable Long id) {
        try {
            PedidoCompra pedidoRejeitado = pedidoCompraService.rejeitar(id);
            return ResponseEntity.ok(pedidoRejeitado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/registrar-recebimento")
    @Operation(summary = "Registrar recebimento do pedido")
    public ResponseEntity<PedidoCompra> registrarRecebimento(@PathVariable Long id,
                                                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataEntrega) {
        try {
            PedidoCompra pedidoRecebido = pedidoCompraService.registrarRecebimento(id, dataEntrega);
            return ResponseEntity.ok(pedidoRecebido);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar pedido de compra por ID")
    public ResponseEntity<PedidoCompra> buscarPorId(@PathVariable Long id) {
        return pedidoCompraService.obterPorId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Listar todos os pedidos de compra ativos")
    public ResponseEntity<List<PedidoCompra>> listarAtivos() {
        List<PedidoCompra> pedidos = pedidoCompraService.listarAtivos();
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Listar pedidos por status")
    public ResponseEntity<List<PedidoCompra>> listarPorStatus(@PathVariable StatusPedidoCompra status) {
        List<PedidoCompra> pedidos = pedidoCompraService.listarPorStatus(status);
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/atrasados")
    @Operation(summary = "Listar pedidos com entrega atrasada")
    public ResponseEntity<List<PedidoCompra>> listarAtrasados() {
        List<PedidoCompra> pedidos = pedidoCompraService.listarPedidosAtrasados();
        return ResponseEntity.ok(pedidos);
    }
}