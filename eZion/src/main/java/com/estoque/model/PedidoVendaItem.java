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
@Table(name = "pedido_venda_item")
public class PedidoVendaItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pedido_venda_id", nullable = false)
    private PedidoVenda pedidoVenda;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    private BigDecimal quantidade;

    private String lote;

    private BigDecimal quantidadeSeparada;

    private BigDecimal quantidadeExpedida;

    private String observacao;

    public PedidoVendaItem() {
        this.quantidadeSeparada = BigDecimal.ZERO;
        this.quantidadeExpedida = BigDecimal.ZERO;
    }

    public PedidoVendaItem(PedidoVenda pedidoVenda, Item item, BigDecimal quantidade, String lote) {
        this();
        this.pedidoVenda = pedidoVenda;
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

    public PedidoVenda getPedidoVenda() {
        return pedidoVenda;
    }

    public void setPedidoVenda(PedidoVenda pedidoVenda) {
        this.pedidoVenda = pedidoVenda;
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

    public BigDecimal getQuantidadeSeparada() {
        return quantidadeSeparada;
    }

    public void setQuantidadeSeparada(BigDecimal quantidadeSeparada) {
        this.quantidadeSeparada = quantidadeSeparada;
    }

    public BigDecimal getQuantidadeExpedida() {
        return quantidadeExpedida;
    }

    public void setQuantidadeExpedida(BigDecimal quantidadeExpedida) {
        this.quantidadeExpedida = quantidadeExpedida;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public boolean estaCompleto() {
        return this.quantidade.compareTo(this.quantidadeExpedida) == 0;
    }
}
