package com.estoque.service;

import com.estoque.model.EstoqueItem;
import com.estoque.model.Lote;
import com.estoque.repository.EstoqueItemRepository;
import com.estoque.repository.LoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoteService {

    private final LoteRepository loteRepository;
    private final EstoqueItemRepository estoqueItemRepository;

    /**
     * Cria um novo lote
     */
    @Transactional
    public Lote criar(Lote lote) {
        log.debug("Criando novo lote: {}", lote.getNumeroLote());

        // Validar se o número do lote já existe
        if (loteRepository.existsByNumeroLote(lote.getNumeroLote())) {
            throw new IllegalArgumentException("Já existe um lote com o número: " + lote.getNumeroLote());
        }

        // Validar se o item existe
        EstoqueItem item = estoqueItemRepository.findById(lote.getEstoqueItem().getId())
            .orElseThrow(() -> new IllegalArgumentException("Item de estoque não encontrado com ID: " + lote.getEstoqueItem().getId()));
        lote.setEstoqueItem(item);

        // Se quantidade disponível não foi informada, usa a quantidade total
        if (lote.getQuantidadeDisponivel() == null) {
            lote.setQuantidadeDisponivel(lote.getQuantidadeTotal());
        }

        return loteRepository.save(lote);
    }

    /**
     * Busca um lote por ID
     */
    @Transactional(readOnly = true)
    public Optional<Lote> obterPorId(Long id) {
        log.debug("Buscando lote com ID: {}", id);
        return loteRepository.findById(id);
    }

    /**
     * Busca um lote por número
     */
    @Transactional(readOnly = true)
    public Optional<Lote> buscarPorNumeroLote(String numeroLote) {
        log.debug("Buscando lote com número: {}", numeroLote);
        return loteRepository.findByNumeroLote(numeroLote);
    }

    /**
     * Lista todos os lotes
     */
    @Transactional(readOnly = true)
    public List<Lote> listarTodos() {
        log.debug("Listando todos os lotes");
        return loteRepository.findAll();
    }

    /**
     * Lista lotes de um item específico
     */
    @Transactional(readOnly = true)
    public List<Lote> listarPorItem(Long itemId) {
        log.debug("Listando lotes do item ID: {}", itemId);
        return loteRepository.findByEstoqueItemIdOrderByDataEntradaDesc(itemId);
    }

    /**
     * Lista lotes ativos de um item (FIFO)
     */
    @Transactional(readOnly = true)
    public List<Lote> listarLotesDisponiveisFIFO(Long itemId) {
        log.debug("Listando lotes disponíveis (FIFO) do item ID: {}", itemId);
        return loteRepository.findLotesDisponiveisByItemId(itemId);
    }

    /**
     * Lista lotes vencidos
     */
    @Transactional(readOnly = true)
    public List<Lote> listarLotesVencidos() {
        log.debug("Listando lotes vencidos");
        return loteRepository.findLotesVencidosAnte(LocalDate.now());
    }

    /**
     * Lista lotes para vencer em um período
     */
    @Transactional(readOnly = true)
    public List<Lote> listarLotesParaVencer(LocalDate dataInicio, LocalDate dataFim) {
        log.debug("Listando lotes para vencer entre {} e {}", dataInicio, dataFim);
        return loteRepository.findLotesParaVencerEntre(dataInicio, dataFim);
    }

    /**
     * Atualiza um lote
     */
    @Transactional
    public Lote atualizar(Long id, Lote loteAtualizado) {
        log.debug("Atualizando lote ID: {}", id);

        Lote lote = loteRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Lote não encontrado com ID: " + id));

        // Validar número do lote se foi alterado
        if (!lote.getNumeroLote().equals(loteAtualizado.getNumeroLote())) {
            if (loteRepository.existsByNumeroLote(loteAtualizado.getNumeroLote())) {
                throw new IllegalArgumentException("Já existe um lote com o número: " + loteAtualizado.getNumeroLote());
            }
            lote.setNumeroLote(loteAtualizado.getNumeroLote());
        }

        // Não permitir reduzir quantidade total abaixo da já consumida
        BigDecimal quantidadeConsumida = lote.getQuantidadeTotal().subtract(lote.getQuantidadeDisponivel());
        if (loteAtualizado.getQuantidadeTotal().compareTo(quantidadeConsumida) < 0) {
            throw new IllegalArgumentException("Quantidade total não pode ser menor que o já consumido");
        }

        lote.setDataEntrada(loteAtualizado.getDataEntrada());
        lote.setDataValidade(loteAtualizado.getDataValidade());
        lote.setQuantidadeTotal(loteAtualizado.getQuantidadeTotal());
        lote.setFornecedor(loteAtualizado.getFornecedor());
        lote.setObservacoes(loteAtualizado.getObservacoes());

        return loteRepository.save(lote);
    }

    /**
     * Reduz quantidade disponível do lote
     */
    @Transactional
    public void reduzirQuantidade(Long loteId, BigDecimal quantidade) {
        log.debug("Reduzindo quantidade do lote ID: {} em: {}", loteId, quantidade);

        Lote lote = loteRepository.findById(loteId)
            .orElseThrow(() -> new IllegalArgumentException("Lote não encontrado com ID: " + loteId));

        if (lote.getQuantidadeDisponivel().compareTo(quantidade) < 0) {
            throw new IllegalArgumentException("Quantidade disponível insuficiente no lote");
        }

        lote.setQuantidadeDisponivel(lote.getQuantidadeDisponivel().subtract(quantidade));
        loteRepository.save(lote);
    }

    /**
     * Aumenta quantidade disponível do lote
     */
    @Transactional
    public void aumentarQuantidade(Long loteId, BigDecimal quantidade) {
        log.debug("Aumentando quantidade do lote ID: {} em: {}", loteId, quantidade);

        Lote lote = loteRepository.findById(loteId)
            .orElseThrow(() -> new IllegalArgumentException("Lote não encontrado com ID: " + loteId));

        BigDecimal novaQuantidade = lote.getQuantidadeDisponivel().add(quantidade);
        if (novaQuantidade.compareTo(lote.getQuantidadeTotal()) > 0) {
            throw new IllegalArgumentException("Quantidade não pode exceder a quantidade total do lote");
        }

        lote.setQuantidadeDisponivel(novaQuantidade);
        loteRepository.save(lote);
    }

    /**
     * Calcula o estoque disponível de um item
     */
    @Transactional(readOnly = true)
    public BigDecimal calcularEstoqueDisponivel(Long itemId) {
        log.debug("Calculando estoque disponível do item ID: {}", itemId);
        BigDecimal estoque = loteRepository.calcularEstoqueDisponivel(itemId);
        return estoque != null ? estoque : BigDecimal.ZERO;
    }

    /**
     * Calcula o estoque total de um item
     */
    @Transactional(readOnly = true)
    public BigDecimal calcularEstoqueTotal(Long itemId) {
        log.debug("Calculando estoque total do item ID: {}", itemId);
        BigDecimal estoque = loteRepository.calcularEstoqueTotal(itemId);
        return estoque != null ? estoque : BigDecimal.ZERO;
    }

    /**
     * Desativa um lote
     */
    @Transactional
    public void desativar(Long id) {
        log.debug("Desativando lote ID: {}", id);
        Lote lote = loteRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Lote não encontrado com ID: " + id));
        lote.setAtivo(false);
        loteRepository.save(lote);
    }

    /**
     * Exclui um lote
     */
    @Transactional
    public void excluir(Long id) {
        log.debug("Excluindo lote ID: {}", id);
        if (!loteRepository.existsById(id)) {
            throw new IllegalArgumentException("Lote não encontrado com ID: " + id);
        }
        loteRepository.deleteById(id);
    }
}