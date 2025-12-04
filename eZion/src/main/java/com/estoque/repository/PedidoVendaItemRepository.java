package com.estoque.repository;

import com.estoque.model.PedidoVendaItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PedidoVendaItemRepository extends JpaRepository<PedidoVendaItem, Long> {
    List<PedidoVendaItem> findByPedidoVendaId(Long pedidoVendaId);
}
