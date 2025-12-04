# Guia de Uso da API eZion

## Inicialização - Gerar Números Sequenciais

### 1. Inicializar Sistema de Numeração
```bash
curl -X POST http://localhost:8080/api/numeracoes/inicializar \
  -H "Content-Type: application/json"
```

**Resposta:**
```json
{
  "message": "Numerações inicializadas com sucesso"
}
```

---

## Fluxo de Vendas

### 1. Criar Pedido de Venda
```bash
curl -X POST http://localhost:8080/api/pedidos-venda \
  -H "Content-Type: application/json" \
  -d '{
    "cliente": "João Silva",
    "dataPrevista": "2025-01-15T00:00:00"
  }'
```

**Resposta:**
```json
{
  "id": 1,
  "numero": "PV-2025-000001",
  "cliente": "João Silva",
  "status": "RASCUNHO",
  "dataPrevista": "2025-01-15T00:00:00"
}
```

### 2. Adicionar Itens ao Pedido (via API de item do pedido)
```bash
curl -X POST http://localhost:8080/api/pedidos-venda/1/itens \
  -H "Content-Type: application/json" \
  -d '{
    "itemId": 1,
    "quantidade": 10,
    "lote": "LOTE-001"
  }'
```

### 3. Confirmar Pedido
```bash
curl -X POST http://localhost:8080/api/pedidos-venda/1/confirmar \
  -H "Content-Type: application/json"
```

**Resposta:** Status muda para `CONFIRMADO`

### 4. Marcar como Separado
```bash
curl -X POST http://localhost:8080/api/pedidos-venda/1/separar \
  -H "Content-Type: application/json"
```

**Resposta:** Status muda para `SEPARADO`, estoque é validado

### 5. Expedir
```bash
curl -X POST http://localhost:8080/api/pedidos-venda/1/expedir \
  -H "Content-Type: application/json"
```

**Resposta:** 
- Status muda para `EXPEDIDO`
- Movimentação de SAÍDA é criada
- Saldo é decrementado

### 6. Confirmar Entrega
```bash
curl -X POST http://localhost:8080/api/pedidos-venda/1/confirmar-entrega \
  -H "Content-Type: application/json" \
  -d '{"dataEntrega": "2025-01-15T14:30:00"}'
```

**Resposta:** Status muda para `ENTREGUE`

---

## Fluxo de Compras

### 1. Criar Ordem de Compra
```bash
curl -X POST http://localhost:8080/api/ordens-compra \
  -H "Content-Type: application/json" \
  -d '{
    "fornecedor": "Fornecedor XYZ",
    "observacao": "Entrega na matriz"
  }'
```

**Resposta:**
```json
{
  "id": 1,
  "numero": "OC-2025-000001",
  "fornecedor": "Fornecedor XYZ",
  "status": "RASCUNHO"
}
```

### 2. Enviar Ordem
```bash
curl -X POST http://localhost:8080/api/ordens-compra/1/enviar \
  -H "Content-Type: application/json"
```

**Resposta:** Status muda para `ENVIADA`

### 3. Confirmar Ordem
```bash
curl -X POST http://localhost:8080/api/ordens-compra/1/confirmar \
  -H "Content-Type: application/json"
```

**Resposta:** Status muda para `CONFIRMADA`

### 4. Receber Parcialmente
```bash
curl -X POST http://localhost:8080/api/ordens-compra/1/receber \
  -H "Content-Type: application/json" \
  -d '{
    "id": <itemId>,
    "quantidadeRecebida": 5,
    "lote": "LOTE-002"
  }'
```

**Resposta:** 
- Status muda para `PARCIALMENTE_RECEBIDA` (ou `RECEBIDA` se tudo recebido)
- Movimentação de ENTRADA criada
- Saldo incrementado

---

## Fluxo de Notas Fiscais

