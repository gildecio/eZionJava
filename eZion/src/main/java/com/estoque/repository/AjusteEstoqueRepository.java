package com.estoque.repository;

import com.estoque.model.AjusteEstoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AjusteEstoqueRepository extends JpaRepository<AjusteEstoque, Long> {
    List<AjusteEstoque> findByNumero(String numero);
    List<AjusteEstoque> findByTipo(AjusteEstoque.Tipo tipo);
    List<AjusteEstoque> findByMotivo(AjusteEstoque.Motivo motivo);
}
