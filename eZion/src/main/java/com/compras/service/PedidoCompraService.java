package com.compras.service;

import com.compras.model.*;
import com.compras.repository.FornecedorRepository;
import com.compras.repository.ItemPedidoCompraRepository;
import com.compras.repository.PedidoCompraRepository;
import com.estoque.model.EstoqueItem;
import com.estoque.repository.EstoqueItemRepository;
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
public class PedidoCompraService {

    private final PedidoCompraRepository pedidoCompraRepository;
    private final ItemPedidoCompraRepository itemPedidoCompraRepository;
    private final FornecedorRepository fornecedorRepository;
    private final EstoqueItemRepository estoqueItemRepository;

    /**
     * Cria um novo pedido de compra
     */
    @Transactional
    public PedidoCompra criar(PedidoCompra pedidoCompra) {
        log.debug("Criando novo pedido de compra: {}", pedidoCompra.getNumeroPedido());

        // Validar se o número do pedido já existe
        if (pedidoCompraRepository.existsByNumeroPedido(pedidoCompra.getNumeroPedido())) {
            throw new IllegalArgumentException("Já existe um pedido com o número: " + pedidoCompra.getNumeroPedido());
        }

        // Validar se o fornecedor existe
        Fornecedor fornecedor = fornecedorRepository.findById(pedidoCompra.getFornecedor().getId())
            .orElseThrow(() -> new IllegalArgumentException("Fornecedor não encontrado com ID: " + pedidoCompra.getFornecedor().getId()));
        pedidoCompra.setFornecedor(fornecedor);

        // Calcular valor total dos itens
        BigDecimal valorTotal = BigDecimal.ZERO;
        if (pedidoCompra.getItens() != null) {
            for (ItemPedidoCompra item : pedidoCompra.getItens()) {
                // Validar se o item de estoque existe
                EstoqueItem estoqueItem = estoqueItemRepository.findById(item.getEstoqueItem().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Item de estoque não encontrado com ID: " + item.getEstoqueItem().getId()));
                item.setEstoqueItem(estoqueItem);
                item.setPedidoCompra(pedidoCompra);

                // Calcular valor total do item
                item.setValorTotal(item.calcularValorTotal());
                valorTotal = valorTotal.add(item.getValorTotal());
            }
        }
        pedidoCompra.setValorTotal(valorTotal);

        return pedidoCompraRepository.save(pedidoCompra);
    }

    /**
     * Atualiza um pedido de compra
     */
    @Transactional
    public PedidoCompra atualizar(Long id, PedidoCompra pedidoAtualizado) {
        log.debug("Atualizando pedido de compra ID: {}", id);

        PedidoCompra pedido = pedidoCompraRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Pedido de compra não encontrado com ID: " + id));

        // Só permitir atualização se estiver PENDENTE
        if (pedido.getStatus() != StatusPedidoCompra.PENDENTE) {
            throw new IllegalArgumentException("Não é possível alterar um pedido que não está PENDENTE");
        }

        pedido.setDataPrevistaEntrega(pedidoAtualizado.getDataPrevistaEntrega());
        pedido.setObservacoes(pedidoAtualizado.getObservacoes());

        return pedidoCompraRepository.save(pedido);
    }

    /**
     * Aprova um pedido de compra
     */
    @Transactional
    public PedidoCompra aprovar(Long id) {
        log.debug("Aprovando pedido de compra ID: {}", id);

        PedidoCompra pedido = pedidoCompraRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Pedido de compra não encontrado com ID: " + id));

        if (pedido.getStatus() != StatusPedidoCompra.PENDENTE) {
            throw new IllegalArgumentException("Só é possível aprovar pedidos PENDENTES");
        }

        pedido.setStatus(StatusPedidoCompra.APROVADO);
        return pedidoCompraRepository.save(pedido);
    }

    /**
     * Rejeita um pedido de compra
     */
    @Transactional
    public PedidoCompra rejeitar(Long id) {
        log.debug("Rejeitando pedido de compra ID: {}", id);

        PedidoCompra pedido = pedidoCompraRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Pedido de compra não encontrado com ID: " + id));

        if (pedido.getStatus() != StatusPedidoCompra.PENDENTE) {
            throw new IllegalArgumentException("Só é possível rejeitar pedidos PENDENTES");
        }

        pedido.setStatus(StatusPedidoCompra.REJEITADO);
        return pedidoCompraRepository.save(pedido);
    }

    /**
     * Registra recebimento parcial/total de um pedido
     */
    @Transactional
    public PedidoCompra registrarRecebimento(Long id, LocalDate dataEntrega) {
        log.debug("Registrando recebimento do pedido ID: {}", id);

        PedidoCompra pedido = pedidoCompraRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Pedido de compra não encontrado com ID: " + id));

        if (pedido.getStatus() != StatusPedidoCompra.APROVADO && pedido.getStatus() != StatusPedidoCompra.ENVIADO) {
            throw new IllegalArgumentException("Só é possível registrar recebimento de pedidos APROVADOS ou ENVIADOS");
        }

        pedido.setDataEntrega(dataEntrega);

        // Verificar se todos os itens foram recebidos
        List<ItemPedidoCompra> itensPendentes = itemPedidoCompraRepository.findItensPendentesRecebimento(id);
        if (itensPendentes.isEmpty()) {
            pedido.setStatus(StatusPedidoCompra.RECEBIDO_TOTAL);
        } else {
            pedido.setStatus(StatusPedidoCompra.RECEBIDO_PARCIAL);
        }

        return pedidoCompraRepository.save(pedido);
    }

    /**
     * Busca pedido por ID
     */
    @Transactional(readOnly = true)
    public Optional<PedidoCompra> obterPorId(Long id) {
        return pedidoCompraRepository.findById(id);
    }

    /**
     * Lista pedidos por status
     */
    @Transactional(readOnly = true)
    public List<PedidoCompra> listarPorStatus(StatusPedidoCompra status) {
        return pedidoCompraRepository.findByStatus(status);
    }

    /**
     * Lista pedidos com entrega atrasada
     */
    @Transactional(readOnly = true)
    public List<PedidoCompra> listarPedidosAtrasados() {
        return pedidoCompraRepository.findPedidosComEntregaAtrasada(LocalDate.now());
    }

    /**
     * Lista todos os pedidos ativos
     */
    @Transactional(readOnly = true)
    public List<PedidoCompra> listarAtivos() {
        return pedidoCompraRepository.findByAtivoTrue();
    }
}