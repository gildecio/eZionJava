package com.estoque.service;

import com.estoque.model.AjusteEstoque;
import com.estoque.model.Local;
import com.estoque.model.MovimentacaoEstoque;
import com.estoque.model.NumeracaoDocumento;
import com.estoque.repository.AjusteEstoqueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AjusteEstoqueService {

    @Autowired
    private AjusteEstoqueRepository ajusteEstoqueRepository;

    @Autowired
    private NumeracaoService numeracaoService;

    @Autowired
    private SaldoEstoqueService saldoEstoqueService;

    public AjusteEstoque createAjusteEstoque(AjusteEstoque ajuste) {
        // Gerar número sequencial
        if (ajuste.getNumero() == null || ajuste.getNumero().isEmpty()) {
            String numero = numeracaoService.gerarNumero(NumeracaoDocumento.TipoDocumento.AJUSTE_ESTOQUE);
            ajuste.setNumero(numero);
        }
        return ajusteEstoqueRepository.save(ajuste);
    }

    public Optional<AjusteEstoque> getAjusteEstoqueById(Long id) {
        return ajusteEstoqueRepository.findById(id);
    }

    public List<AjusteEstoque> getAllAjustesEstoque() {
        return ajusteEstoqueRepository.findAll();
    }

    public List<AjusteEstoque> getAjustesByTipo(AjusteEstoque.Tipo tipo) {
        return ajusteEstoqueRepository.findByTipo(tipo);
    }

    public Optional<AjusteEstoque> getAjusteByNumero(String numero) {
        List<AjusteEstoque> result = ajusteEstoqueRepository.findByNumero(numero);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    public AjusteEstoque updateAjusteEstoque(Long id, AjusteEstoque ajusteDetails) {
        return ajusteEstoqueRepository.findById(id).map(ajuste -> {
            ajuste.setMotivo(ajusteDetails.getMotivo());
            ajuste.setDescricao(ajusteDetails.getDescricao());
            ajuste.setUsuario(ajusteDetails.getUsuario());
            return ajusteEstoqueRepository.save(ajuste);
        }).orElseThrow(() -> new RuntimeException("AjusteEstoque not found with id " + id));
    }

    public void deleteAjusteEstoque(Long id) {
        ajusteEstoqueRepository.deleteById(id);
    }

    @Transactional
    public void processarAjuste(Long ajusteId) {
        AjusteEstoque ajuste = ajusteEstoqueRepository.findById(ajusteId)
                .orElseThrow(() -> new RuntimeException("AjusteEstoque not found"));

        Local localPadrao = obterLocalPadrao();
        String tipoMovimentacao;
        MovimentacaoEstoque.TipoMovimentacao tipoMov = MovimentacaoEstoque.TipoMovimentacao.AJUSTE;

        // Determinar tipo de movimentação baseado no tipo de ajuste
        if (ajuste.getTipo() == AjusteEstoque.Tipo.ENTRADA) {
            tipoMovimentacao = "ENTRADA";
        } else {
            tipoMovimentacao = "SAIDA";
        }

        // Criar movimentação
        MovimentacaoEstoque movimentacao = new MovimentacaoEstoque(
                ajuste.getItem(),
                localPadrao,
                tipoMov,
                ajuste.getQuantidade()
        );
        movimentacao.setLote(ajuste.getLote());
        movimentacao.setReferencia("ADJ-" + ajuste.getNumero());
        movimentacao.setObservacao(ajuste.getMotivo() + " - " + ajuste.getDescricao());

        saldoEstoqueService.adicionarMovimentacaoComLote(
                ajuste.getItem(),
                localPadrao,
                ajuste.getLote(),
                ajuste.getQuantidade(),
                movimentacao,
                tipoMovimentacao
        );

        ajuste.setDataProcessamento(new Date());
        ajusteEstoqueRepository.save(ajuste);
    }

    private Local obterLocalPadrao() {
        Local local = new Local();
        local.setId(1L); // Assumindo que o local com id=1 é o estoque principal
        return local;
    }
}
