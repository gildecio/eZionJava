# Estrutura Recomendada do Frontend Angular

Baseada na organização do backend Spring Boot, a estrutura proposta segue o padrão modular por domínio (módulos de negócio).

## Estrutura Atual vs. Proposta

### Estrutura Atual
```
src/app/
├── components/
│   ├── footer/
│   ├── header/
│   └── sidebar/
├── config/
├── guards/
├── interceptors/
├── layout/
├── pages/
│   ├── dashboard/
│   ├── index/
│   ├── itens/
│   └── login/
└── services/
```

### Estrutura Proposta (Modular por Domínio)

```
src/app/
│
├── core/                          # Núcleo da aplicação (singleton)
│   ├── guards/
│   │   └── auth.guard.ts
│   ├── interceptors/
│   │   └── jwt.interceptor.ts
│   ├── services/
│   │   ├── auth/
│   │   │   ├── auth.service.ts
│   │   │   └── auth.model.ts
│   │   └── api/
│   │       └── api.service.ts
│   └── core.module.ts
│
├── shared/                        # Componentes e utilitários compartilhados
│   ├── components/
│   │   ├── header/
│   │   ├── sidebar/
│   │   └── footer/
│   ├── directives/
│   ├── pipes/
│   ├── models/
│   │   └── common.model.ts
│   ├── styles/
│   │   ├── theme.css
│   │   └── variables.css
│   └── shared.module.ts
│
├── layout/                        # Layout principal
│   ├── layout.component.ts
│   ├── layout.html
│   └── layout.css
│
├── modules/                       # Módulos de negócio (domains)
│   │
│   ├── auth/                      # Módulo de Autenticação
│   │   ├── pages/
│   │   │   └── login/
│   │   │       ├── login.component.ts
│   │   │       ├── login.html
│   │   │       └── login.css
│   │   └── auth.module.ts
│   │
│   ├── dashboard/                 # Módulo de Dashboard
│   │   ├── components/
│   │   │   ├── stats-card/
│   │   │   ├── activities/
│   │   │   └── tasks/
│   │   ├── pages/
│   │   │   └── dashboard/
│   │   │       ├── dashboard.component.ts
│   │   │       ├── dashboard.html
│   │   │       └── dashboard.css
│   │   ├── services/
│   │   │   └── dashboard.service.ts
│   │   ├── models/
│   │   │   └── dashboard.model.ts
│   │   └── dashboard.module.ts
│   │
│   ├── estoque/                   # Módulo de Estoque
│   │   ├── components/
│   │   │   ├── item-form/
│   │   │   ├── item-table/
│   │   │   └── item-modal/
│   │   ├── pages/
│   │   │   ├── itens/
│   │   │   │   ├── itens.component.ts
│   │   │   │   ├── itens.html
│   │   │   │   └── itens.css
│   │   │   ├── item-detail/
│   │   │   └── categorias/
│   │   ├── services/
│   │   │   └── item.service.ts
│   │   ├── models/
│   │   │   └── item.model.ts
│   │   └── estoque.module.ts
│   │
│   ├── relatorios/                # Módulo de Relatórios
│   │   ├── components/
│   │   │   ├── chart/
│   │   │   └── filter/
│   │   ├── pages/
│   │   │   ├── relatorios/
│   │   │   └── vendas/
│   │   ├── services/
│   │   │   └── relatorio.service.ts
│   │   ├── models/
│   │   │   └── relatorio.model.ts
│   │   └── relatorios.module.ts
│   │
│   └── configuracoes/             # Módulo de Configurações
│       ├── pages/
│       │   ├── configuracoes/
│       │   ├── perfil-usuario/
│       │   └── preferencias/
│       ├── services/
│       │   └── configuracoes.service.ts
│       ├── models/
│       │   └── configuracoes.model.ts
│       └── configuracoes.module.ts
│
├── app.component.ts
├── app.routes.ts
├── app.config.ts
└── main.ts
```

## Benefícios desta Organização

### 1. **Escalabilidade**
- Fácil adicionar novos módulos/domínios
- Cada módulo é independente e isolado

### 2. **Manutenibilidade**
- Código organizado por funcionalidade
- Relações claras entre componentes, serviços e modelos

### 3. **Lazy Loading**
- Possibilidade de carregar módulos sob demanda
- Reduz tamanho inicial do bundle

### 4. **Reutilização**
- Componentes compartilhados em `shared/`
- Serviços centralizados em `core/`

### 5. **Seguir Padrões**
- Alinha com estrutura do backend (módulos por domínio)
- Segue as recomendações do Angular Style Guide

## Padrão de Organização de um Módulo

Cada módulo segue a estrutura:

