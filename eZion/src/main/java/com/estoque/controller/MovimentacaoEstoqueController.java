package com.estoque.controller;

import com.estoque.model.MovimentacaoEstoque;
import com.estoque.service.MovimentacaoEstoqueService;
import com.estoque.service.ItemService;
import com.estoque.service.LocalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/movimentacoes-estoque")
public class MovimentacaoEstoqueController {

    @Autowired
    private MovimentacaoEstoqueService movimentacaoEstoqueService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private LocalService localService;

    @GetMapping
    public List<MovimentacaoEstoque> getAllMovimentacoes() {
        return movimentacaoEstoqueService.getAllMovimentacoes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovimentacaoEstoque> getMovimentacaoById(@PathVariable(value = "id") Long movimentacaoId) {
        return movimentacaoEstoqueService.getMovimentacaoById(movimentacaoId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public MovimentacaoEstoque createMovimentacao(@RequestBody MovimentacaoEstoque movimentacao) {
        return movimentacaoEstoqueService.createMovimentacao(movimentacao);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovimentacaoEstoque> updateMovimentacao(@PathVariable(value = "id") Long movimentacaoId, @RequestBody MovimentacaoEstoque movimentacaoDetails) {
        try {
            MovimentacaoEstoque updated = movimentacaoEstoqueService.updateMovimentacao(movimentacaoId, movimentacaoDetails);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMovimentacao(@PathVariable(value = "id") Long movimentacaoId) {
        movimentacaoEstoqueService.deleteMovimentacao(movimentacaoId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/item/{itemId}")
    public List<MovimentacaoEstoque> getMovimentacoesByItem(@PathVariable(value = "itemId") Long itemId) {
        return movimentacaoEstoqueService.getMovimentacoesByItemId(itemId);
    }

    @GetMapping("/tipo/{tipo}")
    public List<MovimentacaoEstoque> getMovimentacoesByTipo(@PathVariable(value = "tipo") String tipo) {
        try {
            MovimentacaoEstoque.TipoMovimentacao tipoMovimentacao = MovimentacaoEstoque.TipoMovimentacao.valueOf(tipo.toUpperCase());
            return movimentacaoEstoqueService.getMovimentacoesByTipo(tipoMovimentacao);
        } catch (IllegalArgumentException e) {
            return List.of();
        }
    }

    @PostMapping("/transferencia")
    public ResponseEntity<?> transferir(@RequestBody Map<String, Object> request) {
        try {
            Long itemId = Long.valueOf(request.get("itemId").toString());
            Long localOrigemId = Long.valueOf(request.get("localOrigemId").toString());
            Long localDestinoId = Long.valueOf(request.get("localDestinoId").toString());
            java.math.BigDecimal quantidade = new java.math.BigDecimal(request.get("quantidade").toString());
            String referencia = (String) request.getOrDefault("referencia", "");

            var item = itemService.findById(itemId)
                    .orElseThrow(() -> new RuntimeException("Item não encontrado"));
            var localOrigem = localService.findById(localOrigemId)
                    .orElseThrow(() -> new RuntimeException("Local de origem não encontrado"));
            var localDestino = localService.findById(localDestinoId)
                    .orElseThrow(() -> new RuntimeException("Local de destino não encontrado"));

            movimentacaoEstoqueService.transferir(item, localOrigem, localDestino, quantidade, referencia);
            return ResponseEntity.ok().body(Map.of("message", "Transferência realizada com sucesso"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
