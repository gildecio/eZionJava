import { Routes } from '@angular/router';
import { AuthGuard } from './guards/auth.guard';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./layout/layout.component').then(m => m.LayoutComponent),
    children: [
      { path: '', redirectTo: 'login', pathMatch: 'full' },
      { path: 'login', loadComponent: () => import('./pages/login/login.component').then(m => m.Login) },
      { path: 'dashboard', loadComponent: () => import('./pages/dashboard/dashboard.component').then(m => m.Dashboard), canActivate: [AuthGuard] },
      { path: 'empresas', loadComponent: () => import('./pages/empresas/empresas.component').then(m => m.Empresas), canActivate: [AuthGuard] },
      { path: 'seguranca', loadComponent: () => import('./pages/seguranca/seguranca.component').then(m => m.Seguranca), canActivate: [AuthGuard] },
    ]
  },
  { path: '**', redirectTo: '/login' }
];
