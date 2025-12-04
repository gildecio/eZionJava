package com.fiscal.repository;

import com.fiscal.model.NaturezaOperacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NaturezaOperacaoRepository extends JpaRepository<NaturezaOperacao, Long> {
}
