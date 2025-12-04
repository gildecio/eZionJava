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
@Table(name = "pedido_venda")
public class PedidoVenda {

    public static enum Status {
        RASCUNHO,
        CONFIRMADO,
        SEPARADO,
        EXPEDIDO,
        ENTREGUE,
        CANCELADO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    private String numero;

    private String cliente;

    private Status status;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataEmissao;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataPrevista;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataExpedicao;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataEntrega;

    @OneToMany(mappedBy = "pedidoVenda")
    private Set<PedidoVendaItem> itens = new HashSet<>();

    private String observacao;

    public PedidoVenda() {
        this.status = Status.RASCUNHO;
        this.dataEmissao = new Date();
    }

    public PedidoVenda(String numero, String cliente) {
        this();
        this.numero = numero;
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

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
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

    public Date getDataPrevista() {
        return dataPrevista;
    }

    public void setDataPrevista(Date dataPrevista) {
        this.dataPrevista = dataPrevista;
    }

    public Date getDataExpedicao() {
        return dataExpedicao;
    }

    public void setDataExpedicao(Date dataExpedicao) {
        this.dataExpedicao = dataExpedicao;
    }

    public Date getDataEntrega() {
        return dataEntrega;
    }

    public void setDataEntrega(Date dataEntrega) {
        this.dataEntrega = dataEntrega;
    }

    public Set<PedidoVendaItem> getItens() {
        return itens;
    }

    public void setItens(Set<PedidoVendaItem> itens) {
        this.itens = itens;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}
