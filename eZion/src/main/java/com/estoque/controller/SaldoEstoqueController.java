package com.estoque.controller;

import com.estoque.model.SaldoEstoque;
import com.estoque.model.SaldoEstoqueHistorico;
import com.estoque.service.SaldoEstoqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/saldos-estoque")
public class SaldoEstoqueController {

    @Autowired
    private SaldoEstoqueService saldoEstoqueService;

    @GetMapping
    public List<SaldoEstoque> getAllSaldos() {
        return saldoEstoqueService.getAllSaldos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaldoEstoque> getSaldoById(@PathVariable(value = "id") Long saldoId) {
        return saldoEstoqueService.getSaldoById(saldoId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/item/{itemId}")
    public List<SaldoEstoque> getSaldosByItem(@PathVariable(value = "itemId") Long itemId) {
        return saldoEstoqueService.getSaldosByItemId(itemId);
    }

    @GetMapping("/local/{localId}")
    public List<SaldoEstoque> getSaldosByLocal(@PathVariable(value = "localId") Long localId) {
        return saldoEstoqueService.getSaldosByLocalId(localId);
    }

    @GetMapping("/lote/{lote}")
    public List<SaldoEstoque> getSaldosByLote(@PathVariable(value = "lote") String lote) {
        return saldoEstoqueService.getSaldosByLote(lote);
    }

    @GetMapping("/item/{itemId}/lote/{lote}")
    public List<SaldoEstoque> getSaldosByItemAndLote(@PathVariable(value = "itemId") Long itemId,
                                                       @PathVariable(value = "lote") String lote) {
        return saldoEstoqueService.getSaldosByItemAndLote(itemId, lote);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarSaldo(@PathVariable(value = "id") Long saldoId) {
        try {
            saldoEstoqueService.deletarSaldo(saldoId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/validar/{itemId}/{localId}")
    public ResponseEntity<?> validarConsistencia(@PathVariable Long itemId, @PathVariable Long localId) {
        return validarConsistenciaComLote(itemId, localId, null);
    }

    @GetMapping("/validar/{itemId}/{localId}/{lote}")
    public ResponseEntity<?> validarConsistenciaComLote(@PathVariable Long itemId, 
                                                         @PathVariable Long localId,
                                                         @PathVariable(required = false) String lote) {
        try {
            var item = new com.estoque.model.Item();
            item.setId(itemId);
            var local = new com.estoque.model.Local();
            local.setId(localId);
            
            boolean consistente;
            if (lote != null && !lote.isEmpty()) {
                consistente = saldoEstoqueService.validarConsistenciaComLote(item, local, lote);
            } else {
                consistente = saldoEstoqueService.validarConsistencia(item, local);
            }
            return ResponseEntity.ok().body(Map.of("consistente", consistente));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Endpoints de Histórico
    @GetMapping("/historico")
    public List<SaldoEstoqueHistorico> getAllHistorico() {
        return saldoEstoqueService.getAllHistorico();
    }

    @GetMapping("/historico/item/{itemId}")
    public List<SaldoEstoqueHistorico> getHistoricoByItem(@PathVariable(value = "itemId") Long itemId) {
        try {
            var item = new com.estoque.model.Item();
            item.setId(itemId);
            return saldoEstoqueService.getHistoricoByItem(item);
        } catch (Exception e) {
            return List.of();
        }
    }

    @GetMapping("/historico/item/{itemId}/local/{localId}")
    public List<SaldoEstoqueHistorico> getHistoricoByItemAndLocal(
            @PathVariable(value = "itemId") Long itemId,
            @PathVariable(value = "localId") Long localId) {
        try {
            var item = new com.estoque.model.Item();
            item.setId(itemId);
            var local = new com.estoque.model.Local();
            local.setId(localId);
            return saldoEstoqueService.getHistoricoByItemAndLocal(item, local);
        } catch (Exception e) {
            return List.of();
        }
    }
}
