package com.seguranca.controller;

import com.seguranca.dto.PermissaoDTO;
import com.seguranca.dto.ErrorResponse;
import com.seguranca.model.Permissao;
import com.seguranca.service.PermissaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/permissoes")
@RequiredArgsConstructor
public class PermissaoController {

    private final PermissaoService permissaoService;

    @GetMapping
    @PreAuthorize("hasAuthority('PERMISSAO_VIEW')")
    public ResponseEntity<?> listarTodas() {
        try {
            List<PermissaoDTO> permissoes = permissaoService.listarTodas()
                .stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());

            return ResponseEntity.ok(permissoes);
        } catch (Exception e) {
            log.error("Erro ao listar permissões: ", e);
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Erro ao listar permissões"));
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('PERMISSAO_VIEW')")
    public ResponseEntity<?> obterPorId(@PathVariable Long id) {
        try {
            Permissao permissao = permissaoService.obterPorId(id)
                .orElseThrow(() -> new RuntimeException("Permissão não encontrada"));

            return ResponseEntity.ok(converterParaDTO(permissao));
        } catch (Exception e) {
            log.error("Erro ao obter permissão: ", e);
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("Permissão não encontrada"));
        }
    }

    @PostMapping
    @PreAuthorize("hasAuthority('PERMISSAO_CREATE')")
    public ResponseEntity<?> criar(@RequestBody PermissaoDTO permissaoDTO) {
        try {
            Permissao permissao = permissaoService.criarPermissao(
                permissaoDTO.getNome(),
                permissaoDTO.getDescricao()
            );

            return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(converterParaDTO(permissao));
        } catch (RuntimeException e) {
            log.error("Erro ao criar permissão: {}", e.getMessage());
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("Erro ao criar permissão: ", e);
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Erro ao criar permissão"));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('PERMISSAO_UPDATE')")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody PermissaoDTO permissaoDTO) {
        try {
            Permissao permissao = permissaoService.atualizarPermissao(
                id,
                permissaoDTO.getNome(),
                permissaoDTO.getDescricao()
            );

            return ResponseEntity.ok(converterParaDTO(permissao));
        } catch (RuntimeException e) {
            log.error("Erro ao atualizar permissão: {}", e.getMessage());
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("Erro ao atualizar permissão: ", e);
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Erro ao atualizar permissão"));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('PERMISSAO_DELETE')")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        try {
            permissaoService.deletarPermissao(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Erro ao deletar permissão: {}", e.getMessage());
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("Erro ao deletar permissão: ", e);
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Erro ao deletar permissão"));
        }
    }

    private PermissaoDTO converterParaDTO(Permissao permissao) {
        return new PermissaoDTO(
            permissao.getId(),
            permissao.getNome(),
            permissao.getDescricao()
        );
    }
}
