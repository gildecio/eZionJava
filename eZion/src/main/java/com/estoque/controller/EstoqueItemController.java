package com.estoque.controller;

import com.estoque.model.EstoqueItem;
import com.estoque.model.EstoqueItem.TipoItem;
import com.estoque.service.EstoqueItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estoque")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Estoque", description = "API para gerenciamento de itens de estoque")
public class EstoqueItemController {

    private final EstoqueItemService service;

    @Operation(summary = "Listar todos os itens", description = "Retorna todos os itens de estoque cadastrados")
    @GetMapping
    public ResponseEntity<List<EstoqueItem>> listarTodos() {
        log.debug("GET /api/estoque - Listar todos os itens");
        List<EstoqueItem> itens = service.listarTodos();
        return ResponseEntity.ok(itens);
    }

    @Operation(summary = "Listar itens ativos", description = "Retorna apenas os itens ativos")
    @GetMapping("/ativos")
    public ResponseEntity<List<EstoqueItem>> listarAtivos() {
        log.debug("GET /api/estoque/ativos - Listar itens ativos");
        List<EstoqueItem> itens = service.listarAtivos();
        return ResponseEntity.ok(itens);
    }

    @Operation(summary = "Buscar item por ID", description = "Retorna um item específico pelo ID")
    @GetMapping("/{id}")
    public ResponseEntity<EstoqueItem> obterPorId(@PathVariable Long id) {
        log.debug("GET /api/estoque/{} - Buscar por ID", id);
        return service.obterPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Buscar item por código", description = "Retorna um item específico pelo código")
    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<EstoqueItem> obterPorCodigo(@PathVariable String codigo) {
        log.debug("GET /api/estoque/codigo/{} - Buscar por código", codigo);
        return service.obterPorCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Listar itens por tipo", description = "Retorna itens de um tipo específico")
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<EstoqueItem>> listarPorTipo(@PathVariable TipoItem tipo) {
        log.debug("GET /api/estoque/tipo/{} - Listar por tipo", tipo);
        List<EstoqueItem> itens = service.listarPorTipo(tipo);
        return ResponseEntity.ok(itens);
    }

    @Operation(summary = "Buscar itens por descrição", description = "Busca itens que contenham o texto na descrição")
    @GetMapping("/buscar")
    public ResponseEntity<List<EstoqueItem>> buscarPorDescricao(@RequestParam String descricao) {
        log.debug("GET /api/estoque/buscar?descricao={}", descricao);
        List<EstoqueItem> itens = service.buscarPorDescricao(descricao);
        return ResponseEntity.ok(itens);
    }

    @Operation(summary = "Criar novo item", description = "Cadastra um novo item de estoque")
    @PostMapping
    public ResponseEntity<EstoqueItem> criar(@Valid @RequestBody EstoqueItem item) {
        log.debug("POST /api/estoque - Criar novo item: {}", item.getCodigo());
        try {
            EstoqueItem novoItem = service.criar(item);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoItem);
        } catch (IllegalArgumentException e) {
            log.error("Erro ao criar item: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Atualizar item", description = "Atualiza os dados de um item existente")
    @PutMapping("/{id}")
    public ResponseEntity<EstoqueItem> atualizar(@PathVariable Long id, @Valid @RequestBody EstoqueItem item) {
        log.debug("PUT /api/estoque/{} - Atualizar item", id);
        try {
            EstoqueItem itemAtualizado = service.atualizar(id, item);
            return ResponseEntity.ok(itemAtualizado);
        } catch (IllegalArgumentException e) {
            log.error("Erro ao atualizar item: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Ativar item", description = "Ativa um item de estoque")
    @PutMapping("/{id}/ativar")
    public ResponseEntity<EstoqueItem> ativar(@PathVariable Long id) {
        log.debug("PUT /api/estoque/{}/ativar", id);
        try {
            EstoqueItem item = service.ativar(id);
            return ResponseEntity.ok(item);
        } catch (IllegalArgumentException e) {
            log.error("Erro ao ativar item: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Desativar item", description = "Desativa um item de estoque")
    @PutMapping("/{id}/desativar")
    public ResponseEntity<EstoqueItem> desativar(@PathVariable Long id) {
        log.debug("PUT /api/estoque/{}/desativar", id);
        try {
            EstoqueItem item = service.desativar(id);
            return ResponseEntity.ok(item);
        } catch (IllegalArgumentException e) {
            log.error("Erro ao desativar item: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Excluir item (lógico)", description = "Desativa um item (exclusão lógica)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        log.debug("DELETE /api/estoque/{} - Exclusão lógica", id);
        try {
            service.excluir(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.error("Erro ao excluir item: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Excluir item (definitivo)", description = "Remove permanentemente um item do banco de dados")
    @DeleteMapping("/{id}/definitivo")
    public ResponseEntity<Void> excluirDefinitivamente(@PathVariable Long id) {
        log.debug("DELETE /api/estoque/{}/definitivo - Exclusão física", id);
        try {
            service.excluirDefinitivamente(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.error("Erro ao excluir definitivamente item: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
