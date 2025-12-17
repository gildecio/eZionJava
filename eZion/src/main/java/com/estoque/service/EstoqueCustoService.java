package com.estoque.service;

import com.estoque.model.EstoqueCusto;
import com.estoque.model.EstoqueCusto.TipoCusto;
import com.estoque.model.EstoqueItem;
import com.estoque.model.Lote;
import com.estoque.repository.EstoqueCustoRepository;
import com.estoque.repository.EstoqueItemRepository;
import com.estoque.repository.LoteRepository;
import com.seguranca.model.Usuario;
import com.seguranca.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EstoqueCustoService {

    private final EstoqueCustoRepository estoqueCustoRepository;
    private final EstoqueItemRepository estoqueItemRepository;
    private final LoteRepository loteRepository;
    private final UsuarioRepository usuarioRepository;

    /**
     * Lista todos os custos
     */
    @Transactional(readOnly = true)
    public List<EstoqueCusto> listarTodos() {
        log.debug("Listando todos os custos de estoque");
        return estoqueCustoRepository.findAll();
    }

    /**
     * Busca um custo por ID
     */
    @Transactional(readOnly = true)
    public Optional<EstoqueCusto> obterPorId(Long id) {
        log.debug("Buscando custo com ID: {}", id);
        return estoqueCustoRepository.findById(id);
    }

    /**
     * Lista custos por item de estoque
     */
    @Transactional(readOnly = true)
    public List<EstoqueCusto> listarPorItem(Long itemId) {
        log.debug("Listando custos do item ID: {}", itemId);
        return estoqueCustoRepository.findByEstoqueItemIdOrderByDataCustoDesc(itemId);
    }

    /**
     * Lista custos por lote
     */
    @Transactional(readOnly = true)
    public List<EstoqueCusto> listarPorLote(Long loteId) {
        log.debug("Listando custos do lote ID: {}", loteId);
        return estoqueCustoRepository.findByLoteIdOrderByDataCustoDesc(loteId);
    }

    /**
     * Lista custos por tipo
     */
    @Transactional(readOnly = true)
    public List<EstoqueCusto> listarPorTipo(TipoCusto tipoCusto) {
        log.debug("Listando custos do tipo: {}", tipoCusto);
        return estoqueCustoRepository.findByTipoCustoOrderByDataCustoDesc(tipoCusto);
    }

    /**
     * Lista custos por período
     */
    @Transactional(readOnly = true)
    public List<EstoqueCusto> listarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        log.debug("Listando custos no período {} - {}", inicio, fim);
        return estoqueCustoRepository.findByDataCustoBetweenOrderByDataCustoDesc(inicio, fim);
    }

    /**
     * Lista custos por item e período
     */
    @Transactional(readOnly = true)
    public List<EstoqueCusto> listarPorItemEPeriodo(Long itemId, LocalDateTime inicio, LocalDateTime fim) {
        log.debug("Listando custos do item {} no período {} - {}", itemId, inicio, fim);
        return estoqueCustoRepository.findByEstoqueItemIdAndDataCustoBetweenOrderByDataCustoDesc(itemId, inicio, fim);
    }

    /**
     * Registra um novo custo
     */
    @Transactional
    public EstoqueCusto registrar(EstoqueCusto estoqueCusto) {
        log.debug("Registrando novo custo: {}", estoqueCusto.getDescricao());

        // Validar se o item existe
        EstoqueItem item = estoqueItemRepository.findById(estoqueCusto.getEstoqueItem().getId())
            .orElseThrow(() -> new IllegalArgumentException("Item de estoque não encontrado com ID: " + estoqueCusto.getEstoqueItem().getId()));
        estoqueCusto.setEstoqueItem(item);

        // Validar se o lote existe (se informado)
        if (estoqueCusto.getLote() != null && estoqueCusto.getLote().getId() != null) {
            Lote lote = loteRepository.findById(estoqueCusto.getLote().getId())
                .orElseThrow(() -> new IllegalArgumentException("Lote não encontrado com ID: " + estoqueCusto.getLote().getId()));
            estoqueCusto.setLote(lote);
        }

        // Validar se o usuário existe
        Usuario usuario = usuarioRepository.findById(estoqueCusto.getUsuario().getId())
            .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com ID: " + estoqueCusto.getUsuario().getId()));
        estoqueCusto.setUsuario(usuario);

        // Definir data do custo se não foi informada
        if (estoqueCusto.getDataCusto() == null) {
            estoqueCusto.setDataCusto(LocalDateTime.now());
        }

        EstoqueCusto custoSalvo = estoqueCustoRepository.save(estoqueCusto);

        // Após salvar, recalcular custos médios se for uma compra
        atualizarCustoMedioAposLancamento(custoSalvo);

        return custoSalvo;
    }

    /**
     * Calcula o custo total de um item
     */
    @Transactional(readOnly = true)
    public BigDecimal calcularCustoTotal(Long itemId) {
        log.debug("Calculando custo total do item ID: {}", itemId);
        BigDecimal custoTotal = estoqueCustoRepository.calcularCustoTotalByEstoqueItemId(itemId);
        return custoTotal != null ? custoTotal : BigDecimal.ZERO;
    }

    /**
     * Calcula o custo total de um lote
     */
    @Transactional(readOnly = true)
    public BigDecimal calcularCustoTotalLote(Long loteId) {
        log.debug("Calculando custo total do lote ID: {}", loteId);
        BigDecimal custoTotal = estoqueCustoRepository.calcularCustoTotalByLoteId(loteId);
        return custoTotal != null ? custoTotal : BigDecimal.ZERO;
    }

    /**
     * Calcula o custo médio ponderado de um item
     */
    @Transactional(readOnly = true)
    public BigDecimal calcularCustoMedioPonderado(Long itemId) {
        log.debug("Calculando custo médio ponderado do item ID: {}", itemId);
        List<EstoqueCusto> custos = estoqueCustoRepository.findByEstoqueItemIdOrderByDataCustoDesc(itemId);

        if (custos.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal totalValor = BigDecimal.ZERO;
        BigDecimal totalQuantidade = BigDecimal.ZERO;

        for (EstoqueCusto custo : custos) {
            if (custo.getQuantidade() != null && custo.getQuantidade().compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal valorComImpostos = custo.calcularCustoTotalComImpostos();
                totalValor = totalValor.add(valorComImpostos.multiply(custo.getQuantidade()));
                totalQuantidade = totalQuantidade.add(custo.getQuantidade());
            }
        }

        if (totalQuantidade.compareTo(BigDecimal.ZERO) > 0) {
            return totalValor.divide(totalQuantidade, 4, RoundingMode.HALF_UP);
        }

        return BigDecimal.ZERO;
    }

    /**
     * Calcula o total de impostos de um item
     */
    @Transactional(readOnly = true)
    public BigDecimal calcularTotalImpostos(Long itemId) {
        log.debug("Calculando total de impostos do item ID: {}", itemId);
        List<EstoqueCusto> custos = estoqueCustoRepository.findByEstoqueItemIdOrderByDataCustoDesc(itemId);

        return custos.stream()
                .map(EstoqueCusto::calcularTotalImpostos)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Calcula o custo total incluindo frete rateado de um item
     */
    @Transactional(readOnly = true)
    public BigDecimal calcularCustoTotalComFrete(Long itemId) {
        log.debug("Calculando custo total com frete do item ID: {}", itemId);
        List<EstoqueCusto> custos = estoqueCustoRepository.findByEstoqueItemIdOrderByDataCustoDesc(itemId);

        return custos.stream()
                .map(EstoqueCusto::calcularCustoTotalComFrete)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Calcula o frete total rateado de um item
     */
    @Transactional(readOnly = true)
    public BigDecimal calcularFreteTotalRateado(Long itemId) {
        log.debug("Calculando frete total rateado do item ID: {}", itemId);
        List<EstoqueCusto> custos = estoqueCustoRepository.findByEstoqueItemIdOrderByDataCustoDesc(itemId);

        return custos.stream()
                .filter(custo -> custo.getFreteRateado() != null)
                .map(EstoqueCusto::getFreteRateado)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Recalcula os custos médios de um item considerando todas as entradas em ordem cronológica
     * Este método deve ser executado após lançamentos retroativos
     */
    @Transactional
    public void recalcularCustosMediosItem(Long itemId) {
        log.debug("Recalculando custos médios do item ID: {}", itemId);

        // Busca todos os custos do item em ordem cronológica (mais antigo primeiro)
        List<EstoqueCusto> custos = estoqueCustoRepository.findByEstoqueItemIdOrderByDataCustoAsc(itemId);

        if (custos.isEmpty()) {
            log.debug("Nenhum custo encontrado para o item ID: {}", itemId);
            return;
        }

        BigDecimal quantidadeAcumulada = BigDecimal.ZERO;
        BigDecimal valorTotalAcumulado = BigDecimal.ZERO;

        for (EstoqueCusto custo : custos) {
            // Só considera custos de compra para cálculo do custo médio
            if (custo.getTipoCusto() == TipoCusto.COMPRA && custo.getQuantidade() != null &&
                custo.getQuantidade().compareTo(BigDecimal.ZERO) > 0) {

                // Calcula o custo total da entrada (valor + impostos + frete rateado)
                BigDecimal custoTotalEntrada = custo.calcularCustoTotalComFrete();

                // Adiciona à quantidade e valor acumulados
                quantidadeAcumulada = quantidadeAcumulada.add(custo.getQuantidade());
                valorTotalAcumulado = valorTotalAcumulado.add(custoTotalEntrada);

                // Calcula o custo médio até este ponto
                if (quantidadeAcumulada.compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal custoMedioAtual = valorTotalAcumulado.divide(quantidadeAcumulada, 6, RoundingMode.HALF_UP)
                            .setScale(4, RoundingMode.HALF_UP);

                    // Atualiza o custo médio do registro
                    custo.setCustoMedio(custoMedioAtual);
                    estoqueCustoRepository.save(custo);

                    log.debug("Custo médio atualizado para entrada {}: {} (quantidade acumulada: {})",
                            custo.getId(), custoMedioAtual, quantidadeAcumulada);
                }
            }
        }

        log.debug("Recálculo de custos médios concluído para item ID: {}", itemId);
    }

    /**
     * Recalcula os custos médios de todos os itens
     * Método para manutenção geral do sistema
     */
    @Transactional
    public void recalcularCustosMediosTodosItens() {
        log.debug("Iniciando recálculo de custos médios para todos os itens");

        // Busca todos os IDs de itens que possuem custos
        List<Long> itemIds = estoqueCustoRepository.findByEstoqueItemIdOrderByDataCustoDesc(0L)
                .stream()
                .map(custo -> custo.getEstoqueItem().getId())
                .distinct()
                .toList();

        for (Long itemId : itemIds) {
            try {
                recalcularCustosMediosItem(itemId);
            } catch (Exception e) {
                log.error("Erro ao recalcular custos do item ID: {}", itemId, e);
            }
        }

        log.debug("Recálculo de custos médios concluído para todos os itens");
    }

    /**
     * Método executado após registrar um novo custo para atualizar custos médios
     */
    @Transactional
    public void atualizarCustoMedioAposLancamento(EstoqueCusto novoCusto) {
        log.debug("Atualizando custos médios após lançamento do custo ID: {}", novoCusto.getId());

        if (novoCusto.getEstoqueItem() != null && novoCusto.getTipoCusto() == TipoCusto.COMPRA) {
            recalcularCustosMediosItem(novoCusto.getEstoqueItem().getId());
        }
    }

    /**
     * Exclui um custo
     */
    @Transactional
    public void excluir(Long id) {
        log.debug("Excluindo custo ID: {}", id);
        if (!estoqueCustoRepository.existsById(id)) {
            throw new IllegalArgumentException("Custo não encontrado com ID: " + id);
        }
        estoqueCustoRepository.deleteById(id);
    }
}