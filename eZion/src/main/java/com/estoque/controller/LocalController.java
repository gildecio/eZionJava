package com.estoque.controller;

import com.estoque.model.Local;
import com.estoque.service.LocalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locais")
@RequiredArgsConstructor
@Tag(name = "Locais", description = "API para gerenciamento de locais")
public class LocalController {

    private final LocalService localService;

    @PostMapping
    @Operation(summary = "Criar novo local")
    public ResponseEntity<Local> criar(@Valid @RequestBody Local local) {
        Local novoLocal = localService.criar(local);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoLocal);
    }

    @GetMapping
    @Operation(summary = "Listar todos os locais")
    public ResponseEntity<List<Local>> listarTodos() {
        List<Local> locais = localService.listarTodos();
        return ResponseEntity.ok(locais);
    }

    @GetMapping("/ativos")
    @Operation(summary = "Listar locais ativos")
    public ResponseEntity<List<Local>> listarAtivos() {
        List<Local> locais = localService.listarAtivos();
        return ResponseEntity.ok(locais);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar local por ID")
    public ResponseEntity<Local> buscarPorId(@PathVariable Long id) {
        return localService.buscarPorId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/nome/{nome}")
    @Operation(summary = "Buscar local por nome")
    public ResponseEntity<Local> buscarPorNome(@PathVariable String nome) {
        return localService.buscarPorNome(nome)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar locais por nome parcial")
    public ResponseEntity<List<Local>> buscarPorNomeParcial(@RequestParam String nome) {
        List<Local> locais = localService.buscarPorNomeParcial(nome);
        return ResponseEntity.ok(locais);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar local")
    public ResponseEntity<Local> atualizar(@PathVariable Long id, @Valid @RequestBody Local local) {
        try {
            Local localAtualizado = localService.atualizar(id, local);
            return ResponseEntity.ok(localAtualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/ativar")
    @Operation(summary = "Ativar local")
    public ResponseEntity<Void> ativar(@PathVariable Long id) {
        try {
            localService.ativar(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/desativar")
    @Operation(summary = "Desativar local")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        try {
            localService.desativar(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir local")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        try {
            localService.excluir(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}