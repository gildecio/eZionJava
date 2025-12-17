package com.estoque.controller;

import com.estoque.model.EstoqueCusto;
import com.estoque.model.EstoqueCusto.TipoCusto;
import com.estoque.service.EstoqueCustoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/estoque/custos")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Custos de Estoque", description = "API para gerenciamento de custos de estoque")
public class EstoqueCustoController {

    private final EstoqueCustoService service;

    @Operation(summary = "Listar todos os custos", description = "Retorna todos os custos de estoque cadastrados")
    @GetMapping
    public ResponseEntity<List<EstoqueCusto>> listarTodos() {
        log.debug("GET /api/estoque/custos - Listar todos os custos");
        List<EstoqueCusto> custos = service.listarTodos();
        return ResponseEntity.ok(custos);
    }

    @Operation(summary = "Buscar custo por ID", description = "Retorna um custo específico pelo ID")
    @GetMapping("/{id}")
    public ResponseEntity<EstoqueCusto> obterPorId(@PathVariable Long id) {
        log.debug("GET /api/estoque/custos/{} - Buscar por ID", id);
        return service.obterPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Listar custos por item", description = "Retorna os custos de um item específico")
    @GetMapping("/item/{itemId}")
    public ResponseEntity<List<EstoqueCusto>> listarPorItem(@PathVariable Long itemId) {
        log.debug("GET /api/estoque/custos/item/{} - Listar por item", itemId);
        List<EstoqueCusto> custos = service.listarPorItem(itemId);
        return ResponseEntity.ok(custos);
    }

    @Operation(summary = "Listar custos por lote", description = "Retorna os custos de um lote específico")
    @GetMapping("/lote/{loteId}")
    public ResponseEntity<List<EstoqueCusto>> listarPorLote(@PathVariable Long loteId) {
        log.debug("GET /api/estoque/custos/lote/{} - Listar por lote", loteId);
        List<EstoqueCusto> custos = service.listarPorLote(loteId);
        return ResponseEntity.ok(custos);
    }

    @Operation(summary = "Listar custos por tipo", description = "Retorna os custos de um tipo específico")
    @GetMapping("/tipo/{tipoCusto}")
    public ResponseEntity<List<EstoqueCusto>> listarPorTipo(@PathVariable TipoCusto tipoCusto) {
        log.debug("GET /api/estoque/custos/tipo/{} - Listar por tipo", tipoCusto);
        List<EstoqueCusto> custos = service.listarPorTipo(tipoCusto);
        return ResponseEntity.ok(custos);
    }

    @Operation(summary = "Listar custos por período", description = "Retorna os custos em um período específico")
    @GetMapping("/periodo")
    public ResponseEntity<List<EstoqueCusto>> listarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
        log.debug("GET /api/estoque/custos/periodo - Listar por período {} - {}", inicio, fim);
        List<EstoqueCusto> custos = service.listarPorPeriodo(inicio, fim);
        return ResponseEntity.ok(custos);
    }

    @Operation(summary = "Listar custos por item e período", description = "Retorna os custos de um item em um período específico")
    @GetMapping("/item/{itemId}/periodo")
    public ResponseEntity<List<EstoqueCusto>> listarPorItemEPeriodo(
            @PathVariable Long itemId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
        log.debug("GET /api/estoque/custos/item/{}/periodo - Listar por item e período {} - {}", itemId, inicio, fim);
        List<EstoqueCusto> custos = service.listarPorItemEPeriodo(itemId, inicio, fim);
        return ResponseEntity.ok(custos);
    }

