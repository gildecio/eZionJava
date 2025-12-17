package com.estoque.controller;

import com.estoque.model.Grupo;
import com.estoque.service.GrupoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grupos")
@RequiredArgsConstructor
@Tag(name = "Grupos", description = "API para gerenciamento de grupos")
public class GrupoController {

    private final GrupoService grupoService;

    @PostMapping
    @Operation(summary = "Criar novo grupo")
    public ResponseEntity<Grupo> criar(@Valid @RequestBody Grupo grupo) {
        Grupo novoGrupo = grupoService.criar(grupo);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoGrupo);
    }

    @GetMapping
    @Operation(summary = "Listar todos os grupos")
    public ResponseEntity<List<Grupo>> listarTodos() {
        List<Grupo> grupos = grupoService.listarTodos();
        return ResponseEntity.ok(grupos);
    }

    @GetMapping("/ativos")
    @Operation(summary = "Listar grupos ativos")
    public ResponseEntity<List<Grupo>> listarAtivos() {
        List<Grupo> grupos = grupoService.listarAtivos();
        return ResponseEntity.ok(grupos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar grupo por ID")
    public ResponseEntity<Grupo> buscarPorId(@PathVariable Long id) {
        return grupoService.buscarPorId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/nome/{nome}")
    @Operation(summary = "Buscar grupo por nome")
    public ResponseEntity<Grupo> buscarPorNome(@PathVariable String nome) {
        return grupoService.buscarPorNome(nome)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar grupos por nome parcial")
    public ResponseEntity<List<Grupo>> buscarPorNomeParcial(@RequestParam String nome) {
        List<Grupo> grupos = grupoService.buscarPorNomeParcial(nome);
        return ResponseEntity.ok(grupos);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar grupo")
    public ResponseEntity<Grupo> atualizar(@PathVariable Long id, @Valid @RequestBody Grupo grupo) {
        try {
            Grupo grupoAtualizado = grupoService.atualizar(id, grupo);
            return ResponseEntity.ok(grupoAtualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/ativar")
    @Operation(summary = "Ativar grupo")
    public ResponseEntity<Void> ativar(@PathVariable Long id) {
        try {
            grupoService.ativar(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/desativar")
    @Operation(summary = "Desativar grupo")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        try {
            grupoService.desativar(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir grupo")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        try {
            grupoService.excluir(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoints para hierarquia
    @GetMapping("/raiz")
    @Operation(summary = "Listar grupos raiz (sem pai)")
    public ResponseEntity<List<Grupo>> listarGruposRaiz() {
        List<Grupo> grupos = grupoService.listarGruposRaiz();
        return ResponseEntity.ok(grupos);
    }

    @GetMapping("/{id}/filhos")
    @Operation(summary = "Listar filhos de um grupo")
    public ResponseEntity<List<Grupo>> listarFilhos(@PathVariable Long id) {
        try {
            List<Grupo> filhos = grupoService.listarFilhos(id);
            return ResponseEntity.ok(filhos);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}/validado")
    @Operation(summary = "Excluir grupo com validação de filhos")
    public ResponseEntity<Void> excluirComValidacao(@PathVariable Long id) {
        try {
            grupoService.excluirComValidacao(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}