# eZion - Sistema com Módulo de Segurança

Sistema Spring Boot com autenticação JWT e gerenciamento de usuários, roles e permissões.

## ✅ Funcionalidades Implementadas

- **Autenticação JWT** - Login, registro e refresh token
- **Autorização com Roles** - Controle de acesso baseado em papéis
- **Gerenciamento de Permissões** - Permissões granulares
- **CRUD de Usuários** - Criar, ler, atualizar, deletar usuários
- **Gerenciamento de Roles** - Criar e gerenciar papéis
- **Gerenciamento de Permissões** - Criar e atribuir permissões

## 🚀 Começar Rapidamente

### Pré-requisitos
- Java 17+
- Maven 3.6+
- PostgreSQL 12+

### Executar a Aplicação

```bash
mvn spring-boot:run
```

A API estará disponível em: http://localhost:8080

### Login com Credenciais Padrão

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'
```

## 🔌 Endpoints Principais

### Autenticação (Públicos)
- `POST /api/auth/login` - Fazer login
- `POST /api/auth/register` - Registrar novo usuário
- `POST /api/auth/refresh` - Renovar token
- `GET /api/auth/me` - Obter usuário autenticado

### Usuários (Protegidos)
- `GET /api/usuarios` - Listar todos
- `GET /api/usuarios/{id}` - Obter por ID
- `PUT /api/usuarios/{id}` - Atualizar
- `DELETE /api/usuarios/{id}` - Deletar

### Roles (Protegidos)
- `GET /api/roles` - Listar todas
- `POST /api/roles` - Criar nova role
- `PUT /api/roles/{id}` - Atualizar
- `DELETE /api/roles/{id}` - Deletar

### Permissões (Protegidos)
- `GET /api/permissoes` - Listar todas
- `POST /api/permissoes` - Criar nova permissão
- `DELETE /api/permissoes/{id}` - Deletar

## 🏗️ Estrutura do Projeto

```
src/main/java/com/
├── seguranca/
│   ├── config/          - Configuração de segurança
│   ├── controller/      - REST Controllers
│   ├── dto/            - Data Transfer Objects
│   ├── model/          - Entidades JPA
│   ├── repository/     - Camada de acesso a dados
│   ├── security/       - Componentes de segurança (JWT, etc)
│   └── service/        - Lógica de negócio
├── contabil/           - Módulo contábil (básico)
└── EzionApplication.java - Aplicação principal
```

## 📚 Compilar e Testar

### Compilar
```bash
mvn clean compile
```

### Testes
```bash
mvn test
```

### Build para Produção
```bash
mvn clean package -DskipTests
```

## 📖 Documentação

- **QUICK_START.md** - Guia de início rápido (5 minutos)

## 🔐 Credenciais Padrão

```
Username: admin
Password: admin123
Email: admin@example.com
```

⚠️ **MUDE EM PRODUÇÃO!**

## 🛠️ Tecnologias

- Spring Boot 3.2.0
- Spring Security
- JWT (JJWT 0.11.5)
- JPA/Hibernate
- PostgreSQL
- Maven
- Java 17

## 📝 Licença

MIT
