package com.estoque.repository;

import com.estoque.model.Unidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UnidadeRepository extends JpaRepository<Unidade, Long> {

    /**
     * Busca uma unidade pela sigla
     */
    Optional<Unidade> findBySigla(String sigla);

    /**
     * Busca todas as unidades ativas
     */
    List<Unidade> findByAtivoTrue();

    /**
     * Busca unidades por descrição (contém o texto)
     */
    List<Unidade> findByDescricaoContainingIgnoreCase(String descricao);

    /**
     * Verifica se existe uma unidade com a sigla informada
     */
    boolean existsBySigla(String sigla);

    /**
     * Busca todas as unidades base (sem unidade pai)
     */
    @Query("SELECT u FROM Unidade u WHERE u.unidadePai IS NULL AND u.ativo = true ORDER BY u.sigla")
    List<Unidade> findUnidadesBase();

    /**
     * Busca unidades derivadas de uma unidade pai específica
     */
    @Query("SELECT u FROM Unidade u WHERE u.unidadePai.id = :unidadePaiId AND u.ativo = true ORDER BY u.fator, u.sigla")
    List<Unidade> findUnidadesDerivadas(@Param("unidadePaiId") Long unidadePaiId);

    /**
     * Busca unidades com fator definido
     */
    @Query("SELECT u FROM Unidade u WHERE u.fator IS NOT NULL AND u.ativo = true ORDER BY u.fator")
    List<Unidade> findUnidadesComFator();
}