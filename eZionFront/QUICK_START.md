# 🚀 Quick Start - eZion Frontend

## ⚡ 5 Minutos para Começar

### Passo 1: Entrar na pasta
```bash
cd /home/gildecio/projetos/eZionJava/eZionFront/ezion-app
```

### Passo 2: Instalar dependências (primeira vez)
```bash
npm install
```

### Passo 3: Iniciar servidor de desenvolvimento
```bash
npm start
```

### Passo 4: Abrir no navegador
```
http://localhost:4200
```

---

## 🔐 Fazer Login

Use as credenciais do backend:

```
Email:  admin@ezion.com
Senha:  admin123456
```

---

## 📁 Estrutura do Projeto

```
ezion-app/
├── src/app/
│   ├── pages/
│   │   ├── login/        🔐 Página de login
│   │   └── dashboard/    📊 Dashboard
│   ├── services/
│   │   ├── api.ts       HTTP requests
│   │   └── auth.ts      Autenticação
│   ├── config/
│   │   └── api.config.ts Configurações
│   └── components/
│       ├── header/
│       ├── sidebar/
│       └── footer/
```

---

## 🛠️ Comandos Úteis

```bash
# Servidor de desenvolvimento
npm start

# Build para produção
npm run build

# Testes
npm test

# Linting
npm run lint

# Limpar cache
npm cache clean --force
rm -rf node_modules
npm install
```

---

## 📡 Conectar com Backend

O frontend está configurado para conectar com:
- **Backend URL**: `http://localhost:8080/api`

Para mudar, edite: `src/app/config/api.config.ts`

---

## ⚙️ Configurações Importantes

### URL da API (`src/app/config/api.config.ts`)
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

### Porta do servidor
```bash
# Usar porta 4201 ao invés de 4200
ng serve --port 4201
```

---

## 🎨 Personalizar

### Cores (componentes)
- Primária: `#667eea` (roxo)
- Secundária: `#764ba2` (roxo escuro)

Edite os arquivos `.css` dos componentes para mudar cores.

---

## 🆘 Problemas Comuns

### Porta 4200 em uso
```bash
ng serve --port 4201
```

### Erro de conexão com backend
1. Verificar se backend está rodando em `http://localhost:8080`
2. Verificar firewall
3. Verificar CORS no backend

### Erros de compilação
```bash
npm cache clean --force
rm -rf node_modules dist
npm install
npm start
```

---

## 📊 Estrutura de Pastas Explicada

| Pasta | Descrição |
|-------|-----------|
| `src/app/pages/` | Páginas da aplicação (login, dashboard) |
| `src/app/components/` | Componentes reutilizáveis (header, footer) |
| `src/app/services/` | Serviços HTTP e lógica de negócio |
| `src/app/config/` | Configurações da aplicação |
| `dist/` | Build compilado para produção |

---

## ✅ Checklist de Verificação

- [ ] Node.js 18+ instalado
- [ ] npm 9+ instalado
- [ ] Dependências instaladas (`npm install`)
- [ ] Backend rodando em localhost:8080
- [ ] Frontend rodando em localhost:4200
- [ ] Consegue fazer login
- [ ] Dashboard carrega corretamente

---

## 📚 Próximos Passos

1. **Explorar componentes**: Abra `src/app/pages/login/` e `dashboard/`
2. **Entender serviços**: Veja `src/app/services/api.ts` e `auth.ts`
3. **Criar novo componente**: `ng generate component components/seu-componente`
4. **Criar novo serviço**: `ng generate service services/seu-servico`
5. **Adicionar página**: `ng generate component pages/sua-pagina`

---

## 🚀 Deploy

### Build para produção
```bash
npm run build
```

Arquivos em: `dist/ezion-app/`

### Servir com Apache/Nginx
```nginx
location / {
    root /path/to/dist/ezion-app/browser;
    try_files $uri $uri/ /index.html;
}
```

---

**Pronto para começar!** 🎉

Dúvidas? Consulte `README_FRONTEND.md` e `SETUP.md`
