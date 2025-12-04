package com.estoque.repository;

import com.estoque.model.PedidoVenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PedidoVendaRepository extends JpaRepository<PedidoVenda, Long> {
    List<PedidoVenda> findByNumero(String numero);
    List<PedidoVenda> findByCliente(String cliente);
    List<PedidoVenda> findByStatus(PedidoVenda.Status status);
}
