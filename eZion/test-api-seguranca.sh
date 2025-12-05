#!/bin/bash

# Script de Testes da API de Segurança
# Testando todos os endpoints do módulo de segurança

BASE_URL="http://localhost:8080"
ADMIN_TOKEN=""
USER_TOKEN=""

# Cores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Função para exibir separador
separator() {
  echo -e "${BLUE}========================================${NC}"
}

# Função para exibir teste
test_endpoint() {
  echo -e "${YELLOW}[$1]${NC}"
  echo "Endpoint: $2"
}

# Função para salvar token
save_token() {
  ADMIN_TOKEN=$(echo $1 | jq -r '.token')
  REFRESH_TOKEN=$(echo $1 | jq -r '.refreshToken')
  echo -e "${GREEN}✓ Token obtido${NC}"
  echo "Access Token: ${ADMIN_TOKEN:0:50}..."
}

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}  TESTES DA API DE SEGURANÇA - eZion${NC}"
echo -e "${BLUE}========================================${NC}"

# ============================================
# 1. TESTE DE REGISTRO
# ============================================
separator
test_endpoint "1. Registrar novo usuário" "POST /api/auth/register"

REGISTER_RESPONSE=$(curl -s -X POST "$BASE_URL/api/auth/register" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "testuser@example.com",
    "senha": "teste123",
    "nomeCompleto": "Usuário Teste"
  }')

echo "$REGISTER_RESPONSE" | jq '.'
echo ""

# ============================================
# 2. TESTE DE LOGIN
# ============================================
separator
test_endpoint "2. Login do usuário" "POST /api/auth/login"

LOGIN_RESPONSE=$(curl -s -X POST "$BASE_URL/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }')

echo "$LOGIN_RESPONSE" | jq '.'
save_token "$LOGIN_RESPONSE"
echo ""

# ============================================
# 3. TESTE DE OBTER USUÁRIO ATUAL
# ============================================
separator
test_endpoint "3. Obter usuário atual" "GET /api/auth/me"

curl -s -X GET "$BASE_URL/api/auth/me" \
  -H "Authorization: Bearer $ADMIN_TOKEN" | jq '.'
echo ""

# ============================================
# 4. TESTE DE LISTAR USUÁRIOS
# ============================================
separator
test_endpoint "4. Listar todos os usuários" "GET /api/usuarios"

curl -s -X GET "$BASE_URL/api/usuarios" \
  -H "Authorization: Bearer $ADMIN_TOKEN" | jq '.'
echo ""

# ============================================
# 5. TESTE DE LISTAR ROLES
# ============================================
separator
test_endpoint "5. Listar todas as roles" "GET /api/roles"

curl -s -X GET "$BASE_URL/api/roles" \
  -H "Authorization: Bearer $ADMIN_TOKEN" | jq '.'
echo ""

# ============================================
# 6. TESTE DE LISTAR PERMISSÕES
# ============================================
separator
test_endpoint "6. Listar todas as permissões" "GET /api/permissoes"

curl -s -X GET "$BASE_URL/api/permissoes" \
  -H "Authorization: Bearer $ADMIN_TOKEN" | jq '.'
echo ""

# ============================================
# 7. TESTE DE CRIAR PERMISSÃO
# ============================================
separator
test_endpoint "7. Criar nova permissão" "POST /api/permissoes"

curl -s -X POST "$BASE_URL/api/permissoes" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -d '{
    "nome": "ITEM_VIEW",
    "descricao": "Visualizar itens de estoque"
  }' | jq '.'
echo ""

# ============================================
# 8. TESTE DE CRIAR ROLE
# ============================================
separator
test_endpoint "8. Criar nova role" "POST /api/roles"

NEW_ROLE=$(curl -s -X POST "$BASE_URL/api/roles" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -d '{
    "nome": "SUPERVISOR",
    "descricao": "Supervisor de estoque"
  }')

