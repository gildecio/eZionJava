package com.seguranca.service;

import com.seguranca.model.Permissao;
import com.seguranca.repository.PermissaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PermissaoService {

    private final PermissaoRepository permissaoRepository;

    public Permissao criarPermissao(String nome, String descricao) {
        if (permissaoRepository.existsByNome(nome)) {
            throw new RuntimeException("Permissão já existe: " + nome);
        }

        Permissao permissao = new Permissao();
        permissao.setNome(nome);
        permissao.setDescricao(descricao);

        return permissaoRepository.save(permissao);
    }

    @Transactional(readOnly = true)
    public Optional<Permissao> obterPorId(Long id) {
        return permissaoRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Permissao> obterPorNome(String nome) {
        return permissaoRepository.findByNome(nome);
    }

    @Transactional(readOnly = true)
    public List<Permissao> listarTodas() {
        return permissaoRepository.findAll();
    }

    public Permissao atualizarPermissao(Long id, String nome, String descricao) {
        Permissao permissao = permissaoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Permissão não encontrada com ID: " + id));

        if (!permissao.getNome().equals(nome) && permissaoRepository.existsByNome(nome)) {
            throw new RuntimeException("Permissão já existe: " + nome);
        }

        permissao.setNome(nome);
        permissao.setDescricao(descricao);

        return permissaoRepository.save(permissao);
    }

    public void deletarPermissao(Long id) {
        Permissao permissao = permissaoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Permissão não encontrada com ID: " + id));

        permissaoRepository.delete(permissao);
    }
}
