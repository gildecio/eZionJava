package com.estoque.model;

import com.contabil.model.Empresa;
import com.fiscal.model.NCM;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "grupo")
public class Grupo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    private String nome;

    private String descricao;

    @ManyToOne
    @JoinColumn(name = "ncm_id")
    private NCM ncm;

    @ManyToOne
    @JoinColumn(name = "grupo_pai_id")
    private Grupo grupoPai;

    @OneToMany(mappedBy = "grupoPai")
    private Set<Grupo> gruposFilhos = new HashSet<>();

    // Constructors
    public Grupo() {}

    public Grupo(String nome, Empresa empresa) {
        this.nome = nome;
        this.empresa = empresa;
    }

    public Grupo(String nome) {
        this.nome = nome;
    }

    public Grupo(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    public Grupo(String nome, String descricao, Grupo grupoPai) {
        this.nome = nome;
        this.descricao = descricao;
        this.grupoPai = grupoPai;
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public NCM getNcm() {
        return ncm;
    }

    public void setNcm(NCM ncm) {
        this.ncm = ncm;
    }

    public Grupo getGrupoPai() {
        return grupoPai;
    }

    public void setGrupoPai(Grupo grupoPai) {
        this.grupoPai = grupoPai;
    }

    public Set<Grupo> getGruposFilhos() {
        return gruposFilhos;
    }

    public void setGruposFilhos(Set<Grupo> gruposFilhos) {
        this.gruposFilhos = gruposFilhos;
    }

    // toString, equals, hashCode can be added if needed
}
