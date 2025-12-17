package com.cadastros.repository;

import com.cadastros.model.Cidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CidadeRepository extends JpaRepository<Cidade, Long> {

    Optional<Cidade> findByNomeAndUf(String nome, String uf);

    List<Cidade> findByUf(String uf);

    List<Cidade> findByAtivoTrue();

    boolean existsByNomeAndUf(String nome, String uf);

    List<Cidade> findByNomeContainingIgnoreCase(String nome);
}