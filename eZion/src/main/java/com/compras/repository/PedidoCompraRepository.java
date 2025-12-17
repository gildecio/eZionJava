package com.compras.repository;

import com.compras.model.PedidoCompra;
import com.compras.model.StatusPedidoCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PedidoCompraRepository extends JpaRepository<PedidoCompra, Long> {

    List<PedidoCompra> findByStatus(StatusPedidoCompra status);

    List<PedidoCompra> findByFornecedorId(Long fornecedorId);

    List<PedidoCompra> findByAtivoTrue();

    @Query("SELECT p FROM PedidoCompra p WHERE p.dataPrevistaEntrega < :data AND p.status IN ('PENDENTE', 'APROVADO', 'ENVIADO')")
    List<PedidoCompra> findPedidosComEntregaAtrasada(@Param("data") LocalDate data);

    @Query("SELECT p FROM PedidoCompra p WHERE p.dataPrevistaEntrega BETWEEN :inicio AND :fim")
    List<PedidoCompra> findPedidosComEntregaPrevistaEntre(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);

    boolean existsByNumeroPedido(String numeroPedido);
}