# eZion Frontend - Angular

Projeto frontend Angular para o sistema integrado de gestão empresarial eZion.

## 📋 Estrutura do Projeto

```
src/
├── app/
│   ├── components/       # Componentes reutilizáveis
│   │   ├── header/
│   │   ├── sidebar/
│   │   └── footer/
│   ├── pages/           # Páginas principais
│   │   ├── login/
│   │   └── dashboard/
│   ├── services/        # Serviços (API, Auth)
│   │   ├── api.ts
│   │   └── auth.ts
│   ├── config/          # Configurações
│   │   └── api.config.ts
│   ├── app.ts           # Componente raiz
│   ├── app.routes.ts    # Rotas da aplicação
│   └── app.config.ts    # Configuração da aplicação
├── main.ts              # Ponto de entrada
├── index.html           # HTML principal
└── styles.css           # Estilos globais
```

## 🚀 Como Começar

### Pré-requisitos
- Node.js 18+
- npm 9+

### Instalação

```bash
cd eZionFront/ezion-app
npm install
```

### Desenvolvimento

Para iniciar o servidor de desenvolvimento:

```bash
npm start
```

A aplicação estará disponível em `http://localhost:4200`

### Build para Produção

```bash
npm run build
```

Os arquivos compilados estarão em `dist/`

## 🔐 Autenticação

### Credenciais Padrão (Backend)

```
Email: admin@ezion.com
Senha: admin123456
```

### Fluxo de Autenticação

1. Usuário acessa `/login`
2. Faz login com email e senha
3. Sistema autentica com o backend (JWT)
4. Token armazenado em localStorage
5. Redirecionado para `/dashboard`

## 🛠️ Tecnologias

- **Angular**: 19.0+ (Standalone Components)
- **TypeScript**: 5.5+
- **RxJS**: 7.8+
- **Angular Forms**: Formulários reativos
- **Angular Router**: Roteamento
- **HttpClient**: Requisições HTTP

## 📡 Integração com Backend

### Configuração da API

Edite `src/app/config/api.config.ts`:

```typescript
export const API_CONFIG = {
  baseUrl: 'http://localhost:8080/api',
  timeout: 30000,
  endpoints: {
    auth: '/auth',
    usuarios: '/usuarios',
    roles: '/roles',
    permissoes: '/permissoes',
    empresas: '/empresas'
  }
};
```

### Endpoints Disponíveis

#### Autenticação
- `POST /api/auth/login` - Login
- `POST /api/auth/register` - Registrar
- `POST /api/auth/refresh` - Renovar token
- `GET /api/auth/me` - Dados do usuário logado

#### Usuários
- `GET /api/usuarios` - Listar
- `GET /api/usuarios/:id` - Detalhe
- `POST /api/usuarios` - Criar
- `PUT /api/usuarios/:id` - Atualizar
- `DELETE /api/usuarios/:id` - Deletar

## 📦 Serviços

### ApiService

Serviço genérico para requisições HTTP:

```typescript
import { ApiService } from './services/api';

constructor(private api: ApiService) {}

// GET
this.api.get<User[]>('/usuarios').subscribe(users => {
  console.log(users);
});

// POST
this.api.post<User>('/usuarios', userData).subscribe(result => {
  console.log(result);
});

// PUT
this.api.put<User>('/usuarios/1', updateData).subscribe(result => {
  console.log(result);
});

// DELETE
this.api.delete('/usuarios/1').subscribe(() => {
  console.log('Deletado');
});
```

### AuthService

Serviço para gerenciar autenticação:

```typescript
import { AuthService } from './services/auth';

constructor(private auth: AuthService) {}

// Login
this.auth.login(email, senha).subscribe(response => {
  console.log('Logado com sucesso');
});

// Verificar autenticação
if (this.auth.isAuthenticated()) {
  console.log('Usuário autenticado');
}

// Obter token
const token = this.auth.getToken();

// Usuário atual
this.auth.currentUser$.subscribe(user => {
  console.log('Usuário:', user);
});

// Logout
this.auth.logout();
```

## 🎯 Próximos Passos

1. **Guards**: Criar route guards para proteger rotas
2. **Interceptadores**: Adicionar token JWT em todas as requisições
3. **Formulários**: Criar módulo de gerenciamento de usuários
4. **Listagens**: Implementar tabelas com paginação
5. **Notificações**: Sistema de toast/notificações
6. **Temas**: Sistema de temas claro/escuro

## 📝 Licença

Projeto proprietário - eZion
