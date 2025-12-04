package com.estoque.service;

import com.estoque.model.Local;
import com.estoque.model.MovimentacaoEstoque;
import com.estoque.model.OrdemCompra;
import com.estoque.model.OrdemCompraItem;
import com.estoque.model.NumeracaoDocumento;
import com.estoque.repository.OrdemCompraRepository;
import com.estoque.repository.OrdemCompraItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrdemCompraService {

    @Autowired
    private OrdemCompraRepository ordemCompraRepository;

    @Autowired
    private OrdemCompraItemRepository ordemCompraItemRepository;

    @Autowired
    private NumeracaoService numeracaoService;

    @Autowired
    private SaldoEstoqueService saldoEstoqueService;

    public OrdemCompra createOrdemCompra(OrdemCompra ordemCompra) {
        // Gerar número sequencial
        if (ordemCompra.getNumero() == null || ordemCompra.getNumero().isEmpty()) {
            String numero = numeracaoService.gerarNumero(NumeracaoDocumento.TipoDocumento.ORDEM_COMPRA);
            ordemCompra.setNumero(numero);
        }
        ordemCompra.setStatus(OrdemCompra.Status.RASCUNHO);
        return ordemCompraRepository.save(ordemCompra);
    }

    public Optional<OrdemCompra> getOrdemCompraById(Long id) {
        return ordemCompraRepository.findById(id);
    }

    public List<OrdemCompra> getAllOrdensCompra() {
        return ordemCompraRepository.findAll();
    }

    public List<OrdemCompra> getOrdensByStatus(OrdemCompra.Status status) {
        return ordemCompraRepository.findByStatus(status);
    }

    public List<OrdemCompra> getOrdensByFornecedor(String fornecedor) {
        return ordemCompraRepository.findByFornecedor(fornecedor);
    }

    public Optional<OrdemCompra> getOrdemCompraByNumero(String numero) {
        List<OrdemCompra> result = ordemCompraRepository.findByNumero(numero);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    public OrdemCompra updateOrdemCompra(Long id, OrdemCompra ordemCompraDetails) {
        return ordemCompraRepository.findById(id).map(ordem -> {
            if (ordem.getStatus() == OrdemCompra.Status.RASCUNHO) {
                ordem.setFornecedor(ordemCompraDetails.getFornecedor());
                ordem.setObservacao(ordemCompraDetails.getObservacao());
            }
            return ordemCompraRepository.save(ordem);
        }).orElseThrow(() -> new RuntimeException("OrdemCompra not found with id " + id));
    }

    public void deleteOrdemCompra(Long id) {
        ordemCompraRepository.deleteById(id);
    }

    @Transactional
    public void enviarOrdemCompra(Long ordemId) {
        OrdemCompra ordem = ordemCompraRepository.findById(ordemId)
                .orElseThrow(() -> new RuntimeException("OrdemCompra not found"));

        if (ordem.getStatus() != OrdemCompra.Status.RASCUNHO) {
            throw new RuntimeException("Apenas ordens em RASCUNHO podem ser enviadas");
        }

        ordem.setStatus(OrdemCompra.Status.ENVIADA);
        ordemCompraRepository.save(ordem);
    }

    @Transactional
    public void confirmarOrdemCompra(Long ordemId) {
        OrdemCompra ordem = ordemCompraRepository.findById(ordemId)
                .orElseThrow(() -> new RuntimeException("OrdemCompra not found"));

        if (ordem.getStatus() != OrdemCompra.Status.ENVIADA) {
            throw new RuntimeException("Apenas ordens ENVIADAS podem ser confirmadas");
        }

        ordem.setStatus(OrdemCompra.Status.CONFIRMADA);
        ordemCompraRepository.save(ordem);
    }

    @Transactional
    public void receberOrdemCompra(Long ordemId, OrdemCompraItem itemRecebido) {
        OrdemCompra ordem = ordemCompraRepository.findById(ordemId)
                .orElseThrow(() -> new RuntimeException("OrdemCompra not found"));

        if (ordem.getStatus() != OrdemCompra.Status.CONFIRMADA && 
            ordem.getStatus() != OrdemCompra.Status.PARCIALMENTE_RECEBIDA) {
            throw new RuntimeException("Apenas ordens CONFIRMADAS ou PARCIALMENTE_RECEBIDAS podem receber itens");
        }

        OrdemCompraItem item = ordemCompraItemRepository.findById(itemRecebido.getId())
                .orElseThrow(() -> new RuntimeException("OrdemCompraItem not found"));

        if (itemRecebido.getQuantidadeRecebida().compareTo(item.getQuantidade()) > 0) {
            throw new RuntimeException("Quantidade recebida maior que a quantidade pedida");
        }

        item.setQuantidadeRecebida(item.getQuantidadeRecebida().add(itemRecebido.getQuantidadeRecebida()));
        ordemCompraItemRepository.save(item);

        // Gerar movimentação de ENTRADA no estoque
        Local localPadrao = obterLocalPadrao();
        MovimentacaoEstoque movimentacao = new MovimentacaoEstoque(
                item.getItem(),
                localPadrao,
                MovimentacaoEstoque.TipoMovimentacao.ENTRADA,
                itemRecebido.getQuantidadeRecebida()
        );
        movimentacao.setLote(itemRecebido.getLote());
        movimentacao.setReferencia("OC-" + ordem.getNumero());
        movimentacao.setObservacao("Recebimento de Ordem de Compra: " + ordem.getNumero());

        saldoEstoqueService.adicionarMovimentacaoComLote(
                item.getItem(),
                localPadrao,
                itemRecebido.getLote(),
                itemRecebido.getQuantidadeRecebida(),
                movimentacao,
                "ENTRADA"
        );

        // Verificar se todos os itens foram recebidos
        boolean todosRecebidos = ordem.getItens().stream()
                .allMatch(i -> i.getQuantidadeRecebida().compareTo(i.getQuantidade()) >= 0);

        if (todosRecebidos) {
            ordem.setStatus(OrdemCompra.Status.RECEBIDA);
        } else {
            ordem.setStatus(OrdemCompra.Status.PARCIALMENTE_RECEBIDA);
        }

        ordemCompraRepository.save(ordem);
    }

    @Transactional
    public void cancelarOrdemCompra(Long ordemId) {
        OrdemCompra ordem = ordemCompraRepository.findById(ordemId)
                .orElseThrow(() -> new RuntimeException("OrdemCompra not found"));

        if (ordem.getStatus() == OrdemCompra.Status.RECEBIDA) {
            throw new RuntimeException("Ordens já recebidas não podem ser canceladas (implemente devolução se necessário)");
        }

        ordem.setStatus(OrdemCompra.Status.CANCELADA);
        ordemCompraRepository.save(ordem);
    }

    public List<OrdemCompraItem> getItensOrdemCompra(Long ordemId) {
        return ordemCompraItemRepository.findByOrdemCompraId(ordemId);
    }

    private Local obterLocalPadrao() {
        Local local = new Local();
        local.setId(1L); // Assumindo que o local com id=1 é o estoque principal
        return local;
    }
}
