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
@Table(name = "ordem_compra_item")
public class OrdemCompraItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ordem_compra_id", nullable = false)
    private OrdemCompra ordemCompra;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    private BigDecimal quantidade;

    private String lote;

    private BigDecimal quantidadeRecebida;

    private String observacao;

    public OrdemCompraItem() {
        this.quantidadeRecebida = BigDecimal.ZERO;
    }

    public OrdemCompraItem(OrdemCompra ordemCompra, Item item, BigDecimal quantidade, String lote) {
        this();
        this.ordemCompra = ordemCompra;
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

    public OrdemCompra getOrdemCompra() {
        return ordemCompra;
    }

    public void setOrdemCompra(OrdemCompra ordemCompra) {
        this.ordemCompra = ordemCompra;
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
