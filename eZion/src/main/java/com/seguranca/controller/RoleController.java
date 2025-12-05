package com.seguranca.controller;

import com.seguranca.dto.RoleDTO;
import com.seguranca.dto.ErrorResponse;
import com.seguranca.model.Role;
import com.seguranca.service.RoleService;
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
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_VIEW')")
    public ResponseEntity<?> listarTodas() {
        try {
            List<RoleDTO> roles = roleService.listarTodas()
                .stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());

            return ResponseEntity.ok(roles);
        } catch (Exception e) {
            log.error("Erro ao listar roles: ", e);
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Erro ao listar roles"));
        }
    }

    @GetMapping("/ativas")
    @PreAuthorize("hasAuthority('ROLE_VIEW')")
    public ResponseEntity<?> listarAtivas() {
        try {
            List<RoleDTO> roles = roleService.listarAtivas()
                .stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());

            return ResponseEntity.ok(roles);
        } catch (Exception e) {
            log.error("Erro ao listar roles ativas: ", e);
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Erro ao listar roles ativas"));
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_VIEW')")
    public ResponseEntity<?> obterPorId(@PathVariable Long id) {
        try {
            Role role = roleService.obterPorId(id)
                .orElseThrow(() -> new RuntimeException("Role não encontrada"));

            return ResponseEntity.ok(converterParaDTO(role));
        } catch (Exception e) {
            log.error("Erro ao obter role: ", e);
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("Role não encontrada"));
        }
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_CREATE')")
    public ResponseEntity<?> criar(@RequestBody RoleDTO roleDTO) {
        try {
            Role role = roleService.criarRole(
                roleDTO.getNome(),
                roleDTO.getDescricao()
            );

            return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(converterParaDTO(role));
        } catch (RuntimeException e) {
            log.error("Erro ao criar role: {}", e.getMessage());
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("Erro ao criar role: ", e);
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Erro ao criar role"));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_UPDATE')")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody RoleDTO roleDTO) {
        try {
            Role role = roleService.atualizarRole(
                id,
                roleDTO.getNome(),
                roleDTO.getDescricao()
            );

            return ResponseEntity.ok(converterParaDTO(role));
        } catch (RuntimeException e) {
            log.error("Erro ao atualizar role: {}", e.getMessage());
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("Erro ao atualizar role: ", e);
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Erro ao atualizar role"));
        }
    }

    @PostMapping("/{roleId}/permissoes/{permissaoId}")
    @PreAuthorize("hasAuthority('ROLE_UPDATE')")
    public ResponseEntity<?> adicionarPermissao(@PathVariable Long roleId, @PathVariable Long permissaoId) {
        try {
            Role role = roleService.adicionarPermissao(roleId, permissaoId);
            return ResponseEntity.ok(converterParaDTO(role));
        } catch (RuntimeException e) {
            log.error("Erro ao adicionar permissão: {}", e.getMessage());
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("Erro ao adicionar permissão: ", e);
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Erro ao adicionar permissão"));
        }
    }

    @DeleteMapping("/{roleId}/permissoes/{permissaoId}")
    @PreAuthorize("hasAuthority('ROLE_UPDATE')")
    public ResponseEntity<?> removerPermissao(@PathVariable Long roleId, @PathVariable Long permissaoId) {
        try {
            Role role = roleService.removerPermissao(roleId, permissaoId);
            return ResponseEntity.ok(converterParaDTO(role));
        } catch (RuntimeException e) {
            log.error("Erro ao remover permissão: {}", e.getMessage());
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("Erro ao remover permissão: ", e);
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Erro ao remover permissão"));
        }
    }

    @PutMapping("/{id}/ativar")
    @PreAuthorize("hasAuthority('ROLE_UPDATE')")
    public ResponseEntity<?> ativar(@PathVariable Long id) {
        try {
            Role role = roleService.ativarRole(id);
            return ResponseEntity.ok(converterParaDTO(role));
        } catch (RuntimeException e) {
            log.error("Erro ao ativar role: {}", e.getMessage());
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("Erro ao ativar role: ", e);
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Erro ao ativar role"));
        }
    }

    @PutMapping("/{id}/desativar")
    @PreAuthorize("hasAuthority('ROLE_UPDATE')")
    public ResponseEntity<?> desativar(@PathVariable Long id) {
        try {
            Role role = roleService.desativarRole(id);
            return ResponseEntity.ok(converterParaDTO(role));
        } catch (RuntimeException e) {
            log.error("Erro ao desativar role: {}", e.getMessage());
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("Erro ao desativar role: ", e);
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Erro ao desativar role"));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_DELETE')")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        try {
            roleService.deletarRole(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Erro ao deletar role: {}", e.getMessage());
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("Erro ao deletar role: ", e);
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Erro ao deletar role"));
        }
    }

    private RoleDTO converterParaDTO(Role role) {
        return new RoleDTO(
            role.getId(),
            role.getNome(),
            role.getDescricao(),
            role.getAtivo(),
            role.getPermissoes().stream()
                .map(p -> new com.seguranca.dto.PermissaoDTO(p.getId(), p.getNome(), p.getDescricao()))
                .collect(Collectors.toList())
        );
    }
}
