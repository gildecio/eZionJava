package com.compras.repository;

import com.compras.model.ItemPedidoCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemPedidoCompraRepository extends JpaRepository<ItemPedidoCompra, Long> {

    List<ItemPedidoCompra> findByPedidoCompraId(Long pedidoCompraId);

    List<ItemPedidoCompra> findByEstoqueItemId(Long estoqueItemId);

    @Query("SELECT i FROM ItemPedidoCompra i WHERE i.pedidoCompra.id = :pedidoId AND i.quantidadeRecebida < i.quantidade")
    List<ItemPedidoCompra> findItensPendentesRecebimento(@Param("pedidoId") Long pedidoId);

    @Query("SELECT SUM(i.quantidadeRecebida) FROM ItemPedidoCompra i WHERE i.estoqueItem.id = :itemId")
    Double calcularTotalRecebidoPorItem(@Param("itemId") Long itemId);
}