package com.seguranca.service;

import com.seguranca.model.Usuario;
import com.seguranca.repository.UsuarioRepository;
import com.seguranca.dto.UsuarioDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public Usuario criarUsuario(String username, String email, String senha, String nomeCompleto) {
        // Validações
        if (usuarioRepository.existsByUsername(username)) {
            throw new RuntimeException("Username já existe: " + username);
        }

        if (usuarioRepository.existsByEmail(email)) {
            throw new RuntimeException("Email já existe: " + email);
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setEmail(email);
        usuario.setSenha(passwordEncoder.encode(senha));
        usuario.setNomeCompleto(nomeCompleto);
        usuario.setAtivo(true);
        usuario.setBloqueado(false);

        return usuarioRepository.save(usuario);
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> obterPorUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> obterPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> obterPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<UsuarioDTO> listarTodos() {
        return usuarioRepository.findAll()
            .stream()
            .map(this::converterParaDTO)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UsuarioDTO> listarAtivos() {
        return usuarioRepository.findByAtivoTrue()
            .stream()
            .map(this::converterParaDTO)
            .collect(Collectors.toList());
    }

    public Usuario atualizarUsuario(Long id, String email, String nomeCompleto) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + id));

        if (!usuario.getEmail().equals(email) && usuarioRepository.existsByEmail(email)) {
            throw new RuntimeException("Email já existe: " + email);
        }

        usuario.setEmail(email);
        usuario.setNomeCompleto(nomeCompleto);
        usuario.setAtualizadoEm(LocalDateTime.now());

        return usuarioRepository.save(usuario);
    }

    public Usuario alterarSenha(Long id, String senhaAtual, String novaSenha) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + id));

        if (!passwordEncoder.matches(senhaAtual, usuario.getSenha())) {
            throw new RuntimeException("Senha atual incorreta");
        }

        usuario.setSenha(passwordEncoder.encode(novaSenha));
        usuario.setAtualizadoEm(LocalDateTime.now());

        return usuarioRepository.save(usuario);
    }

    public Usuario ativarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + id));

        usuario.setAtivo(true);
        usuario.setAtualizadoEm(LocalDateTime.now());

        return usuarioRepository.save(usuario);
    }

    public Usuario desativarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + id));

        usuario.setAtivo(false);
        usuario.setAtualizadoEm(LocalDateTime.now());

        return usuarioRepository.save(usuario);
    }

    public Usuario bloquearUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + id));

        usuario.setBloqueado(true);
        usuario.setAtualizadoEm(LocalDateTime.now());

        return usuarioRepository.save(usuario);
    }

    public Usuario desbloquearUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + id));

        usuario.setBloqueado(false);
        usuario.setAtualizadoEm(LocalDateTime.now());

        return usuarioRepository.save(usuario);
    }

    public void deletarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + id));

        usuarioRepository.delete(usuario);
    }

    public void registrarUltimoAcesso(String username) {
        usuarioRepository.findByUsername(username)
            .ifPresent(usuario -> {
                usuario.setUltimoAcesso(LocalDateTime.now());
                usuarioRepository.save(usuario);
            });
    }

    private UsuarioDTO converterParaDTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setUsername(usuario.getUsername());
        dto.setEmail(usuario.getEmail());
        dto.setNomeCompleto(usuario.getNomeCompleto());
        dto.setAtivo(usuario.getAtivo());
        dto.setBloqueado(usuario.getBloqueado());
        return dto;
    }
}
