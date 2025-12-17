package com.estoque.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "lote")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Item de estoque é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estoque_item_id", nullable = false)
    private EstoqueItem estoqueItem;

    @NotBlank(message = "Número do lote é obrigatório")
    @Size(max = 50, message = "Número do lote deve ter no máximo 50 caracteres")
    @Column(nullable = false, unique = true, length = 50)
    private String numeroLote;

    @NotNull(message = "Data de entrada é obrigatória")
    @Column(name = "data_entrada", nullable = false)
    private LocalDate dataEntrada;

    @Column(name = "data_validade")
    private LocalDate dataValidade;

    @NotNull(message = "Quantidade é obrigatória")
    @DecimalMin(value = "0.01", message = "Quantidade deve ser maior que zero")
    @Column(precision = 15, scale = 4, nullable = false)
    private BigDecimal quantidadeTotal;

    @NotNull(message = "Quantidade disponível é obrigatória")
    @DecimalMin(value = "0.00", message = "Quantidade disponível não pode ser negativa")
    @Column(precision = 15, scale = 4, nullable = false)
    private BigDecimal quantidadeDisponivel;

    @Size(max = 100, message = "Fornecedor deve ter no máximo 100 caracteres")
    @Column(length = 100)
    private String fornecedor;

    @Size(max = 255, message = "Observações devem ter no máximo 255 caracteres")
    @Column(length = 255)
    private String observacoes;

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
        if (quantidadeDisponivel == null) {
            quantidadeDisponivel = quantidadeTotal;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        dataAtualizacao = LocalDateTime.now();
    }

    /**
     * Verifica se o lote está vencido
     */
    public boolean estaVencido() {
        if (dataValidade == null) {
            return false;
        }
        return LocalDate.now().isAfter(dataValidade);
    }
}