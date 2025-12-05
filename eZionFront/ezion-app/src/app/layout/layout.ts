import { Component, HostListener, ElementRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet, Router } from '@angular/router';
import { AuthService } from '../services/auth';

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [CommonModule, RouterOutlet],
  templateUrl: './layout.html',
  styleUrl: './layout.css'
})
export class Layout {
  usuarioLogado: string = '';
  dropdownAberto = false;

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

  @HostListener('document:click', ['$event'])
  onClickOutside(event: MouseEvent) {
    const target = event.target as HTMLElement;
    const userDropdown = this.elementRef.nativeElement.querySelector('.user-dropdown');
    
    if (userDropdown && !userDropdown.contains(target)) {
      this.fecharDropdown();
    }
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
