import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet, RouterModule, Router, NavigationEnd, ActivatedRoute } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { ConfirmDeleteModalComponent } from '../pages/empresas/confirm-delete-modal.component';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterModule, ConfirmDeleteModalComponent],
  templateUrl: './layout.component.html',
  styleUrls: ['./layout.component.css']
})
export class LayoutComponent implements OnInit {
  menuItems = [
    { label: 'Dashboard', route: '/dashboard', icon: '🏠' },
    { 
      label: 'Contábil', 
      icon: '🧮',
      submenu: [
        { label: 'Empresas', route: '/empresas', icon: '🏢' }
      ]
    },
    { label: 'Segurança', route: '/seguranca', icon: '🛡️' }
  ];

  openSubmenu: string | null = null;
  isLoginPage = false;

  constructor(private authService: AuthService, private router: Router, private activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    // Detectar se estamos na página de login através das rotas filhas
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe((event: NavigationEnd) => {
      this.checkIfLoginPage(event.url);
    });

    // Verificar rota inicial
    this.checkIfLoginPage(this.router.url);
  }

  private checkIfLoginPage(url: string) {
    // Verificar se a URL contém '/login'
    this.isLoginPage = url.includes('/login');
    console.log('URL atual:', url, 'isLoginPage:', this.isLoginPage);
  }

  logout() {
    this.authService.logout();
  }

  toggleSubmenu(label: string) {
    this.openSubmenu = this.openSubmenu === label ? null : label;
  }

  closeSubmenu() {
    this.openSubmenu = null;
  }
}