# Documentação Swagger/OpenAPI

## Acessar a Documentação

Após iniciar a aplicação, acesse a documentação interativa do Swagger em:

```
http://localhost:8080/doc
```

## Rotas Disponíveis

### Autenticação
- **POST** `/api/auth/login` - Realizar login
- **POST** `/api/auth/register` - Registrar novo usuário
- **POST** `/api/auth/refresh` - Atualizar token JWT

### Usuários
- **GET** `/api/usuarios` - Listar usuários
- **GET** `/api/usuarios/{id}` - Obter usuário por ID
- **POST** `/api/usuarios` - Criar novo usuário
- **PUT** `/api/usuarios/{id}` - Atualizar usuário
- **DELETE** `/api/usuarios/{id}` - Deletar usuário

### Roles
- **GET** `/api/roles` - Listar roles
- **GET** `/api/roles/{id}` - Obter role por ID
- **POST** `/api/roles` - Criar nova role
- **PUT** `/api/roles/{id}` - Atualizar role
- **DELETE** `/api/roles/{id}` - Deletar role

### Permissões
- **GET** `/api/permissoes` - Listar permissões
- **GET** `/api/permissoes/{id}` - Obter permissão por ID
- **POST** `/api/permissoes` - Criar nova permissão
- **PUT** `/api/permissoes/{id}` - Atualizar permissão
- **DELETE** `/api/permissoes/{id}` - Deletar permissão

## Autenticação com JWT

1. **Realizar Login**: Envie suas credenciais para `/api/auth/login`
2. **Copiar Token**: O token JWT será retornado na resposta
3. **Usar Token**: Clique no botão "Authorize" no Swagger e insira o token:
   ```
   Bearer seu_token_aqui
   ```
4. **Requisições Autenticadas**: Todas as requisições subsequentes incluirão o token automaticamente

## Exemplo de Requisição

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"usuario":"admin","senha":"admin"}'
```

Resposta:
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "username": "admin",
  "email": "admin@ezion.com",
  "roles": ["ADMIN"]
}
```

## Acessos de API

- **Públicos** (sem autenticação):
  - `/api/auth/login`
  - `/api/auth/register`
  - `/api/auth/refresh`
  - `/doc` (Swagger UI)
  - `/v3/api-docs` (OpenAPI JSON)

- **Autenticados** (requer JWT):
  - Todos os endpoints de `/api/usuarios`
  - Todos os endpoints de `/api/roles`
  - Todos os endpoints de `/api/permissoes`

## Configuração

As configurações do Swagger estão em `application.properties`:

```properties
springdoc.api-docs.path=/v3/api-docs
springdoc.swagger-ui.path=/doc
springdoc.swagger-ui.enabled=true
springdoc.swagger-ui.operations-sorter=method
springdoc.swagger-ui.tags-sorter=alpha
```
