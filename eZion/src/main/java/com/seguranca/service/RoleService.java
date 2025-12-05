package com.seguranca.service;

import com.seguranca.model.Permissao;
import com.seguranca.model.Role;
import com.seguranca.repository.PermissaoRepository;
import com.seguranca.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleService {

    private final RoleRepository roleRepository;
    private final PermissaoRepository permissaoRepository;

    public Role criarRole(String nome, String descricao) {
        if (roleRepository.existsByNome(nome)) {
            throw new RuntimeException("Role já existe: " + nome);
        }

        Role role = new Role();
        role.setNome(nome);
        role.setDescricao(descricao);
        role.setAtivo(true);

        return roleRepository.save(role);
    }

    @Transactional(readOnly = true)
    public Optional<Role> obterPorId(Long id) {
        return roleRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Role> obterPorNome(String nome) {
        return roleRepository.findByNome(nome);
    }

    @Transactional(readOnly = true)
    public List<Role> listarTodas() {
        return roleRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Role> listarAtivas() {
        return roleRepository.findByAtivoTrue();
    }

    public Role atualizarRole(Long id, String nome, String descricao) {
        Role role = roleRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Role não encontrada com ID: " + id));

        if (!role.getNome().equals(nome) && roleRepository.existsByNome(nome)) {
            throw new RuntimeException("Role já existe: " + nome);
        }

        role.setNome(nome);
        role.setDescricao(descricao);
        role.setAtualizadoEm(LocalDateTime.now());

        return roleRepository.save(role);
    }

    public Role adicionarPermissao(Long roleId, Long permissaoId) {
        Role role = roleRepository.findById(roleId)
            .orElseThrow(() -> new RuntimeException("Role não encontrada com ID: " + roleId));

        Permissao permissao = permissaoRepository.findById(permissaoId)
            .orElseThrow(() -> new RuntimeException("Permissão não encontrada com ID: " + permissaoId));

        role.getPermissoes().add(permissao);
        role.setAtualizadoEm(LocalDateTime.now());

        return roleRepository.save(role);
    }

    public Role removerPermissao(Long roleId, Long permissaoId) {
        Role role = roleRepository.findById(roleId)
            .orElseThrow(() -> new RuntimeException("Role não encontrada com ID: " + roleId));

        Permissao permissao = permissaoRepository.findById(permissaoId)
            .orElseThrow(() -> new RuntimeException("Permissão não encontrada com ID: " + permissaoId));

        role.getPermissoes().remove(permissao);
        role.setAtualizadoEm(LocalDateTime.now());

        return roleRepository.save(role);
    }

    public Role ativarRole(Long id) {
        Role role = roleRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Role não encontrada com ID: " + id));

        role.setAtivo(true);
        role.setAtualizadoEm(LocalDateTime.now());

        return roleRepository.save(role);
    }

    public Role desativarRole(Long id) {
        Role role = roleRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Role não encontrada com ID: " + id));

        role.setAtivo(false);
        role.setAtualizadoEm(LocalDateTime.now());

        return roleRepository.save(role);
    }

    public void deletarRole(Long id) {
        Role role = roleRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Role não encontrada com ID: " + id));

        roleRepository.delete(role);
    }
}
