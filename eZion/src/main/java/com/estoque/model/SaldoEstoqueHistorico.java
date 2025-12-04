package com.estoque.model;

import com.contabil.model.Empresa;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "saldo_estoque_historico")
public class SaldoEstoqueHistorico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @ManyToOne
    @JoinColumn(name = "local_id", nullable = false)
    private Local local;

    private String lote;

    private BigDecimal saldoAnterior;

    private BigDecimal saldoNovo;

    private BigDecimal quantidadeMovimentada;

    @ManyToOne
    @JoinColumn(name = "movimentacao_estoque_id")
    private MovimentacaoEstoque movimentacao;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataMovimentacao;

    private String tipoMovimentacao;

    private String observacao;

    public SaldoEstoqueHistorico() {
    }

    public SaldoEstoqueHistorico(Item item, Local local, String lote, BigDecimal saldoAnterior, Empresa empresa,
                                 BigDecimal saldoNovo, BigDecimal quantidadeMovimentada, MovimentacaoEstoque movimentacao,
                                 String tipoMovimentacao, String observacao) {
        this.item = item;
        this.local = local;
        this.lote = lote;
        this.saldoAnterior = saldoAnterior;
        this.empresa = empresa;
        this.saldoNovo = saldoNovo;
        this.quantidadeMovimentada = quantidadeMovimentada;
        this.movimentacao = movimentacao;
        this.dataMovimentacao = new Date();
        this.tipoMovimentacao = tipoMovimentacao;
        this.observacao = observacao;
    }

    public SaldoEstoqueHistorico(Item item, Local local, String lote, BigDecimal saldoAnterior, BigDecimal saldoNovo,
                                 BigDecimal quantidadeMovimentada, MovimentacaoEstoque movimentacao,
                                 String tipoMovimentacao, String observacao) {
        this.item = item;
        this.local = local;
        this.lote = lote;
        this.saldoAnterior = saldoAnterior;
        this.saldoNovo = saldoNovo;
        this.quantidadeMovimentada = quantidadeMovimentada;
        this.movimentacao = movimentacao;
        this.dataMovimentacao = new Date();
        this.tipoMovimentacao = tipoMovimentacao;
        this.observacao = observacao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Local getLocal() {
        return local;
    }

    public void setLocal(Local local) {
        this.local = local;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public BigDecimal getSaldoAnterior() {
        return saldoAnterior;
    }

    public void setSaldoAnterior(BigDecimal saldoAnterior) {
        this.saldoAnterior = saldoAnterior;
    }

    public BigDecimal getSaldoNovo() {
        return saldoNovo;
    }

    public void setSaldoNovo(BigDecimal saldoNovo) {
        this.saldoNovo = saldoNovo;
    }

    public BigDecimal getQuantidadeMovimentada() {
        return quantidadeMovimentada;
    }

    public void setQuantidadeMovimentada(BigDecimal quantidadeMovimentada) {
        this.quantidadeMovimentada = quantidadeMovimentada;
    }

    public MovimentacaoEstoque getMovimentacao() {
        return movimentacao;
    }

    public void setMovimentacao(MovimentacaoEstoque movimentacao) {
        this.movimentacao = movimentacao;
    }

    public Date getDataMovimentacao() {
        return dataMovimentacao;
    }

    public void setDataMovimentacao(Date dataMovimentacao) {
        this.dataMovimentacao = dataMovimentacao;
    }

    public String getTipoMovimentacao() {
        return tipoMovimentacao;
    }

    public void setTipoMovimentacao(String tipoMovimentacao) {
        this.tipoMovimentacao = tipoMovimentacao;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}
