package com.example.estoque.controller;

import com.example.estoque.model.Local;
import com.example.estoque.service.LocalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locais")
public class LocalController {

    @Autowired
    private LocalService localService;

    @GetMapping
    public List<Local> getAllLocais() {
        return localService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Local> getLocalById(@PathVariable Long id) {
        return localService.findById(id)
                .map(local -> ResponseEntity.ok(local))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Local createLocal(@RequestBody Local local) {
        return localService.save(local);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Local> updateLocal(@PathVariable Long id, @RequestBody Local localDetails) {
        return localService.findById(id)
                .map(local -> {
                    local.setNome(localDetails.getNome());
                    local.setAtivo(localDetails.isAtivo());
                    Local updatedLocal = localService.save(local);
                    return ResponseEntity.ok(updatedLocal);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocal(@PathVariable Long id) {
        if (localService.findById(id).isPresent()) {
            localService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}