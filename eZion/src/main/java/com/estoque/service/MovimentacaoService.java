package com.estoque.service;

import com.estoque.model.EstoqueItem;
import com.estoque.model.Local;
import com.estoque.model.Lote;
import com.estoque.model.Movimentacao;
import com.estoque.repository.EstoqueItemRepository;
import com.estoque.repository.LocalRepository;
import com.estoque.repository.LoteRepository;
import com.estoque.repository.MovimentacaoRepository;
import com.seguranca.model.Usuario;
import com.seguranca.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovimentacaoService {

    private final MovimentacaoRepository movimentacaoRepository;
    private final EstoqueItemRepository estoqueItemRepository;
    private final LocalRepository localRepository;
    private final LoteRepository loteRepository;
    private final UsuarioRepository usuarioRepository;
    private final EstoqueCustoService estoqueCustoService;

    /**
     * Lista todas as movimentações
     */
    @Transactional(readOnly = true)
    public List<Movimentacao> listarTodas() {
        log.debug("Listando todas as movimentações");
        return movimentacaoRepository.findAll();
    }

    /**
     * Busca uma movimentação por ID
     */
    @Transactional(readOnly = true)
    public Optional<Movimentacao> obterPorId(Long id) {
        log.debug("Buscando movimentação com ID: {}", id);
        return movimentacaoRepository.findById(id);
    }

    /**
     * Lista movimentações de um item específico
     */
    @Transactional(readOnly = true)
    public List<Movimentacao> listarPorItem(Long itemId) {
        log.debug("Listando movimentações do item ID: {}", itemId);
        return movimentacaoRepository.findByEstoqueItemIdOrderByDataMovimentacaoDesc(itemId);
    }

    /**
     * Lista movimentações de um item em um período
     */
    @Transactional(readOnly = true)
    public List<Movimentacao> listarPorItemEPeriodo(Long itemId, LocalDateTime inicio, LocalDateTime fim) {
        log.debug("Listando movimentações do item {} no período {} - {}", itemId, inicio, fim);
        return movimentacaoRepository.findByEstoqueItemIdAndPeriodo(itemId, inicio, fim);
    }

    /**
     * Lista movimentações por local
     */
    @Transactional(readOnly = true)
    public List<Movimentacao> listarPorLocal(Long localId) {
        log.debug("Listando movimentações do local ID: {}", localId);
        return movimentacaoRepository.findByLocalIdOrderByDataMovimentacaoDesc(localId);
    }

    /**
     * Lista movimentações por tipo
     */
    @Transactional(readOnly = true)
    public List<Movimentacao> listarPorTipo(Movimentacao.TipoMovimentacao tipo) {
        log.debug("Listando movimentações do tipo: {}", tipo);
        return movimentacaoRepository.findByTipoMovimentacaoOrderByDataMovimentacaoDesc(tipo);
    }

    /**
     * Lista movimentações por período
     */
    @Transactional(readOnly = true)
    public List<Movimentacao> listarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        log.debug("Listando movimentações no período {} - {}", inicio, fim);
        return movimentacaoRepository.findByPeriodo(inicio, fim);
    }

    /**
     * Registra uma nova movimentação
     */
    @Transactional
    public Movimentacao registrar(Movimentacao movimentacao) {
        log.debug("Registrando nova movimentação: {}", movimentacao.getObservacoes());

        // Validar se o item existe
        EstoqueItem item = estoqueItemRepository.findById(movimentacao.getEstoqueItem().getId())
            .orElseThrow(() -> new IllegalArgumentException("Item de estoque não encontrado com ID: " + movimentacao.getEstoqueItem().getId()));
        movimentacao.setEstoqueItem(item);

        // Validar se o local existe (se informado)
        if (movimentacao.getLocal() != null && movimentacao.getLocal().getId() != null) {
            Local local = localRepository.findById(movimentacao.getLocal().getId())
                .orElseThrow(() -> new IllegalArgumentException("Local não encontrado com ID: " + movimentacao.getLocal().getId()));
            movimentacao.setLocal(local);
        }

        // Validar se o lote existe (se informado)
        if (movimentacao.getLote() != null && movimentacao.getLote().getId() != null) {
            Lote lote = loteRepository.findById(movimentacao.getLote().getId())
                .orElseThrow(() -> new IllegalArgumentException("Lote não encontrado com ID: " + movimentacao.getLote().getId()));
            movimentacao.setLote(lote);
        }

        // Validar se o usuário existe
        Usuario usuario = usuarioRepository.findById(movimentacao.getUsuario().getId())
            .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com ID: " + movimentacao.getUsuario().getId()));
        movimentacao.setUsuario(usuario);

        // Definir data da movimentação se não foi informada
        if (movimentacao.getDataMovimentacao() == null) {
            movimentacao.setDataMovimentacao(LocalDateTime.now());
        }

        // Calcular o custo da movimentação baseado no último custo médio do item
        if (movimentacao.getCusto() == null) {
            BigDecimal custoMedioItem = estoqueCustoService.calcularCustoMedioPonderado(item.getId());
            BigDecimal custoCalculado = movimentacao.calcularCustoMovimentacao(custoMedioItem);
            movimentacao.setCusto(custoCalculado);
            log.debug("Custo calculado para movimentação: {} (custo médio: {}, quantidade: {})",
                    custoCalculado, custoMedioItem, movimentacao.getQuantidade());
        }

        return movimentacaoRepository.save(movimentacao);
    }

    /**
     * Calcula o saldo atual de um item baseado nas movimentações
     */
    @Transactional(readOnly = true)
    public BigDecimal calcularSaldoAtual(Long itemId) {
        log.debug("Calculando saldo atual do item ID: {}", itemId);
        BigDecimal saldo = movimentacaoRepository.calcularSaldoByEstoqueItemId(itemId);
        return saldo != null ? saldo : BigDecimal.ZERO;
    }

    /**
     * Exclui uma movimentação
     */
    @Transactional
    public void excluir(Long id) {
        log.debug("Excluindo movimentação ID: {}", id);
        if (!movimentacaoRepository.existsById(id)) {
            throw new IllegalArgumentException("Movimentação não encontrada com ID: " + id);
        }
        movimentacaoRepository.deleteById(id);
    }
}