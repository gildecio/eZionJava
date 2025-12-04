package com.estoque.model;

import com.contabil.model.Empresa;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;

@Entity
@Table(name = "numeracao_documento")
public class NumeracaoDocumento {

    public static enum TipoDocumento {
        PEDIDO_VENDA("PV", "PV-YYYY-000001"),
        NOTA_FISCAL_ENTRADA("NFE", "NFE-YYYY-000001"),
        NOTA_FISCAL_SAIDA("NFS", "NFS-YYYY-000001"),
        ORDEM_COMPRA("OC", "OC-YYYY-000001"),
        DEVOLUCAO("DEV", "DEV-YYYY-000001"),
        AJUSTE_ESTOQUE("ADJ", "ADJ-YYYY-000001");

        private String prefixo;
        private String formato;

        TipoDocumento(String prefixo, String formato) {
            this.prefixo = prefixo;
            this.formato = formato;
        }

        public String getPrefixo() {
            return prefixo;
        }

        public String getFormato() {
            return formato;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private TipoDocumento tipoDocumento;

    @Column(nullable = false)
    private Long proximoNumero = 1L;

    @Column(nullable = false)
    private Integer anoVigente;

    @Temporal(TemporalType.TIMESTAMP)
    private Date ultimaAtualizacao;

    public NumeracaoDocumento() {
        this.proximoNumero = 1L;
        this.anoVigente = java.time.LocalDate.now().getYear();
        this.ultimaAtualizacao = new Date();
    }

    public NumeracaoDocumento(TipoDocumento tipoDocumento) {
        this();
        this.tipoDocumento = tipoDocumento;
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

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public Long getProximoNumero() {
        return proximoNumero;
    }

    public void setProximoNumero(Long proximoNumero) {
        this.proximoNumero = proximoNumero;
    }

    public Integer getAnoVigente() {
        return anoVigente;
    }

    public void setAnoVigente(Integer anoVigente) {
        this.anoVigente = anoVigente;
    }

    public Date getUltimaAtualizacao() {
        return ultimaAtualizacao;
    }

    public void setUltimaAtualizacao(Date ultimaAtualizacao) {
        this.ultimaAtualizacao = ultimaAtualizacao;
    }
}
