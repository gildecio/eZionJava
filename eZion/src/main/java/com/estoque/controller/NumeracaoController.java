package com.estoque.controller;

import com.estoque.model.NumeracaoDocumento;
import com.estoque.service.NumeracaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/numeracoes")
public class NumeracaoController {

    @Autowired
    private NumeracaoService numeracaoService;

    @GetMapping("/{tipoDocumento}")
    public ResponseEntity<NumeracaoDocumento> getNumeracao(@PathVariable String tipoDocumento) {
        try {
            NumeracaoDocumento.TipoDocumento tipo = NumeracaoDocumento.TipoDocumento.valueOf(tipoDocumento.toUpperCase());
            NumeracaoDocumento numeracao = numeracaoService.getNumeracao(tipo);
            if (numeracao != null) {
                return ResponseEntity.ok(numeracao);
            }
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/gerar/{tipoDocumento}")
    public ResponseEntity<?> gerarNumero(@PathVariable String tipoDocumento) {
        try {
            NumeracaoDocumento.TipoDocumento tipo = NumeracaoDocumento.TipoDocumento.valueOf(tipoDocumento.toUpperCase());
            String numero = numeracaoService.gerarNumero(tipo);
            return ResponseEntity.ok().body(Map.of("numero", numero));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Tipo de documento inválido"));
        }
    }

    @PostMapping("/resetar/{tipoDocumento}")
    public ResponseEntity<?> resetarNumeracao(@PathVariable String tipoDocumento) {
        try {
            NumeracaoDocumento.TipoDocumento tipo = NumeracaoDocumento.TipoDocumento.valueOf(tipoDocumento.toUpperCase());
            numeracaoService.resetarNumeracao(tipo);
            return ResponseEntity.ok().body(Map.of("message", "Numeração resetada com sucesso"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Tipo de documento inválido"));
        }
    }

    @PostMapping("/inicializar")
    public ResponseEntity<?> inicializarNumeracoes() {
        try {
            numeracaoService.inicializarNumeracoes();
            return ResponseEntity.ok().body(Map.of("message", "Numerações inicializadas com sucesso"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
