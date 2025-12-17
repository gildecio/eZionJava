package com.estoque.repository;

import com.estoque.model.Lote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LoteRepository extends JpaRepository<Lote, Long> {

    Optional<Lote> findByNumeroLote(String numeroLote);

    boolean existsByNumeroLote(String numeroLote);

    @Query("SELECT l FROM Lote l WHERE l.estoqueItem.id = :itemId ORDER BY l.dataEntrada DESC")
    List<Lote> findByEstoqueItemIdOrderByDataEntradaDesc(@Param("itemId") Long itemId);

    @Query("SELECT l FROM Lote l WHERE l.estoqueItem.id = :itemId AND l.ativo = true ORDER BY l.dataEntrada ASC")
    List<Lote> findAtivosByEstoqueItemIdOrderByDataEntradaAsc(@Param("itemId") Long itemId);

    @Query("SELECT l FROM Lote l WHERE l.ativo = true AND l.dataValidade IS NOT NULL AND l.dataValidade < :data ORDER BY l.dataValidade ASC")
    List<Lote> findLotesVencidosAnte(@Param("data") LocalDate data);

    @Query("SELECT l FROM Lote l WHERE l.ativo = true AND l.dataValidade IS NOT NULL AND l.dataValidade BETWEEN :inicio AND :fim ORDER BY l.dataValidade ASC")
    List<Lote> findLotesParaVencerEntre(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);

    @Query("SELECT l FROM Lote l WHERE l.ativo = true AND l.quantidadeDisponivel > 0 ORDER BY l.dataEntrada ASC")
    List<Lote> findLotesComEstoqueDisponivel();

    @Query("SELECT l FROM Lote l WHERE l.ativo = true AND l.estoqueItem.id = :itemId AND l.quantidadeDisponivel > 0 ORDER BY l.dataEntrada ASC")
    List<Lote> findLotesDisponiveisByItemId(@Param("itemId") Long itemId);

    @Query("SELECT SUM(l.quantidadeDisponivel) FROM Lote l WHERE l.estoqueItem.id = :itemId AND l.ativo = true")
    BigDecimal calcularEstoqueDisponivel(@Param("itemId") Long itemId);

    @Query("SELECT SUM(l.quantidadeTotal) FROM Lote l WHERE l.estoqueItem.id = :itemId AND l.ativo = true")
    BigDecimal calcularEstoqueTotal(@Param("itemId") Long itemId);

    @Query("SELECT COUNT(l) FROM Lote l WHERE l.ativo = true AND l.estoqueItem.id = :itemId AND l.dataValidade IS NOT NULL AND l.dataValidade < CURRENT_DATE")
    long countLotesVencidos(@Param("itemId") Long itemId);
}