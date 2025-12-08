import { Routes } from '@angular/router';
import { Login } from './pages/login/login';
import { Layout } from './layout/layout';
import { Index } from './pages/index/index';
import { Dashboard } from './pages/dashboard/dashboard';
import { EmpresasComponent } from './modules/contabil/pages/empresas/empresas';
import { UnidadesComponent } from './modules/cadastros/pages/unidades/unidades';
import { LocaisComponent } from './modules/cadastros/pages/locais/locais';
import { AuthGuard } from './guards/auth.guard';

// Placeholder components for estoque module (to be implemented)
const EstoqueItens = () => import('./pages/index/index').then(m => m.Index);
const EstoqueMovimentacoes = () => import('./pages/index/index').then(m => m.Index);
const EstoqueCustos = () => import('./pages/index/index').then(m => m.Index);
const EstoqueLotes = () => import('./pages/index/index').then(m => m.Index);

// Placeholder components for cadastros module (to be implemented)
const CadastrosGrupos = () => import('./pages/index/index').then(m => m.Index);
const CadastrosCidades = () => import('./pages/index/index').then(m => m.Index);
const CadastrosBairros = () => import('./pages/index/index').then(m => m.Index);
const CadastrosFornecedores = () => import('./pages/index/index').then(m => m.Index);

export const routes: Routes = [
  { path: 'login', component: Login },
  {
    path: '',
    component: Layout,
    canActivate: [AuthGuard],
    children: [
      { path: '', redirectTo: '/index', pathMatch: 'full' },
      { path: 'index', component: Index },
      { path: 'dashboard', component: Dashboard },
      { path: 'contabil/empresas', component: EmpresasComponent },
      { path: 'estoque/itens', loadComponent: EstoqueItens },
      { path: 'estoque/movimentacoes', loadComponent: EstoqueMovimentacoes },
      { path: 'estoque/custos', loadComponent: EstoqueCustos },
      { path: 'estoque/lotes', loadComponent: EstoqueLotes },
      { path: 'estoque/locais', component: LocaisComponent },
      { path: 'cadastros/grupos', loadComponent: CadastrosGrupos },
      { path: 'cadastros/unidades', component: UnidadesComponent },
      { path: 'cadastros/cidades', loadComponent: CadastrosCidades },
      { path: 'cadastros/bairros', loadComponent: CadastrosBairros },
      { path: 'cadastros/fornecedores', loadComponent: CadastrosFornecedores },
    ]
  },
  { path: '**', redirectTo: '/index' }
];
