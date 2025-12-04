package com.fiscal.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "natureza_operacao")
public class NaturezaOperacao {

    public static enum Tipo {
        ENTRADA,
        SAIDA
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codigo;

    private String descricao;

    private Tipo tipo;

    // Constructors
    public NaturezaOperacao() {}

    public NaturezaOperacao(String codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public NaturezaOperacao(String codigo, String descricao, Tipo tipo) {
        this.codigo = codigo;
        this.descricao = descricao;
        this.tipo = tipo;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    // toString, equals, hashCode can be added if needed
}
