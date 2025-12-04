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
@Table(name = "movimentacao_estoque")
public class MovimentacaoEstoque {

    public static enum TipoMovimentacao {
        ENTRADA,
        SAIDA,
        AJUSTE,
        DEVOLUCAO
    }

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

    private TipoMovimentacao tipo;

    private BigDecimal quantidade;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataMovimentacao;

    private String observacao;

    private String referencia;

    public MovimentacaoEstoque() {
    }

    public MovimentacaoEstoque(Item item, Local local, TipoMovimentacao tipo, BigDecimal quantidade, Empresa empresa) {
        this.item = item;
        this.local = local;
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.empresa = empresa;
        this.dataMovimentacao = new Date();
    }

    public MovimentacaoEstoque(Item item, Local local, TipoMovimentacao tipo, BigDecimal quantidade) {
        this.item = item;
        this.local = local;
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.dataMovimentacao = new Date();
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

    public TipoMovimentacao getTipo() {
        return tipo;
    }

    public void setTipo(TipoMovimentacao tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

    public Date getDataMovimentacao() {
        return dataMovimentacao;
    }

    public void setDataMovimentacao(Date dataMovimentacao) {
        this.dataMovimentacao = dataMovimentacao;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }
}
