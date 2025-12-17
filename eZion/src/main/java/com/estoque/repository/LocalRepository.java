package com.estoque.repository;

import com.estoque.model.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocalRepository extends JpaRepository<Local, Long> {

    Optional<Local> findByNome(String nome);

    boolean existsByNome(String nome);

    @Query("SELECT l FROM Local l WHERE l.ativo = true ORDER BY l.nome")
    List<Local> findAllAtivosOrderByNome();

    @Query("SELECT l FROM Local l WHERE l.ativo = true AND LOWER(l.nome) LIKE LOWER(CONCAT('%', :nome, '%')) ORDER BY l.nome")
    List<Local> findByNomeContainingIgnoreCaseAndAtivo(@Param("nome") String nome);
}