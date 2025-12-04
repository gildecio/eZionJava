package com.estoque.model;

import com.contabil.model.Empresa;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "embalagem")
public class Embalagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    private String nome;

    @ManyToOne
    @JoinColumn(name = "unidade_id")
    private Unidade unidade;

    private BigDecimal fator;

    @ManyToMany(mappedBy = "embalagens")
    private Set<Item> itens = new HashSet<>();

    // Constructors
    public Embalagem() {}

    public Embalagem(String nome, Unidade unidade, BigDecimal fator, Empresa empresa) {
        this.nome = nome;
        this.unidade = unidade;
        this.fator = fator;
        this.empresa = empresa;
    }

    public Embalagem(String nome, Unidade unidade, BigDecimal fator) {
        this.nome = nome;
        this.unidade = unidade;
        this.fator = fator;
    }

    // Getters and Setters
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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Unidade getUnidade() {
        return unidade;
    }

    public void setUnidade(Unidade unidade) {
        this.unidade = unidade;
    }

    public BigDecimal getFator() {
        return fator;
    }

    public void setFator(BigDecimal fator) {
        this.fator = fator;
    }

    public Set<Item> getItens() {
        return itens;
    }

    public void setItens(Set<Item> itens) {
        this.itens = itens;
    }

    // toString, equals, hashCode can be added if needed
}