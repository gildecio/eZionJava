package com.contabil.service;

import com.contabil.model.Empresa;
import com.contabil.repository.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    @PostConstruct
    public void initData() {
        if (empresaRepository.count() == 0) {
            Empresa emp1 = new Empresa();
            emp1.setRazaoSocial("Empresa Exemplo Ltda");
            emp1.setNomeFantasia("Exemplo Corp");
            emp1.setCnpj("12345678000123");
            emp1.setInscricaoEstadual("123456789");
            emp1.setInscricaoMunicipal("987654321");
            emp1.setEmail("contato@exemplo.com");
            emp1.setTelefone("11999999999");
            emp1.setLogradouro("Rua das Flores");
            emp1.setNumero("123");
            emp1.setComplemento("Sala 1");
            emp1.setBairro("Centro");
            emp1.setCidade("São Paulo");
            emp1.setEstado("SP");
            emp1.setCep("01234567");
            emp1.setRegimeEscal(Empresa.Regime.SIMPLES_NACIONAL);
            emp1.setTipoContribuinte(Empresa.TipoContribuinte.CONTRIBUINTE_ICMS);
            emp1.setAliquotaPIS("0.65");
            emp1.setAliquotaCOFINS("3.0");
            emp1.setAliquotaIRRF("1.5");
            emp1.setAliquotaINSS("11.0");
            emp1.setFaturamentoAnual(new BigDecimal("100000.00"));
            emp1.setResponsavelNome("João Silva");
            emp1.setResponsavelCPF("12345678901");
            emp1.setResponsavelEmail("joao@exemplo.com");
            emp1.setResponsavelTelefone("11999999999");
            empresaRepository.save(emp1);

            Empresa emp2 = new Empresa();
            emp2.setRazaoSocial("Outra Empresa S.A.");
            emp2.setNomeFantasia("Outra Ltda");
            emp2.setCnpj("98765432000198");
            emp2.setInscricaoEstadual("987654321");
            emp2.setInscricaoMunicipal("123456789");
            emp2.setEmail("info@outra.com");
            emp2.setTelefone("11888888888");
            emp2.setLogradouro("Av. Paulista");
            emp2.setNumero("456");
            emp2.setBairro("Bela Vista");
            emp2.setCidade("São Paulo");
            emp2.setEstado("SP");
            emp2.setCep("01310999");
            emp2.setRegimeEscal(Empresa.Regime.LUCRO_PRESUMIDO);
            emp2.setTipoContribuinte(Empresa.TipoContribuinte.CONTRIBUINTE_ICMS);
            emp2.setAliquotaPIS("0.65");
            emp2.setAliquotaCOFINS("3.0");
            emp2.setAliquotaIRRF("1.5");
            emp2.setAliquotaINSS("11.0");
            emp2.setFaturamentoAnual(new BigDecimal("500000.00"));
            emp2.setResponsavelNome("Maria Santos");
            emp2.setResponsavelCPF("98765432109");
            emp2.setResponsavelEmail("maria@outra.com");
            emp2.setResponsavelTelefone("11888888888");
            empresaRepository.save(emp2);

            Empresa emp3 = new Empresa();
            emp3.setRazaoSocial("Teste Indústria Ltda");
            emp3.setNomeFantasia("Teste Ind");
            emp3.setCnpj("45678912000145");
            emp3.setInscricaoEstadual("456789123");
            emp3.setInscricaoMunicipal("321654987");
            emp3.setEmail("teste@teste.com");
            emp3.setTelefone("11777777777");
            emp3.setLogradouro("Rua do Comércio");
            emp3.setNumero("789");
            emp3.setComplemento("Loja 2");
            emp3.setBairro("Vila Madalena");
            emp3.setCidade("São Paulo");
            emp3.setEstado("SP");
            emp3.setCep("05443000");
            emp3.setRegimeEscal(Empresa.Regime.LUCRO_REAL);
            emp3.setTipoContribuinte(Empresa.TipoContribuinte.CONTRIBUINTE_ICMS);
            emp3.setAliquotaPIS("0.65");
            emp3.setAliquotaCOFINS("3.0");
            emp3.setAliquotaIRRF("1.5");
            emp3.setAliquotaINSS("11.0");
            emp3.setFaturamentoAnual(new BigDecimal("2000000.00"));
            emp3.setResponsavelNome("Pedro Oliveira");
            emp3.setResponsavelCPF("45678912345");
            emp3.setResponsavelEmail("pedro@teste.com");
            emp3.setResponsavelTelefone("11777777777");
            empresaRepository.save(emp3);
        }
    }

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
            empresa.setAtiva(empresaDetails.getAtiva());
            empresa.setDataAtualizacao(new Date());
            return empresaRepository.save(empresa);
        }).orElseThrow(() -> new RuntimeException("Empresa not found with id " + id));
    }

    public void deleteEmpresa(Long id) {
        empresaRepository.deleteById(id);
    }
}
