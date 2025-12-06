import { Component, ElementRef, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet, Router, RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../services/auth';

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './layout.html',
  styleUrl: './layout.css'
})
export class Layout {
  usuarioLogado: string = '';
  dropdownAberto = false;
  contabilAberto = false;

  constructor(
    private authService: AuthService,
    private router: Router,
    private elementRef: ElementRef
  ) {
    this.usuarioLogado = this.authService.getUsuario();
  }

  toggleDropdown() {
    this.dropdownAberto = !this.dropdownAberto;
  }

  fecharDropdown() {
    this.dropdownAberto = false;
  }

  toggleContabilSubmenu() {
    this.contabilAberto = !this.contabilAberto;
  }

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent) {
    const target = event.target as HTMLElement;
    if (!this.elementRef.nativeElement.contains(target)) {
      this.fecharDropdown();
    }
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