echo "$NEW_ROLE" | jq '.'
ROLE_ID=$(echo "$NEW_ROLE" | jq -r '.id')
echo ""

# ============================================
# 9. TESTE DE REFRESH TOKEN
# ============================================
separator
test_endpoint "9. Renovar token (refresh)" "POST /api/auth/refresh"

REFRESH_RESPONSE=$(curl -s -X POST "$BASE_URL/api/auth/refresh" \
  -H "Content-Type: application/json" \
  -d "{
    \"refreshToken\": \"$REFRESH_TOKEN\"
  }")

echo "$REFRESH_RESPONSE" | jq '.'
NEW_TOKEN=$(echo "$REFRESH_RESPONSE" | jq -r '.token')
echo ""

# ============================================
# 10. TESTE DE ATUALIZAR USUÁRIO
# ============================================
separator
test_endpoint "10. Atualizar usuário" "PUT /api/usuarios/{id}"

curl -s -X PUT "$BASE_URL/api/usuarios/1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -d '{
    "email": "newemail@example.com",
    "nomeCompleto": "Novo Nome Admin"
  }' | jq '.'
echo ""

# ============================================
# 11. TESTE SEM AUTENTICAÇÃO (deve falhar)
# ============================================
separator
test_endpoint "11. Teste de acesso sem autenticação (deve falhar)" "GET /api/usuarios"

curl -s -X GET "$BASE_URL/api/usuarios" | jq '.'
echo ""

# ============================================
# 12. TESTE COM TOKEN INVÁLIDO (deve falhar)
# ============================================
separator
test_endpoint "12. Teste com token inválido (deve falhar)" "GET /api/usuarios"

curl -s -X GET "$BASE_URL/api/usuarios" \
  -H "Authorization: Bearer invalid_token_12345" | jq '.'
echo ""

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}  TESTES CONCLUÍDOS${NC}"
echo -e "${GREEN}========================================${NC}"

# ============================================
# RESUMO DE ENDPOINTS
# ============================================
echo ""
echo -e "${BLUE}ENDPOINTS DA API:${NC}"
echo ""
echo -e "${YELLOW}Autenticação:${NC}"
echo "  POST   /api/auth/login"
echo "  POST   /api/auth/register"
echo "  POST   /api/auth/refresh"
echo "  GET    /api/auth/me"
echo ""
echo -e "${YELLOW}Usuários:${NC}"
echo "  GET    /api/usuarios"
echo "  GET    /api/usuarios/ativos"
echo "  GET    /api/usuarios/{id}"
echo "  PUT    /api/usuarios/{id}"
echo "  POST   /api/usuarios/{id}/alterar-senha"
echo "  PUT    /api/usuarios/{id}/ativar"
echo "  PUT    /api/usuarios/{id}/desativar"
echo "  PUT    /api/usuarios/{id}/bloquear"
echo "  PUT    /api/usuarios/{id}/desbloquear"
echo "  DELETE /api/usuarios/{id}"
echo ""
echo -e "${YELLOW}Roles:${NC}"
echo "  GET    /api/roles"
echo "  GET    /api/roles/ativas"
echo "  GET    /api/roles/{id}"
echo "  POST   /api/roles"
echo "  PUT    /api/roles/{id}"
echo "  POST   /api/roles/{roleId}/permissoes/{permissaoId}"
echo "  DELETE /api/roles/{roleId}/permissoes/{permissaoId}"
echo "  PUT    /api/roles/{id}/ativar"
echo "  PUT    /api/roles/{id}/desativar"
echo "  DELETE /api/roles/{id}"
echo ""
echo -e "${YELLOW}Permissões:${NC}"
echo "  GET    /api/permissoes"
echo "  GET    /api/permissoes/{id}"
echo "  POST   /api/permissoes"
echo "  PUT    /api/permissoes/{id}"
echo "  DELETE /api/permissoes/{id}"
echo ""
