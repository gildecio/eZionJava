package com.fiscal.controller;

import com.fiscal.model.NaturezaOperacao;
import com.fiscal.service.NaturezaOperacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/naturezas-operacao")
public class NaturezaOperacaoController {

    @Autowired
    private NaturezaOperacaoService naturezaOperacaoService;

    @GetMapping
    public List<NaturezaOperacao> getAllNaturezasOperacao() {
        return naturezaOperacaoService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<NaturezaOperacao> getNaturezaOperacaoById(@PathVariable Long id) {
        return naturezaOperacaoService.findById(id)
                .map(natureza -> ResponseEntity.ok(natureza))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public NaturezaOperacao createNaturezaOperacao(@RequestBody NaturezaOperacao naturezaOperacao) {
        return naturezaOperacaoService.save(naturezaOperacao);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NaturezaOperacao> updateNaturezaOperacao(@PathVariable Long id, @RequestBody NaturezaOperacao naturezaOperacaoDetails) {
        return naturezaOperacaoService.findById(id)
                .map(natureza -> {
                    natureza.setCodigo(naturezaOperacaoDetails.getCodigo());
                    natureza.setDescricao(naturezaOperacaoDetails.getDescricao());
                    natureza.setTipo(naturezaOperacaoDetails.getTipo());
                    NaturezaOperacao updatedNatureza = naturezaOperacaoService.save(natureza);
                    return ResponseEntity.ok(updatedNatureza);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNaturezaOperacao(@PathVariable Long id) {
        if (naturezaOperacaoService.findById(id).isPresent()) {
            naturezaOperacaoService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
