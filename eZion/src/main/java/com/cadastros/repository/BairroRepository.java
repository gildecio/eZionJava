package com.cadastros.repository;

import com.cadastros.model.Bairro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BairroRepository extends JpaRepository<Bairro, Long> {

    List<Bairro> findByCidadeId(Long cidadeId);

    List<Bairro> findByCidadeIdAndAtivoTrue(Long cidadeId);

    List<Bairro> findByAtivoTrue();

    Optional<Bairro> findByNomeAndCidadeId(String nome, Long cidadeId);

    boolean existsByNomeAndCidadeId(String nome, Long cidadeId);

    List<Bairro> findByNomeContainingIgnoreCase(String nome);
}