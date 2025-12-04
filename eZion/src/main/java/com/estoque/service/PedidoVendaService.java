package com.estoque.service;

import com.estoque.model.Item;
import com.estoque.model.Local;
import com.estoque.model.MovimentacaoEstoque;
import com.estoque.model.NumeracaoDocumento;
import com.estoque.model.PedidoVenda;
import com.estoque.model.PedidoVendaItem;
import com.estoque.repository.PedidoVendaRepository;
import com.estoque.repository.PedidoVendaItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoVendaService {

    @Autowired
    private PedidoVendaRepository pedidoVendaRepository;

    @Autowired
    private PedidoVendaItemRepository pedidoVendaItemRepository;

    @Autowired
    private MovimentacaoEstoqueService movimentacaoEstoqueService;

    @Autowired
    private SaldoEstoqueService saldoEstoqueService;

    @Autowired
    private NumeracaoService numeracaoService;

    public PedidoVenda createPedidoVenda(PedidoVenda pedidoVenda) {
        // Gerar número sequencial para o pedido
        if (pedidoVenda.getNumero() == null || pedidoVenda.getNumero().isEmpty()) {
            String numero = numeracaoService.gerarNumero(NumeracaoDocumento.TipoDocumento.PEDIDO_VENDA);
            pedidoVenda.setNumero(numero);
        }
        pedidoVenda.setStatus(PedidoVenda.Status.RASCUNHO);
        return pedidoVendaRepository.save(pedidoVenda);
    }

    public Optional<PedidoVenda> getPedidoVendaById(Long id) {
        return pedidoVendaRepository.findById(id);
    }

    public List<PedidoVenda> getAllPedidosVenda() {
        return pedidoVendaRepository.findAll();
    }

    public List<PedidoVenda> getPedidoVendaByStatus(PedidoVenda.Status status) {
        return pedidoVendaRepository.findByStatus(status);
    }

    public List<PedidoVenda> getPedidoVendaByCliente(String cliente) {
        return pedidoVendaRepository.findByCliente(cliente);
    }

    public PedidoVenda updatePedidoVenda(Long id, PedidoVenda pedidoVendaDetails) {
        return pedidoVendaRepository.findById(id).map(pedido -> {
            if (pedido.getStatus() == PedidoVenda.Status.RASCUNHO || 
                pedido.getStatus() == PedidoVenda.Status.CONFIRMADO) {
                pedido.setNumero(pedidoVendaDetails.getNumero());
                pedido.setCliente(pedidoVendaDetails.getCliente());
                pedido.setDataPrevista(pedidoVendaDetails.getDataPrevista());
                pedido.setObservacao(pedidoVendaDetails.getObservacao());
            }
            return pedidoVendaRepository.save(pedido);
        }).orElseThrow(() -> new RuntimeException("PedidoVenda not found with id " + id));
    }

    public void deletePedidoVenda(Long id) {
        pedidoVendaRepository.deleteById(id);
    }

    @Transactional
    public void confirmarPedidoVenda(Long pedidoVendaId) {
        PedidoVenda pedido = pedidoVendaRepository.findById(pedidoVendaId)
                .orElseThrow(() -> new RuntimeException("PedidoVenda not found"));

        if (pedido.getStatus() != PedidoVenda.Status.RASCUNHO) {
            throw new RuntimeException("Apenas pedidos em RASCUNHO podem ser confirmados");
        }

        pedido.setStatus(PedidoVenda.Status.CONFIRMADO);
        pedidoVendaRepository.save(pedido);
    }

    @Transactional
    public void marcarComoSeparado(Long pedidoVendaId) {
        PedidoVenda pedido = pedidoVendaRepository.findById(pedidoVendaId)
                .orElseThrow(() -> new RuntimeException("PedidoVenda not found"));

        if (pedido.getStatus() != PedidoVenda.Status.CONFIRMADO) {
            throw new RuntimeException("Apenas pedidos CONFIRMADOS podem ser separados");
        }

        // Validar se há estoque suficiente para todos os itens
        for (PedidoVendaItem item : pedido.getItens()) {
            var saldo = saldoEstoqueService.getSaldoComLote(item.getItem(), 
                    obterLocalPadrao(), item.getLote())
                    .orElse(null);

            if (saldo == null || !saldo.temQuantidadeSuficiente(item.getQuantidade())) {
                throw new RuntimeException("Estoque insuficiente para o item: " + item.getItem().getNome());
            }
        }

        pedido.setStatus(PedidoVenda.Status.SEPARADO);
        pedidoVendaRepository.save(pedido);
    }

    @Transactional
    public void expedir(Long pedidoVendaId, Local localDestino) {
        PedidoVenda pedido = pedidoVendaRepository.findById(pedidoVendaId)
                .orElseThrow(() -> new RuntimeException("PedidoVenda not found"));

        if (pedido.getStatus() != PedidoVenda.Status.SEPARADO && 
            pedido.getStatus() != PedidoVenda.Status.CONFIRMADO) {
            throw new RuntimeException("Apenas pedidos CONFIRMADOS ou SEPARADOS podem ser expedidos");
        }

        Local localOrigem = obterLocalPadrao();

        // Gerar movimentações de SAÍDA para cada item
        for (PedidoVendaItem item : pedido.getItens()) {
            MovimentacaoEstoque movimentacao = new MovimentacaoEstoque(
                    item.getItem(),
                    localOrigem,
                    MovimentacaoEstoque.TipoMovimentacao.SAIDA,
                    item.getQuantidade()
            );
            movimentacao.setLote(item.getLote());
            movimentacao.setReferencia("PV-" + pedido.getNumero());
            movimentacao.setObservacao("Expedição para cliente: " + pedido.getCliente());

            movimentacaoEstoqueService.createMovimentacao(movimentacao);
            item.setQuantidadeExpedida(item.getQuantidade());
            pedidoVendaItemRepository.save(item);
        }

        pedido.setStatus(PedidoVenda.Status.EXPEDIDO);
        pedido.setDataExpedicao(new Date());
        pedidoVendaRepository.save(pedido);
    }

    @Transactional
    public void confirmarEntrega(Long pedidoVendaId, Date dataEntrega) {
        PedidoVenda pedido = pedidoVendaRepository.findById(pedidoVendaId)
                .orElseThrow(() -> new RuntimeException("PedidoVenda not found"));

        if (pedido.getStatus() != PedidoVenda.Status.EXPEDIDO) {
            throw new RuntimeException("Apenas pedidos EXPEDIDOS podem ter entrega confirmada");
        }

        pedido.setStatus(PedidoVenda.Status.ENTREGUE);
        pedido.setDataEntrega(dataEntrega != null ? dataEntrega : new Date());
        pedidoVendaRepository.save(pedido);
    }

    @Transactional
    public void cancelarPedidoVenda(Long pedidoVendaId) {
        PedidoVenda pedido = pedidoVendaRepository.findById(pedidoVendaId)
                .orElseThrow(() -> new RuntimeException("PedidoVenda not found"));

        if (pedido.getStatus() == PedidoVenda.Status.ENTREGUE) {
            throw new RuntimeException("Pedidos ENTREGUES não podem ser cancelados");
        }

        pedido.setStatus(PedidoVenda.Status.CANCELADO);
        pedidoVendaRepository.save(pedido);
    }

    public List<PedidoVendaItem> getItensPedidoVenda(Long pedidoVendaId) {
        return pedidoVendaItemRepository.findByPedidoVendaId(pedidoVendaId);
    }

    // Método auxiliar para obter local padrão (estoque principal)
    // TODO: Implementar configuração de local padrão no sistema
    private Local obterLocalPadrao() {
        Local local = new Local();
        local.setId(1L); // Assumindo que o local com id=1 é o estoque principal
        return local;
    }
}
