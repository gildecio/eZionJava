package com.estoque.controller;

import com.estoque.model.Movimentacao;
import com.estoque.service.MovimentacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/movimentacoes")
@RequiredArgsConstructor
@Tag(name = "Movimentações", description = "API para gerenciamento de movimentações de estoque")
public class MovimentacaoController {

    private final MovimentacaoService movimentacaoService;

    @PostMapping
    @Operation(summary = "Registrar nova movimentação")
    public ResponseEntity<Movimentacao> registrar(@Valid @RequestBody Movimentacao movimentacao) {
        try {
            Movimentacao novaMovimentacao = movimentacaoService.registrar(movimentacao);
            return ResponseEntity.status(HttpStatus.CREATED).body(novaMovimentacao);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    @Operation(summary = "Listar todas as movimentações")
    public ResponseEntity<List<Movimentacao>> listarTodas() {
        List<Movimentacao> movimentacoes = movimentacaoService.listarTodas();
        return ResponseEntity.ok(movimentacoes);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar movimentação por ID")
    public ResponseEntity<Movimentacao> buscarPorId(@PathVariable Long id) {
        return movimentacaoService.obterPorId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/item/{itemId}")
    @Operation(summary = "Listar movimentações de um item")
    public ResponseEntity<List<Movimentacao>> listarPorItem(@PathVariable Long itemId) {
        try {
            List<Movimentacao> movimentacoes = movimentacaoService.listarPorItem(itemId);
            return ResponseEntity.ok(movimentacoes);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/item/{itemId}/periodo")
    @Operation(summary = "Listar movimentações de um item por período")
    public ResponseEntity<List<Movimentacao>> listarPorItemEPeriodo(
            @PathVariable Long itemId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
        try {
            List<Movimentacao> movimentacoes = movimentacaoService.listarPorItemEPeriodo(itemId, inicio, fim);
            return ResponseEntity.ok(movimentacoes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/local/{localId}")
    @Operation(summary = "Listar movimentações por local")
    public ResponseEntity<List<Movimentacao>> listarPorLocal(@PathVariable Long localId) {
        try {
            List<Movimentacao> movimentacoes = movimentacaoService.listarPorLocal(localId);
            return ResponseEntity.ok(movimentacoes);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/tipo/{tipo}")
    @Operation(summary = "Listar movimentações por tipo")
    public ResponseEntity<List<Movimentacao>> listarPorTipo(@PathVariable Movimentacao.TipoMovimentacao tipo) {
        List<Movimentacao> movimentacoes = movimentacaoService.listarPorTipo(tipo);
        return ResponseEntity.ok(movimentacoes);
    }

    @GetMapping("/periodo")
    @Operation(summary = "Listar movimentações por período")
    public ResponseEntity<List<Movimentacao>> listarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
        List<Movimentacao> movimentacoes = movimentacaoService.listarPorPeriodo(inicio, fim);
        return ResponseEntity.ok(movimentacoes);
    }

    @GetMapping("/item/{itemId}/saldo")
    @Operation(summary = "Calcular saldo atual de um item")
    public ResponseEntity<BigDecimal> calcularSaldoAtual(@PathVariable Long itemId) {
        try {
            BigDecimal saldo = movimentacaoService.calcularSaldoAtual(itemId);
            return ResponseEntity.ok(saldo);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir movimentação")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        try {
            movimentacaoService.excluir(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}