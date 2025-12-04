# eZion - Sistema de Gestão de Estoque

## Status de Implementação ✅

Compilação: **BUILD SUCCESS**

## Resumo das Implementações Realizadas

### 1. Integração do Sistema de Numeração com NumeracaoService

#### Criado:
- **NumeracaoController** (`/api/numeracoes`)
  - GET `/{tipoDocumento}` - Obter numeração por tipo
  - POST `/gerar/{tipoDocumento}` - Gerar número sequencial
  - POST `/resetar/{tipoDocumento}` - Resetar numeração
  - POST `/inicializar` - Inicializar todas as numerações

#### Atualizado:
- **PedidoVendaService** - `createPedidoVenda()` agora chama `numeracaoService.gerarNumero(TipoDocumento.PEDIDO_VENDA)`

### 2. Services para Documentos

#### NotaFiscalEntradaService
- `createNotaFiscalEntrada()` - Gera número sequencial
- `processarNotaFiscalEntrada()` - Cria movimentações de ENTRADA
- `cancelarNotaFiscalEntrada()` - Cancela (não permite se já processada)
- Queries: por ID, número, status, fornecedor

#### NotaFiscalSaidaService
- `createNotaFiscalSaida()` - Gera número sequencial
- `processarNotaFiscalSaida()` - Valida estoque e cria movimentações de SAÍDA
- `cancelarNotaFiscalSaida()` - Cancela (não permite se já processada)
- Queries: por ID, número, status, cliente

#### OrdemCompraService
- `createOrdemCompra()` - Gera número sequencial
- `enviarOrdemCompra()` - RASCUNHO → ENVIADA
- `confirmarOrdemCompra()` - ENVIADA → CONFIRMADA
- `receberOrdemCompra()` - Recebe itens com suporte a recebimento parcial
- Controla fluxo: RASCUNHO → ENVIADA → CONFIRMADA → (PARCIALMENTE_RECEBIDA) → RECEBIDA
- Integração com SaldoEstoqueService ao receber

#### DevolutionService (Devolucao)
- `createDevolucao()` - Gera número sequencial
- `processarDevolucao()` - Diferencia DEVOLUCAO_CLIENTE (ENTRADA) de DEVOLUCAO_FORNECEDOR (SAÍDA)
- `cancelarDevolucao()` - Cancela
- Queries: por ID, número, status, tipo

#### AjusteEstoqueService
- `createAjusteEstoque()` - Gera número sequencial
- `processarAjuste()` - Cria movimentação AJUSTE com tipo ENTRADA ou SAÍDA
- Queries: por ID, número, tipo

### 3. Controllers para Documentos

#### NotaFiscalEntradaController (`/api/notas-fiscais-entrada`)
- CRUD completo
- POST `/{id}/processar` - Processar nota
- POST `/{id}/cancelar` - Cancelar nota
- GET `/status/{status}` - Filtrar por status
- GET `/fornecedor/{fornecedor}` - Filtrar por fornecedor

#### NotaFiscalSaidaController (`/api/notas-fiscais-saida`)
- CRUD completo
- POST `/{id}/processar` - Processar nota
- POST `/{id}/cancelar` - Cancelar nota
- GET `/status/{status}` - Filtrar por status
- GET `/cliente/{cliente}` - Filtrar por cliente

#### OrdemCompraController (`/api/ordens-compra`)
- CRUD completo
- POST `/{id}/enviar` - Enviar ordem
- POST `/{id}/confirmar` - Confirmar ordem
- POST `/{id}/receber` - Receber itens
- POST `/{id}/cancelar` - Cancelar ordem
- GET `/status/{status}` - Filtrar por status
- GET `/fornecedor/{fornecedor}` - Filtrar por fornecedor
- GET `/{id}/itens` - Listar itens da ordem

#### DevolucaoController (`/api/devolucoes`)
- CRUD completo
- POST `/{id}/processar` - Processar devolução
- POST `/{id}/cancelar` - Cancelar devolução
- GET `/status/{status}` - Filtrar por status
- GET `/tipo/{tipo}` - Filtrar por tipo

#### AjusteEstoqueController (`/api/ajustes-estoque`)
- CRUD completo
- POST `/{id}/processar` - Processar ajuste
- GET `/tipo/{tipo}` - Filtrar por tipo

### 4. Integrações com SaldoEstoqueService

Todos os documentos agora chamam `saldoEstoqueService.adicionarMovimentacaoComLote()` com:
- **Item**: qual item
- **Local**: local de origem/destino (sempre localPadrao = 1L)
- **Lote**: rastreabilidade de lote
- **Quantidade**: BigDecimal
- **MovimentacaoEstoque**: objeto com tipo (ENTRADA, SAIDA, AJUSTE, DEVOLUCAO)
- **TipoMovimentacao**: String ("ENTRADA" | "SAIDA" | "AJUSTE" | "DEVOLUCAO")

