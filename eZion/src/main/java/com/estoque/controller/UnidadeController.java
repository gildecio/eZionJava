package com.estoque.controller;

import com.estoque.model.Unidade;
import com.estoque.service.UnidadeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/unidades")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Unidades", description = "API para gerenciamento de unidades de medida")
public class UnidadeController {

    private final UnidadeService service;

    @Operation(summary = "Listar todas as unidades", description = "Retorna todas as unidades cadastradas")
    @GetMapping
    public ResponseEntity<List<Unidade>> listarTodas() {
        log.debug("GET /api/unidades");
        List<Unidade> unidades = service.listarTodas();
        return ResponseEntity.ok(unidades);
    }

    @Operation(summary = "Listar unidades ativas", description = "Retorna apenas unidades ativas")
    @GetMapping("/ativas")
    public ResponseEntity<List<Unidade>> listarAtivas() {
        log.debug("GET /api/unidades/ativas");
        List<Unidade> unidades = service.listarAtivas();
        return ResponseEntity.ok(unidades);
    }

    @Operation(summary = "Buscar unidade por ID", description = "Retorna uma unidade específica pelo ID")
    @GetMapping("/{id}")
    public ResponseEntity<Unidade> buscarPorId(@PathVariable Long id) {
        log.debug("GET /api/unidades/{}", id);
        Optional<Unidade> unidade = service.buscarPorId(id);
        return unidade.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Buscar unidade por sigla", description = "Retorna uma unidade específica pela sigla")
    @GetMapping("/sigla/{sigla}")
    public ResponseEntity<Unidade> buscarPorSigla(@PathVariable String sigla) {
        log.debug("GET /api/unidades/sigla/{}", sigla);
        Optional<Unidade> unidade = service.buscarPorSigla(sigla);
        return unidade.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Criar nova unidade", description = "Cadastra uma nova unidade de medida")
    @PostMapping
    public ResponseEntity<Unidade> criar(@Valid @RequestBody Unidade unidade) {
        log.debug("POST /api/unidades - Criar nova unidade: {}", unidade.getSigla());
        try {
            Unidade novaUnidade = service.criar(unidade);
            return ResponseEntity.status(HttpStatus.CREATED).body(novaUnidade);
        } catch (IllegalArgumentException e) {
            log.error("Erro ao criar unidade: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Atualizar unidade", description = "Atualiza os dados de uma unidade existente")
    @PutMapping("/{id}")
    public ResponseEntity<Unidade> atualizar(@PathVariable Long id, @Valid @RequestBody Unidade unidade) {
        log.debug("PUT /api/unidades/{} - Atualizar unidade", id);
        try {
            Unidade unidadeAtualizada = service.atualizar(id, unidade);
            return ResponseEntity.ok(unidadeAtualizada);
        } catch (IllegalArgumentException e) {
            log.error("Erro ao atualizar unidade: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Ativar unidade", description = "Ativa uma unidade de medida")
    @PutMapping("/{id}/ativar")
    public ResponseEntity<Unidade> ativar(@PathVariable Long id) {
        log.debug("PUT /api/unidades/{}/ativar", id);
        try {
            Unidade unidade = service.ativar(id);
            return ResponseEntity.ok(unidade);
        } catch (IllegalArgumentException e) {
            log.error("Erro ao ativar unidade: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Desativar unidade", description = "Desativa uma unidade de medida")
    @PutMapping("/{id}/desativar")
    public ResponseEntity<Unidade> desativar(@PathVariable Long id) {
        log.debug("PUT /api/unidades/{}/desativar", id);
        try {
            Unidade unidade = service.desativar(id);
            return ResponseEntity.ok(unidade);
        } catch (IllegalArgumentException e) {
            log.error("Erro ao desativar unidade: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Excluir unidade", description = "Remove uma unidade de medida")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        log.debug("DELETE /api/unidades/{}", id);
        try {
            service.excluir(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.error("Erro ao excluir unidade: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Listar unidades base", description = "Retorna apenas unidades base (sem unidade pai)")
    @GetMapping("/base")
    public ResponseEntity<List<Unidade>> listarUnidadesBase() {
        log.debug("GET /api/unidades/base");
        List<Unidade> unidades = service.listarUnidadesBase();
        return ResponseEntity.ok(unidades);
    }

    @Operation(summary = "Listar unidades derivadas", description = "Retorna unidades derivadas de uma unidade pai específica")
    @GetMapping("/derivadas/{unidadePaiId}")
    public ResponseEntity<List<Unidade>> listarUnidadesDerivadas(@PathVariable Long unidadePaiId) {
        log.debug("GET /api/unidades/derivadas/{}", unidadePaiId);
        List<Unidade> unidades = service.listarUnidadesDerivadas(unidadePaiId);
        return ResponseEntity.ok(unidades);
    }

    @Operation(summary = "Listar unidades com fator", description = "Retorna unidades que possuem fator de conversão definido")
    @GetMapping("/com-fator")
    public ResponseEntity<List<Unidade>> listarUnidadesComFator() {
        log.debug("GET /api/unidades/com-fator");
        List<Unidade> unidades = service.listarUnidadesComFator();
        return ResponseEntity.ok(unidades);
    }

    @Operation(summary = "Calcular fator total", description = "Calcula o fator total de uma unidade em relação à unidade base")
    @GetMapping("/{id}/fator-total")
    public ResponseEntity<java.math.BigDecimal> calcularFatorTotal(@PathVariable Long id) {
        log.debug("GET /api/unidades/{}/fator-total", id);
        try {
            java.math.BigDecimal fatorTotal = service.calcularFatorTotal(id);
            return ResponseEntity.ok(fatorTotal);
        } catch (IllegalArgumentException e) {
            log.error("Erro ao calcular fator total: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}