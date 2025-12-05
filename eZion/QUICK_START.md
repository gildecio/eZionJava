# 🚀 INÍCIO RÁPIDO - Módulo de Segurança

## ⚡ Quick Start (5 minutos)

### 1. Pré-requisitos
```bash
# Verificar Java 17+
java -version

# Verificar Maven 3.6+
mvn -version

# PostgreSQL 12+ rodando na porta 5432
# Database: eZionDB
# User: postgres
# Password: 123
```

### 2. Compilar o Projeto
```bash
cd /home/gildecio/projetos/eZionJava/eZion
mvn clean compile -q
echo "✅ Compilação bem-sucedida!"
```

### 3. Executar a Aplicação
```bash
mvn spring-boot:run
```

A aplicação estará disponível em: **http://localhost:8080**

### 4. Testar Login
```bash
# Terminal 1: Aplicação rodando

# Terminal 2: Fazer login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'

# Resposta esperada:
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "refreshToken": "eyJhbGciOiJIUzUxMiJ9...",
  "type": "Bearer",
  "id": 1,
  "username": "admin",
  "email": "admin@example.com",
  "nomeCompleto": "Administrador",
  "roles": ["ADMIN"],
  "permissoes": [
    "USUARIO_VIEW",
    "USUARIO_CREATE",
    ...
  ]
}
```

### 5. Usar o Token
```bash
# Copie o token da resposta anterior

TOKEN="eyJhbGciOiJIUzUxMiJ9..."

# Fazer requisição autenticada
curl -X GET http://localhost:8080/api/usuarios \
  -H "Authorization: Bearer $TOKEN"

# Resposta esperada: Lista de usuários
[
  {
    "id": 1,
    "username": "admin",
    "email": "admin@example.com",
    "nomeCompleto": "Administrador",
    "ativo": true,
    "bloqueado": false
  }
]
```

---

## 📋 Principais Endpoints

### Autenticação (Públicos)
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/auth/login` | Fazer login |
| POST | `/api/auth/register` | Registrar novo usuário |
| POST | `/api/auth/refresh` | Renovar token |
| GET | `/api/auth/me` | Dados do usuário autenticado |

### Usuários (Requer autenticação)
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/usuarios` | Listar todos |
| GET | `/api/usuarios/{id}` | Obter por ID |
| PUT | `/api/usuarios/{id}` | Atualizar |
| DELETE | `/api/usuarios/{id}` | Deletar |

### Roles (Requer autenticação)
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/roles` | Listar todos |
| POST | `/api/roles` | Criar novo |
| PUT | `/api/roles/{id}` | Atualizar |
| DELETE | `/api/roles/{id}` | Deletar |

### Permissões (Requer autenticação)
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/permissoes` | Listar todos |
| POST | `/api/permissoes` | Criar novo |
| PUT | `/api/permissoes/{id}` | Atualizar |
| DELETE | `/api/permissoes/{id}` | Deletar |

---

## 🔓 Credentials Padrão

```
Username: admin
Password: admin123
Email: admin@example.com
```

**⚠️ IMPORTANTE**: Mude a senha em produção!

---

## 📊 Exemplos de Uso

### Registrar novo usuário
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "joao",
    "email": "joao@example.com",
    "senha": "senha123",
    "nomeCompleto": "João Silva"
  }'
```

### Criar nova permissão
```bash
curl -X POST http://localhost:8080/api/permissoes \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "nome": "ITEM_DELETE",
    "descricao": "Deletar itens de estoque"
  }'
```

### Criar nova role
```bash
curl -X POST http://localhost:8080/api/roles \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "nome": "SUPERVISOR",
    "descricao": "Supervisor de estoque"
  }'
```

### Adicionar permissão a uma role
```bash
# Assumindo: roleId=2, permissaoId=1
curl -X POST http://localhost:8080/api/roles/2/permissoes/1 \
  -H "Authorization: Bearer $TOKEN"
```

---

## 🧪 Script de Testes Automático

Execute o script de testes:
```bash
chmod +x test-api-seguranca.sh
./test-api-seguranca.sh
```

Este script testa automaticamente:
- Login
- Registro
- Obter usuário
- Listar usuários
- Criar roles e permissões
- Refresh token
- E muito mais!

---

## 🛠️ Solução de Problemas

### Erro: "Connection refused"
```
Solução: Verifique se PostgreSQL está rodando
sudo systemctl start postgresql
```

### Erro: "Database does not exist"
```
Solução: Crie o banco de dados
psql -U postgres -c "CREATE DATABASE eZionDB;"
```

### Erro: "Invalid JWT"
```
Solução: Token expirado, faça login novamente
```

### Erro: "Access denied"
```
Solução: Sua permissão não permite esta ação
Peça a um admin para adicionar permissões
```

---

## 📚 Documentação Completa

- **MODULO_SEGURANCA.md** - Todos os endpoints com exemplos
- **README_SEGURANCA.md** - Arquitetura e componentes
- **ARQUITETURA_SEGURANCA.md** - Diagramas visuais
- **SUMARIO_SEGURANCA.md** - Resumo da implementação
- **CHECKLIST_SEGURANCA.md** - Checklist completo

---

## 🔐 Configurações de Segurança

### JWT
- **Expiração**: 24 horas
- **Refresh Token**: 7 dias
- **Algoritmo**: HS512
- **Secret**: Configurado em `application.properties`

### Senhas
- **Algoritmo**: BCrypt
- **Iterações**: 10
- **Seguro**: Não recuperável

### CORS
- **Origins**: localhost:3000, 4200, 8080
- **Métodos**: GET, POST, PUT, DELETE, OPTIONS

---

## 🌟 Funcionalidades

✅ Login/Logout  
✅ Registro de usuários  
✅ Refresh automático de token  
✅ Controle de acesso por roles  
✅ Permissões granulares  
✅ Gerenciamento de usuários  
✅ Ativação/desativação de contas  
✅ Bloqueio de usuários  
✅ Alteração de senha  
✅ Registro de último acesso  

---

## 📞 Suporte

Consulte a documentação completa em:
- `MODULO_SEGURANCA.md`
- `ARQUITETURA_SEGURANCA.md`

---

## ✨ Próximos Passos

1. ✅ Módulo de Segurança implementado
2. 🔄 Integrar com endpoints de estoque
3. 📊 Adicionar auditoria
4. 🧪 Testes unitários
5. 🚀 Deploy em produção

---

*Bem-vindo ao módulo de segurança eZion!*  
*Happy coding! 🚀*