### 5. Fluxos de Documentos Implementados

#### Pedido de Venda (PedidoVenda)
```
RASCUNHO → CONFIRMADO → SEPARADO → EXPEDIDO → ENTREGUE
                                 ↓
                           CANCELADO (em qualquer ponto)
```
- Ao expedir: gera movimentações de SAÍDA com referência "PV-{numero}"

#### Nota Fiscal de Entrada (NotaFiscalEntrada)
```
RASCUNHO → PROCESSADA
   ↓
CANCELADA
```
- Ao processar: gera movimentações de ENTRADA para cada item

#### Nota Fiscal de Saída (NotaFiscalSaida)
```
RASCUNHO → PROCESSADA
   ↓
CANCELADA
```
- Valida estoque antes de processar
- Gera movimentações de SAÍDA para cada item

#### Ordem de Compra (OrdemCompra)
```
RASCUNHO → ENVIADA → CONFIRMADA → RECEBIDA
                          ↓
                  PARCIALMENTE_RECEBIDA
                          ↓ (ao receber)
                       RECEBIDA
    ↓
CANCELADA
```
- Suporta recebimento parcial
- Gera movimentações de ENTRADA ao receber

#### Devolução (Devolucao)
```
RASCUNHO → PROCESSADA
   ↓
CANCELADA
```
- DEVOLUCAO_CLIENTE: gera ENTRADA (devolução recebida)
- DEVOLUCAO_FORNECEDOR: gera SAÍDA (devolução enviada)

#### Ajuste de Estoque (AjusteEstoque)
```
Criado → PROCESSADO
```
- Tipo ENTRADA: gera movimentação de ENTRADA
- Tipo SAÍDA: gera movimentação de SAÍDA

## Arquitetura Geral

### Pacotes
- `com.estoque.model` - 15 entidades JPA
- `com.estoque.service` - 13 services com lógica de negócio
- `com.estoque.repository` - 15 repositories
- `com.estoque.controller` - 13 controllers REST
- `com.fiscal.model` - 2 entidades (NaturezaOperacao, NCM)

### Fluxo de Dados
```
Request → Controller → Service (transactional) → Repository → Database
                           ↓
                  SaldoEstoqueService
                           ↓
                  MovimentacaoEstoque criada
                  SaldoEstoque atualizado
                  SaldoEstoqueHistorico registrado
```

### Numeração Automática
Todos os documentos agora possuem número sequencial gerado automaticamente:
- **Formato**: `{PREFIXO}-{YEAR}-{SEQUENCE:06d}`
  - Exemplos: `PV-2025-000001`, `NF-E-2025-000001`, `OC-2025-000001`
- **Prefixos configurados**:
  - PEDIDO_VENDA → "PV"
  - NOTA_FISCAL_ENTRADA → "NF-E"
  - NOTA_FISCAL_SAIDA → "NF-S"
  - ORDEM_COMPRA → "OC"
  - DEVOLUCAO → "DEV"
  - AJUSTE_ESTOQUE → "ADJ"
- **Reset anual**: automaticamente resetado 1º de janeiro

## Endpoints Implementados

### Numerações
- `POST /api/numeracoes/gerar/{tipoDocumento}` - Gerar número
- `GET /api/numeracoes/{tipoDocumento}` - Obter config
- `POST /api/numeracoes/resetar/{tipoDocumento}` - Resetar
- `POST /api/numeracoes/inicializar` - Inicializar todos

### Documentos
- `/api/notas-fiscais-entrada` - NotaFiscalEntrada CRUD + processar/cancelar
- `/api/notas-fiscais-saida` - NotaFiscalSaida CRUD + processar/cancelar
- `/api/ordens-compra` - OrdemCompra CRUD + enviar/confirmar/receber/cancelar
- `/api/devolucoes` - Devolucao CRUD + processar/cancelar
- `/api/ajustes-estoque` - AjusteEstoque CRUD + processar

## Próximas Etapas (Sugeridas)

1. **Integração com Notas Fiscais Eletrônicas**
   - Validação de CHave NFe
   - Sincronização com SEFAZ

2. **Relatórios**
   - Saldo por item
   - Movimentações por período
   - Análise de estoque (ABC, FIFO/LIFO)

3. **Auditoria e Rastreabilidade**
   - Log de quem criou/modificou cada documento
   - Histórico de mudanças de status

4. **Workflow Aprovação**
   - Fluxo de aprovação para pedidos acima de limite
   - Integração com notificações

5. **Testes Unitários**
   - Testes para cada service
   - Testes de integração

## Verificação de Compilação

✅ **BUILD SUCCESS** - Todos os 75 arquivos fonte compilados sem erros

---

*Projeto: eZion - Estoque*
*Status: Implementação completa de funcionalidades núcleo*
*Data: 2025-12-04*
