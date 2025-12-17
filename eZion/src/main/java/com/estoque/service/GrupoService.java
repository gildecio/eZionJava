package com.estoque.service;

import com.estoque.model.Grupo;
import com.estoque.repository.GrupoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class GrupoService {

    private final GrupoRepository grupoRepository;

    public Grupo criar(Grupo grupo) {
        if (grupoRepository.existsByNome(grupo.getNome())) {
            throw new IllegalArgumentException("Já existe um grupo com este nome");
        }

        // Se código não foi informado, será definido após gerar o ID
        if (grupo.getCodigo() != null && !grupo.getCodigo().trim().isEmpty()) {
            if (grupoRepository.existsByCodigo(grupo.getCodigo())) {
                throw new IllegalArgumentException("Já existe um grupo com este código");
            }
        }

        // Validação da hierarquia
        if (grupo.getGrupoPai() != null) {
            Grupo pai = grupoRepository.findById(grupo.getGrupoPai().getId())
                .orElseThrow(() -> new IllegalArgumentException("Grupo pai não encontrado"));
            grupo.setGrupoPai(pai);
        }

        grupo.setAtivo(true);
        Grupo grupoSalvo = grupoRepository.save(grupo);

        // Se código não foi informado, define como o ID convertido para string
        if (grupo.getCodigo() == null || grupo.getCodigo().trim().isEmpty()) {
            grupoSalvo.setCodigo(String.valueOf(grupoSalvo.getId()));
            grupoSalvo = grupoRepository.save(grupoSalvo);
        }

        return grupoSalvo;
    }

    @Transactional(readOnly = true)
    public List<Grupo> listarTodos() {
        return grupoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Grupo> listarAtivos() {
        return grupoRepository.findAllAtivosOrderByNome();
    }

    @Transactional(readOnly = true)
    public Optional<Grupo> buscarPorId(Long id) {
        return grupoRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Grupo> buscarPorNome(String nome) {
        return grupoRepository.findByNome(nome);
    }

    @Transactional(readOnly = true)
    public List<Grupo> buscarPorNomeParcial(String nome) {
        return grupoRepository.findByNomeContainingIgnoreCaseAndAtivo(nome);
    }

    public Grupo atualizar(Long id, Grupo grupoAtualizado) {
        Grupo grupo = grupoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Grupo não encontrado"));

        if (!grupo.getNome().equals(grupoAtualizado.getNome()) &&
            grupoRepository.existsByNome(grupoAtualizado.getNome())) {
            throw new IllegalArgumentException("Já existe um grupo com este nome");
        }

        // Verificar código se foi alterado
        if (grupoAtualizado.getCodigo() != null && !grupoAtualizado.getCodigo().trim().isEmpty()) {
            if (!grupoAtualizado.getCodigo().equals(grupo.getCodigo()) &&
                grupoRepository.existsByCodigo(grupoAtualizado.getCodigo())) {
                throw new IllegalArgumentException("Já existe um grupo com este código");
            }
            grupo.setCodigo(grupoAtualizado.getCodigo());
        }

        // Validação da hierarquia
        if (grupoAtualizado.getGrupoPai() != null) {
            if (grupoAtualizado.getGrupoPai().getId().equals(id)) {
                throw new IllegalArgumentException("Um grupo não pode ser pai de si mesmo");
            }
            Grupo pai = grupoRepository.findById(grupoAtualizado.getGrupoPai().getId())
                .orElseThrow(() -> new IllegalArgumentException("Grupo pai não encontrado"));
            grupo.setGrupoPai(pai);
        } else {
            grupo.setGrupoPai(null);
        }

        grupo.setNome(grupoAtualizado.getNome());
        return grupoRepository.save(grupo);
    }

    public void ativar(Long id) {
        Grupo grupo = grupoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Grupo não encontrado"));
        grupo.setAtivo(true);
        grupoRepository.save(grupo);
    }

    public void desativar(Long id) {
        Grupo grupo = grupoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Grupo não encontrado"));
        grupo.setAtivo(false);
        grupoRepository.save(grupo);
    }

    public void excluir(Long id) {
        if (!grupoRepository.existsById(id)) {
            throw new IllegalArgumentException("Grupo não encontrado");
        }
        grupoRepository.deleteById(id);
    }

    // Métodos para hierarquia
    @Transactional(readOnly = true)
    public List<Grupo> listarGruposRaiz() {
        return grupoRepository.findGruposRaizAtivos();
    }

    @Transactional(readOnly = true)
    public List<Grupo> listarFilhos(Long paiId) {
        return grupoRepository.findFilhosByPaiIdAndAtivo(paiId);
    }

    @Transactional(readOnly = true)
    public List<Grupo> listarTodosFilhos(Long paiId) {
        return grupoRepository.findFilhosByPaiId(paiId);
    }

    public boolean podeExcluir(Long id) {
        return grupoRepository.countFilhosByPaiId(id) == 0;
    }

    public void excluirComValidacao(Long id) {
        if (!podeExcluir(id)) {
            throw new IllegalArgumentException("Não é possível excluir um grupo que possui filhos");
        }
        excluir(id);
    }
}