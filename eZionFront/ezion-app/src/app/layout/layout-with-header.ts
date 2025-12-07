import { Component, OnInit, HostListener, ElementRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet, Router, RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../services/auth';

@Component({
  selector: 'app-layout-with-header',
  standalone: true,
  imports: [
    CommonModule,
    RouterOutlet,
    RouterLink,
    RouterLinkActive
  ],
  templateUrl: './layout-with-header.html',
  styleUrl: './layout-with-header.css'
})
export class LayoutWithHeader implements OnInit {
  usuarioLogado: string = '';
  dropdownAberto: boolean = false;

  constructor(
    private authService: AuthService,
    private router: Router,
    private elementRef: ElementRef
  ) {
    this.usuarioLogado = this.authService.getUsuario();
  }

  ngOnInit(): void {
    // Atualizar usuário ao inicializar
    this.usuarioLogado = this.authService.getUsuario();
  }

  toggleDropdown(): void {
    this.dropdownAberto = !this.dropdownAberto;
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  // Fechar dropdown ao clicar fora
  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent): void {
    const target = event.target as HTMLElement;
    const userDropdown = this.elementRef.nativeElement.querySelector('.user-dropdown');
    
    if (userDropdown && !userDropdown.contains(target)) {
      this.dropdownAberto = false;
    }
  }
}
