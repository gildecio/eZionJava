import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { trigger, transition, style, animate } from '@angular/animations';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
  animations: [
    trigger('slideIn', [
      transition(':enter', [
        style({ opacity: 0, transform: 'translateY(-10px)' }),
        animate('300ms ease-out', style({ opacity: 1, transform: 'translateY(0)' }))
      ]),
      transition(':leave', [
        animate('300ms ease-in', style({ opacity: 0, transform: 'translateY(-10px)' }))
      ])
    ])
  ]
})
export class Login {
  loginForm: FormGroup;
  loading = false;
  submitted = false;
  error: string = '';
  successMessage: string = '';

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.loginForm = this.formBuilder.group({
      usuario: ['', Validators.required],
      senha: ['', Validators.required]
    });
  }

  get f() {
    return this.loginForm.controls;
  }

  onSubmit() {
    this.submitted = true;
    this.error = '';
    this.successMessage = '';

    if (this.loginForm.invalid) {
      return;
    }

    this.loading = true;

    this.authService.login(this.f['usuario'].value, this.f['senha'].value)
      .subscribe({
        next: () => {
          this.successMessage = 'Login realizado com sucesso! Redirecionando...';
          setTimeout(() => {
            this.router.navigate(['/index']);
          }, 1500);
        },
        error: (err: any) => {
          // Tratar diferentes tipos de erro
          if (err.status === 401) {
            this.error = 'Usuário ou senha inválidos. Verifique suas credenciais.';
          } else if (err.status === 400) {
            this.error = err.error?.message || 'Dados inválidos. Verifique o formulário.';
          } else if (err.status === 0) {
            this.error = 'Erro de conexão! Verifique se o backend está rodando em localhost:8080.';
          } else if (err.status === 500) {
            this.error = 'Erro no servidor. Tente novamente mais tarde.';
          } else if (err.status === 403) {
            this.error = 'Acesso negado. Você não tem permissão para acessar.';
          } else {
            this.error = err.error?.message || 'Erro ao fazer login. Tente novamente.';
          }
          this.loading = false;
          this.submitted = false;
        }
      });
  }
}
