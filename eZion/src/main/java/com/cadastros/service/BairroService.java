package com.cadastros.service;

import com.cadastros.model.Bairro;
import com.cadastros.model.Cidade;
import com.cadastros.repository.BairroRepository;
import com.cadastros.repository.CidadeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BairroService {

    private final BairroRepository bairroRepository;
    private final CidadeRepository cidadeRepository;

    /**
     * Cria um novo bairro
     */
    @Transactional
    public Bairro criar(Bairro bairro) {
        log.debug("Criando novo bairro: {} - {}", bairro.getNome(), bairro.getCidade().getNome());

        // Validar se a cidade existe
        Cidade cidade = cidadeRepository.findById(bairro.getCidade().getId())
            .orElseThrow(() -> new IllegalArgumentException("Cidade não encontrada com ID: " + bairro.getCidade().getId()));
        bairro.setCidade(cidade);

        // Validar se já existe um bairro com mesmo nome na mesma cidade
        if (bairroRepository.existsByNomeAndCidadeId(bairro.getNome(), bairro.getCidade().getId())) {
            throw new IllegalArgumentException("Já existe um bairro com o nome '" + bairro.getNome() + "' na cidade " + cidade.getNome());
        }

        return bairroRepository.save(bairro);
    }

    /**
     * Atualiza um bairro existente
     */
    @Transactional
    public Bairro atualizar(Long id, Bairro bairroAtualizado) {
        log.debug("Atualizando bairro ID: {}", id);

        Bairro bairro = bairroRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Bairro não encontrado com ID: " + id));

        // Validar se a cidade existe
        Cidade cidade = cidadeRepository.findById(bairroAtualizado.getCidade().getId())
            .orElseThrow(() -> new IllegalArgumentException("Cidade não encontrada com ID: " + bairroAtualizado.getCidade().getId()));
        bairroAtualizado.setCidade(cidade);

        // Validar unicidade (exceto para o próprio bairro)
        if (!bairro.getNome().equals(bairroAtualizado.getNome()) ||
            !bairro.getCidade().getId().equals(bairroAtualizado.getCidade().getId())) {
            if (bairroRepository.existsByNomeAndCidadeId(bairroAtualizado.getNome(), bairroAtualizado.getCidade().getId())) {
                throw new IllegalArgumentException("Já existe um bairro com o nome '" + bairroAtualizado.getNome() + "' na cidade " + cidade.getNome());
            }
        }

        bairro.setNome(bairroAtualizado.getNome());
        bairro.setCidade(cidade);
        bairro.setCep(bairroAtualizado.getCep());
        bairro.setObservacoes(bairroAtualizado.getObservacoes());

        return bairroRepository.save(bairro);
    }

    /**
     * Busca bairro por ID
     */
    @Transactional(readOnly = true)
    public Optional<Bairro> obterPorId(Long id) {
        return bairroRepository.findById(id);
    }

    /**
     * Lista bairros por cidade
     */
    @Transactional(readOnly = true)
    public List<Bairro> listarPorCidade(Long cidadeId) {
        return bairroRepository.findByCidadeId(cidadeId);
    }

    /**
     * Lista bairros ativos por cidade
     */
    @Transactional(readOnly = true)
    public List<Bairro> listarAtivosPorCidade(Long cidadeId) {
        return bairroRepository.findByCidadeIdAndAtivoTrue(cidadeId);
    }

    /**
     * Lista bairros ativos
     */
    @Transactional(readOnly = true)
    public List<Bairro> listarAtivos() {
        return bairroRepository.findByAtivoTrue();
    }

    /**
     * Lista todos os bairros
     */
    @Transactional(readOnly = true)
    public List<Bairro> listarTodos() {
        return bairroRepository.findAll();
    }

    /**
     * Busca bairros por nome (like)
     */
    @Transactional(readOnly = true)
    public List<Bairro> buscarPorNome(String nome) {
        return bairroRepository.findByNomeContainingIgnoreCase(nome);
    }

    /**
     * Desativa um bairro
     */
    @Transactional
    public void desativar(Long id) {
        log.debug("Desativando bairro ID: {}", id);
        Bairro bairro = bairroRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Bairro não encontrado com ID: " + id));
        bairro.setAtivo(false);
        bairroRepository.save(bairro);
    }
}