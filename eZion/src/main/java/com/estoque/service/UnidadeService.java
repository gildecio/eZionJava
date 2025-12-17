package com.estoque.service;

import com.estoque.model.Unidade;
import com.estoque.repository.UnidadeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UnidadeService {

    private final UnidadeRepository repository;

    /**
     * Lista todas as unidades
     */
    @Transactional(readOnly = true)
    public List<Unidade> listarTodas() {
        log.debug("Listando todas as unidades");
        return repository.findAll();
    }

    /**
     * Lista unidades ativas
     */
    @Transactional(readOnly = true)
    public List<Unidade> listarAtivas() {
        log.debug("Listando unidades ativas");
        return repository.findByAtivoTrue();
    }

    /**
     * Busca uma unidade por ID
     */
    @Transactional(readOnly = true)
    public Optional<Unidade> buscarPorId(Long id) {
        log.debug("Buscando unidade por ID: {}", id);
        return repository.findById(id);
    }

    /**
     * Busca uma unidade por sigla
     */
    @Transactional(readOnly = true)
    public Optional<Unidade> buscarPorSigla(String sigla) {
        log.debug("Buscando unidade por sigla: {}", sigla);
        return repository.findBySigla(sigla);
    }

    /**
     * Cria uma nova unidade
     */
    @Transactional
    public Unidade criar(Unidade unidade) {
        log.debug("Criando nova unidade: {}", unidade.getSigla());

        // Validar se a sigla já existe
        if (repository.existsBySigla(unidade.getSigla())) {
            throw new IllegalArgumentException("Já existe uma unidade com a sigla: " + unidade.getSigla());
        }

        return repository.save(unidade);
    }

    /**
     * Atualiza uma unidade existente
     */
    @Transactional
    public Unidade atualizar(Long id, Unidade unidadeAtualizada) {
        log.debug("Atualizando unidade ID: {}", id);

        Unidade unidadeExistente = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Unidade não encontrada com ID: " + id));

        // Validar se a sigla já existe em outra unidade
        if (!unidadeExistente.getSigla().equals(unidadeAtualizada.getSigla())) {
            if (repository.existsBySigla(unidadeAtualizada.getSigla())) {
                throw new IllegalArgumentException("Já existe uma unidade com a sigla: " + unidadeAtualizada.getSigla());
            }
        }

        // Atualizar campos
        unidadeExistente.setSigla(unidadeAtualizada.getSigla());
        unidadeExistente.setDescricao(unidadeAtualizada.getDescricao());
        unidadeExistente.setAtivo(unidadeAtualizada.getAtivo());

        return repository.save(unidadeExistente);
    }

    /**
     * Ativa uma unidade
     */
    @Transactional
    public Unidade ativar(Long id) {
        log.debug("Ativando unidade ID: {}", id);

        Unidade unidade = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Unidade não encontrada com ID: " + id));

        unidade.setAtivo(true);
        return repository.save(unidade);
    }

    /**
     * Desativa uma unidade
     */
    @Transactional
    public Unidade desativar(Long id) {
        log.debug("Desativando unidade ID: {}", id);

        Unidade unidade = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Unidade não encontrada com ID: " + id));

        unidade.setAtivo(false);
        return repository.save(unidade);
    }

    /**
     * Exclui uma unidade
     */
    @Transactional
    public void excluir(Long id) {
        log.debug("Excluindo unidade ID: {}", id);

        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Unidade não encontrada com ID: " + id);
        }

        repository.deleteById(id);
    }

    /**
     * Lista todas as unidades base (sem unidade pai)
     */
    @Transactional(readOnly = true)
    public List<Unidade> listarUnidadesBase() {
        log.debug("Listando unidades base");
        return repository.findUnidadesBase();
    }

    /**
     * Lista unidades derivadas de uma unidade pai
     */
    @Transactional(readOnly = true)
    public List<Unidade> listarUnidadesDerivadas(Long unidadePaiId) {
        log.debug("Listando unidades derivadas da unidade pai ID: {}", unidadePaiId);
        return repository.findUnidadesDerivadas(unidadePaiId);
    }

    /**
     * Lista unidades que possuem fator definido
     */
    @Transactional(readOnly = true)
    public List<Unidade> listarUnidadesComFator() {
        log.debug("Listando unidades com fator definido");
        return repository.findUnidadesComFator();
    }

    /**
     * Valida se uma unidade pode ser usada como unidade pai
     */
    @Transactional(readOnly = true)
    public boolean validarUnidadePai(Long unidadePaiId) {
        if (unidadePaiId == null) {
            return true; // null é válido (unidade base)
        }

        Optional<Unidade> unidadePai = repository.findById(unidadePaiId);
        return unidadePai.isPresent() && unidadePai.get().getAtivo();
    }

    /**
     * Calcula o fator total de uma unidade em relação à sua unidade base
     */
    @Transactional(readOnly = true)
    public java.math.BigDecimal calcularFatorTotal(Long unidadeId) {
        Optional<Unidade> unidade = repository.findById(unidadeId);
        if (unidade.isEmpty()) {
            throw new IllegalArgumentException("Unidade não encontrada com ID: " + unidadeId);
        }

        return unidade.get().getFatorTotal();
    }
}