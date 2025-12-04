package com.estoque.repository;

import com.estoque.model.OrdemCompraItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrdemCompraItemRepository extends JpaRepository<OrdemCompraItem, Long> {
    List<OrdemCompraItem> findByOrdemCompraId(Long ordemCompraId);
}
