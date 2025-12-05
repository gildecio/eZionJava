package com.seguranca.controller;

import com.seguranca.dto.UsuarioDTO;
import com.seguranca.dto.ErrorResponse;
import com.seguranca.model.Usuario;
import com.seguranca.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    @PreAuthorize("hasAuthority('USUARIO_VIEW')")
    public ResponseEntity<?> listarTodos() {
        try {
            List<UsuarioDTO> usuarios = usuarioService.listarTodos();
            return ResponseEntity.ok(usuarios);
        } catch (Exception e) {
            log.error("Erro ao listar usuários: ", e);
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Erro ao listar usuários"));
        }
    }

    @GetMapping("/ativos")
    @PreAuthorize("hasAuthority('USUARIO_VIEW')")
    public ResponseEntity<?> listarAtivos() {
        try {
            List<UsuarioDTO> usuarios = usuarioService.listarAtivos();
            return ResponseEntity.ok(usuarios);
        } catch (Exception e) {
            log.error("Erro ao listar usuários ativos: ", e);
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Erro ao listar usuários ativos"));
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USUARIO_VIEW')")
    public ResponseEntity<?> obterPorId(@PathVariable Long id) {
        try {
            Usuario usuario = usuarioService.obterPorId(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            return ResponseEntity.ok(new UsuarioDTO(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getEmail(),
                usuario.getNomeCompleto(),
                usuario.getAtivo(),
                usuario.getBloqueado()
            ));
        } catch (Exception e) {
            log.error("Erro ao obter usuário: ", e);
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("Usuário não encontrado"));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('USUARIO_UPDATE')")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Map<String, String> dados) {
        try {
            Usuario usuario = usuarioService.atualizarUsuario(
                id,
                dados.get("email"),
                dados.get("nomeCompleto")
            );

            return ResponseEntity.ok(new UsuarioDTO(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getEmail(),
                usuario.getNomeCompleto(),
                usuario.getAtivo(),
                usuario.getBloqueado()
            ));
        } catch (RuntimeException e) {
            log.error("Erro ao atualizar usuário: {}", e.getMessage());
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("Erro ao atualizar usuário: ", e);
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Erro ao atualizar usuário"));
        }
    }

    @PostMapping("/{id}/alterar-senha")
    @PreAuthorize("hasAuthority('USUARIO_UPDATE')")
    public ResponseEntity<?> alterarSenha(@PathVariable Long id, @RequestBody Map<String, String> senhas) {
        try {
            usuarioService.alterarSenha(
                id,
                senhas.get("senhaAtual"),
                senhas.get("novaSenha")
            );

            Map<String, String> response = new HashMap<>();
            response.put("mensagem", "Senha alterada com sucesso");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Erro ao alterar senha: {}", e.getMessage());
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("Erro ao alterar senha: ", e);
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Erro ao alterar senha"));
        }
    }

    @PutMapping("/{id}/ativar")
    @PreAuthorize("hasAuthority('USUARIO_UPDATE')")
    public ResponseEntity<?> ativar(@PathVariable Long id) {
        try {
            Usuario usuario = usuarioService.ativarUsuario(id);
            return ResponseEntity.ok(new UsuarioDTO(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getEmail(),
                usuario.getNomeCompleto(),
                usuario.getAtivo(),
                usuario.getBloqueado()
            ));
        } catch (RuntimeException e) {
            log.error("Erro ao ativar usuário: {}", e.getMessage());
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("Erro ao ativar usuário: ", e);
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Erro ao ativar usuário"));
        }
    }

    @PutMapping("/{id}/desativar")
    @PreAuthorize("hasAuthority('USUARIO_UPDATE')")
    public ResponseEntity<?> desativar(@PathVariable Long id) {
        try {
            Usuario usuario = usuarioService.desativarUsuario(id);
            return ResponseEntity.ok(new UsuarioDTO(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getEmail(),
                usuario.getNomeCompleto(),
                usuario.getAtivo(),
                usuario.getBloqueado()
            ));
        } catch (RuntimeException e) {
            log.error("Erro ao desativar usuário: {}", e.getMessage());
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("Erro ao desativar usuário: ", e);
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Erro ao desativar usuário"));
        }
    }

    @PutMapping("/{id}/bloquear")
    @PreAuthorize("hasAuthority('USUARIO_UPDATE')")
    public ResponseEntity<?> bloquear(@PathVariable Long id) {
        try {
            Usuario usuario = usuarioService.bloquearUsuario(id);
            return ResponseEntity.ok(new UsuarioDTO(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getEmail(),
                usuario.getNomeCompleto(),
                usuario.getAtivo(),
                usuario.getBloqueado()
            ));
        } catch (RuntimeException e) {
            log.error("Erro ao bloquear usuário: {}", e.getMessage());
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("Erro ao bloquear usuário: ", e);
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Erro ao bloquear usuário"));
        }
    }

    @PutMapping("/{id}/desbloquear")
    @PreAuthorize("hasAuthority('USUARIO_UPDATE')")
    public ResponseEntity<?> desbloquear(@PathVariable Long id) {
        try {
            Usuario usuario = usuarioService.desbloquearUsuario(id);
            return ResponseEntity.ok(new UsuarioDTO(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getEmail(),
                usuario.getNomeCompleto(),
                usuario.getAtivo(),
                usuario.getBloqueado()
            ));
        } catch (RuntimeException e) {
            log.error("Erro ao desbloquear usuário: {}", e.getMessage());
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("Erro ao desbloquear usuário: ", e);
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Erro ao desbloquear usuário"));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USUARIO_DELETE')")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        try {
            usuarioService.deletarUsuario(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Erro ao deletar usuário: {}", e.getMessage());
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("Erro ao deletar usuário: ", e);
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Erro ao deletar usuário"));
        }
    }
}
