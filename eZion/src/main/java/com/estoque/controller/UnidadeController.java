package com.estoque.controller;

import com.estoque.model.Unidade;
import com.estoque.service.UnidadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/unidades")
public class UnidadeController {

    @Autowired
    private UnidadeService unidadeService;

    @GetMapping
    public List<Unidade> getAllUnidades() {
        return unidadeService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Unidade> getUnidadeById(@PathVariable Long id) {
        return unidadeService.findById(id)
                .map(unidade -> ResponseEntity.ok(unidade))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Unidade createUnidade(@RequestBody Unidade unidade) {
        return unidadeService.save(unidade);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Unidade> updateUnidade(@PathVariable Long id, @RequestBody Unidade unidadeDetails) {
        return unidadeService.findById(id)
                .map(unidade -> {
                    unidade.setNome(unidadeDetails.getNome());
                    unidade.setDescricao(unidadeDetails.getDescricao());
                    Unidade updatedUnidade = unidadeService.save(unidade);
                    return ResponseEntity.ok(updatedUnidade);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUnidade(@PathVariable Long id) {
        if (unidadeService.findById(id).isPresent()) {
            unidadeService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}