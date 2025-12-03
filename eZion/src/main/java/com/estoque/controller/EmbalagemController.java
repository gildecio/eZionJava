package com.estoque.controller;

import com.estoque.model.Embalagem;
import com.estoque.service.EmbalagemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/embalagens")
public class EmbalagemController {

    @Autowired
    private EmbalagemService embalagemService;

    @GetMapping
    public List<Embalagem> getAllEmbalagens() {
        return embalagemService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Embalagem> getEmbalagemById(@PathVariable Long id) {
        return embalagemService.findById(id)
                .map(embalagem -> ResponseEntity.ok(embalagem))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Embalagem createEmbalagem(@RequestBody Embalagem embalagem) {
        return embalagemService.save(embalagem);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Embalagem> updateEmbalagem(@PathVariable Long id, @RequestBody Embalagem embalagemDetails) {
        return embalagemService.findById(id)
                .map(embalagem -> {
                    embalagem.setNome(embalagemDetails.getNome());
                    embalagem.setUnidade(embalagemDetails.getUnidade());
                    embalagem.setFator(embalagemDetails.getFator());
                    Embalagem updatedEmbalagem = embalagemService.save(embalagem);
                    return ResponseEntity.ok(updatedEmbalagem);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmbalagem(@PathVariable Long id) {
        if (embalagemService.findById(id).isPresent()) {
            embalagemService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}