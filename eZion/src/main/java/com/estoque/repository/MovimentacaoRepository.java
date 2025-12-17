package com.estoque.repository;

import com.estoque.model.Movimentacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Long> {

    @Query("SELECT m FROM Movimentacao m WHERE m.estoqueItem.id = :itemId ORDER BY m.dataMovimentacao DESC")
    List<Movimentacao> findByEstoqueItemIdOrderByDataMovimentacaoDesc(@Param("itemId") Long itemId);

    @Query("SELECT m FROM Movimentacao m WHERE m.estoqueItem.id = :itemId AND m.dataMovimentacao BETWEEN :inicio AND :fim ORDER BY m.dataMovimentacao DESC")
    List<Movimentacao> findByEstoqueItemIdAndPeriodo(@Param("itemId") Long itemId,
                                                    @Param("inicio") LocalDateTime inicio,
                                                    @Param("fim") LocalDateTime fim);

    @Query("SELECT m FROM Movimentacao m WHERE m.local.id = :localId ORDER BY m.dataMovimentacao DESC")
    List<Movimentacao> findByLocalIdOrderByDataMovimentacaoDesc(@Param("localId") Long localId);

    @Query("SELECT m FROM Movimentacao m WHERE m.tipoMovimentacao = :tipo ORDER BY m.dataMovimentacao DESC")
    List<Movimentacao> findByTipoMovimentacaoOrderByDataMovimentacaoDesc(@Param("tipo") Movimentacao.TipoMovimentacao tipo);

    @Query("SELECT m FROM Movimentacao m WHERE m.dataMovimentacao BETWEEN :inicio AND :fim ORDER BY m.dataMovimentacao DESC")
    List<Movimentacao> findByPeriodo(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

    @Query("SELECT SUM(CASE WHEN m.tipoMovimentacao = 'ENTRADA' THEN m.quantidade ELSE -m.quantidade END) " +
           "FROM Movimentacao m WHERE m.estoqueItem.id = :itemId")
    BigDecimal calcularSaldoByEstoqueItemId(@Param("itemId") Long itemId);
}