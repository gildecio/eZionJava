package com.estoque.model;

import com.seguranca.model.Usuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimentacao")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movimentacao {

    /**
     * Enumeração dos tipos de movimentação
     */
    public enum TipoMovimentacao {
        ENTRADA("Entrada"),
        SAIDA("Saída");

        private final String descricao;

        TipoMovimentacao(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Item de estoque é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estoque_item_id", nullable = false)
    private EstoqueItem estoqueItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lote_id")
    private Lote lote;

    @NotNull(message = "Tipo de movimentação é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private TipoMovimentacao tipoMovimentacao;

    @NotNull(message = "Quantidade é obrigatória")
    @DecimalMin(value = "0.01", message = "Quantidade deve ser maior que zero")
    @Column(precision = 15, scale = 4, nullable = false)
    private BigDecimal quantidade;

    @DecimalMin(value = "0.00", message = "Custo não pode ser negativo")
    @Column(precision = 15, scale = 4)
    private BigDecimal custo;

    @NotNull(message = "Local é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "local_id", nullable = false)
    private Local local;

    @NotBlank(message = "Observações são obrigatórias")
    @Size(max = 255, message = "Observações devem ter no máximo 255 caracteres")
    @Column(nullable = false, length = 255)
    private String observacoes;

    @NotNull(message = "Data da movimentação é obrigatória")
    @Column(name = "data_movimentacao", nullable = false)
    private LocalDateTime dataMovimentacao;

    @NotNull(message = "Usuário responsável é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @Column(nullable = false)
    private LocalDateTime dataAtualizacao;

    /**
     * Calcula o custo da movimentação baseado no custo médio do item
     * custo = quantidade × custo_médio_do_item
     */
    public BigDecimal calcularCustoMovimentacao(BigDecimal custoMedioItem) {
        if (quantidade != null && custoMedioItem != null && custoMedioItem.compareTo(BigDecimal.ZERO) > 0) {
            return quantidade.multiply(custoMedioItem);
        }
        return BigDecimal.ZERO;
    }

    @PrePersist
    protected void onCreate() {
        dataCriacao = LocalDateTime.now();
        dataAtualizacao = LocalDateTime.now();
        if (dataMovimentacao == null) {
            dataMovimentacao = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        dataAtualizacao = LocalDateTime.now();
    }
}