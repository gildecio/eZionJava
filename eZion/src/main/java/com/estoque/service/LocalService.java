package com.estoque.service;

import com.estoque.model.Local;
import com.estoque.repository.LocalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class LocalService {

    private final LocalRepository localRepository;

    public Local criar(Local local) {
        if (localRepository.existsByNome(local.getNome())) {
            throw new IllegalArgumentException("Já existe um local com este nome");
        }
        local.setAtivo(true);
        return localRepository.save(local);
    }

    @Transactional(readOnly = true)
    public List<Local> listarTodos() {
        return localRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Local> listarAtivos() {
        return localRepository.findAllAtivosOrderByNome();
    }

    @Transactional(readOnly = true)
    public Optional<Local> buscarPorId(Long id) {
        return localRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Local> buscarPorNome(String nome) {
        return localRepository.findByNome(nome);
    }

    @Transactional(readOnly = true)
    public List<Local> buscarPorNomeParcial(String nome) {
        return localRepository.findByNomeContainingIgnoreCaseAndAtivo(nome);
    }

    public Local atualizar(Long id, Local localAtualizado) {
        Local local = localRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Local não encontrado"));

        if (!local.getNome().equals(localAtualizado.getNome()) &&
            localRepository.existsByNome(localAtualizado.getNome())) {
            throw new IllegalArgumentException("Já existe um local com este nome");
        }

        local.setNome(localAtualizado.getNome());
        return localRepository.save(local);
    }

    public void ativar(Long id) {
        Local local = localRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Local não encontrado"));
        local.setAtivo(true);
        localRepository.save(local);
    }

    public void desativar(Long id) {
        Local local = localRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Local não encontrado"));
        local.setAtivo(false);
        localRepository.save(local);
    }

    public void excluir(Long id) {
        if (!localRepository.existsById(id)) {
            throw new IllegalArgumentException("Local não encontrado");
        }
        localRepository.deleteById(id);
    }
}