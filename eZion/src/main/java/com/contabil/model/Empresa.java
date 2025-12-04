package com.contabil.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "empresa")
public class Empresa {

    public static enum Regime {
        LUCRO_REAL,
        LUCRO_PRESUMIDO,
        SIMPLES_NACIONAL,
        MEI
    }

    public static enum TipoContribuinte {
        CONTRIBUINTE_ICMS,
        CONTRIBUINTE_ISENTO,
        NAO_CONTRIBUINTE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String razaoSocial;

    private String nomeFantasia;

    private String cnpj;

    private String inscricaoEstadual;

    private String inscricaoMunicipal;

    private String email;

    private String telefone;

    private String logradouro;

    private String numero;

    private String complemento;

    private String bairro;

    private String cidade;

    private String estado;

    private String cep;

    private Regime regimeEscal;

    private TipoContribuinte tipoContribuinte;

    private String aliquotaPIS;

    private String aliquotaCOFINS;

    private String aliquotaIRRF;

    private String aliquotaINSS;

    private BigDecimal faturamentoAnual;

    private String responsavelNome;

    private String responsavelCPF;

    private String responsavelEmail;

    private String responsavelTelefone;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCriacao;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataAtualizacao;

    private Boolean ativa;

    public Empresa() {
        this.dataCriacao = new Date();
        this.dataAtualizacao = new Date();
        this.ativa = true;
    }

    public Empresa(String razaoSocial, String nomeFantasia, String cnpj) {
        this();
        this.razaoSocial = razaoSocial;
        this.nomeFantasia = nomeFantasia;
        this.cnpj = cnpj;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getInscricaoEstadual() {
        return inscricaoEstadual;
    }

    public void setInscricaoEstadual(String inscricaoEstadual) {
        this.inscricaoEstadual = inscricaoEstadual;
    }

    public String getInscricaoMunicipal() {
        return inscricaoMunicipal;
    }

    public void setInscricaoMunicipal(String inscricaoMunicipal) {
        this.inscricaoMunicipal = inscricaoMunicipal;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public Regime getRegimeEscal() {
        return regimeEscal;
    }

    public void setRegimeEscal(Regime regimeEscal) {
        this.regimeEscal = regimeEscal;
    }

    public TipoContribuinte getTipoContribuinte() {
        return tipoContribuinte;
    }

    public void setTipoContribuinte(TipoContribuinte tipoContribuinte) {
        this.tipoContribuinte = tipoContribuinte;
    }

    public String getAliquotaPIS() {
        return aliquotaPIS;
    }

    public void setAliquotaPIS(String aliquotaPIS) {
        this.aliquotaPIS = aliquotaPIS;
    }

    public String getAliquotaCOFINS() {
        return aliquotaCOFINS;
    }

    public void setAliquotaCOFINS(String aliquotaCOFINS) {
        this.aliquotaCOFINS = aliquotaCOFINS;
    }

    public String getAliquotaIRRF() {
        return aliquotaIRRF;
    }

    public void setAliquotaIRRF(String aliquotaIRRF) {
        this.aliquotaIRRF = aliquotaIRRF;
    }

    public String getAliquotaINSS() {
        return aliquotaINSS;
    }

    public void setAliquotaINSS(String aliquotaINSS) {
        this.aliquotaINSS = aliquotaINSS;
    }

    public BigDecimal getFaturamentoAnual() {
        return faturamentoAnual;
    }

    public void setFaturamentoAnual(BigDecimal faturamentoAnual) {
        this.faturamentoAnual = faturamentoAnual;
    }

    public String getResponsavelNome() {
        return responsavelNome;
    }

    public void setResponsavelNome(String responsavelNome) {
        this.responsavelNome = responsavelNome;
    }

    public String getResponsavelCPF() {
        return responsavelCPF;
    }

    public void setResponsavelCPF(String responsavelCPF) {
        this.responsavelCPF = responsavelCPF;
    }

    public String getResponsavelEmail() {
        return responsavelEmail;
    }

    public void setResponsavelEmail(String responsavelEmail) {
        this.responsavelEmail = responsavelEmail;
    }

    public String getResponsavelTelefone() {
        return responsavelTelefone;
    }

    public void setResponsavelTelefone(String responsavelTelefone) {
        this.responsavelTelefone = responsavelTelefone;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Date getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(Date dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public Boolean getAtiva() {
        return ativa;
    }

    public void setAtiva(Boolean ativa) {
        this.ativa = ativa;
    }
}
