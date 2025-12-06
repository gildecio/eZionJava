import { Component, ElementRef, HostListener, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet, Router, RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../services/auth';
import { ButtonModule } from 'primeng/button';
import { AvatarModule } from 'primeng/avatar';
import { BadgeModule } from 'primeng/badge';
import { MenuModule, Menu } from 'primeng/menu';
import { MenuItem } from 'primeng/api';

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [
    CommonModule,
    RouterOutlet,
    RouterLink,
    RouterLinkActive,
    ButtonModule,
    AvatarModule,
    BadgeModule,
    MenuModule
  ],
  templateUrl: './layout.html',
  styleUrl: './layout.css'
})
export class Layout {
  @ViewChild('userMenu') userMenu!: Menu;

  usuarioLogado: string = '';
  contabilAberto = false;
  userMenuItems: MenuItem[] = [];

  constructor(
    private authService: AuthService,
    private router: Router,
    private elementRef: ElementRef
  ) {
    this.usuarioLogado = this.authService.getUsuario();
    this.initializeUserMenu();
  }

  private initializeUserMenu() {
    this.userMenuItems = [
      {
        label: 'Sair',
        icon: 'pi pi-sign-out',
        command: () => this.logout(),
        styleClass: 'p-menuitem-danger'
      }
    ];
  }

  toggleUserMenu(event: Event) {
    this.userMenu.toggle(event);
  }

  toggleContabilSubmenu() {
    this.contabilAberto = !this.contabilAberto;
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
