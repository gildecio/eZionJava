package com.estoque.repository;

import com.estoque.model.Item;
import com.estoque.model.MovimentacaoEstoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MovimentacaoEstoqueRepository extends JpaRepository<MovimentacaoEstoque, Long> {
    List<MovimentacaoEstoque> findByItem(Item item);
    List<MovimentacaoEstoque> findByTipo(MovimentacaoEstoque.TipoMovimentacao tipo);
    List<MovimentacaoEstoque> findByItemId(Long itemId);
}
