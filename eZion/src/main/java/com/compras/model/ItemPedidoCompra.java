package com.compras.model;

import com.estoque.model.EstoqueItem;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "item_pedido_compra")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemPedidoCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Pedido de compra é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_compra_id", nullable = false)
    private PedidoCompra pedidoCompra;

    @NotNull(message = "Item de estoque é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estoque_item_id", nullable = false)
    private EstoqueItem estoqueItem;

    @NotNull(message = "Quantidade é obrigatória")
    @DecimalMin(value = "0.01", message = "Quantidade deve ser maior que zero")
    @Column(precision = 15, scale = 4, nullable = false)
    private BigDecimal quantidade;

    @DecimalMin(value = "0.00", message = "Valor unitário não pode ser negativo")
    @Column(name = "valor_unitario", precision = 12, scale = 4, nullable = false)
    private BigDecimal valorUnitario = BigDecimal.ZERO;

    @DecimalMin(value = "0.00", message = "Valor total não pode ser negativo")
    @Column(name = "valor_total", precision = 15, scale = 2, nullable = false)
    private BigDecimal valorTotal = BigDecimal.ZERO;

    @DecimalMin(value = "0.00", message = "Quantidade recebida não pode ser negativa")
    @Column(name = "quantidade_recebida", precision = 15, scale = 4, nullable = false)
    private BigDecimal quantidadeRecebida = BigDecimal.ZERO;

    @Size(max = 255, message = "Observações devem ter no máximo 255 caracteres")
    @Column(length = 255)
    private String observacoes;

    /**
     * Calcula o valor total do item (quantidade × valor unitário)
     */
    public BigDecimal calcularValorTotal() {
        return quantidade.multiply(valorUnitario);
    }

    /**
     * Verifica se o item foi totalmente recebido
     */
    public boolean isRecebimentoTotal() {
        return quantidadeRecebida.compareTo(quantidade) >= 0;
    }

    /**
     * Calcula a quantidade pendente de recebimento
     */
    public BigDecimal getQuantidadePendente() {
        return quantidade.subtract(quantidadeRecebida);
    }
}