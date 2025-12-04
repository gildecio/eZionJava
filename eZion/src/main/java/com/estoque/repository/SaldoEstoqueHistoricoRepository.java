package com.estoque.repository;

import com.estoque.model.MovimentacaoEstoque;
import com.estoque.model.SaldoEstoqueHistorico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SaldoEstoqueHistoricoRepository extends JpaRepository<SaldoEstoqueHistorico, Long> {
    List<SaldoEstoqueHistorico> findByMovimentacao(MovimentacaoEstoque movimentacao);
    List<SaldoEstoqueHistorico> findByItemId(Long itemId);
    List<SaldoEstoqueHistorico> findByLocalId(Long localId);
    List<SaldoEstoqueHistorico> findByItemIdAndLocalId(Long itemId, Long localId);
}