### 1. Criar Nota Fiscal de Entrada
```bash
curl -X POST http://localhost:8080/api/notas-fiscais-entrada \
  -H "Content-Type: application/json" \
  -d '{
    "fornecedor": "Fornecedor ABC",
    "chaveNFe": "35250101234567000198550010000000011234567890",
    "serie": 1
  }'
```

**Resposta:**
```json
{
  "id": 1,
  "numero": "NF-E-2025-000001",
  "fornecedor": "Fornecedor ABC",
  "chaveNFe": "35250101234567000198550010000000011234567890",
  "status": "RASCUNHO"
}
```

### 2. Processar Nota Fiscal de Entrada
```bash
curl -X POST http://localhost:8080/api/notas-fiscais-entrada/1/processar \
  -H "Content-Type: application/json"
```

**Resposta:**
- Status muda para `PROCESSADA`
- Movimentações de ENTRADA criadas para cada item
- Saldos incrementados

### 3. Criar e Processar Nota Fiscal de Saída
```bash
# Criar
curl -X POST http://localhost:8080/api/notas-fiscais-saida \
  -H "Content-Type: application/json" \
  -d '{
    "cliente": "Cliente XYZ",
    "chaveNFe": "35250101234567000198550010000000021234567891",
    "serie": 1
  }'

# Processar
curl -X POST http://localhost:8080/api/notas-fiscais-saida/1/processar \
  -H "Content-Type: application/json"
```

**Resposta:**
- Status muda para `PROCESSADA`
- Estoque é validado
- Movimentações de SAÍDA criadas
- Saldos decrementados

---

## Devolução de Mercadorias

### 1. Devolução de Cliente (Entrada no Estoque)
```bash
curl -X POST http://localhost:8080/api/devolucoes \
  -H "Content-Type: application/json" \
  -d '{
    "tipo": "DEVOLUCAO_CLIENTE",
    "itemId": 1,
    "quantidade": 5,
    "lote": "LOTE-001",
    "motivo": "Produto com defeito",
    "observacao": "Cliente reclamou da qualidade"
  }'
```

**Resposta:**
```json
{
  "id": 1,
  "numero": "DEV-2025-000001",
  "tipo": "DEVOLUCAO_CLIENTE",
  "status": "RASCUNHO"
}
```

### 2. Processar Devolução
```bash
curl -X POST http://localhost:8080/api/devolucoes/1/processar \
  -H "Content-Type: application/json"
```

**Resposta:**
- Tipo DEVOLUCAO_CLIENTE: cria ENTRADA (volta pro estoque)
- Tipo DEVOLUCAO_FORNECEDOR: cria SAÍDA (devolve pro fornecedor)

---

## Ajustes de Estoque

### 1. Criar Ajuste de Entrada (Inventário encontrou produto a mais)
```bash
curl -X POST http://localhost:8080/api/ajustes-estoque \
  -H "Content-Type: application/json" \
  -d '{
    "tipo": "ENTRADA",
    "itemId": 1,
    "quantidade": 10,
    "lote": "LOTE-003",
    "motivo": "CONTAGEM_INVENTARIO",
    "descricao": "Diferença encontrada em contagem",
    "usuario": "admin"
  }'
```

**Resposta:**
```json
{
  "id": 1,
  "numero": "ADJ-2025-000001",
  "tipo": "ENTRADA",
  "status": "CRIADO"
}
```

### 2. Processar Ajuste
```bash
curl -X POST http://localhost:8080/api/ajustes-estoque/1/processar \
  -H "Content-Type: application/json"
```

**Resposta:**
- Movimentação criada
- Saldo ajustado
- Histórico registrado

---

## Consultas e Relatórios

### 1. Consultar Saldo de Estoque
```bash
curl -X GET "http://localhost:8080/api/saldos-estoque?itemId=1&localId=1" \
  -H "Content-Type: application/json"
```

**Resposta:**
```json
[
  {
    "id": 1,
    "item": {"id": 1, "nome": "Produto A"},
    "local": {"id": 1, "nome": "Estoque Principal"},
    "lote": "LOTE-001",
    "quantidade": 95
  }
]
```

