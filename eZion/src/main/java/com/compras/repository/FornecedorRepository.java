package com.compras.repository;

import com.compras.model.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {

    Optional<Fornecedor> findByCodigo(String codigo);

    Optional<Fornecedor> findByCnpj(String cnpj);

    List<Fornecedor> findByAtivoTrue();

    boolean existsByCodigo(String codigo);

    boolean existsByCnpj(String cnpj);
}