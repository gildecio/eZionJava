package com.estoque.controller;

import com.estoque.model.AjusteEstoque;
import com.estoque.service.AjusteEstoqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ajustes-estoque")
public class AjusteEstoqueController {

    @Autowired
    private AjusteEstoqueService ajusteEstoqueService;

    @PostMapping
    public ResponseEntity<AjusteEstoque> createAjusteEstoque(@RequestBody AjusteEstoque ajuste) {
        AjusteEstoque created = ajusteEstoqueService.createAjusteEstoque(ajuste);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<AjusteEstoque>> getAllAjustesEstoque() {
        List<AjusteEstoque> ajustes = ajusteEstoqueService.getAllAjustesEstoque();
        return ResponseEntity.ok(ajustes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AjusteEstoque> getAjusteEstoqueById(@PathVariable Long id) {
        Optional<AjusteEstoque> ajuste = ajusteEstoqueService.getAjusteEstoqueById(id);
        return ajuste.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/numero/{numero}")
    public ResponseEntity<AjusteEstoque> getAjusteByNumero(@PathVariable String numero) {
        Optional<AjusteEstoque> ajuste = ajusteEstoqueService.getAjusteByNumero(numero);
        return ajuste.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<AjusteEstoque>> getAjustesByTipo(@PathVariable String tipo) {
        try {
            AjusteEstoque.Tipo t = AjusteEstoque.Tipo.valueOf(tipo.toUpperCase());
            List<AjusteEstoque> ajustes = ajusteEstoqueService.getAjustesByTipo(t);
            return ResponseEntity.ok(ajustes);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<AjusteEstoque> updateAjusteEstoque(@PathVariable Long id, @RequestBody AjusteEstoque ajusteDetails) {
        try {
            AjusteEstoque updated = ajusteEstoqueService.updateAjusteEstoque(id, ajusteDetails);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAjusteEstoque(@PathVariable Long id) {
        try {
            ajusteEstoqueService.deleteAjusteEstoque(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/processar")
    public ResponseEntity<?> processarAjuste(@PathVariable Long id) {
        try {
            ajusteEstoqueService.processarAjuste(id);
            return ResponseEntity.ok().body("Ajuste de Estoque processado com sucesso");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