### 2. Consultar Pedidos por Status
```bash
curl -X GET "http://localhost:8080/api/pedidos-venda/status/CONFIRMADO" \
  -H "Content-Type: application/json"
```

### 3. Consultar Ordens de Compra Pendentes
```bash
curl -X GET "http://localhost:8080/api/ordens-compra/status/PARCIALMENTE_RECEBIDA" \
  -H "Content-Type: application/json"
```

### 4. Consultar Movimentações por Item
```bash
curl -X GET "http://localhost:8080/api/movimentacoes/item/1" \
  -H "Content-Type: application/json"
```

### 5. Histórico de Saldo (Auditoria)
```bash
curl -X GET "http://localhost:8080/api/saldos-estoque-historico/item/1" \
  -H "Content-Type: application/json"
```

**Resposta:**
```json
[
  {
    "id": 1,
    "item": {"id": 1},
    "local": {"id": 1},
    "lote": "LOTE-001",
    "saldoAnterior": 100,
    "saldoNovo": 95,
    "quantidadeMovimentada": 5,
    "tipoMovimentacao": "SAIDA",
    "movimentacao": {"id": 1, "referencia": "PV-2025-000001"},
    "dataMovimentacao": "2025-01-15T14:30:00"
  }
]
```

---

## Geração de Números Sequenciais

### 1. Gerar Número Manual (útil para testes)
```bash
curl -X POST "http://localhost:8080/api/numeracoes/gerar/PEDIDO_VENDA" \
  -H "Content-Type: application/json"
```

**Resposta:**
```json
{
  "numero": "PV-2025-000005"
}
```

### 2. Consultar Configuração de Numeração
```bash
curl -X GET "http://localhost:8080/api/numeracoes/NOTA_FISCAL_ENTRADA" \
  -H "Content-Type: application/json"
```

**Resposta:**
```json
{
  "id": 1,
  "tipoDocumento": "NOTA_FISCAL_ENTRADA",
  "proximoNumero": 2,
  "anoVigente": 2025,
  "ultimaAtualizacao": "2025-01-15T10:00:00"
}
```

### 3. Resetar Numeração (resetar manualmente se necessário)
```bash
curl -X POST "http://localhost:8080/api/numeracoes/resetar/PEDIDO_VENDA" \
  -H "Content-Type: application/json"
```

---

## Tratamento de Erros

Todos os endpoints retornam códigos HTTP apropriados:

### Sucesso
- `200 OK` - Operação bem-sucedida
- `201 Created` - Recurso criado
- `204 No Content` - Deletado com sucesso

### Erros
- `400 Bad Request` - Dados inválidos ou operação não permitida (ex: estoque insuficiente)
- `404 Not Found` - Recurso não encontrado
- `409 Conflict` - Conflito de estado

### Exemplo de Erro
```bash
curl -X POST http://localhost:8080/api/pedidos-venda/1/expedir
```

**Resposta (400):**
```json
{
  "error": "Estoque insuficiente para o item: Produto A"
}
```

---

## Dicas Importantes

1. **Lotes (Batch Tracking)**
   - Sempre informar lote ao criar movimentações
   - Sistema permite rastrear cada lote independentemente
   - Histórico separado por lote

2. **Números Sequenciais**
   - Gerados automaticamente ao criar documento
   - Formato: `{PREFIXO}-{YEAR}-{SEQUENCE}`
   - Reset automático anualmente

3. **Fluxos de Status**
   - Sempre validar status atual antes de transição
   - Não pular etapas (ex: não pode ir de RASCUNHO direto para ENTREGUE)
   - Alguns documentos não podem ser cancelados após processamento

4. **Transações**
   - Todas as operações críticas são transacionais
   - Se uma falha, tudo é revertido
   - Exemplo: criar movimentação + atualizar saldo é tudo ou nada

5. **Auditoria**
   - Histórico de saldo é imutável
   - Referências (PV-, NF-E-, etc.) rastreiam origem
   - Timestamps automáticos

