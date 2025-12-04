package com.estoque.repository;

import com.estoque.model.Devolucao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DevolucaoRepository extends JpaRepository<Devolucao, Long> {
    List<Devolucao> findByNumero(String numero);
    List<Devolucao> findByTipo(Devolucao.Tipo tipo);
    List<Devolucao> findByStatus(Devolucao.Status status);
}
