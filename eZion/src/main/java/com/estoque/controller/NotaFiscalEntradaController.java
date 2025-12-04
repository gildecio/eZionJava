package com.estoque.controller;

import com.estoque.model.NotaFiscalEntrada;
import com.estoque.service.NotaFiscalEntradaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notas-fiscais-entrada")
public class NotaFiscalEntradaController {

    @Autowired
    private NotaFiscalEntradaService notaFiscalEntradaService;

    @PostMapping
    public ResponseEntity<NotaFiscalEntrada> createNotaFiscalEntrada(@RequestBody NotaFiscalEntrada notaFiscalEntrada) {
        NotaFiscalEntrada created = notaFiscalEntradaService.createNotaFiscalEntrada(notaFiscalEntrada);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<NotaFiscalEntrada>> getAllNotasFiscaisEntrada() {
        List<NotaFiscalEntrada> notas = notaFiscalEntradaService.getAllNotasFiscaisEntrada();
        return ResponseEntity.ok(notas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotaFiscalEntrada> getNotaFiscalEntradaById(@PathVariable Long id) {
        Optional<NotaFiscalEntrada> nota = notaFiscalEntradaService.getNotaFiscalEntradaById(id);
        return nota.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/numero/{numero}")
    public ResponseEntity<NotaFiscalEntrada> getNotaFiscalByNumero(@PathVariable String numero) {
        Optional<NotaFiscalEntrada> nota = notaFiscalEntradaService.getNotaFiscalByNumero(numero);
        return nota.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<NotaFiscalEntrada>> getNotasFiscaisByStatus(@PathVariable String status) {
        try {
            NotaFiscalEntrada.Status s = NotaFiscalEntrada.Status.valueOf(status.toUpperCase());
            List<NotaFiscalEntrada> notas = notaFiscalEntradaService.getNotasFiscaisByStatus(s);
            return ResponseEntity.ok(notas);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/fornecedor/{fornecedor}")
    public ResponseEntity<List<NotaFiscalEntrada>> getNotasFiscaisByFornecedor(@PathVariable String fornecedor) {
        List<NotaFiscalEntrada> notas = notaFiscalEntradaService.getNotasFiscaisByFornecedor(fornecedor);
        return ResponseEntity.ok(notas);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NotaFiscalEntrada> updateNotaFiscalEntrada(@PathVariable Long id, @RequestBody NotaFiscalEntrada notaFiscalEntradaDetails) {
        try {
            NotaFiscalEntrada updated = notaFiscalEntradaService.updateNotaFiscalEntrada(id, notaFiscalEntradaDetails);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotaFiscalEntrada(@PathVariable Long id) {
        try {
            notaFiscalEntradaService.deleteNotaFiscalEntrada(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/processar")
    public ResponseEntity<?> processarNotaFiscalEntrada(@PathVariable Long id) {
        try {
            notaFiscalEntradaService.processarNotaFiscalEntrada(id);
            return ResponseEntity.ok().body("NotaFiscalEntrada processada com sucesso");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelarNotaFiscalEntrada(@PathVariable Long id) {
        try {
            notaFiscalEntradaService.cancelarNotaFiscalEntrada(id);
            return ResponseEntity.ok().body("NotaFiscalEntrada cancelada com sucesso");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
