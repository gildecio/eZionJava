package com.estoque.model;

import com.seguranca.model.Usuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Entity
@Table(name = "estoque_custo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstoqueCusto {

    /**
     * Enumeração dos tipos de custo
     */
    public enum TipoCusto {
        COMPRA("Compra"),
        TRANSPORTE("Transporte"),
        ARMAZENAGEM("Armazenagem"),
        MANUTENCAO("Manutenção"),
        DEPRECIACAO("Depreciação"),
        ICMS("ICMS"),
        IPI("IPI"),
        PIS("PIS"),
        COFINS("COFINS"),
        ICMS_ST("ICMS-ST"),
        ISS("ISS"),
        IRPJ("IRPJ"),
        CSLL("CSLL"),
        OUTROS("Outros");

        private final String descricao;

        TipoCusto(String descricao) {
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

    @NotNull(message = "Tipo de custo é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_custo", nullable = false, length = 20)
    private TipoCusto tipoCusto;

    @NotNull(message = "Valor é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
    @Column(precision = 15, scale = 4, nullable = false)
    private BigDecimal valor;

    @DecimalMin(value = "0.00", message = "Custo unitário não pode ser negativo")
    @Column(name = "custo_unitario", precision = 15, scale = 4)
    private BigDecimal custoUnitario;

    @DecimalMin(value = "0.00", message = "Custo médio com frete não pode ser negativo")
    @Column(name = "custo_medio_com_frete", precision = 15, scale = 4)
    private BigDecimal custoMedioComFrete;

    @DecimalMin(value = "0.00", message = "Frete rateado não pode ser negativo")
    @Column(name = "frete_rateado", precision = 15, scale = 4)
    private BigDecimal freteRateado;

    @DecimalMin(value = "0.00", message = "Custo médio não pode ser negativo")
    @Column(name = "custo_medio", precision = 15, scale = 4)
    private BigDecimal custoMedio;

    @DecimalMin(value = "0.00", message = "Quantidade não pode ser negativa")
    @Column(precision = 15, scale = 4)
    private BigDecimal quantidade;

    @DecimalMin(value = "0.00", message = "Valor base não pode ser negativo")
    @Column(name = "valor_base", precision = 15, scale = 4)
    private BigDecimal valorBase;

    // Impostos
    @DecimalMin(value = "0.00", message = "ICMS não pode ser negativo")
    @Column(precision = 15, scale = 4)
    private BigDecimal icms;

    @DecimalMin(value = "0.00", message = "IPI não pode ser negativo")
    @Column(precision = 15, scale = 4)
    private BigDecimal ipi;

    @DecimalMin(value = "0.00", message = "PIS não pode ser negativo")
    @Column(precision = 15, scale = 4)
    private BigDecimal pis;

    @DecimalMin(value = "0.00", message = "COFINS não pode ser negativo")
    @Column(precision = 15, scale = 4)
    private BigDecimal cofins;

    @DecimalMin(value = "0.00", message = "ICMS-ST não pode ser negativo")
    @Column(name = "icms_st", precision = 15, scale = 4)
    private BigDecimal icmsSt;

    @DecimalMin(value = "0.00", message = "ISS não pode ser negativo")
    @Column(precision = 15, scale = 4)
    private BigDecimal iss;

    @DecimalMin(value = "0.00", message = "IRPJ não pode ser negativo")
    @Column(precision = 15, scale = 4)
    private BigDecimal irpj;

    @DecimalMin(value = "0.00", message = "CSLL não pode ser negativo")
    @Column(precision = 15, scale = 4)
    private BigDecimal csll;

    @NotNull(message = "Data do custo é obrigatória")
    @Column(name = "data_custo", nullable = false)
    private LocalDateTime dataCusto;

    @NotNull(message = "Usuário responsável é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Size(max = 255, message = "Descrição deve ter no máximo 255 caracteres")
    @Column(length = 255)
    private String descricao;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @Column(nullable = false)
    private LocalDateTime dataAtualizacao;

    /**
     * Calcula o custo unitário baseado na quantidade
     */
    public BigDecimal calcularCustoUnitario() {
        if (quantidade != null && quantidade.compareTo(BigDecimal.ZERO) > 0 && valor != null) {
            return valor.divide(quantidade, 4, RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO;
    }

    /**
     * Calcula o total de impostos
     */
    public BigDecimal calcularTotalImpostos() {
        BigDecimal total = BigDecimal.ZERO;
        if (icms != null) total = total.add(icms);
        if (ipi != null) total = total.add(ipi);
        if (pis != null) total = total.add(pis);
        if (cofins != null) total = total.add(cofins);
        if (icmsSt != null) total = total.add(icmsSt);
        if (iss != null) total = total.add(iss);
        if (irpj != null) total = total.add(irpj);
        if (csll != null) total = total.add(csll);
        return total;
    }

    /**
     * Calcula o custo total incluindo impostos
     */
    public BigDecimal calcularCustoTotalComImpostos() {
        return valor.add(calcularTotalImpostos());
    }

    /**
     * Calcula o percentual de impostos sobre o valor base
     */
    public BigDecimal calcularPercentualImpostos() {
        if (valorBase != null && valorBase.compareTo(BigDecimal.ZERO) > 0) {
            return calcularTotalImpostos().divide(valorBase, 4, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"));
        }
        return BigDecimal.ZERO;
    }

    /**
     * Calcula o frete rateado baseado na quantidade
     * Este método assume que o valor do frete total está armazenado em um custo do tipo TRANSPORTE
     */
    public BigDecimal calcularFreteRateado(BigDecimal freteTotal, BigDecimal quantidadeTotal) {
        if (freteTotal != null && freteTotal.compareTo(BigDecimal.ZERO) > 0 &&
            quantidadeTotal != null && quantidadeTotal.compareTo(BigDecimal.ZERO) > 0 &&
            quantidade != null && quantidade.compareTo(BigDecimal.ZERO) > 0) {

            // Frete rateado = (frete total / quantidade total) * quantidade do item
            BigDecimal freteUnitario = freteTotal.divide(quantidadeTotal, 6, RoundingMode.HALF_UP);
            return freteUnitario.multiply(quantidade).setScale(4, RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO;
    }

    /**
     * Calcula o custo total incluindo frete rateado
     */
    public BigDecimal calcularCustoTotalComFrete() {
        BigDecimal custoTotal = calcularCustoTotalComImpostos();
        if (freteRateado != null) {
            custoTotal = custoTotal.add(freteRateado);
        }
        return custoTotal;
    }

    @PrePersist
    protected void onCreate() {
        dataCriacao = LocalDateTime.now();
        dataAtualizacao = LocalDateTime.now();
        if (dataCusto == null) {
            dataCusto = LocalDateTime.now();
        }
        // Calcula custo unitário automaticamente se não foi informado
        if (custoUnitario == null && quantidade != null && quantidade.compareTo(BigDecimal.ZERO) > 0) {
            custoUnitario = calcularCustoUnitario();
        }
        // Para custos de transporte, o frete rateado pode ser igual ao valor se for por item
        if (freteRateado == null && tipoCusto == TipoCusto.TRANSPORTE && valor != null) {
            freteRateado = valor;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        dataAtualizacao = LocalDateTime.now();
    }
}