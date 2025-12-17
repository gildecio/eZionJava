package com.cadastros.controller;

import com.cadastros.model.Cidade;
import com.cadastros.service.CidadeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cidades")
@RequiredArgsConstructor
@Tag(name = "Cidades", description = "API para gerenciamento de cidades")
public class CidadeController {

    private final CidadeService cidadeService;

    @PostMapping
    @Operation(summary = "Criar nova cidade")
    public ResponseEntity<Cidade> criar(@Valid @RequestBody Cidade cidade) {
        try {
            Cidade novaCidade = cidadeService.criar(cidade);
            return ResponseEntity.status(HttpStatus.CREATED).body(novaCidade);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar cidade")
    public ResponseEntity<Cidade> atualizar(@PathVariable Long id, @Valid @RequestBody Cidade cidade) {
        try {
            Cidade cidadeAtualizada = cidadeService.atualizar(id, cidade);
            return ResponseEntity.ok(cidadeAtualizada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar cidade por ID")
    public ResponseEntity<Cidade> buscarPorId(@PathVariable Long id) {
        return cidadeService.obterPorId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar cidade por nome e UF")
    public ResponseEntity<Cidade> buscarPorNomeEUf(@RequestParam String nome, @RequestParam String uf) {
        return cidadeService.obterPorNomeEUf(nome, uf)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/uf/{uf}")
    @Operation(summary = "Listar cidades por UF")
    public ResponseEntity<List<Cidade>> listarPorUf(@PathVariable String uf) {
        List<Cidade> cidades = cidadeService.listarPorUf(uf);
        return ResponseEntity.ok(cidades);
    }

    @GetMapping
    @Operation(summary = "Listar todas as cidades")
    public ResponseEntity<List<Cidade>> listarTodas() {
        List<Cidade> cidades = cidadeService.listarTodas();
        return ResponseEntity.ok(cidades);
    }

    @GetMapping("/ativas")
    @Operation(summary = "Listar cidades ativas")
    public ResponseEntity<List<Cidade>> listarAtivas() {
        List<Cidade> cidades = cidadeService.listarAtivas();
        return ResponseEntity.ok(cidades);
    }

    @GetMapping("/buscar-por-nome")
    @Operation(summary = "Buscar cidades por nome")
    public ResponseEntity<List<Cidade>> buscarPorNome(@RequestParam String nome) {
        List<Cidade> cidades = cidadeService.buscarPorNome(nome);
        return ResponseEntity.ok(cidades);
    }

    @PutMapping("/{id}/desativar")
    @Operation(summary = "Desativar cidade")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        try {
            cidadeService.desativar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}