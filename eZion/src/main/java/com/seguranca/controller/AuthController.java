package com.seguranca.controller;

import com.seguranca.dto.*;
import com.seguranca.model.Usuario;
import com.seguranca.security.JwtProvider;
import com.seguranca.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Autenticação", description = "Endpoints de autenticação e autorização")
@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final UsuarioService usuarioService;

    @Operation(
        summary = "Realizar login", 
        description = "Autentica um usuário e retorna um token JWT",
        security = {}
    )
    @ApiResponse(responseCode = "200", description = "Login bem-sucedido",
            content = @Content(schema = @Schema(implementation = JwtResponse.class)))
    @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    @ApiResponse(responseCode = "400", description = "Requisição inválida")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
                )
            );

            String jwt = jwtProvider.generateJwtToken(authentication);
            String refreshToken = jwtProvider.generateRefreshToken(loginRequest.getUsername());

            Usuario usuario = usuarioService.obterPorUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            usuarioService.registrarUltimoAcesso(loginRequest.getUsername());

            List<String> roles = usuario.getRoles().stream()
                .map(r -> r.getNome())
                .collect(Collectors.toList());

            List<String> permissoes = usuario.getRoles().stream()
                .flatMap(r -> r.getPermissoes().stream())
                .map(p -> p.getNome())
                .distinct()
                .collect(Collectors.toList());

            JwtResponse response = JwtResponse.builder()
                .token(jwt)
                .refreshToken(refreshToken)
                .id(usuario.getId())
                .username(usuario.getUsername())
                .email(usuario.getEmail())
                .nomeCompleto(usuario.getNomeCompleto())
                .roles(roles)
                .permissoes(permissoes)
                .build();

            return ResponseEntity.ok(response);

        } catch (AuthenticationException e) {
            log.error("Falha na autenticação para usuário: {}", loginRequest.getUsername());
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("Usuário ou senha inválidos"));
        } catch (Exception e) {
            log.error("Erro durante login: ", e);
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Erro no servidor: " + e.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        try {
            if (usuarioService.obterPorUsername(registerRequest.getUsername()).isPresent()) {
                return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Username já existe"));
            }

            if (usuarioService.obterPorEmail(registerRequest.getEmail()).isPresent()) {
                return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Email já existe"));
            }

            Usuario usuario = usuarioService.criarUsuario(
                registerRequest.getUsername(),
                registerRequest.getEmail(),
                registerRequest.getSenha(),
                registerRequest.getNomeCompleto()
            );

            UsuarioDTO usuarioDTO = new UsuarioDTO(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getEmail(),
                usuario.getNomeCompleto(),
                usuario.getAtivo(),
                usuario.getBloqueado()
            );

            return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(usuarioDTO);

        } catch (Exception e) {
            log.error("Erro durante registro: ", e);
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Erro no servidor: " + e.getMessage()));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {
        try {
            if (jwtProvider.validateJwtToken(request.getRefreshToken())) {
                String username = jwtProvider.getUsernameFromJwtToken(request.getRefreshToken());
                String newToken = jwtProvider.generateTokenFromUsername(username);
                String newRefreshToken = jwtProvider.generateRefreshToken(username);

                Usuario usuario = usuarioService.obterPorUsername(username)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

                List<String> roles = usuario.getRoles().stream()
                    .map(r -> r.getNome())
                    .collect(Collectors.toList());

                List<String> permissoes = usuario.getRoles().stream()
                    .flatMap(r -> r.getPermissoes().stream())
                    .map(p -> p.getNome())
                    .distinct()
                    .collect(Collectors.toList());

                JwtResponse response = JwtResponse.builder()
                    .token(newToken)
                    .refreshToken(newRefreshToken)
                    .id(usuario.getId())
                    .username(usuario.getUsername())
                    .email(usuario.getEmail())
                    .nomeCompleto(usuario.getNomeCompleto())
                    .roles(roles)
                    .permissoes(permissoes)
                    .build();

                return ResponseEntity.ok(response);
            }

            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("Token de refresh inválido"));

        } catch (Exception e) {
            log.error("Erro ao renovar token: ", e);
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Erro no servidor: " + e.getMessage()));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        try {
            Authentication authentication = org.springframework.security.core.context.SecurityContextHolder
                .getContext()
                .getAuthentication();

            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Usuário não autenticado"));
            }

            String username = authentication.getName();
            Usuario usuario = usuarioService.obterPorUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            List<String> roles = usuario.getRoles().stream()
                .map(r -> r.getNome())
                .collect(Collectors.toList());

            List<String> permissoes = usuario.getRoles().stream()
                .flatMap(r -> r.getPermissoes().stream())
                .map(p -> p.getNome())
                .distinct()
                .collect(Collectors.toList());

            JwtResponse response = JwtResponse.builder()
                .id(usuario.getId())
                .username(usuario.getUsername())
                .email(usuario.getEmail())
                .nomeCompleto(usuario.getNomeCompleto())
                .roles(roles)
                .permissoes(permissoes)
                .build();

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Erro ao obter usuário atual: ", e);
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Erro no servidor: " + e.getMessage()));
        }
    }
}
