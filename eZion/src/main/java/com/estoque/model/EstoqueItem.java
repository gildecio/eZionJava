package com.estoque.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "estoque_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstoqueItem {

    /**
     * Enumeração dos tipos de item de estoque
     */
    public enum TipoItem {
        PRODUTO("Produto"),
        PRODUTO_EM_ELABORACAO("Produto em Elaboração"),
        INSUMO("Insumo"),
        CONSUMO("Consumo"),
        IMOBILIZADO("Imobilizado"),
        SERVICO("Serviço");

        private final String descricao;

        TipoItem(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Código é obrigatório")
    @Size(max = 50, message = "Código deve ter no máximo 50 caracteres")
    @Column(nullable = false, unique = true, length = 50)
    private String codigo;

    @NotBlank(message = "Descrição é obrigatória")
    @Size(max = 200, message = "Descrição deve ter no máximo 200 caracteres")
    @Column(nullable = false, length = 200)
    private String descricao;

    @Column(columnDefinition = "TEXT")
    private String descricaoDetalhada;

    @NotNull(message = "Tipo de item é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoItem tipoItem;

    @NotNull(message = "Grupo é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grupo_id", nullable = false)
    private Grupo grupo;

    @NotNull(message = "Unidade é obrigatória")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unidade_id", nullable = false)
    private Unidade unidade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "local_entrada_padrao_id")
    private Local localEntradaPadrao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "local_saida_padrao_id")
    private Local localSaidaPadrao;

    @DecimalMin(value = "0.00", message = "Quantidade mínima não pode ser negativa")
    @Column(precision = 15, scale = 4)
    private BigDecimal quantidadeMinima = BigDecimal.ZERO;

    @DecimalMin(value = "0.00", message = "Estoque de segurança não pode ser negativo")
    @Column(precision = 15, scale = 4)
    private BigDecimal estoqueSeguranca = BigDecimal.ZERO;

    @DecimalMin(value = "0.00", message = "Quantidade máxima não pode ser negativa")
    @Column(precision = 15, scale = 4)
    private BigDecimal quantidadeMaxima = BigDecimal.ZERO;

    @Column(nullable = false)
    private Boolean ativo = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @Column(nullable = false)
    private LocalDateTime dataAtualizacao;

    @PrePersist
    protected void onCreate() {
        dataCriacao = LocalDateTime.now();
        dataAtualizacao = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        dataAtualizacao = LocalDateTime.now();
    }
}
