import { Component } from '@angular/core';
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

  constructor(
    private authService: AuthService,
    private router: Router
  ) {
    this.usuarioLogado = this.authService.getUsuario();
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