    @Operation(summary = "Registrar novo custo", description = "Registra um novo custo de estoque")
    @PostMapping
    public ResponseEntity<EstoqueCusto> registrar(@Valid @RequestBody EstoqueCusto estoqueCusto) {
        log.debug("POST /api/estoque/custos - Registrar novo custo");
        try {
            EstoqueCusto novoCusto = service.registrar(estoqueCusto);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoCusto);
        } catch (IllegalArgumentException e) {
            log.error("Erro ao registrar custo: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Calcular custo total do item", description = "Calcula o custo total acumulado de um item")
    @GetMapping("/item/{itemId}/custo-total")
    public ResponseEntity<BigDecimal> calcularCustoTotal(@PathVariable Long itemId) {
        log.debug("GET /api/estoque/custos/item/{}/custo-total - Calcular custo total", itemId);
        BigDecimal custoTotal = service.calcularCustoTotal(itemId);
        return ResponseEntity.ok(custoTotal);
    }

    @Operation(summary = "Calcular custo total do lote", description = "Calcula o custo total acumulado de um lote")
    @GetMapping("/lote/{loteId}/custo-total")
    public ResponseEntity<BigDecimal> calcularCustoTotalLote(@PathVariable Long loteId) {
        log.debug("GET /api/estoque/custos/lote/{}/custo-total - Calcular custo total do lote", loteId);
        BigDecimal custoTotal = service.calcularCustoTotalLote(loteId);
        return ResponseEntity.ok(custoTotal);
    }

    @Operation(summary = "Calcular custo médio ponderado do item", description = "Calcula o custo médio ponderado incluindo impostos")
    @GetMapping("/item/{itemId}/custo-medio-ponderado")
    public ResponseEntity<BigDecimal> calcularCustoMedioPonderado(@PathVariable Long itemId) {
        log.debug("GET /api/estoque/custos/item/{}/custo-medio-ponderado - Calcular custo médio ponderado", itemId);
        BigDecimal custoMedioPonderado = service.calcularCustoMedioPonderado(itemId);
        return ResponseEntity.ok(custoMedioPonderado);
    }

    @Operation(summary = "Calcular total de impostos do item", description = "Calcula o total de impostos acumulados do item")
    @GetMapping("/item/{itemId}/total-impostos")
    public ResponseEntity<BigDecimal> calcularTotalImpostos(@PathVariable Long itemId) {
        log.debug("GET /api/estoque/custos/item/{}/total-impostos - Calcular total de impostos", itemId);
        BigDecimal totalImpostos = service.calcularTotalImpostos(itemId);
        return ResponseEntity.ok(totalImpostos);
    }

    @Operation(summary = "Calcular custo total com frete do item", description = "Calcula o custo total incluindo frete rateado")
    @GetMapping("/item/{itemId}/custo-total-com-frete")
    public ResponseEntity<BigDecimal> calcularCustoTotalComFrete(@PathVariable Long itemId) {
        log.debug("GET /api/estoque/custos/item/{}/custo-total-com-frete - Calcular custo total com frete", itemId);
        BigDecimal custoTotalComFrete = service.calcularCustoTotalComFrete(itemId);
        return ResponseEntity.ok(custoTotalComFrete);
    }

    @Operation(summary = "Calcular frete total rateado do item", description = "Calcula o total de frete rateado acumulado do item")
    @GetMapping("/item/{itemId}/frete-total-rateado")
    public ResponseEntity<BigDecimal> calcularFreteTotalRateado(@PathVariable Long itemId) {
        log.debug("GET /api/estoque/custos/item/{}/frete-total-rateado - Calcular frete total rateado", itemId);
        BigDecimal freteTotalRateado = service.calcularFreteTotalRateado(itemId);
        return ResponseEntity.ok(freteTotalRateado);
    }

    @Operation(summary = "Recalcular custos médios do item", description = "Executa recálculo dos custos médios após lançamentos retroativos")
    @PostMapping("/item/{itemId}/recalcular-custos-medios")
    public ResponseEntity<Void> recalcularCustosMediosItem(@PathVariable Long itemId) {
        log.debug("POST /api/estoque/custos/item/{}/recalcular-custos-medios - Recalculando custos médios", itemId);
        try {
            service.recalcularCustosMediosItem(itemId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Erro ao recalcular custos médios do item: {}", itemId, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "Recalcular custos médios de todos os itens", description = "Executa recálculo geral dos custos médios de todos os itens")
    @PostMapping("/recalcular-todos-custos-medios")
    public ResponseEntity<Void> recalcularCustosMediosTodosItens() {
        log.debug("POST /api/estoque/custos/recalcular-todos-custos-medios - Recalculando custos médios de todos os itens");
        try {
            service.recalcularCustosMediosTodosItens();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Erro ao recalcular custos médios de todos os itens", e);
            return ResponseEntity.internalServerError().build();
        }
    }
}