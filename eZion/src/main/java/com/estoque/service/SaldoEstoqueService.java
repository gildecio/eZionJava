package com.estoque.service;

import com.estoque.model.Item;
import com.estoque.model.Local;
import com.estoque.model.MovimentacaoEstoque;
import com.estoque.model.SaldoEstoque;
import com.estoque.model.SaldoEstoqueHistorico;
import com.estoque.repository.SaldoEstoqueRepository;
import com.estoque.repository.SaldoEstoqueHistoricoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class SaldoEstoqueService {

    @Autowired
    private SaldoEstoqueRepository saldoEstoqueRepository;

    @Autowired
    private SaldoEstoqueHistoricoRepository saldoEstoqueHistoricoRepository;

    public Optional<SaldoEstoque> getSaldo(Item item, Local local) {
        return saldoEstoqueRepository.findByItemAndLocal(item, local);
    }

    public Optional<SaldoEstoque> getSaldoComLote(Item item, Local local, String lote) {
        return saldoEstoqueRepository.findByItemAndLocalAndLote(item, local, lote);
    }

    public Optional<SaldoEstoque> getSaldoById(Long id) {
        return saldoEstoqueRepository.findById(id);
    }

    public List<SaldoEstoque> getSaldosByItem(Item item) {
        return saldoEstoqueRepository.findByItem(item);
    }

    public List<SaldoEstoque> getSaldosByLocal(Local local) {
        return saldoEstoqueRepository.findByLocal(local);
    }

    public List<SaldoEstoque> getSaldosByItemId(Long itemId) {
        return saldoEstoqueRepository.findByItemId(itemId);
    }

    public List<SaldoEstoque> getSaldosByLocalId(Long localId) {
        return saldoEstoqueRepository.findByLocalId(localId);
    }

    public List<SaldoEstoque> getAllSaldos() {
        return saldoEstoqueRepository.findAll();
    }

    public List<SaldoEstoque> getSaldosByLote(String lote) {
        return saldoEstoqueRepository.findByLote(lote);
    }

    public List<SaldoEstoque> getSaldosByItemAndLote(Long itemId, String lote) {
        return saldoEstoqueRepository.findByItemIdAndLote(itemId, lote);
    }

    @Transactional
    public void adicionarMovimentacao(Item item, Local local, BigDecimal quantidade,
                                      MovimentacaoEstoque movimentacao, String tipoMovimentacao) {
        adicionarMovimentacaoComLote(item, local, null, quantidade, movimentacao, tipoMovimentacao);
    }

    @Transactional
    public void adicionarMovimentacaoComLote(Item item, Local local, String lote, BigDecimal quantidade,
                                            MovimentacaoEstoque movimentacao, String tipoMovimentacao) {
        SaldoEstoque saldo;
        
        if (lote != null && !lote.isEmpty()) {
            saldo = saldoEstoqueRepository.findByItemAndLocalAndLote(item, local, lote)
                    .orElseGet(() -> new SaldoEstoque(item, local, lote, BigDecimal.ZERO));
        } else {
            saldo = saldoEstoqueRepository.findByItemAndLocal(item, local)
                    .orElseGet(() -> new SaldoEstoque(item, local, null, BigDecimal.ZERO));
        }

        BigDecimal saldoAnterior = saldo.getQuantidade();

        // Aplicar a movimentação
        if (tipoMovimentacao.equals("ENTRADA") || tipoMovimentacao.equals("AJUSTE") || 
            (tipoMovimentacao.equals("DEVOLUCAO"))) {
            saldo.adicionarQuantidade(quantidade);
        } else if (tipoMovimentacao.equals("SAIDA")) {
            if (!saldo.temQuantidadeSuficiente(quantidade)) {
                throw new RuntimeException("Quantidade insuficiente no estoque. Disponível: " + 
                                         saldo.getQuantidade() + ", Solicitado: " + quantidade);
            }
            saldo.removerQuantidade(quantidade);
        }

        // Salvar saldo atualizado
        SaldoEstoque saldoAtualizado = saldoEstoqueRepository.save(saldo);

        // Registrar no histórico (imutável)
        SaldoEstoqueHistorico historico = new SaldoEstoqueHistorico(
                item,
                local,
                lote,
                saldoAnterior,
                saldoAtualizado.getQuantidade(),
                quantidade,
                movimentacao,
                tipoMovimentacao,
                movimentacao.getObservacao()
        );
        saldoEstoqueHistoricoRepository.save(historico);
    }

    public List<SaldoEstoqueHistorico> getHistoricoByMovimentacao(MovimentacaoEstoque movimentacao) {
        return saldoEstoqueHistoricoRepository.findByMovimentacao(movimentacao);
    }

    public List<SaldoEstoqueHistorico> getHistoricoByItem(Item item) {
        return saldoEstoqueHistoricoRepository.findByItemId(item.getId());
    }

    public List<SaldoEstoqueHistorico> getHistoricoByItemAndLocal(Item item, Local local) {
        return saldoEstoqueHistoricoRepository.findByItemIdAndLocalId(item.getId(), local.getId());
    }

    public List<SaldoEstoqueHistorico> getAllHistorico() {
        return saldoEstoqueHistoricoRepository.findAll();
    }

    @Transactional
    public void deletarSaldo(Long id) {
        saldoEstoqueRepository.deleteById(id);
    }

    // Validar consistência: SUM(histórico) deve == saldo_atual
    public boolean validarConsistencia(Item item, Local local) {
        return validarConsistenciaComLote(item, local, null);
    }

    public boolean validarConsistenciaComLote(Item item, Local local, String lote) {
        SaldoEstoque saldo;
        
        if (lote != null && !lote.isEmpty()) {
            saldo = saldoEstoqueRepository.findByItemAndLocalAndLote(item, local, lote)
                    .orElse(null);
        } else {
            saldo = saldoEstoqueRepository.findByItemAndLocal(item, local)
                    .orElse(null);
        }

        if (saldo == null) {
            return true; // Sem saldo, sem histórico = consistente
        }

        List<SaldoEstoqueHistorico> historico = saldoEstoqueHistoricoRepository
                .findByItemIdAndLocalId(item.getId(), local.getId());

        if (historico.isEmpty()) {
            return saldo.getQuantidade().compareTo(BigDecimal.ZERO) == 0;
        }

        // Recalcular saldo a partir do histórico
        BigDecimal saldoRecalculado = historico.stream()
                .filter(h -> lote == null || lote.isEmpty() || (h.getLote() != null && h.getLote().equals(lote)))
                .map(h -> {
                    if (h.getTipoMovimentacao().equals("ENTRADA") || 
                        h.getTipoMovimentacao().equals("AJUSTE") ||
                        h.getTipoMovimentacao().equals("DEVOLUCAO")) {
                        return h.getQuantidadeMovimentada();
                    } else if (h.getTipoMovimentacao().equals("SAIDA")) {
                        return h.getQuantidadeMovimentada().negate();
                    }
                    return BigDecimal.ZERO;
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return saldo.getQuantidade().compareTo(saldoRecalculado) == 0;
    }
}
