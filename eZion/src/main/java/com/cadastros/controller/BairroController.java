package com.cadastros.controller;

import com.cadastros.model.Bairro;
import com.cadastros.service.BairroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bairros")
@RequiredArgsConstructor
@Tag(name = "Bairros", description = "API para gerenciamento de bairros")
public class BairroController {

    private final BairroService bairroService;

    @PostMapping
    @Operation(summary = "Criar novo bairro")
    public ResponseEntity<Bairro> criar(@Valid @RequestBody Bairro bairro) {
        try {
            Bairro novoBairro = bairroService.criar(bairro);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoBairro);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar bairro")
    public ResponseEntity<Bairro> atualizar(@PathVariable Long id, @Valid @RequestBody Bairro bairro) {
        try {
            Bairro bairroAtualizado = bairroService.atualizar(id, bairro);
            return ResponseEntity.ok(bairroAtualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar bairro por ID")
    public ResponseEntity<Bairro> buscarPorId(@PathVariable Long id) {
        return bairroService.obterPorId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cidade/{cidadeId}")
    @Operation(summary = "Listar bairros por cidade")
    public ResponseEntity<List<Bairro>> listarPorCidade(@PathVariable Long cidadeId) {
        List<Bairro> bairros = bairroService.listarPorCidade(cidadeId);
        return ResponseEntity.ok(bairros);
    }

    @GetMapping("/cidade/{cidadeId}/ativos")
    @Operation(summary = "Listar bairros ativos por cidade")
    public ResponseEntity<List<Bairro>> listarAtivosPorCidade(@PathVariable Long cidadeId) {
        List<Bairro> bairros = bairroService.listarAtivosPorCidade(cidadeId);
        return ResponseEntity.ok(bairros);
    }

    @GetMapping
    @Operation(summary = "Listar todos os bairros")
    public ResponseEntity<List<Bairro>> listarTodos() {
        List<Bairro> bairros = bairroService.listarTodos();
        return ResponseEntity.ok(bairros);
    }

    @GetMapping("/ativos")
    @Operation(summary = "Listar bairros ativos")
    public ResponseEntity<List<Bairro>> listarAtivos() {
        List<Bairro> bairros = bairroService.listarAtivos();
        return ResponseEntity.ok(bairros);
    }

    @GetMapping("/buscar-por-nome")
    @Operation(summary = "Buscar bairros por nome")
    public ResponseEntity<List<Bairro>> buscarPorNome(@RequestParam String nome) {
        List<Bairro> bairros = bairroService.buscarPorNome(nome);
        return ResponseEntity.ok(bairros);
    }

    @PutMapping("/{id}/desativar")
    @Operation(summary = "Desativar bairro")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        try {
            bairroService.desativar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}