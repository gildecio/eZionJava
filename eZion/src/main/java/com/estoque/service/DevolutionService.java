package com.estoque.service;

import com.estoque.model.Devolucao;
import com.estoque.model.Local;
import com.estoque.model.MovimentacaoEstoque;
import com.estoque.model.NumeracaoDocumento;
import com.estoque.repository.DevolucaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class DevolutionService {

    @Autowired
    private DevolucaoRepository devolucaoRepository;

    @Autowired
    private NumeracaoService numeracaoService;

    @Autowired
    private SaldoEstoqueService saldoEstoqueService;

    public Devolucao createDevolucao(Devolucao devolucao) {
        // Gerar número sequencial
        if (devolucao.getNumero() == null || devolucao.getNumero().isEmpty()) {
            String numero = numeracaoService.gerarNumero(NumeracaoDocumento.TipoDocumento.DEVOLUCAO);
            devolucao.setNumero(numero);
        }
        devolucao.setStatus(Devolucao.Status.RASCUNHO);
        return devolucaoRepository.save(devolucao);
    }

    public Optional<Devolucao> getDevolutionById(Long id) {
        return devolucaoRepository.findById(id);
    }

    public List<Devolucao> getAllDevolucoes() {
        return devolucaoRepository.findAll();
    }

    public List<Devolucao> getDevolutionsByStatus(Devolucao.Status status) {
        return devolucaoRepository.findByStatus(status);
    }

    public List<Devolucao> getDevolutionsByTipo(Devolucao.Tipo tipo) {
        return devolucaoRepository.findByTipo(tipo);
    }

    public Optional<Devolucao> getDevolutionByNumero(String numero) {
        List<Devolucao> result = devolucaoRepository.findByNumero(numero);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    public Devolucao updateDevolucao(Long id, Devolucao devolutionDetails) {
        return devolucaoRepository.findById(id).map(dev -> {
            if (dev.getStatus() == Devolucao.Status.RASCUNHO) {
                dev.setMotivo(devolutionDetails.getMotivo());
                dev.setObservacao(devolutionDetails.getObservacao());
            }
            return devolucaoRepository.save(dev);
        }).orElseThrow(() -> new RuntimeException("Devolucao not found with id " + id));
    }

    public void deleteDevolucao(Long id) {
        devolucaoRepository.deleteById(id);
    }

    @Transactional
    public void processarDevolucao(Long devolutionId) {
        Devolucao devolucao = devolucaoRepository.findById(devolutionId)
                .orElseThrow(() -> new RuntimeException("Devolucao not found"));

        if (devolucao.getStatus() != Devolucao.Status.RASCUNHO) {
            throw new RuntimeException("Apenas devoluções em RASCUNHO podem ser processadas");
        }

        Local localPadrao = obterLocalPadrao();
        String tipoMovimentacao;
        MovimentacaoEstoque.TipoMovimentacao tipoMov;

        // Determinar o tipo de movimentação baseado no tipo de devolução
        if (devolucao.getTipo() == Devolucao.Tipo.DEVOLUCAO_CLIENTE) {
            // Devolução de cliente = ENTRADA no estoque
            tipoMovimentacao = "ENTRADA";
            tipoMov = MovimentacaoEstoque.TipoMovimentacao.DEVOLUCAO;
        } else {
            // Devolução de fornecedor = SAÍDA do estoque
            tipoMovimentacao = "SAIDA";
            tipoMov = MovimentacaoEstoque.TipoMovimentacao.DEVOLUCAO;
        }

        // Criar movimentação
        MovimentacaoEstoque movimentacao = new MovimentacaoEstoque(
                devolucao.getItem(),
                localPadrao,
                tipoMov,
                devolucao.getQuantidade()
        );
        movimentacao.setLote(devolucao.getLote());
        movimentacao.setReferencia("DEV-" + devolucao.getNumero());
        movimentacao.setObservacao(devolucao.getMotivo());

        saldoEstoqueService.adicionarMovimentacaoComLote(
                devolucao.getItem(),
                localPadrao,
                devolucao.getLote(),
                devolucao.getQuantidade(),
                movimentacao,
                tipoMovimentacao
        );

        devolucao.setStatus(Devolucao.Status.PROCESSADA);
        devolucao.setDataProcessamento(new Date());
        devolucaoRepository.save(devolucao);
    }

    @Transactional
    public void cancelarDevolucao(Long devolutionId) {
        Devolucao devolucao = devolucaoRepository.findById(devolutionId)
                .orElseThrow(() -> new RuntimeException("Devolucao not found"));

        if (devolucao.getStatus() == Devolucao.Status.PROCESSADA) {
            throw new RuntimeException("Devoluções já processadas não podem ser canceladas (implemente reversão se necessário)");
        }

        devolucao.setStatus(Devolucao.Status.CANCELADA);
        devolucaoRepository.save(devolucao);
    }

    private Local obterLocalPadrao() {
        Local local = new Local();
        local.setId(1L); // Assumindo que o local com id=1 é o estoque principal
        return local;
    }
}
