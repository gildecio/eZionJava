package com.estoque.repository;

import com.estoque.model.EstoqueItem;
import com.estoque.model.EstoqueItem.TipoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstoqueItemRepository extends JpaRepository<EstoqueItem, Long> {

    /**
     * Busca um item pelo código
     */
    Optional<EstoqueItem> findByCodigo(String codigo);

    /**
     * Busca todos os itens ativos
     */
    List<EstoqueItem> findByAtivoTrue();

    /**
     * Busca itens por tipo
     */
    List<EstoqueItem> findByTipoItem(TipoItem tipoItem);

    /**
     * Busca itens ativos por tipo
     */
    List<EstoqueItem> findByTipoItemAndAtivoTrue(TipoItem tipoItem);

    /**
     * Busca itens por descrição (contém o texto)
     */
    List<EstoqueItem> findByDescricaoContainingIgnoreCase(String descricao);

    /**
     * Verifica se existe um item com o código informado
     */
    boolean existsByCodigo(String codigo);
}
