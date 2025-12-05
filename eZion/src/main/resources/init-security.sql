-- Script de Inicialização do Módulo de Segurança
-- Cria permissões, roles e usuário admin padrão

-- ============================================
-- 1. INSERIR PERMISSÕES
-- ============================================

INSERT INTO permissoes (nome, descricao, criado_em) VALUES
  ('USUARIO_VIEW', 'Visualizar usuários', NOW()),
  ('USUARIO_CREATE', 'Criar usuários', NOW()),
  ('USUARIO_UPDATE', 'Atualizar usuários', NOW()),
  ('USUARIO_DELETE', 'Deletar usuários', NOW()),
  ('ROLE_VIEW', 'Visualizar roles', NOW()),
  ('ROLE_CREATE', 'Criar roles', NOW()),
  ('ROLE_UPDATE', 'Atualizar roles', NOW()),
  ('ROLE_DELETE', 'Deletar roles', NOW()),
  ('PERMISSAO_VIEW', 'Visualizar permissões', NOW()),
  ('PERMISSAO_CREATE', 'Criar permissões', NOW()),
  ('PERMISSAO_UPDATE', 'Atualizar permissões', NOW()),
  ('PERMISSAO_DELETE', 'Deletar permissões', NOW());

-- ============================================
-- 2. INSERIR ROLES
-- ============================================

INSERT INTO roles (nome, descricao, ativo, criado_em, atualizado_em) VALUES
  ('ADMIN', 'Administrador do sistema', true, NOW(), NOW()),
  ('GERENTE', 'Gerente de estoque', true, NOW(), NOW()),
  ('OPERADOR', 'Operador de estoque', true, NOW(), NOW()),
  ('CONSULTOR', 'Consultor apenas leitura', true, NOW(), NOW());

-- ============================================
-- 3. ASSOCIAR PERMISSÕES ÀS ROLES
-- ============================================

-- ADMIN: Todas as permissões
INSERT INTO role_permissoes (role_id, permissao_id)
  SELECT 1, id FROM permissoes;

-- GERENTE: Visualizar e criar/atualizar (sem deletar)
INSERT INTO role_permissoes (role_id, permissao_id)
  SELECT 2, id FROM permissoes 
  WHERE nome IN ('USUARIO_VIEW', 'USUARIO_CREATE', 'USUARIO_UPDATE',
                 'ROLE_VIEW', 'ROLE_CREATE', 'ROLE_UPDATE',
                 'PERMISSAO_VIEW');

-- OPERADOR: Apenas visualizar
INSERT INTO role_permissoes (role_id, permissao_id)
  SELECT 3, id FROM permissoes 
  WHERE nome IN ('USUARIO_VIEW', 'ROLE_VIEW', 'PERMISSAO_VIEW');

-- CONSULTOR: Apenas visualizar
INSERT INTO role_permissoes (role_id, permissao_id)
  SELECT 4, id FROM permissoes 
  WHERE nome IN ('USUARIO_VIEW', 'ROLE_VIEW', 'PERMISSAO_VIEW');

-- ============================================
-- 4. CRIAR USUÁRIO ADMIN
-- ============================================

-- Nota: A senha é "admin" codificada em BCrypt
-- Hash: $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36P4/F1.

INSERT INTO usuarios (username, email, senha, nome_completo, ativo, bloqueado, criado_em, atualizado_em)
VALUES ('admin', 'admin@ezion.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36P4/F1.', 'Administrador', true, false, NOW(), NOW());

-- ============================================
-- 5. ASSOCIAR ADMIN AO USUÁRIO ADMIN
-- ============================================

INSERT INTO usuario_roles (usuario_id, role_id)
SELECT id, 1 FROM usuarios WHERE username = 'admin';

-- ============================================
-- FIM DO SCRIPT
-- ============================================

-- Usuários de teste criados:
-- - username: admin
-- - password: admin
-- - email: admin@ezion.com
-- - role: ADMIN (com todas as permissões)
