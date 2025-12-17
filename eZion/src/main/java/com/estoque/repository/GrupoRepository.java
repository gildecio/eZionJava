package com.estoque.repository;

import com.estoque.model.Grupo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GrupoRepository extends JpaRepository<Grupo, Long> {

    Optional<Grupo> findByNome(String nome);

    boolean existsByNome(String nome);

    boolean existsByCodigo(String codigo);

    @Query("SELECT g FROM Grupo g WHERE g.ativo = true ORDER BY g.nome")
    List<Grupo> findAllAtivosOrderByNome();

    @Query("SELECT g FROM Grupo g WHERE g.ativo = true AND LOWER(g.nome) LIKE LOWER(CONCAT('%', :nome, '%')) ORDER BY g.nome")
    List<Grupo> findByNomeContainingIgnoreCaseAndAtivo(@Param("nome") String nome);

    // Métodos para hierarquia
    @Query("SELECT g FROM Grupo g WHERE g.grupoPai IS NULL AND g.ativo = true ORDER BY g.nome")
    List<Grupo> findGruposRaizAtivos();

    @Query("SELECT g FROM Grupo g WHERE g.grupoPai.id = :paiId AND g.ativo = true ORDER BY g.nome")
    List<Grupo> findFilhosByPaiIdAndAtivo(@Param("paiId") Long paiId);

    @Query("SELECT g FROM Grupo g WHERE g.grupoPai.id = :paiId ORDER BY g.nome")
    List<Grupo> findFilhosByPaiId(@Param("paiId") Long paiId);

    @Query("SELECT COUNT(g) FROM Grupo g WHERE g.grupoPai.id = :paiId")
    long countFilhosByPaiId(@Param("paiId") Long paiId);
}