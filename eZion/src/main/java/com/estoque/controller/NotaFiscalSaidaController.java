package com.estoque.controller;

import com.estoque.model.NotaFiscalSaida;
import com.estoque.service.NotaFiscalSaidaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notas-fiscais-saida")
public class NotaFiscalSaidaController {

    @Autowired
    private NotaFiscalSaidaService notaFiscalSaidaService;

    @PostMapping
    public ResponseEntity<NotaFiscalSaida> createNotaFiscalSaida(@RequestBody NotaFiscalSaida notaFiscalSaida) {
        NotaFiscalSaida created = notaFiscalSaidaService.createNotaFiscalSaida(notaFiscalSaida);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<NotaFiscalSaida>> getAllNotasFiscaisSaida() {
        List<NotaFiscalSaida> notas = notaFiscalSaidaService.getAllNotasFiscaisSaida();
        return ResponseEntity.ok(notas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotaFiscalSaida> getNotaFiscalSaidaById(@PathVariable Long id) {
        Optional<NotaFiscalSaida> nota = notaFiscalSaidaService.getNotaFiscalSaidaById(id);
        return nota.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/numero/{numero}")
    public ResponseEntity<NotaFiscalSaida> getNotaFiscalByNumero(@PathVariable String numero) {
        Optional<NotaFiscalSaida> nota = notaFiscalSaidaService.getNotaFiscalByNumero(numero);
        return nota.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<NotaFiscalSaida>> getNotasFiscaisByStatus(@PathVariable String status) {
        try {
            NotaFiscalSaida.Status s = NotaFiscalSaida.Status.valueOf(status.toUpperCase());
            List<NotaFiscalSaida> notas = notaFiscalSaidaService.getNotasFiscaisByStatus(s);
            return ResponseEntity.ok(notas);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/cliente/{cliente}")
    public ResponseEntity<List<NotaFiscalSaida>> getNotasFiscaisByCliente(@PathVariable String cliente) {
        List<NotaFiscalSaida> notas = notaFiscalSaidaService.getNotasFiscaisByCliente(cliente);
        return ResponseEntity.ok(notas);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NotaFiscalSaida> updateNotaFiscalSaida(@PathVariable Long id, @RequestBody NotaFiscalSaida notaFiscalSaidaDetails) {
        try {
            NotaFiscalSaida updated = notaFiscalSaidaService.updateNotaFiscalSaida(id, notaFiscalSaidaDetails);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotaFiscalSaida(@PathVariable Long id) {
        try {
            notaFiscalSaidaService.deleteNotaFiscalSaida(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/processar")
    public ResponseEntity<?> processarNotaFiscalSaida(@PathVariable Long id) {
        try {
            notaFiscalSaidaService.processarNotaFiscalSaida(id);
            return ResponseEntity.ok().body("NotaFiscalSaida processada com sucesso");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelarNotaFiscalSaida(@PathVariable Long id) {
        try {
            notaFiscalSaidaService.cancelarNotaFiscalSaida(id);
            return ResponseEntity.ok().body("NotaFiscalSaida cancelada com sucesso");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
