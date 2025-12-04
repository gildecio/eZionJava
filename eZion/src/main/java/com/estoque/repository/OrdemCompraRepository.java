package com.estoque.repository;

import com.estoque.model.OrdemCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrdemCompraRepository extends JpaRepository<OrdemCompra, Long> {
    List<OrdemCompra> findByNumero(String numero);
    List<OrdemCompra> findByFornecedor(String fornecedor);
    List<OrdemCompra> findByStatus(OrdemCompra.Status status);
}
