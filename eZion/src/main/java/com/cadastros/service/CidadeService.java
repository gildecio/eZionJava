package com.cadastros.service;

import com.cadastros.model.Cidade;
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
public class CidadeService {

    private final CidadeRepository cidadeRepository;

    /**
     * Cria uma nova cidade
     */
    @Transactional
    public Cidade criar(Cidade cidade) {
        log.debug("Criando nova cidade: {} - {}", cidade.getNome(), cidade.getUf());

        // Validar se já existe uma cidade com mesmo nome e UF
        if (cidadeRepository.existsByNomeAndUf(cidade.getNome(), cidade.getUf())) {
            throw new IllegalArgumentException("Já existe uma cidade com o nome '" + cidade.getNome() + "' no estado " + cidade.getUf());
        }

        return cidadeRepository.save(cidade);
    }

    /**
     * Atualiza uma cidade existente
     */
    @Transactional
    public Cidade atualizar(Long id, Cidade cidadeAtualizada) {
        log.debug("Atualizando cidade ID: {}", id);

        Cidade cidade = cidadeRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Cidade não encontrada com ID: " + id));

        // Validar unicidade (exceto para a própria cidade)
        if (!cidade.getNome().equals(cidadeAtualizada.getNome()) ||
            !cidade.getUf().equals(cidadeAtualizada.getUf())) {
            if (cidadeRepository.existsByNomeAndUf(cidadeAtualizada.getNome(), cidadeAtualizada.getUf())) {
                throw new IllegalArgumentException("Já existe uma cidade com o nome '" + cidadeAtualizada.getNome() + "' no estado " + cidadeAtualizada.getUf());
            }
        }

        cidade.setNome(cidadeAtualizada.getNome());
        cidade.setUf(cidadeAtualizada.getUf());
        cidade.setCodigoIbge(cidadeAtualizada.getCodigoIbge());
        cidade.setObservacoes(cidadeAtualizada.getObservacoes());

        return cidadeRepository.save(cidade);
    }

    /**
     * Busca cidade por ID
     */
    @Transactional(readOnly = true)
    public Optional<Cidade> obterPorId(Long id) {
        return cidadeRepository.findById(id);
    }

    /**
     * Busca cidade por nome e UF
     */
    @Transactional(readOnly = true)
    public Optional<Cidade> obterPorNomeEUf(String nome, String uf) {
        return cidadeRepository.findByNomeAndUf(nome, uf);
    }

    /**
     * Lista cidades por UF
     */
    @Transactional(readOnly = true)
    public List<Cidade> listarPorUf(String uf) {
        return cidadeRepository.findByUf(uf);
    }

    /**
     * Lista cidades ativas
     */
    @Transactional(readOnly = true)
    public List<Cidade> listarAtivas() {
        return cidadeRepository.findByAtivoTrue();
    }

    /**
     * Lista todas as cidades
     */
    @Transactional(readOnly = true)
    public List<Cidade> listarTodas() {
        return cidadeRepository.findAll();
    }

    /**
     * Busca cidades por nome (like)
     */
    @Transactional(readOnly = true)
    public List<Cidade> buscarPorNome(String nome) {
        return cidadeRepository.findByNomeContainingIgnoreCase(nome);
    }

    /**
     * Desativa uma cidade
     */
    @Transactional
    public void desativar(Long id) {
        log.debug("Desativando cidade ID: {}", id);
        Cidade cidade = cidadeRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Cidade não encontrada com ID: " + id));
        cidade.setAtivo(false);
        cidadeRepository.save(cidade);
    }
}