package com.estoque.repository;

import com.estoque.model.NotaFiscalSaida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotaFiscalSaidaRepository extends JpaRepository<NotaFiscalSaida, Long> {
    List<NotaFiscalSaida> findByNumero(String numero);
    List<NotaFiscalSaida> findByCliente(String cliente);
    List<NotaFiscalSaida> findByStatus(NotaFiscalSaida.Status status);
}
