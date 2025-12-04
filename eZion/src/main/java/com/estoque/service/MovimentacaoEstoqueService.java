package com.estoque.service;

import com.estoque.model.Item;
import com.estoque.model.Local;
import com.estoque.model.MovimentacaoEstoque;
import com.estoque.repository.MovimentacaoEstoqueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class MovimentacaoEstoqueService {

    @Autowired
    private MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;

    @Autowired
    private SaldoEstoqueService saldoEstoqueService;

    @Transactional
    public MovimentacaoEstoque createMovimentacao(MovimentacaoEstoque movimentacao) {
        MovimentacaoEstoque saved = movimentacaoEstoqueRepository.save(movimentacao);
        
        // Atualizar saldo com a nova movimentação
        String tipoMovimentacao = movimentacao.getTipo().toString();
        saldoEstoqueService.adicionarMovimentacaoComLote(
                movimentacao.getItem(),
                movimentacao.getLocal(),
                movimentacao.getLote(),
                movimentacao.getQuantidade(),
                saved,
                tipoMovimentacao
        );
        
        return saved;
    }

    @Transactional
    public void transferir(Item item, Local localOrigem, Local localDestino, java.math.BigDecimal quantidade, String referencia) {
        transferirComLote(item, localOrigem, localDestino, null, quantidade, referencia);
    }

    @Transactional
    public void transferirComLote(Item item, Local localOrigem, Local localDestino, String lote, java.math.BigDecimal quantidade, String referencia) {
        // Validar saldo no local de origem
        var saldoOrigem = lote != null && !lote.isEmpty() ?
                saldoEstoqueService.getSaldoComLote(item, localOrigem, lote).orElse(null) :
                saldoEstoqueService.getSaldo(item, localOrigem).orElse(null);
        
        if (saldoOrigem == null || !saldoOrigem.temQuantidadeSuficiente(quantidade)) {
            throw new RuntimeException("Quantidade insuficiente no local de origem");
        }

        // Criar movimento de SAÍDA do local de origem
        MovimentacaoEstoque saida = new MovimentacaoEstoque(item, localOrigem, MovimentacaoEstoque.TipoMovimentacao.SAIDA, quantidade);
        saida.setLote(lote);
        saida.setReferencia(referencia);
        saida.setObservacao("Transferência para " + localDestino.getNome());
        MovimentacaoEstoque saidaSalva = movimentacaoEstoqueRepository.save(saida);
        saldoEstoqueService.adicionarMovimentacaoComLote(item, localOrigem, lote, quantidade, saidaSalva, "SAIDA");

        // Criar movimento de ENTRADA no local de destino
        MovimentacaoEstoque entrada = new MovimentacaoEstoque(item, localDestino, MovimentacaoEstoque.TipoMovimentacao.ENTRADA, quantidade);
        entrada.setLote(lote);
        entrada.setReferencia(referencia);
        entrada.setObservacao("Transferência do " + localOrigem.getNome());
        MovimentacaoEstoque entradaSalva = movimentacaoEstoqueRepository.save(entrada);
        saldoEstoqueService.adicionarMovimentacaoComLote(item, localDestino, lote, quantidade, entradaSalva, "ENTRADA");
    }

    public List<MovimentacaoEstoque> getAllMovimentacoes() {
        return movimentacaoEstoqueRepository.findAll();
    }

    public Optional<MovimentacaoEstoque> getMovimentacaoById(Long id) {
        return movimentacaoEstoqueRepository.findById(id);
    }

    @Transactional
    public MovimentacaoEstoque updateMovimentacao(Long id, MovimentacaoEstoque movimentacaoDetails) {
        return movimentacaoEstoqueRepository.findById(id).map(movimentacao -> {
            movimentacao.setItem(movimentacaoDetails.getItem());
            movimentacao.setLocal(movimentacaoDetails.getLocal());
            movimentacao.setTipo(movimentacaoDetails.getTipo());
            movimentacao.setQuantidade(movimentacaoDetails.getQuantidade());
            movimentacao.setDataMovimentacao(movimentacaoDetails.getDataMovimentacao());
            movimentacao.setObservacao(movimentacaoDetails.getObservacao());
            movimentacao.setReferencia(movimentacaoDetails.getReferencia());
            return movimentacaoEstoqueRepository.save(movimentacao);
        }).orElseThrow(() -> new RuntimeException("MovimentacaoEstoque not found with id " + id));
    }

    @Transactional
    public void deleteMovimentacao(Long id) {
        movimentacaoEstoqueRepository.deleteById(id);
    }

    public List<MovimentacaoEstoque> getMovimentacoesByItem(Item item) {
        return movimentacaoEstoqueRepository.findByItem(item);
    }

    public List<MovimentacaoEstoque> getMovimentacoesByItemId(Long itemId) {
        return movimentacaoEstoqueRepository.findByItemId(itemId);
    }

    public List<MovimentacaoEstoque> getMovimentacoesByTipo(MovimentacaoEstoque.TipoMovimentacao tipo) {
        return movimentacaoEstoqueRepository.findByTipo(tipo);
    }
}
