package com.estoque.repository;

import com.estoque.model.Item;
import com.estoque.model.Local;
import com.estoque.model.SaldoEstoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SaldoEstoqueRepository extends JpaRepository<SaldoEstoque, Long> {
    Optional<SaldoEstoque> findByItemAndLocal(Item item, Local local);
    Optional<SaldoEstoque> findByItemAndLocalAndLote(Item item, Local local, String lote);
    List<SaldoEstoque> findByItem(Item item);
    List<SaldoEstoque> findByLocal(Local local);
    List<SaldoEstoque> findByItemId(Long itemId);
    List<SaldoEstoque> findByLocalId(Long localId);
    List<SaldoEstoque> findByLote(String lote);
    List<SaldoEstoque> findByItemIdAndLote(Long itemId, String lote);
}
