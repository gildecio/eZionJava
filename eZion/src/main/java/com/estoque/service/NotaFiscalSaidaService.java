package com.estoque.service;

import com.estoque.model.Local;
import com.estoque.model.MovimentacaoEstoque;
import com.estoque.model.NotaFiscalSaida;
import com.estoque.model.NotaFiscalSaidaItem;
import com.estoque.model.NumeracaoDocumento;
import com.estoque.repository.NotaFiscalSaidaRepository;
import com.estoque.repository.NotaFiscalSaidaItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class NotaFiscalSaidaService {

    @Autowired
    private NotaFiscalSaidaRepository notaFiscalSaidaRepository;

    @Autowired
    private NotaFiscalSaidaItemRepository notaFiscalSaidaItemRepository;

    @Autowired
    private NumeracaoService numeracaoService;

    @Autowired
    private SaldoEstoqueService saldoEstoqueService;

    public NotaFiscalSaida createNotaFiscalSaida(NotaFiscalSaida notaFiscalSaida) {
        // Gerar número sequencial
        if (notaFiscalSaida.getNumero() == null || notaFiscalSaida.getNumero().isEmpty()) {
            String numero = numeracaoService.gerarNumero(NumeracaoDocumento.TipoDocumento.NOTA_FISCAL_SAIDA);
            notaFiscalSaida.setNumero(numero);
        }
        notaFiscalSaida.setStatus(NotaFiscalSaida.Status.RASCUNHO);
        notaFiscalSaida.setDataEmissao(new Date());
        return notaFiscalSaidaRepository.save(notaFiscalSaida);
    }

    public Optional<NotaFiscalSaida> getNotaFiscalSaidaById(Long id) {
        return notaFiscalSaidaRepository.findById(id);
    }

    public List<NotaFiscalSaida> getAllNotasFiscaisSaida() {
        return notaFiscalSaidaRepository.findAll();
    }

    public List<NotaFiscalSaida> getNotasFiscaisByStatus(NotaFiscalSaida.Status status) {
        return notaFiscalSaidaRepository.findByStatus(status);
    }

    public List<NotaFiscalSaida> getNotasFiscaisByCliente(String cliente) {
        return notaFiscalSaidaRepository.findByCliente(cliente);
    }

    public Optional<NotaFiscalSaida> getNotaFiscalByNumero(String numero) {
        List<NotaFiscalSaida> result = notaFiscalSaidaRepository.findByNumero(numero);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    public NotaFiscalSaida updateNotaFiscalSaida(Long id, NotaFiscalSaida notaFiscalSaidaDetails) {
        return notaFiscalSaidaRepository.findById(id).map(nota -> {
            if (nota.getStatus() == NotaFiscalSaida.Status.RASCUNHO) {
                nota.setCliente(notaFiscalSaidaDetails.getCliente());
                nota.setChaveNFe(notaFiscalSaidaDetails.getChaveNFe());
                nota.setSerie(notaFiscalSaidaDetails.getSerie());
                nota.setObservacao(notaFiscalSaidaDetails.getObservacao());
            }
            return notaFiscalSaidaRepository.save(nota);
        }).orElseThrow(() -> new RuntimeException("NotaFiscalSaida not found with id " + id));
    }

    public void deleteNotaFiscalSaida(Long id) {
        notaFiscalSaidaRepository.deleteById(id);
    }

    @Transactional
    public void processarNotaFiscalSaida(Long notaId) {
        NotaFiscalSaida nota = notaFiscalSaidaRepository.findById(notaId)
                .orElseThrow(() -> new RuntimeException("NotaFiscalSaida not found"));

        if (nota.getStatus() != NotaFiscalSaida.Status.RASCUNHO) {
            throw new RuntimeException("Apenas notas em RASCUNHO podem ser processadas");
        }

        // Validar e gerar movimentações de SAÍDA para cada item
        Local localPadrao = obterLocalPadrao();
        for (NotaFiscalSaidaItem item : nota.getItens()) {
            // Validar estoque disponível
            var saldo = saldoEstoqueService.getSaldoComLote(item.getItem(), localPadrao, item.getLote())
                    .orElse(null);

            if (saldo == null || !saldo.temQuantidadeSuficiente(item.getQuantidade())) {
                throw new RuntimeException("Estoque insuficiente para o item: " + item.getItem().getNome());
            }

            // Criar movimentação
            MovimentacaoEstoque movimentacao = new MovimentacaoEstoque(
                    item.getItem(),
                    localPadrao,
                    MovimentacaoEstoque.TipoMovimentacao.SAIDA,
                    item.getQuantidade()
            );
            movimentacao.setLote(item.getLote());
            movimentacao.setReferencia("NF-S-" + nota.getNumero());
            movimentacao.setObservacao("Saída pela Nota Fiscal: " + nota.getNumero());

            saldoEstoqueService.adicionarMovimentacaoComLote(
                    item.getItem(),
                    localPadrao,
                    item.getLote(),
                    item.getQuantidade(),
                    movimentacao,
                    "SAIDA"
            );
        }

        nota.setStatus(NotaFiscalSaida.Status.PROCESSADA);
        nota.setDataProcessamento(new Date());
        notaFiscalSaidaRepository.save(nota);
    }

    @Transactional
    public void cancelarNotaFiscalSaida(Long notaId) {
        NotaFiscalSaida nota = notaFiscalSaidaRepository.findById(notaId)
                .orElseThrow(() -> new RuntimeException("NotaFiscalSaida not found"));

        if (nota.getStatus() == NotaFiscalSaida.Status.PROCESSADA) {
            throw new RuntimeException("Notas já processadas não podem ser canceladas (implemente reversão se necessário)");
        }

        nota.setStatus(NotaFiscalSaida.Status.CANCELADA);
        notaFiscalSaidaRepository.save(nota);
    }

    public List<NotaFiscalSaidaItem> getItensNotaFiscal(Long notaId) {
        return notaFiscalSaidaItemRepository.findByNotaFiscalSaidaId(notaId);
    }

    private Local obterLocalPadrao() {
        Local local = new Local();
        local.setId(1L); // Assumindo que o local com id=1 é o estoque principal
        return local;
    }
}
