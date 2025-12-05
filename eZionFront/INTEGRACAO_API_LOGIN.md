# Integração Login com API

## 🔐 Como Funciona o Login com API

### 1. Fluxo de Autenticação

```
Frontend (Login)
    ↓
    [POST /api/auth/login { usuario, senha }]
    ↓
Backend (API)
    ↓
    [Validar credenciais]
    ↓
    [Resposta: { token, usuario }]
    ↓
Frontend (Store Token)
    ↓
[Salvar token em localStorage]
    ↓
[Redirecionar para Dashboard]
```

### 2. Componentes Implementados

#### **AuthService** (`src/app/services/auth.ts`)
- Responsável pela comunicação com a API
- Armazena token JWT em localStorage
- Gerencia estado do usuário com RxJS Observable
- Métodos: `login()`, `logout()`, `isAuthenticated()`, `getToken()`

#### **JwtInterceptor** (`src/app/interceptors/jwt.interceptor.ts`)
- Intercepta todas as requisições HTTP
- Adiciona header `Authorization: Bearer {token}` automaticamente
- Trata erros 401/403 (token expirado)
- Redireciona para login se necessário

#### **AuthGuard** (`src/app/guards/auth.guard.ts`)
- Protege rotas autenticadas
- Verifica se usuário está logado
- Redireciona para login se não autenticado

### 3. Requisição de Login

**Endpoint:**
```
POST http://localhost:8080/api/auth/login
```

**Payload:**
```json
{
  "usuario": "admin@ezion.com",
  "senha": "admin123456"
}
```

**Resposta (Sucesso 200):**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "usuario": {
    "id": 1,
    "usuario": "admin@ezion.com",
    "nome": "Administrador",
    "email": "admin@ezion.com",
    "roles": ["ADMIN"]
  }
}
```

**Resposta (Erro 401):**
```json
{
  "message": "Usuário ou senha inválidos",
  "status": 401
}
```

### 4. Fluxo de Tokens

#### Após Login Bem-sucedido:
```
1. Token JWT armazenado em localStorage
   localStorage.setItem('auth_token', token)

2. Usuário armazenado em localStorage
   localStorage.setItem('usuario', JSON.stringify(usuario))

3. Usuário é notificado via Observable
   this.currentUserSubject.next(usuario)

4. Redireciona para /dashboard
```

#### Em Requisições Subsequentes:
```
1. JwtInterceptor captura a requisição
2. Adiciona header Authorization
   Authorization: Bearer {token}
3. Envia requisição com token
4. Se resposta for 401, faz logout automático
```

### 5. Tratamento de Erros

O login trata diferentes tipos de erro:

| Erro | Mensagem |
|------|----------|
| 401 | "Usuário ou senha inválidos" |
| 400 | Mensagem do servidor ou "Dados inválidos" |
| 0 | "Não foi possível conectar ao servidor..." |
| Outro | Mensagem do servidor ou "Erro ao fazer login" |

### 6. Segurança Implementada

✅ **JWT Bearer Token**
- Padrão de autenticação HTTP
- Token é adicionado em Authorization header
- Válido para todas as requisições autenticadas

✅ **Armazenamento Seguro**
- Token em localStorage
- Usuário em localStorage
- Limpos ao fazer logout

✅ **Proteção de Rotas**
- AuthGuard impede acesso a rotas sem autenticação
- Dashboard protegida por canActivate
- Login redirecionado para dashboard se já autenticado

✅ **Interceptação Automática**
- JwtInterceptor adiciona token a todas as requisições
- Detecta tokens expirados (401)
- Faz logout automático se token inválido

### 7. Configuração da API

Edite `src/app/config/api.config.ts`:

```typescript
export const API_CONFIG = {
  baseUrl: 'http://localhost:8080/api',
  endpoints: {
    auth: '/auth',
    usuarios: '/usuarios',
    roles: '/roles',
    permissoes: '/permissoes',
    empresas: '/empresas'
  }
};
```

### 8. Testando o Login

**Credenciais Padrão (Backend):**
```
Usuário: admin@ezion.com
Senha:   admin123456
```

**Passos para testar:**

1. Certifique-se que o backend está rodando:
   ```bash
   cd /home/gildecio/projetos/eZionJava/eZion
   mvn spring-boot:run
   ```

2. Inicie o frontend:
   ```bash
   cd /home/gildecio/projetos/eZionJava/eZionFront/ezion-app
   npm start
   ```

3. Abra http://localhost:4200

4. Digite credenciais e clique em "Entrar"

5. Verifique no console do navegador:
   - Network: POST /api/auth/login
   - LocalStorage: auth_token deve existir
   - URL deve ser http://localhost:4200/dashboard

### 9. Debugging

**Verificar Token no Console:**
```javascript
localStorage.getItem('auth_token')
localStorage.getItem('usuario')
```

**Verificar Interceptador:**
- Abra Developer Tools → Network
- Faça qualquer requisição após login
- Verifique se existe header `Authorization: Bearer...`

**Logs do Backend:**
- Verifique se request chega no backend
- Valide se credenciais estão corretas
- Confirme se resposta tem token

### 10. Próximos Passos

- [ ] Implementar refresh token
- [ ] Adicionar "Lembrar-se de mim"
- [ ] Recuperação de senha
- [ ] Validação de token no backend periodicamente
- [ ] Logout com timer (session timeout)
