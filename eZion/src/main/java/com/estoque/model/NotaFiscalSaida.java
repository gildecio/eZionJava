package com.estoque.model;

import com.contabil.model.Empresa;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "nota_fiscal_saida")
public class NotaFiscalSaida {

    public static enum Status {
        RASCUNHO,
        EMITIDA,
        PROCESSADA,
        CANCELADA
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    private String numero;

    private String serie;

    private String cliente;

    private String chaveNFe;

    private Status status;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataEmissao;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataProcessamento;

    @OneToMany(mappedBy = "notaFiscalSaida")
    private Set<NotaFiscalSaidaItem> itens = new HashSet<>();

    private String observacao;

    public NotaFiscalSaida() {
        this.status = Status.RASCUNHO;
        this.dataEmissao = new Date();
    }

    public NotaFiscalSaida(String numero, String serie, String cliente) {
        this();
        this.numero = numero;
        this.serie = serie;
        this.cliente = cliente;
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

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getChaveNFe() {
        return chaveNFe;
    }

    public void setChaveNFe(String chaveNFe) {
        this.chaveNFe = chaveNFe;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(Date dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public Date getDataProcessamento() {
        return dataProcessamento;
    }

    public void setDataProcessamento(Date dataProcessamento) {
        this.dataProcessamento = dataProcessamento;
    }

    public Set<NotaFiscalSaidaItem> getItens() {
        return itens;
    }

    public void setItens(Set<NotaFiscalSaidaItem> itens) {
        this.itens = itens;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}
