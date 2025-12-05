# eZion Frontend

Projeto frontend Angular para o sistema integrado de gestão empresarial eZion.

## 📦 Conteúdo

O projeto `ezion-app/` contém:

### ✅ Estrutura Criada

```
ezion-app/
├── src/
│   ├── app/
│   │   ├── components/              # Componentes base
│   │   │   ├── header/
│   │   │   ├── sidebar/
│   │   │   └── footer/
│   │   ├── pages/                   # Páginas principais
│   │   │   ├── login/              # 🔐 Página de login
│   │   │   └── dashboard/          # 📊 Dashboard
│   │   ├── services/               # Serviços
│   │   │   ├── api.ts             # HTTP genérico
│   │   │   └── auth.ts            # Autenticação
│   │   ├── config/                 # Configurações
│   │   │   └── api.config.ts       # URLs da API
│   │   ├── app.ts                  # Componente raiz
│   │   ├── app.routes.ts           # Rotas
│   │   └── app.config.ts           # Config Angular
│   ├── main.ts
│   ├── index.html
│   └── styles.css
├── dist/                            # Build compilado ✔️
├── package.json
├── angular.json
├── tsconfig.json
├── README_FRONTEND.md               # Documentação detalhada
├── SETUP.md                         # Guia de setup
└── setup.sh                         # Script de inicialização
```

## 🚀 Início Rápido

### 1. Navegar até o projeto
```bash
cd eZionFront/ezion-app
```

### 2. Instalar dependências (primeira vez)
```bash
npm install
```

### 3. Iniciar servidor de desenvolvimento
```bash
npm start
```

Acesse em `http://localhost:4200`

### 4. Compilar para produção
```bash
npm run build
```

## 📋 Componentes Criados

### 🔐 Login (`pages/login/`)
- Formulário com email e senha
- Validação de dados
- Integração com AuthService
- Estilos responsivos

### �� Dashboard (`pages/dashboard/`)
- Layout principal
- Bem-vindo ao usuário
- Cards de módulos
- Botão de logout

### 🧩 Componentes Base
- **Header**: Cabeçalho da aplicação
- **Sidebar**: Menu lateral (estrutura)
- **Footer**: Rodapé (estrutura)

## 🔧 Serviços

### ApiService
Serviço genérico para requisições HTTP:
```typescript
get<T>(endpoint)    // GET request
post<T>(endpoint, data)   // POST request
put<T>(endpoint, data)    // PUT request
delete<T>(endpoint)       // DELETE request
```

### AuthService
Gerenciar autenticação:
```typescript
login(email, senha)       // Fazer login
logout()                  // Fazer logout
isAuthenticated()         // Verificar autenticação
getToken()               // Obter JWT token
currentUser$             // Observable do usuário
```

## 📡 Integração Backend

**Base URL**: `http://localhost:8080/api`

### Endpoints
- `POST /auth/login` - Autenticação
- `POST /auth/register` - Registro
- `GET /usuarios` - Listar usuários
- `GET /roles` - Listar roles
- `GET /permissoes` - Listar permissões
- `GET /empresas` - Listar empresas

## 🛠️ Tecnologias

| Tecnologia | Versão |
|-----------|--------|
| Node.js | 18+ |
| npm | 9+ |
| Angular | 19+ |
| TypeScript | 5.5+ |
| RxJS | 7.8+ |

## 📝 Próximos Passos

- [ ] Criar Guards de autenticação
- [ ] Implementar Interceptadores JWT
- [ ] Adicionar módulo de usuários
- [ ] Criar módulo de empresas
- [ ] Implementar sistema de notificações
- [ ] Adicionar suporte a temas

## 📚 Documentação

- `README_FRONTEND.md` - Documentação completa
- `SETUP.md` - Guia de configuração
- `setup.sh` - Script de inicialização

## ✨ Features

✅ Autenticação JWT
✅ Formulários Reativos
✅ Roteamento Angular
✅ Serviços HTTP
✅ Layout responsivo
✅ Componentes standalone

## 🆘 Troubleshooting

### Porta 4200 em uso
```bash
ng serve --port 4201
```

### Limpar cache
```bash
npm cache clean --force
rm -rf node_modules
npm install
```

### Erros de compilação
```bash
npm run clean
npm install
npm run build
```

---

**Status**: ✅ Pronto para desenvolvimento

Para mais informações, consulte a documentação em `README_FRONTEND.md`
