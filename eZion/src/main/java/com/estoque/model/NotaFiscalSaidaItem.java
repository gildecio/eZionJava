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
@Table(name = "nota_fiscal_saida_item")
public class NotaFiscalSaidaItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "nota_fiscal_saida_id", nullable = false)
    private NotaFiscalSaida notaFiscalSaida;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    private BigDecimal quantidade;

    private String lote;

    private String observacao;

    public NotaFiscalSaidaItem() {
    }

    public NotaFiscalSaidaItem(NotaFiscalSaida notaFiscalSaida, Item item, BigDecimal quantidade, String lote) {
        this.notaFiscalSaida = notaFiscalSaida;
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

    public NotaFiscalSaida getNotaFiscalSaida() {
        return notaFiscalSaida;
    }

    public void setNotaFiscalSaida(NotaFiscalSaida notaFiscalSaida) {
        this.notaFiscalSaida = notaFiscalSaida;
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

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}
