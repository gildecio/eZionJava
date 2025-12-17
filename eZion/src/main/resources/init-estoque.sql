-- ============================================
-- Script de criação das tabelas
-- ============================================

CREATE TABLE IF NOT EXISTS unidade (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    sigla VARCHAR(10) NOT NULL UNIQUE,
    descricao VARCHAR(100) NOT NULL,
    fator DECIMAL(14, 4),
    unidade_pai_id BIGINT,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (unidade_pai_id) REFERENCES unidade(id),
    INDEX idx_sigla (sigla),
    INDEX idx_unidade_pai_id (unidade_pai_id),
    INDEX idx_ativo_unidade (ativo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- Tabelas do módulo CADASTROS
-- ============================================

-- Tabela de cidades
CREATE TABLE IF NOT EXISTS cidade (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    uf VARCHAR(2) NOT NULL,
    codigo_ibge VARCHAR(10),
    observacoes VARCHAR(500),
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    INDEX idx_nome_cidade (nome),
    INDEX idx_uf (uf),
    INDEX idx_codigo_ibge (codigo_ibge),
    INDEX idx_ativo_cidade (ativo),
    UNIQUE KEY uk_cidade_nome_uf (nome, uf)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabela de bairros
CREATE TABLE IF NOT EXISTS bairro (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cidade_id BIGINT NOT NULL,
    cep VARCHAR(10),
    observacoes VARCHAR(500),
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (cidade_id) REFERENCES cidade(id),
    INDEX idx_nome_bairro (nome),
    INDEX idx_cidade_id (cidade_id),
    INDEX idx_ativo_bairro (ativo),
    UNIQUE KEY uk_bairro_nome_cidade (nome, cidade_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS local (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL UNIQUE,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    INDEX idx_nome (nome),
    INDEX idx_ativo_local (ativo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS grupo (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL UNIQUE,
    codigo VARCHAR(50) UNIQUE,
    grupo_pai_id BIGINT,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (grupo_pai_id) REFERENCES grupo(id),
    INDEX idx_nome (nome),
    INDEX idx_codigo (codigo),
    INDEX idx_grupo_pai_id (grupo_pai_id),
    INDEX idx_ativo_grupo (ativo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS estoque_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(50) NOT NULL UNIQUE,
    descricao VARCHAR(200) NOT NULL,
    descricao_detalhada TEXT,
    tipo_item VARCHAR(30) NOT NULL,
    grupo_id BIGINT NOT NULL,
    unidade_id BIGINT NOT NULL,
    local_entrada_padrao_id BIGINT,
    local_saida_padrao_id BIGINT,
    quantidade_minima DECIMAL(15, 4) DEFAULT 0.00,
    estoque_seguranca DECIMAL(15, 4) DEFAULT 0.00,
    quantidade_maxima DECIMAL(15, 4) DEFAULT 0.00,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (unidade_id) REFERENCES unidade(id),
    FOREIGN KEY (grupo_id) REFERENCES grupo(id),
    FOREIGN KEY (local_entrada_padrao_id) REFERENCES local(id),
    FOREIGN KEY (local_saida_padrao_id) REFERENCES local(id),
    INDEX idx_codigo (codigo),
    INDEX idx_tipo_item (tipo_item),
    INDEX idx_grupo_id (grupo_id),
    INDEX idx_unidade_id (unidade_id),
    INDEX idx_local_entrada_padrao_id (local_entrada_padrao_id),
    INDEX idx_local_saida_padrao_id (local_saida_padrao_id),
    INDEX idx_ativo (ativo),
    INDEX idx_descricao (descricao)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS movimentacao (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    estoque_item_id BIGINT NOT NULL,
    tipo_movimentacao VARCHAR(10) NOT NULL,
    quantidade DECIMAL(15, 4) NOT NULL,
    custo DECIMAL(15, 4),
    local_id BIGINT NOT NULL,
    observacoes VARCHAR(255) NOT NULL,
    data_movimentacao TIMESTAMP NOT NULL,
    usuario_id BIGINT NOT NULL,
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (estoque_item_id) REFERENCES estoque_item(id),
    FOREIGN KEY (local_id) REFERENCES local(id),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    INDEX idx_estoque_item_id (estoque_item_id),
    INDEX idx_tipo_movimentacao (tipo_movimentacao),
    INDEX idx_local_id (local_id),
    INDEX idx_usuario_id (usuario_id),
    INDEX idx_custo (custo),
    INDEX idx_data_movimentacao (data_movimentacao)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS lote (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    estoque_item_id BIGINT NOT NULL,
    numero_lote VARCHAR(50) NOT NULL UNIQUE,
    data_entrada DATE NOT NULL,
    data_validade DATE,
    quantidade_total DECIMAL(15, 4) NOT NULL,
    quantidade_disponivel DECIMAL(15, 4) NOT NULL,
    fornecedor VARCHAR(100),
    observacoes VARCHAR(255),
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (estoque_item_id) REFERENCES estoque_item(id),
    INDEX idx_estoque_item_id (estoque_item_id),
    INDEX idx_numero_lote (numero_lote),
    INDEX idx_data_entrada (data_entrada),
    INDEX idx_data_validade (data_validade),
    INDEX idx_ativo_lote (ativo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS estoque_custo (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    estoque_item_id BIGINT NOT NULL,
    lote_id BIGINT,
    tipo_custo VARCHAR(20) NOT NULL,
    valor DECIMAL(15, 4) NOT NULL,
    custo_unitario DECIMAL(15, 4),
    custo_medio_com_frete DECIMAL(15, 4),
    frete_rateado DECIMAL(15, 4),
    custo_medio DECIMAL(15, 4),
    quantidade DECIMAL(15, 4),
    valor_base DECIMAL(15, 4),
    icms DECIMAL(15, 4),
    ipi DECIMAL(15, 4),
    pis DECIMAL(15, 4),
    cofins DECIMAL(15, 4),
    icms_st DECIMAL(15, 4),
    iss DECIMAL(15, 4),
    irpj DECIMAL(15, 4),
    csll DECIMAL(15, 4),
    data_custo TIMESTAMP NOT NULL,
    usuario_id BIGINT NOT NULL,
    descricao VARCHAR(255),
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (estoque_item_id) REFERENCES estoque_item(id),
    FOREIGN KEY (lote_id) REFERENCES lote(id),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    INDEX idx_estoque_item_id_custo (estoque_item_id),
    INDEX idx_lote_id_custo (lote_id),
    INDEX idx_tipo_custo (tipo_custo),
    INDEX idx_usuario_id_custo (usuario_id),
    INDEX idx_data_custo (data_custo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- Dados de exemplo (opcional)
-- ============================================

-- Unidades de medida
INSERT INTO unidade (sigla, descricao, ativo) VALUES
('UN', 'Unidade', TRUE),
('KG', 'Quilograma', TRUE),
('L', 'Litro', TRUE),
('M', 'Metro', TRUE),
('M2', 'Metro Quadrado', TRUE),
('M3', 'Metro Cúbico', TRUE),
('HR', 'Hora', TRUE);

-- Unidades derivadas (exemplos)
INSERT INTO unidade (sigla, descricao, fator, unidade_pai_id, ativo) VALUES
('CX10', 'Caixa com 10 unidades', 10.0000, 1, TRUE),
('CX20', 'Caixa com 20 unidades', 20.0000, 1, TRUE),
('PAL', 'Palete (80 caixas)', 80.0000, (SELECT id FROM unidade WHERE sigla = 'CX20'), TRUE),
('PCT', 'Pacote com 5 unidades', 5.0000, 1, TRUE),
('DZ', 'Dúzia (12 unidades)', 12.0000, 1, TRUE);

-- Locais
INSERT INTO local (nome, ativo) VALUES
('Depósito Principal', TRUE),
('Almoxarifado', TRUE),
('Sala de Produção', TRUE),
('Escritório Administrativo', TRUE),
('Loja de Vendas', TRUE);

-- Grupos (grupos raiz)
INSERT INTO grupo (nome, codigo, ativo) VALUES
('Eletrônicos', 'ELET', TRUE),
('Ferramentas', 'FERR', TRUE),
('Materiais de Construção', 'MATC', TRUE),
('Produtos de Limpeza', 'LIMPEZA', TRUE),
('Escritório', 'ESCRIT', TRUE);

-- Subgrupos (grupos filhos)
INSERT INTO grupo (nome, codigo, grupo_pai_id, ativo) VALUES
('Computadores', 'ELET_COMP', 1, TRUE),
('Periféricos', 'ELET_PER', 1, TRUE),
('Ferramentas Manuais', 'FERR_MAN', 2, TRUE),
('Ferramentas Elétricas', 'FERR_ELET', 2, TRUE);

-- Produtos
INSERT INTO estoque_item (codigo, descricao, descricao_detalhada, tipo_item, grupo_id, unidade_id, quantidade_minima, quantidade_maxima, ativo) 
VALUES 
('PROD-001', 'Produto Acabado A', 'Produto acabado tipo A com embalagem padrão', 'PRODUTO', 1, 1, 20.00, 500.00, TRUE),
('PROD-002', 'Produto Acabado B', 'Produto acabado tipo B com embalagem premium', 'PRODUTO', 1, 1, 15.00, 300.00, TRUE);

-- Produtos em Elaboração
INSERT INTO estoque_item (codigo, descricao, descricao_detalhada, tipo_item, grupo_id, unidade_id, quantidade_minima, quantidade_maxima, ativo) 
VALUES 
('PE-001', 'Produto em Elaboração X', 'Produto em processo de fabricação', 'PRODUTO_EM_ELABORACAO', 1, 1, 5.00, 100.00, TRUE);

-- Insumos
INSERT INTO estoque_item (codigo, descricao, descricao_detalhada, tipo_item, unidade_id, quantidade_minima, quantidade_maxima, ativo) 
VALUES 
('INS-001', 'Matéria Prima A', 'Matéria prima principal para produção', 'INSUMO', 2, 100.00, 2000.00, TRUE),
('INS-002', 'Matéria Prima B', 'Matéria prima secundária', 'INSUMO', 2, 50.00, 1000.00, TRUE);

-- Material de Consumo
INSERT INTO estoque_item (codigo, descricao, descricao_detalhada, tipo_item, grupo_id, unidade_id, quantidade_minima, quantidade_maxima, ativo) 
VALUES 
('CONS-001', 'Material de Escritório', 'Diversos materiais de escritório', 'CONSUMO', 5, 1, 30.00, 500.00, TRUE),
('CONS-002', 'Material de Limpeza', 'Produtos de limpeza diversos', 'CONSUMO', 4, 1, 50.00, 800.00, TRUE);

-- Imobilizado
INSERT INTO estoque_item (codigo, descricao, descricao_detalhada, tipo_item, grupo_id, unidade_id, quantidade_minima, quantidade_maxima, ativo) 
VALUES 
('IMOB-001', 'Equipamento Industrial A', 'Equipamento para linha de produção', 'IMOBILIZADO', 1, 1, 1.00, 10.00, TRUE),
('IMOB-002', 'Veículo de Transporte', 'Veículo para transporte de mercadorias', 'IMOBILIZADO', 1, 1, 1.00, 5.00, TRUE);

-- Lotes de exemplo
INSERT INTO lote (estoque_item_id, numero_lote, data_entrada, data_validade, quantidade_total, quantidade_disponivel, fornecedor, observacoes) VALUES
(1, 'LOTE-2025-001', '2025-01-10', '2025-12-31', 100.00, 75.00, 'Fornecedor A', 'Primeira remessa'),
(1, 'LOTE-2025-002', '2025-01-20', '2025-12-25', 50.00, 50.00, 'Fornecedor A', 'Segunda remessa'),
(2, 'LOTE-2025-003', '2025-01-15', '2025-06-30', 200.00, 175.00, 'Fornecedor B', 'Matéria-prima'),
(4, 'LOTE-2025-004', '2025-01-12', '2025-11-30', 75.00, 75.00, 'Fornecedor C', 'Material consumo'),
(5, 'LOTE-2025-005', '2025-01-18', '2025-10-15', 30.00, 30.00, 'Fornecedor D', 'Limpeza');

-- ============================================
-- Movimentações de exemplo
INSERT INTO movimentacao (estoque_item_id, lote_id, tipo_movimentacao, quantidade, custo, local_id, observacoes, data_movimentacao, usuario_id) VALUES
(1, 1, 'ENTRADA', 100.00, 5000.00, 1, 'Recebimento de compra inicial', '2025-01-15 08:00:00', 1),
(1, 1, 'SAIDA', 25.00, 1250.00, 2, 'Venda para cliente', '2025-01-20 14:30:00', 1),
(2, 3, 'ENTRADA', 200.00, 8000.00, 1, 'Entrada de matéria-prima', '2025-01-22 09:00:00', 1),
(4, 4, 'ENTRADA', 75.00, 2250.00, 1, 'Compra de material de consumo', '2025-01-25 11:20:00', 1),
(5, 5, 'ENTRADA', 30.00, 450.00, 1, 'Compra de material de limpeza', '2025-01-28 13:45:00', 1);

-- ============================================
-- Custos de exemplo
INSERT INTO estoque_custo (estoque_item_id, lote_id, tipo_custo, valor, custo_unitario, custo_medio_com_frete, frete_rateado, custo_medio, quantidade, valor_base, icms, ipi, pis, cofins, data_custo, usuario_id, descricao) VALUES
(1, 1, 'COMPRA', 5000.00, 50.00, 52.50, 0.00, 50.00, 100.00, 4500.00, 450.00, 250.00, 45.00, 105.00, '2025-01-15 08:00:00', 1, 'Custo de compra do lote LT-001'),
(1, 1, 'TRANSPORTE', 150.00, 1.50, 1.50, 150.00, 1.50, 100.00, 150.00, 0.00, 0.00, 0.00, 0.00, '2025-01-15 09:00:00', 1, 'Frete e transporte'),
(2, 3, 'COMPRA', 8000.00, 40.00, 42.00, 0.00, 40.00, 200.00, 7200.00, 720.00, 400.00, 72.00, 168.00, '2025-01-22 09:00:00', 1, 'Custo de compra da matéria-prima'),
(2, 3, 'ARMAZENAGEM', 200.00, 1.00, 1.00, 0.00, 1.00, 200.00, 200.00, 0.00, 0.00, 0.00, 0.00, '2025-01-22 10:00:00', 1, 'Custos de armazenagem mensal'),
(4, 4, 'COMPRA', 2250.00, 30.00, 31.50, 0.00, 30.00, 75.00, 2025.00, 202.50, 112.50, 20.25, 47.25, '2025-01-25 11:20:00', 1, 'Custo de material de consumo'),
(5, 5, 'COMPRA', 450.00, 15.00, 15.75, 0.00, 15.00, 30.00, 405.00, 40.50, 22.50, 4.05, 9.45, '2025-01-28 13:45:00', 1, 'Custo de produtos de limpeza');

-- Serviços
INSERT INTO estoque_item (codigo, descricao, descricao_detalhada, tipo_item, unidade_id, quantidade_minima, quantidade_maxima, ativo) 
VALUES 
('SERV-001', 'Serviço de Manutenção', 'Serviço de manutenção preventiva', 'SERVICO', 7, 0.00, 0.00, TRUE),
('SERV-002', 'Consultoria Técnica', 'Serviço de consultoria técnica especializada', 'SERVICO', 7, 0.00, 0.00, TRUE);

-- ============================================
-- Tabelas do módulo COMPRAS
-- ============================================

-- Tabela de fornecedores
CREATE TABLE IF NOT EXISTS fornecedor (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(20) NOT NULL UNIQUE,
    razao_social VARCHAR(100) NOT NULL,
    nome_fantasia VARCHAR(100),
    cnpj VARCHAR(18) NOT NULL UNIQUE,
    email VARCHAR(100),
    telefone VARCHAR(20),
    endereco VARCHAR(200),
    cidade_id BIGINT NOT NULL,
    bairro_id BIGINT,
    cep VARCHAR(10),
    observacoes VARCHAR(500),
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (cidade_id) REFERENCES cidade(id),
    FOREIGN KEY (bairro_id) REFERENCES bairro(id),
    INDEX idx_codigo_fornecedor (codigo),
    INDEX idx_cnpj_fornecedor (cnpj),
    INDEX idx_cidade_id_fornecedor (cidade_id),
    INDEX idx_bairro_id_fornecedor (bairro_id),
    INDEX idx_ativo_fornecedor (ativo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabela de pedidos de compra
CREATE TABLE IF NOT EXISTS pedido_compra (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero_pedido VARCHAR(50) NOT NULL UNIQUE,
    data_pedido DATE NOT NULL,
    data_prevista_entrega DATE,
    data_entrega DATE,
    fornecedor_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDENTE',
    valor_total DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
    observacoes VARCHAR(500),
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (fornecedor_id) REFERENCES fornecedor(id),
    INDEX idx_numero_pedido (numero_pedido),
    INDEX idx_fornecedor_id (fornecedor_id),
    INDEX idx_status_pedido (status),
    INDEX idx_data_pedido (data_pedido),
    INDEX idx_data_prevista_entrega (data_prevista_entrega),
    INDEX idx_ativo_pedido (ativo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabela de itens do pedido de compra
CREATE TABLE IF NOT EXISTS item_pedido_compra (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pedido_compra_id BIGINT NOT NULL,
    estoque_item_id BIGINT NOT NULL,
    quantidade DECIMAL(15, 4) NOT NULL,
    valor_unitario DECIMAL(12, 4) NOT NULL DEFAULT 0.00,
    valor_total DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
    quantidade_recebida DECIMAL(15, 4) NOT NULL DEFAULT 0.00,
    observacoes VARCHAR(255),

    FOREIGN KEY (pedido_compra_id) REFERENCES pedido_compra(id),
    FOREIGN KEY (estoque_item_id) REFERENCES estoque_item(id),
    INDEX idx_pedido_compra_id (pedido_compra_id),
    INDEX idx_estoque_item_id_item_pedido (estoque_item_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- Dados de exemplo - COMPRAS
-- ============================================

-- Fornecedores de exemplo
INSERT INTO fornecedor (codigo, razao_social, nome_fantasia, cnpj, email, telefone, endereco, cidade_id, bairro_id, cep, observacoes) VALUES
('FORN-001', 'Distribuidora ABC Ltda', 'ABC Distribuidora', '12.345.678/0001-90', 'contato@abcdistribuidora.com.br', '(11) 3456-7890', 'Rua das Flores, 123', 1, 2, '01234-567', 'Fornecedor principal de matéria-prima'),
('FORN-002', 'Tech Solutions S.A.', 'Tech Solutions', '98.765.432/0001-10', 'vendas@techsolutions.com.br', '(21) 9876-5432', 'Av. Tecnológica, 456', 2, 7, '20000-000', 'Fornecedor de equipamentos tecnológicos'),
('FORN-003', 'Materiais Industriais XYZ', 'Materiais XYZ', '55.444.333/0001-22', 'compras@materiaisxyz.com.br', '(31) 5555-1234', 'Rua Industrial, 789', 3, 11, '30000-000', 'Especialista em materiais de consumo');

-- Pedidos de compra de exemplo
INSERT INTO pedido_compra (numero_pedido, data_pedido, data_prevista_entrega, fornecedor_id, status, valor_total, observacoes) VALUES
('PC-2025-001', '2025-01-10', '2025-01-25', 1, 'APROVADO', 15000.00, 'Pedido urgente de matéria-prima'),
('PC-2025-002', '2025-01-15', '2025-02-10', 2, 'PENDENTE', 25000.00, 'Compra de equipamentos'),
('PC-2025-003', '2025-01-20', '2025-02-05', 3, 'ENVIADO', 8000.00, 'Materiais de consumo mensal');

-- Itens dos pedidos de compra
INSERT INTO item_pedido_compra (pedido_compra_id, estoque_item_id, quantidade, valor_unitario, valor_total, quantidade_recebida, observacoes) VALUES
(1, 2, 100.00, 50.00, 5000.00, 100.00, 'Matéria-prima recebida completamente'),
(1, 4, 200.00, 25.00, 5000.00, 150.00, 'Material de consumo - recebimento parcial'),
(1, 5, 50.00, 20.00, 1000.00, 0.00, 'Material de limpeza - pendente'),
(2, 1, 5.00, 5000.00, 25000.00, 0.00, 'Equipamento industrial - aguardando entrega'),
(3, 4, 100.00, 30.00, 3000.00, 100.00, 'Material consumo - recebido'),
(3, 5, 100.00, 50.00, 5000.00, 80.00, 'Material limpeza - recebimento parcial');

-- ============================================
-- Dados de exemplo - CADASTROS
-- ============================================

-- Cidades de exemplo
INSERT INTO cidade (nome, uf, codigo_ibge, observacoes) VALUES
('São Paulo', 'SP', '3550308', 'Capital do estado de São Paulo'),
('Rio de Janeiro', 'RJ', '3304557', 'Capital do estado do Rio de Janeiro'),
('Belo Horizonte', 'MG', '3106200', 'Capital do estado de Minas Gerais'),
('Salvador', 'BA', '2927408', 'Capital do estado da Bahia'),
('Brasília', 'DF', '5300108', 'Capital Federal'),
('Curitiba', 'PR', '4106902', 'Capital do estado do Paraná'),
('Porto Alegre', 'RS', '4314902', 'Capital do estado do Rio Grande do Sul'),
('Recife', 'PE', '2611606', 'Capital do estado de Pernambuco'),
('Fortaleza', 'CE', '2304400', 'Capital do estado do Ceará'),
('Manaus', 'AM', '1302603', 'Capital do estado do Amazonas');

-- Bairros de exemplo
INSERT INTO bairro (nome, cidade_id, cep, observacoes) VALUES
-- São Paulo (id=1)
('Centro', 1, '01000-000', 'Centro histórico de São Paulo'),
('Jardins', 1, '01400-000', 'Bairro nobre de São Paulo'),
('Pinheiros', 1, '05400-000', 'Bairro moderno com vida noturna'),
('Vila Madalena', 1, '05400-000', 'Bairro boêmio e cultural'),
('Moema', 1, '04000-000', 'Bairro residencial de alto padrão'),

-- Rio de Janeiro (id=2)
('Copacabana', 2, '22000-000', 'Famoso bairro à beira-mar'),
('Ipanema', 2, '22400-000', 'Bairro elegante e sofisticado'),
('Leblon', 2, '22400-000', 'Bairro de alto padrão'),
('Botafogo', 2, '22200-000', 'Bairro tradicional e comercial'),
('Flamengo', 2, '22200-000', 'Bairro histórico com vista para a baía'),

-- Belo Horizonte (id=3)
('Centro', 3, '30100-000', 'Centro financeiro e comercial'),
('Savassi', 3, '30100-000', 'Bairro nobre e cultural'),
('Lourdes', 3, '30100-000', 'Bairro residencial de classe média alta'),
('Funcionários', 3, '30100-000', 'Bairro residencial tradicional'),
('Pampulha', 3, '31200-000', 'Bairro moderno com lago'),

-- Salvador (id=4)
('Centro Histórico', 4, '40000-000', 'Centro histórico e turístico'),
('Barra', 4, '40100-000', 'Bairro nobre à beira-mar'),
('Rio Vermelho', 4, '41900-000', 'Bairro boêmio e cultural'),
('Pituba', 4, '41800-000', 'Bairro residencial de classe média alta'),
('Caminho das Árvores', 4, '41800-000', 'Bairro residencial planejado'),

-- Brasília (id=5)
('Asa Norte', 5, '70700-000', 'Setor administrativo e comercial'),
('Asa Sul', 5, '70300-000', 'Setor administrativo e residencial'),
('Lago Norte', 5, '71500-000', 'Bairro residencial de alto padrão'),
('Lago Sul', 5, '71600-000', 'Bairro residencial de luxo'),
('Sudoeste', 5, '70600-000', 'Setor misto residencial e comercial');

-- ============================================
-- Fim do script
-- ============================================
