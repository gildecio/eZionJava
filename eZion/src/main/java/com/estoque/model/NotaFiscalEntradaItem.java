package com.estoque.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "nota_fiscal_entrada_item")
public class NotaFiscalEntradaItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "nota_fiscal_entrada_id", nullable = false)
    private NotaFiscalEntrada notaFiscalEntrada;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    private BigDecimal quantidade;

    private String lote;

    private BigDecimal quantidadeRecebida;

    private String observacao;

    public NotaFiscalEntradaItem() {
        this.quantidadeRecebida = BigDecimal.ZERO;
    }

    public NotaFiscalEntradaItem(NotaFiscalEntrada notaFiscalEntrada, Item item, BigDecimal quantidade, String lote) {
        this();
        this.notaFiscalEntrada = notaFiscalEntrada;
        this.item = item;
        this.quantidade = quantidade;
        this.lote = lote;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NotaFiscalEntrada getNotaFiscalEntrada() {
        return notaFiscalEntrada;
    }

    public void setNotaFiscalEntrada(NotaFiscalEntrada notaFiscalEntrada) {
        this.notaFiscalEntrada = notaFiscalEntrada;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public BigDecimal getQuantidadeRecebida() {
        return quantidadeRecebida;
    }

    public void setQuantidadeRecebida(BigDecimal quantidadeRecebida) {
        this.quantidadeRecebida = quantidadeRecebida;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public boolean estaCompleto() {
        return this.quantidade.compareTo(this.quantidadeRecebida) == 0;
    }
}
