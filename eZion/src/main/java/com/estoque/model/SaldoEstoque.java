package com.estoque.model;

import com.contabil.model.Empresa;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "saldo_estoque", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"item_id", "local_id", "lote"})
})
public class SaldoEstoque {

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

    private BigDecimal quantidade;

    private Date dataUltimaAtualizacao;

    public SaldoEstoque() {
    }

    public SaldoEstoque(Item item, Local local, String lote, BigDecimal quantidade, Empresa empresa) {
        this.item = item;
        this.local = local;
        this.lote = lote;
        this.quantidade = quantidade != null ? quantidade : BigDecimal.ZERO;
        this.empresa = empresa;
        this.dataUltimaAtualizacao = new Date();
    }

    public SaldoEstoque(Item item, Local local, String lote, BigDecimal quantidade) {
        this.item = item;
        this.local = local;
        this.lote = lote;
        this.quantidade = quantidade != null ? quantidade : BigDecimal.ZERO;
        this.dataUltimaAtualizacao = new Date();
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

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade != null ? quantidade : BigDecimal.ZERO;
        this.dataUltimaAtualizacao = new Date();
    }

    public Date getDataUltimaAtualizacao() {
        return dataUltimaAtualizacao;
    }

    public void setDataUltimaAtualizacao(Date dataUltimaAtualizacao) {
        this.dataUltimaAtualizacao = dataUltimaAtualizacao;
    }

    public void adicionarQuantidade(BigDecimal qtd) {
        if (qtd != null && qtd.compareTo(BigDecimal.ZERO) > 0) {
            this.quantidade = this.quantidade.add(qtd);
            this.dataUltimaAtualizacao = new Date();
        }
    }

    public void removerQuantidade(BigDecimal qtd) {
        if (qtd != null && qtd.compareTo(BigDecimal.ZERO) > 0) {
            this.quantidade = this.quantidade.subtract(qtd);
            this.dataUltimaAtualizacao = new Date();
        }
    }

    public boolean temQuantidadeSuficiente(BigDecimal qtd) {
        return this.quantidade.compareTo(qtd) >= 0;
    }
}
