package com.contabil.service;

import com.contabil.model.Empresa;
import com.contabil.repository.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    public Empresa createEmpresa(Empresa empresa) {
        empresa.setDataCriacao(new Date());
        empresa.setDataAtualizacao(new Date());
        empresa.setAtiva(true);
        return empresaRepository.save(empresa);
    }

    public Optional<Empresa> getEmpresaById(Long id) {
        return empresaRepository.findById(id);
    }

    public List<Empresa> getAllEmpresas() {
        return empresaRepository.findAll();
    }

    public List<Empresa> getEmpresasAtivas() {
        return empresaRepository.findByAtiva(true);
    }

    public List<Empresa> getEmpresasInativas() {
        return empresaRepository.findByAtiva(false);
    }

    public Optional<Empresa> getEmpresaByCnpj(String cnpj) {
        return empresaRepository.findByCnpj(cnpj);
    }

    public Optional<Empresa> getEmpresaByInscricaoEstadual(String inscricaoEstadual) {
        return empresaRepository.findByInscricaoEstadual(inscricaoEstadual);
    }

    public List<Empresa> getEmpresasByRegime(Empresa.Regime regimeEscal) {
        return empresaRepository.findByRegimeEscal(regimeEscal);
    }

    public List<Empresa> getEmpresasByTipoContribuinte(Empresa.TipoContribuinte tipoContribuinte) {
        return empresaRepository.findByTipoContribuinte(tipoContribuinte);
    }

    @Transactional
    public Empresa updateEmpresa(Long id, Empresa empresaDetails) {
        return empresaRepository.findById(id).map(empresa -> {
            empresa.setRazaoSocial(empresaDetails.getRazaoSocial());
            empresa.setNomeFantasia(empresaDetails.getNomeFantasia());
            empresa.setEmail(empresaDetails.getEmail());
            empresa.setTelefone(empresaDetails.getTelefone());
            empresa.setLogradouro(empresaDetails.getLogradouro());
            empresa.setNumero(empresaDetails.getNumero());
            empresa.setComplemento(empresaDetails.getComplemento());
            empresa.setBairro(empresaDetails.getBairro());
            empresa.setCidade(empresaDetails.getCidade());
            empresa.setEstado(empresaDetails.getEstado());
            empresa.setCep(empresaDetails.getCep());
            empresa.setRegimeEscal(empresaDetails.getRegimeEscal());
            empresa.setTipoContribuinte(empresaDetails.getTipoContribuinte());
            empresa.setAliquotaPIS(empresaDetails.getAliquotaPIS());
            empresa.setAliquotaCOFINS(empresaDetails.getAliquotaCOFINS());
            empresa.setAliquotaIRRF(empresaDetails.getAliquotaIRRF());
            empresa.setAliquotaINSS(empresaDetails.getAliquotaINSS());
            empresa.setFaturamentoAnual(empresaDetails.getFaturamentoAnual());
            empresa.setResponsavelNome(empresaDetails.getResponsavelNome());
            empresa.setResponsavelCPF(empresaDetails.getResponsavelCPF());
            empresa.setResponsavelEmail(empresaDetails.getResponsavelEmail());
            empresa.setResponsavelTelefone(empresaDetails.getResponsavelTelefone());
            empresa.setDataAtualizacao(new Date());
            return empresaRepository.save(empresa);
        }).orElseThrow(() -> new RuntimeException("Empresa not found with id " + id));
    }

    @Transactional
    public void desativarEmpresa(Long id) {
        empresaRepository.findById(id).ifPresent(empresa -> {
            empresa.setAtiva(false);
            empresa.setDataAtualizacao(new Date());
            empresaRepository.save(empresa);
        });
    }

    @Transactional
    public void ativarEmpresa(Long id) {
        empresaRepository.findById(id).ifPresent(empresa -> {
            empresa.setAtiva(true);
            empresa.setDataAtualizacao(new Date());
            empresaRepository.save(empresa);
        });
    }

    public void deleteEmpresa(Long id) {
        empresaRepository.deleteById(id);
    }
}
