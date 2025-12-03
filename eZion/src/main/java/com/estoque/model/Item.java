package com.estoque.model;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "item")
public class Item {
    public static enum TipoItem {
        PRODUTO,
        PRODUTO_SEMI_ELABORADO,
        INSUMO,
        IMOBILIZADO,
        CONSUMO,
        EMBALAGEM,
        SERVICO
    }

    private TipoItem tipoItem;

    public TipoItem getTipoItem() {
        return tipoItem;
    }

    public void setTipoItem(TipoItem tipoItem) {
        this.tipoItem = tipoItem;
    }

    // ...existing code...

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private BigDecimal saldo;

    @ManyToOne
    @JoinColumn(name = "embalagem_padrao_id")
    private Embalagem embalagemPadrao;

    @ManyToMany
    @JoinTable(name = "item_embalagem",
               joinColumns = @JoinColumn(name = "item_id"),
               inverseJoinColumns = @JoinColumn(name = "embalagem_id"))
    private Set<Embalagem> embalagens = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "entrada_padrao_id")
    private Local entradaPadrao;

    @ManyToOne
    @JoinColumn(name = "saida_padrao_id")
    private Local saidaPadrao;

    // Constructors
    public Item() {}

    public Item(String nome, BigDecimal saldo, Embalagem embalagemPadrao) {
        this.nome = nome;
        this.saldo = saldo;
        this.embalagemPadrao = embalagemPadrao;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public Embalagem getEmbalagemPadrao() {
        return embalagemPadrao;
    }

    public void setEmbalagemPadrao(Embalagem embalagemPadrao) {
        this.embalagemPadrao = embalagemPadrao;
    }

    public Set<Embalagem> getEmbalagens() {
        return embalagens;
    }

    public void setEmbalagens(Set<Embalagem> embalagens) {
        this.embalagens = embalagens;
    }

    public Local getEntradaPadrao() {
        return entradaPadrao;
    }

    public void setEntradaPadrao(Local entradaPadrao) {
        this.entradaPadrao = entradaPadrao;
    }

    public Local getSaidaPadrao() {
        return saidaPadrao;
    }

    public void setSaidaPadrao(Local saidaPadrao) {
        this.saidaPadrao = saidaPadrao;
    }

    // toString, equals, hashCode can be added if needed
}