```
modulo-name/
├── components/              # Componentes específicos do módulo
│   ├── component-a/
│   │   ├── component-a.ts
│   │   ├── component-a.html
│   │   └── component-a.css
│   └── component-b/
├── pages/                   # Páginas/containers do módulo
│   ├── page-name/
│   │   ├── page-name.ts
│   │   ├── page-name.html
│   │   └── page-name.css
├── services/                # Serviços de API e lógica
│   └── modulo.service.ts
├── models/                  # Interfaces/DTOs
│   └── modulo.model.ts
├── modulo.module.ts         # Arquivo de módulo (se necessário)
└── modulo.routing.ts        # Rotas específicas (opcional)
```

## Estrutura do Core (Núcleo)

O `core/` deve conter apenas serviços singleton:

```
core/
├── guards/
│   ├── auth.guard.ts
│   └── role.guard.ts
├── interceptors/
│   └── jwt.interceptor.ts
├── services/
│   ├── auth/
│   │   ├── auth.service.ts
│   │   ├── auth.model.ts
│   │   └── token.service.ts
│   ├── api/
│   │   └── api.service.ts
│   └── storage/
│       └── local-storage.service.ts
└── core.module.ts
```

## Estrutura do Shared (Compartilhado)

O `shared/` contém componentes, diretivas e pipes reutilizáveis:

```
shared/
├── components/
│   ├── header/
│   ├── sidebar/
│   ├── footer/
│   ├── button/
│   └── modal/
├── directives/
│   ├── highlight.directive.ts
│   └── has-role.directive.ts
├── pipes/
│   ├── currency.pipe.ts
│   └── truncate.pipe.ts
├── models/
│   ├── api-response.model.ts
│   └── pagination.model.ts
├── styles/
│   ├── theme.css
│   ├── variables.css
│   └── mixins.css
└── shared.module.ts
```

## Rotação de Arquivos (se usando .module.ts)

Se preferir usar NgModules em vez de standalone:

```typescript
// estoque.module.ts
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EstoqueRoutingModule } from './estoque.routing';
import { ItensComponent } from './pages/itens/itens.component';
import { ItemFormComponent } from './components/item-form/item-form.component';
import { ItemService } from './services/item.service';

@NgModule({
  declarations: [ItensComponent, ItemFormComponent],
  imports: [CommonModule, EstoqueRoutingModule],
  providers: [ItemService]
})
export class EstoqueModule { }
```

## Rotas com Lazy Loading

```typescript
// app.routes.ts
export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  {
    path: '',
    component: LayoutComponent,
    canActivate: [AuthGuard],
    children: [
      { path: 'dashboard', loadComponent: () => import('./modules/dashboard/pages/dashboard/dashboard.component').then(m => m.DashboardComponent) },
      { path: 'estoque', loadChildren: () => import('./modules/estoque/estoque.routing').then(m => m.ESTOQUE_ROUTES) },
      { path: 'relatorios', loadChildren: () => import('./modules/relatorios/relatorios.routing').then(m => m.RELATORIOS_ROUTES) },
      { path: 'configuracoes', loadChildren: () => import('./modules/configuracoes/configuracoes.routing').then(m => m.CONFIGURACOES_ROUTES) }
    ]
  }
];
```

## Passo a Passo para Migração

### Fase 1: Preparação
1. Criar estrutura de pastas `core/`, `shared/`, `modules/`
2. Mover serviços para `core/services/`
3. Mover componentes compartilhados para `shared/components/`

### Fase 2: Modularização
1. Criar módulo `estoque/` com páginas e componentes
2. Mover `pages/itens/` para `modules/estoque/pages/itens/`
3. Mover `services/item.ts` para `modules/estoque/services/item.service.ts`

### Fase 3: Otimização
1. Implementar lazy loading nas rotas
2. Ajustar imports e dependências
3. Testar todas as funcionalidades

### Fase 4: Documentação
1. Atualizar README com nova estrutura
2. Documentar convenções de nomenclatura
3. Criar guia de desenvolvimento

## Convenções de Nomenclatura

### Componentes
- `arquivo-name.component.ts` (página/container)
- `item-form.component.ts` (componente reutilizável)

### Serviços
- `item.service.ts`

### Modelos/Interfaces
- `item.model.ts`
- `dto/create-item.dto.ts`

### Módulos
- `estoque.module.ts`

### Rotas
- `estoque.routing.ts`

## Comparação com Backend

| Backend (Spring) | Frontend (Angular) |
|---|---|
| `controller/` | `pages/` + `components/` |
| `service/` | `services/` |
| `model/` | `models/` |
| `repository/` | `services/` (via HTTP) |
| `config/` | `core/` |
| `security/` | `core/guards/` |
| `interceptor/` | `core/interceptors/` |

Esta estrutura mantém simetria com o backend e facilita a comunicação entre equipes!
