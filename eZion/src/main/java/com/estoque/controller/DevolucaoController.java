package com.estoque.controller;

import com.estoque.model.Devolucao;
import com.estoque.service.DevolutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/devolucoes")
public class DevolucaoController {

    @Autowired
    private DevolutionService devolutionService;

    @PostMapping
    public ResponseEntity<Devolucao> createDevolucao(@RequestBody Devolucao devolucao) {
        Devolucao created = devolutionService.createDevolucao(devolucao);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<Devolucao>> getAllDevolucoes() {
        List<Devolucao> devolucoes = devolutionService.getAllDevolucoes();
        return ResponseEntity.ok(devolucoes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Devolucao> getDevolutionById(@PathVariable Long id) {
        Optional<Devolucao> devolucao = devolutionService.getDevolutionById(id);
        return devolucao.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/numero/{numero}")
    public ResponseEntity<Devolucao> getDevolutionByNumero(@PathVariable String numero) {
        Optional<Devolucao> devolucao = devolutionService.getDevolutionByNumero(numero);
        return devolucao.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Devolucao>> getDevolutionsByStatus(@PathVariable String status) {
        try {
            Devolucao.Status s = Devolucao.Status.valueOf(status.toUpperCase());
            List<Devolucao> devolucoes = devolutionService.getDevolutionsByStatus(s);
            return ResponseEntity.ok(devolucoes);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Devolucao>> getDevolutionsByTipo(@PathVariable String tipo) {
        try {
            Devolucao.Tipo t = Devolucao.Tipo.valueOf(tipo.toUpperCase());
            List<Devolucao> devolucoes = devolutionService.getDevolutionsByTipo(t);
            return ResponseEntity.ok(devolucoes);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Devolucao> updateDevolucao(@PathVariable Long id, @RequestBody Devolucao devolutionDetails) {
        try {
            Devolucao updated = devolutionService.updateDevolucao(id, devolutionDetails);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevolucao(@PathVariable Long id) {
        try {
            devolutionService.deleteDevolucao(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/processar")
    public ResponseEntity<?> processarDevolucao(@PathVariable Long id) {
        try {
            devolutionService.processarDevolucao(id);
            return ResponseEntity.ok().body("Devolução processada com sucesso");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelarDevolucao(@PathVariable Long id) {
        try {
            devolutionService.cancelarDevolucao(id);
            return ResponseEntity.ok().body("Devolução cancelada com sucesso");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
