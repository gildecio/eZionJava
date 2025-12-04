package com.estoque.repository;

import com.estoque.model.NotaFiscalEntrada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotaFiscalEntradaRepository extends JpaRepository<NotaFiscalEntrada, Long> {
    List<NotaFiscalEntrada> findByNumero(String numero);
    List<NotaFiscalEntrada> findByFornecedor(String fornecedor);
    List<NotaFiscalEntrada> findByStatus(NotaFiscalEntrada.Status status);
}
