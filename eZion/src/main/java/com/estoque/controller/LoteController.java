package com.estoque.controller;

import com.estoque.model.Lote;
import com.estoque.service.LoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/lotes")
@RequiredArgsConstructor
@Tag(name = "Lotes", description = "API para gerenciamento de lotes de estoque")
public class LoteController {

    private final LoteService loteService;

    @PostMapping
    @Operation(summary = "Criar novo lote")
    public ResponseEntity<Lote> criar(@Valid @RequestBody Lote lote) {
        try {
            Lote novoLote = loteService.criar(lote);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoLote);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    @Operation(summary = "Listar todos os lotes")
    public ResponseEntity<List<Lote>> listarTodos() {
        List<Lote> lotes = loteService.listarTodos();
        return ResponseEntity.ok(lotes);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar lote por ID")
    public ResponseEntity<Lote> buscarPorId(@PathVariable Long id) {
        return loteService.obterPorId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/numero/{numeroLote}")
    @Operation(summary = "Buscar lote por número")
    public ResponseEntity<Lote> buscarPorNumero(@PathVariable String numeroLote) {
        return loteService.buscarPorNumeroLote(numeroLote)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/item/{itemId}")
    @Operation(summary = "Listar lotes de um item")
    public ResponseEntity<List<Lote>> listarPorItem(@PathVariable Long itemId) {
        try {
            List<Lote> lotes = loteService.listarPorItem(itemId);
            return ResponseEntity.ok(lotes);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/item/{itemId}/fifo")
    @Operation(summary = "Listar lotes disponíveis (FIFO)")
    public ResponseEntity<List<Lote>> listarLotesDisponiveisFIFO(@PathVariable Long itemId) {
        try {
            List<Lote> lotes = loteService.listarLotesDisponiveisFIFO(itemId);
            return ResponseEntity.ok(lotes);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/vencidos")
    @Operation(summary = "Listar lotes vencidos")
    public ResponseEntity<List<Lote>> listarVencidos() {
        List<Lote> lotes = loteService.listarLotesVencidos();
        return ResponseEntity.ok(lotes);
    }

    @GetMapping("/para-vencer")
    @Operation(summary = "Listar lotes para vencer em um período")
    public ResponseEntity<List<Lote>> listarParaVencer(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        List<Lote> lotes = loteService.listarLotesParaVencer(inicio, fim);
        return ResponseEntity.ok(lotes);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar lote")
    public ResponseEntity<Lote> atualizar(@PathVariable Long id, @Valid @RequestBody Lote lote) {
        try {
            Lote loteAtualizado = loteService.atualizar(id, lote);
            return ResponseEntity.ok(loteAtualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/reduzir-quantidade")
    @Operation(summary = "Reduzir quantidade disponível do lote")
    public ResponseEntity<Void> reduzirQuantidade(
            @PathVariable Long id,
            @RequestParam BigDecimal quantidade) {
        try {
            loteService.reduzirQuantidade(id, quantidade);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/aumentar-quantidade")
    @Operation(summary = "Aumentar quantidade disponível do lote")
    public ResponseEntity<Void> aumentarQuantidade(
            @PathVariable Long id,
            @RequestParam BigDecimal quantidade) {
        try {
            loteService.aumentarQuantidade(id, quantidade);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/item/{itemId}/estoque-disponivel")
    @Operation(summary = "Calcular estoque disponível de um item")
    public ResponseEntity<BigDecimal> calcularEstoqueDisponivel(@PathVariable Long itemId) {
        try {
            BigDecimal estoque = loteService.calcularEstoqueDisponivel(itemId);
            return ResponseEntity.ok(estoque);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/item/{itemId}/estoque-total")
    @Operation(summary = "Calcular estoque total de um item")
    public ResponseEntity<BigDecimal> calcularEstoqueTotal(@PathVariable Long itemId) {
        try {
            BigDecimal estoque = loteService.calcularEstoqueTotal(itemId);
            return ResponseEntity.ok(estoque);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/desativar")
    @Operation(summary = "Desativar lote")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        try {
            loteService.desativar(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir lote")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        try {
            loteService.excluir(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}