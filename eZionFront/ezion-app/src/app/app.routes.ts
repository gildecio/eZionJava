import { Routes } from '@angular/router';
import { Login } from './pages/login/login';
import { Layout } from './layout/layout';
import { Index } from './pages/index/index';
import { Dashboard } from './pages/dashboard/dashboard';
import { EmpresasComponent } from './modules/contabil/pages/empresas/empresas';
import { AuthGuard } from './guards/auth.guard';

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
    ]
  },
  { path: '**', redirectTo: '/login' }
];
