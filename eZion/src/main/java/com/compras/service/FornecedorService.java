package com.compras.service;

import com.cadastros.repository.BairroRepository;
import com.cadastros.repository.CidadeRepository;
import com.compras.model.Fornecedor;
import com.compras.repository.FornecedorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FornecedorService {

    private final FornecedorRepository fornecedorRepository;
    private final CidadeRepository cidadeRepository;
    private final BairroRepository bairroRepository;

    /**
     * Cria um novo fornecedor
     */
    @Transactional
    public Fornecedor criar(Fornecedor fornecedor) {
        log.debug("Criando novo fornecedor: {}", fornecedor.getRazaoSocial());

        // Validar se o código já existe
        if (fornecedorRepository.existsByCodigo(fornecedor.getCodigo())) {
            throw new IllegalArgumentException("Já existe um fornecedor com o código: " + fornecedor.getCodigo());
        }

        // Validar se o CNPJ já existe
        if (fornecedorRepository.existsByCnpj(fornecedor.getCnpj())) {
            throw new IllegalArgumentException("Já existe um fornecedor com o CNPJ: " + fornecedor.getCnpj());
        }

        // Validar se a cidade existe
        if (fornecedor.getCidade() != null && fornecedor.getCidade().getId() != null) {
            cidadeRepository.findById(fornecedor.getCidade().getId())
                .orElseThrow(() -> new IllegalArgumentException("Cidade não encontrada com ID: " + fornecedor.getCidade().getId()));
        }

        // Validar se o bairro existe (se informado)
        if (fornecedor.getBairro() != null && fornecedor.getBairro().getId() != null) {
            bairroRepository.findById(fornecedor.getBairro().getId())
                .orElseThrow(() -> new IllegalArgumentException("Bairro não encontrado com ID: " + fornecedor.getBairro().getId()));
        }

        return fornecedorRepository.save(fornecedor);
    }

    /**
     * Atualiza um fornecedor existente
     */
    @Transactional
    public Fornecedor atualizar(Long id, Fornecedor fornecedorAtualizado) {
        log.debug("Atualizando fornecedor ID: {}", id);

        Fornecedor fornecedor = fornecedorRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Fornecedor não encontrado com ID: " + id));

        // Validar código único (exceto para o próprio fornecedor)
        if (!fornecedor.getCodigo().equals(fornecedorAtualizado.getCodigo()) &&
            fornecedorRepository.existsByCodigo(fornecedorAtualizado.getCodigo())) {
            throw new IllegalArgumentException("Já existe um fornecedor com o código: " + fornecedorAtualizado.getCodigo());
        }

        // Validar CNPJ único (exceto para o próprio fornecedor)
        if (!fornecedor.getCnpj().equals(fornecedorAtualizado.getCnpj()) &&
            fornecedorRepository.existsByCnpj(fornecedorAtualizado.getCnpj())) {
            throw new IllegalArgumentException("Já existe um fornecedor com o CNPJ: " + fornecedorAtualizado.getCnpj());
        }

        // Validar se a cidade existe
        if (fornecedorAtualizado.getCidade() != null && fornecedorAtualizado.getCidade().getId() != null) {
            cidadeRepository.findById(fornecedorAtualizado.getCidade().getId())
                .orElseThrow(() -> new IllegalArgumentException("Cidade não encontrada com ID: " + fornecedorAtualizado.getCidade().getId()));
        }

        // Validar se o bairro existe (se informado)
        if (fornecedorAtualizado.getBairro() != null && fornecedorAtualizado.getBairro().getId() != null) {
            bairroRepository.findById(fornecedorAtualizado.getBairro().getId())
                .orElseThrow(() -> new IllegalArgumentException("Bairro não encontrado com ID: " + fornecedorAtualizado.getBairro().getId()));
        }

        fornecedor.setCodigo(fornecedorAtualizado.getCodigo());
        fornecedor.setRazaoSocial(fornecedorAtualizado.getRazaoSocial());
        fornecedor.setNomeFantasia(fornecedorAtualizado.getNomeFantasia());
        fornecedor.setCnpj(fornecedorAtualizado.getCnpj());
        fornecedor.setEmail(fornecedorAtualizado.getEmail());
        fornecedor.setTelefone(fornecedorAtualizado.getTelefone());
        fornecedor.setEndereco(fornecedorAtualizado.getEndereco());
        fornecedor.setCidade(fornecedorAtualizado.getCidade());
        fornecedor.setBairro(fornecedorAtualizado.getBairro());
        fornecedor.setCep(fornecedorAtualizado.getCep());
        fornecedor.setObservacoes(fornecedorAtualizado.getObservacoes());

        return fornecedorRepository.save(fornecedor);
    }

    /**
     * Busca fornecedor por ID
     */
    @Transactional(readOnly = true)
    public Optional<Fornecedor> obterPorId(Long id) {
        return fornecedorRepository.findById(id);
    }

    /**
     * Busca fornecedor por código
     */
    @Transactional(readOnly = true)
    public Optional<Fornecedor> obterPorCodigo(String codigo) {
        return fornecedorRepository.findByCodigo(codigo);
    }

    /**
     * Lista todos os fornecedores ativos
     */
    @Transactional(readOnly = true)
    public List<Fornecedor> listarAtivos() {
        return fornecedorRepository.findByAtivoTrue();
    }

    /**
     * Lista todos os fornecedores
     */
    @Transactional(readOnly = true)
    public List<Fornecedor> listarTodos() {
        return fornecedorRepository.findAll();
    }

    /**
     * Desativa um fornecedor
     */
    @Transactional
    public void desativar(Long id) {
        log.debug("Desativando fornecedor ID: {}", id);
        Fornecedor fornecedor = fornecedorRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Fornecedor não encontrado com ID: " + id));
        fornecedor.setAtivo(false);
        fornecedorRepository.save(fornecedor);
    }
}