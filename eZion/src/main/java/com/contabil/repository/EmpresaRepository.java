package com.contabil.repository;

import com.contabil.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
    Optional<Empresa> findByCnpj(String cnpj);
    List<Empresa> findByAtiva(Boolean ativa);
    List<Empresa> findByRegimeEscal(Empresa.Regime regimeEscal);
    List<Empresa> findByTipoContribuinte(Empresa.TipoContribuinte tipoContribuinte);
    Optional<Empresa> findByInscricaoEstadual(String inscricaoEstadual);
}
