# Configuração do eZion Frontend

## Variáveis de Ambiente

Crie um arquivo `.env` na raiz do projeto com as seguintes variáveis:

```
# Desenvolvimento
NG_CLI_ANALYTICS=false
NODE_ENV=development

# URLs da API
API_BASE_URL=http://localhost:8080/api
API_TIMEOUT=30000

# Autenticação
JWT_TOKEN_KEY=auth_token
```

## Modo Desenvolvimento

Por padrão, o projeto usa:
- API: `http://localhost:8080/api`
- Porta: `4200`
- Mock: Desativado

Para iniciar:
```bash
npm start
```

## Modo Produção

Para compilar para produção:
```bash
npm run build
```

Arquivos compilados estarão em `dist/ezion-app/`

### Deploy no Nginx

```nginx
server {
    listen 80;
    server_name ezion.localhost;

    location / {
        root /path/to/ezion-app/dist/ezion-app/browser;
        index index.html;
        try_files $uri $uri/ /index.html;
    }

    location /api/ {
        proxy_pass http://localhost:8080/api/;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection 'upgrade';
        proxy_set_header Host $host;
        proxy_cache_bypass $http_upgrade;
    }
}
```

## Estrutura de Pastas

```
src/
├── app/
│   ├── components/          # Componentes reutilizáveis
│   │   ├── header/
│   │   ├── sidebar/
│   │   └── footer/
│   ├── pages/              # Páginas principais
│   │   ├── login/
│   │   └── dashboard/
│   ├── services/           # Serviços HTTP
│   │   ├── api.ts         # Serviço genérico
│   │   └── auth.ts        # Autenticação
│   ├── config/             # Configurações
│   │   └── api.config.ts   # URLs da API
│   ├── app.ts              # Componente raiz
│   ├── app.routes.ts       # Rotas
│   └── app.config.ts       # Config. Angular
├── main.ts                 # Entry point
├── index.html              # HTML principal
└── styles.css              # Estilos globais
```

## Scripts Disponíveis

```bash
# Desenvolvimento
npm start          # Inicia servidor dev (port 4200)

# Build
npm run build      # Build produção

# Testes
npm test          # Executa testes
npm run test:cov  # Com cobertura

# Linting
npm run lint      # Verifica código

# Limpeza
npm run clean     # Remove dist/ .angular/
```

## Dependências Principais

- **@angular/core**: Framework
- **@angular/common**: Componentes comuns
- **@angular/router**: Roteamento
- **@angular/forms**: Formulários reativos
- **rxjs**: Programação reativa

## Guia de Contribuição

1. Criar branch: `git checkout -b feature/sua-feature`
2. Fazer commits: `git commit -m "Add: descrição"`
3. Push: `git push origin feature/sua-feature`
4. Pull Request

## Troubleshooting

### Porta 4200 já em uso
```bash
ng serve --port 4201
```

### Cache de npm
```bash
npm cache clean --force
rm -rf node_modules package-lock.json
npm install
```

### Erros de compilação
```bash
npm run clean
npm install
npm run build
```
