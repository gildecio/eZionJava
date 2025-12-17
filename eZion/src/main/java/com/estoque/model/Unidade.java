package com.estoque.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "unidade", uniqueConstraints = {
    @UniqueConstraint(columnNames = "sigla")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Unidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Sigla é obrigatória")
    @Size(max = 10, message = "Sigla deve ter no máximo 10 caracteres")
    @Column(nullable = false, unique = true, length = 10)
    private String sigla;

    @NotBlank(message = "Descrição é obrigatória")
    @Size(max = 100, message = "Descrição deve ter no máximo 100 caracteres")
    @Column(nullable = false, length = 100)
    private String descricao;

    @DecimalMin(value = "0.0001", message = "Fator deve ser maior que zero")
    @Digits(integer = 10, fraction = 4, message = "Fator deve ter no máximo 10 dígitos inteiros e 4 decimais")
    @Column(precision = 14, scale = 4)
    private BigDecimal fator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unidade_pai_id")
    private Unidade unidadePai;

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

    /**
     * Verifica se esta unidade é uma unidade base (não tem unidade pai)
     */
    public boolean isUnidadeBase() {
        return unidadePai == null;
    }

    /**
     * Calcula o fator total em relação à unidade base
     */
    public BigDecimal getFatorTotal() {
        if (isUnidadeBase()) {
            return BigDecimal.ONE;
        }

        BigDecimal fatorTotal = this.fator != null ? this.fator : BigDecimal.ONE;
        Unidade unidadeAtual = this.unidadePai;

        while (unidadeAtual != null) {
            if (unidadeAtual.fator != null) {
                fatorTotal = fatorTotal.multiply(unidadeAtual.fator);
            }
            unidadeAtual = unidadeAtual.unidadePai;
        }

        return fatorTotal;
    }
}