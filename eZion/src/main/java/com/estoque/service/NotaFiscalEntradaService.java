package com.estoque.service;

import com.estoque.model.Local;
import com.estoque.model.MovimentacaoEstoque;
import com.estoque.model.NotaFiscalEntrada;
import com.estoque.model.NotaFiscalEntradaItem;
import com.estoque.model.NumeracaoDocumento;
import com.estoque.repository.NotaFiscalEntradaRepository;
import com.estoque.repository.NotaFiscalEntradaItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class NotaFiscalEntradaService {

    @Autowired
    private NotaFiscalEntradaRepository notaFiscalEntradaRepository;

    @Autowired
    private NotaFiscalEntradaItemRepository notaFiscalEntradaItemRepository;

    @Autowired
    private NumeracaoService numeracaoService;

    @Autowired
    private SaldoEstoqueService saldoEstoqueService;

    public NotaFiscalEntrada createNotaFiscalEntrada(NotaFiscalEntrada notaFiscalEntrada) {
        // Gerar número sequencial
        if (notaFiscalEntrada.getNumero() == null || notaFiscalEntrada.getNumero().isEmpty()) {
            String numero = numeracaoService.gerarNumero(NumeracaoDocumento.TipoDocumento.NOTA_FISCAL_ENTRADA);
            notaFiscalEntrada.setNumero(numero);
        }
        notaFiscalEntrada.setStatus(NotaFiscalEntrada.Status.RASCUNHO);
        notaFiscalEntrada.setDataEmissao(new Date());
        return notaFiscalEntradaRepository.save(notaFiscalEntrada);
    }

    public Optional<NotaFiscalEntrada> getNotaFiscalEntradaById(Long id) {
        return notaFiscalEntradaRepository.findById(id);
    }

    public List<NotaFiscalEntrada> getAllNotasFiscaisEntrada() {
        return notaFiscalEntradaRepository.findAll();
    }

    public List<NotaFiscalEntrada> getNotasFiscaisByStatus(NotaFiscalEntrada.Status status) {
        return notaFiscalEntradaRepository.findByStatus(status);
    }

    public List<NotaFiscalEntrada> getNotasFiscaisByFornecedor(String fornecedor) {
        return notaFiscalEntradaRepository.findByFornecedor(fornecedor);
    }

    public Optional<NotaFiscalEntrada> getNotaFiscalByNumero(String numero) {
        List<NotaFiscalEntrada> result = notaFiscalEntradaRepository.findByNumero(numero);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    public NotaFiscalEntrada updateNotaFiscalEntrada(Long id, NotaFiscalEntrada notaFiscalEntradaDetails) {
        return notaFiscalEntradaRepository.findById(id).map(nota -> {
            if (nota.getStatus() == NotaFiscalEntrada.Status.RASCUNHO) {
                nota.setFornecedor(notaFiscalEntradaDetails.getFornecedor());
                nota.setChaveNFe(notaFiscalEntradaDetails.getChaveNFe());
                nota.setSerie(notaFiscalEntradaDetails.getSerie());
                nota.setObservacao(notaFiscalEntradaDetails.getObservacao());
            }
            return notaFiscalEntradaRepository.save(nota);
        }).orElseThrow(() -> new RuntimeException("NotaFiscalEntrada not found with id " + id));
    }

    public void deleteNotaFiscalEntrada(Long id) {
        notaFiscalEntradaRepository.deleteById(id);
    }

    @Transactional
    public void processarNotaFiscalEntrada(Long notaId) {
        NotaFiscalEntrada nota = notaFiscalEntradaRepository.findById(notaId)
                .orElseThrow(() -> new RuntimeException("NotaFiscalEntrada not found"));

        if (nota.getStatus() != NotaFiscalEntrada.Status.RASCUNHO) {
            throw new RuntimeException("Apenas notas em RASCUNHO podem ser processadas");
        }

        // Gerar movimentações de ENTRADA para cada item
        Local localPadrao = obterLocalPadrao();
        for (NotaFiscalEntradaItem item : nota.getItens()) {
            MovimentacaoEstoque movimentacao = new MovimentacaoEstoque(
                    item.getItem(),
                    localPadrao,
                    MovimentacaoEstoque.TipoMovimentacao.ENTRADA,
                    item.getQuantidade()
            );
            movimentacao.setLote(item.getLote());
            movimentacao.setReferencia("NF-E-" + nota.getNumero());
            movimentacao.setObservacao("Entrada da Nota Fiscal: " + nota.getNumero());

            saldoEstoqueService.adicionarMovimentacaoComLote(
                    item.getItem(),
                    localPadrao,
                    item.getLote(),
                    item.getQuantidade(),
                    movimentacao,
                    "ENTRADA"
            );
        }

        nota.setStatus(NotaFiscalEntrada.Status.PROCESSADA);
        nota.setDataProcessamento(new Date());
        notaFiscalEntradaRepository.save(nota);
    }

    @Transactional
    public void cancelarNotaFiscalEntrada(Long notaId) {
        NotaFiscalEntrada nota = notaFiscalEntradaRepository.findById(notaId)
                .orElseThrow(() -> new RuntimeException("NotaFiscalEntrada not found"));

        if (nota.getStatus() == NotaFiscalEntrada.Status.PROCESSADA) {
            throw new RuntimeException("Notas já processadas não podem ser canceladas (implemente reversão se necessário)");
        }

        nota.setStatus(NotaFiscalEntrada.Status.CANCELADA);
        notaFiscalEntradaRepository.save(nota);
    }

    public List<NotaFiscalEntradaItem> getItensNotaFiscal(Long notaId) {
        return notaFiscalEntradaItemRepository.findByNotaFiscalEntradaId(notaId);
    }

    private Local obterLocalPadrao() {
        Local local = new Local();
        local.setId(1L); // Assumindo que o local com id=1 é o estoque principal
        return local;
    }
}
