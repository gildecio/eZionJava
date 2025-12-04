package com.estoque.controller;

import com.estoque.model.Grupo;
import com.estoque.service.GrupoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grupos")
public class GrupoController {

    @Autowired
    private GrupoService grupoService;

    @GetMapping
    public List<Grupo> getAllGrupos() {
        return grupoService.getAllGrupos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Grupo> getGrupoById(@PathVariable(value = "id") Long grupoId) {
        return grupoService.getGrupoById(grupoId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Grupo createGrupo(@RequestBody Grupo grupo) {
        return grupoService.createGrupo(grupo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Grupo> updateGrupo(@PathVariable(value = "id") Long grupoId, @RequestBody Grupo grupoDetails) {
        try {
            Grupo updated = grupoService.updateGrupo(grupoId, grupoDetails);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGrupo(@PathVariable(value = "id") Long grupoId) {
        grupoService.deleteGrupo(grupoId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/parent/{id}")
    public List<Grupo> getGruposFilhos(@PathVariable(value = "id") Long grupoId) {
        return grupoService.getGruposByGrupoPai(grupoId);
    }

    @GetMapping("/root")
    public List<Grupo> getRootGrupos() {
        return grupoService.getRootGrupos();
    }
}
