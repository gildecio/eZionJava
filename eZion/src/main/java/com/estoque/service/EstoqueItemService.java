package com.estoque.service;

import com.estoque.model.EstoqueItem;
import com.estoque.model.EstoqueItem.TipoItem;
import com.estoque.model.Grupo;
import com.estoque.repository.EstoqueItemRepository;
import com.estoque.repository.GrupoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EstoqueItemService {

    private final EstoqueItemRepository repository;
    private final GrupoRepository grupoRepository;

    /**
     * Lista todos os itens de estoque
     */
    @Transactional(readOnly = true)
    public List<EstoqueItem> listarTodos() {
        log.debug("Listando todos os itens de estoque");
        return repository.findAll();
    }

    /**
     * Lista todos os itens ativos
     */
    @Transactional(readOnly = true)
    public List<EstoqueItem> listarAtivos() {
        log.debug("Listando itens ativos");
        return repository.findByAtivoTrue();
    }

    /**
     * Busca um item por ID
     */
    @Transactional(readOnly = true)
    public Optional<EstoqueItem> obterPorId(Long id) {
        log.debug("Buscando item de estoque com ID: {}", id);
        return repository.findById(id);
    }

    /**
     * Busca um item por código
     */
    @Transactional(readOnly = true)
    public Optional<EstoqueItem> obterPorCodigo(String codigo) {
        log.debug("Buscando item de estoque com código: {}", codigo);
        return repository.findByCodigo(codigo);
    }

    /**
     * Lista itens por tipo
     */
    @Transactional(readOnly = true)
    public List<EstoqueItem> listarPorTipo(TipoItem tipoItem) {
        log.debug("Listando itens do tipo: {}", tipoItem);
        return repository.findByTipoItemAndAtivoTrue(tipoItem);
    }

    /**
     * Busca itens por descrição
     */
    @Transactional(readOnly = true)
    public List<EstoqueItem> buscarPorDescricao(String descricao) {
        log.debug("Buscando itens com descrição contendo: {}", descricao);
        return repository.findByDescricaoContainingIgnoreCase(descricao);
    }

    /**
     * Cria um novo item de estoque
     */
    @Transactional
    public EstoqueItem criar(EstoqueItem item) {
        log.debug("Criando novo item de estoque: {}", item.getCodigo());
        
        // Validar se o código já existe
        if (repository.existsByCodigo(item.getCodigo())) {
            throw new IllegalArgumentException("Já existe um item com o código: " + item.getCodigo());
        }

        // Validar se o grupo existe
        if (item.getGrupo() != null && item.getGrupo().getId() != null) {
            Grupo grupo = grupoRepository.findById(item.getGrupo().getId())
                .orElseThrow(() -> new IllegalArgumentException("Grupo não encontrado com ID: " + item.getGrupo().getId()));
            item.setGrupo(grupo);
        }

        return repository.save(item);
    }

    /**
     * Atualiza um item de estoque existente
     */
    @Transactional
    public EstoqueItem atualizar(Long id, EstoqueItem itemAtualizado) {
        log.debug("Atualizando item de estoque ID: {}", id);
        
        EstoqueItem itemExistente = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Item não encontrado com ID: " + id));

        // Validar se o código já existe em outro item
        if (!itemExistente.getCodigo().equals(itemAtualizado.getCodigo())) {
            if (repository.existsByCodigo(itemAtualizado.getCodigo())) {
                throw new IllegalArgumentException("Já existe um item com o código: " + itemAtualizado.getCodigo());
            }
        }

        // Atualizar campos
        itemExistente.setCodigo(itemAtualizado.getCodigo());
        itemExistente.setDescricao(itemAtualizado.getDescricao());
        itemExistente.setDescricaoDetalhada(itemAtualizado.getDescricaoDetalhada());
        itemExistente.setTipoItem(itemAtualizado.getTipoItem());
        
        // Validar e atualizar grupo
        if (itemAtualizado.getGrupo() != null && itemAtualizado.getGrupo().getId() != null) {
            Grupo grupo = grupoRepository.findById(itemAtualizado.getGrupo().getId())
                .orElseThrow(() -> new IllegalArgumentException("Grupo não encontrado com ID: " + itemAtualizado.getGrupo().getId()));
            itemExistente.setGrupo(grupo);
        }
        
        itemExistente.setUnidade(itemAtualizado.getUnidade());
        itemExistente.setQuantidadeMinima(itemAtualizado.getQuantidadeMinima());
        itemExistente.setQuantidadeMaxima(itemAtualizado.getQuantidadeMaxima());
        itemExistente.setAtivo(itemAtualizado.getAtivo());

        return repository.save(itemExistente);
    }

    /**
     * Ativa um item
     */
    @Transactional
    public EstoqueItem ativar(Long id) {
        log.debug("Ativando item ID: {}", id);
        
        EstoqueItem item = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Item não encontrado com ID: " + id));

        item.setAtivo(true);
        return repository.save(item);
    }

    /**
     * Desativa um item
     */
    @Transactional
    public EstoqueItem desativar(Long id) {
        log.debug("Desativando item ID: {}", id);
        
        EstoqueItem item = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Item não encontrado com ID: " + id));

        item.setAtivo(false);
        return repository.save(item);
    }

    /**
     * Exclui um item (exclusão lógica)
     */
    @Transactional
    public void excluir(Long id) {
        log.debug("Excluindo item ID: {}", id);
        
        EstoqueItem item = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Item não encontrado com ID: " + id));

        item.setAtivo(false);
        repository.save(item);
    }

    /**
     * Exclui um item definitivamente
     */
    @Transactional
    public void excluirDefinitivamente(Long id) {
        log.debug("Excluindo definitivamente item ID: {}", id);
        
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Item não encontrado com ID: " + id);
        }

        repository.deleteById(id);
    }
}
