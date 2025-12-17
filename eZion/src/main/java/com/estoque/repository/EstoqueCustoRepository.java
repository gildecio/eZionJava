package com.estoque.repository;

import com.estoque.model.EstoqueCusto;
import com.estoque.model.EstoqueCusto.TipoCusto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EstoqueCustoRepository extends JpaRepository<EstoqueCusto, Long> {

    /**
     * Busca custos por item de estoque (ordem ascendente por data)
     */
    List<EstoqueCusto> findByEstoqueItemIdOrderByDataCustoAsc(@Param("estoqueItemId") Long estoqueItemId);

    /**
     * Busca custos por item de estoque (ordem descendente por data)
     */
    List<EstoqueCusto> findByEstoqueItemIdOrderByDataCustoDesc(@Param("estoqueItemId") Long estoqueItemId);

    /**
     * Busca custos por lote
     */
    List<EstoqueCusto> findByLoteIdOrderByDataCustoDesc(@Param("loteId") Long loteId);

    /**
     * Busca custos por tipo
     */
    List<EstoqueCusto> findByTipoCustoOrderByDataCustoDesc(@Param("tipoCusto") TipoCusto tipoCusto);

    /**
     * Busca custos por período
     */
    List<EstoqueCusto> findByDataCustoBetweenOrderByDataCustoDesc(@Param("inicio") LocalDateTime inicio,
                                                                 @Param("fim") LocalDateTime fim);

    /**
     * Busca custos por item e período
     */
    List<EstoqueCusto> findByEstoqueItemIdAndDataCustoBetweenOrderByDataCustoDesc(
            @Param("estoqueItemId") Long estoqueItemId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim);

    /**
     * Calcula o custo total de um item
     */
    @Query("SELECT SUM(ec.valor) FROM EstoqueCusto ec WHERE ec.estoqueItem.id = :estoqueItemId")
    BigDecimal calcularCustoTotalByEstoqueItemId(@Param("estoqueItemId") Long estoqueItemId);

    /**
     * Calcula o custo total de um lote
     */
    @Query("SELECT SUM(ec.valor) FROM EstoqueCusto ec WHERE ec.lote.id = :loteId")
    BigDecimal calcularCustoTotalByLoteId(@Param("loteId") Long loteId);

    /**
     * Calcula o custo médio de um item
     */
    @Query("SELECT AVG(ec.valor) FROM EstoqueCusto ec WHERE ec.estoqueItem.id = :estoqueItemId")
    BigDecimal calcularCustoMedioByEstoqueItemId(@Param("estoqueItemId") Long estoqueItemId);
}