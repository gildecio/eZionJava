import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService, Empresa } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class Login implements OnInit {
  loginForm: FormGroup;
  empresas: Empresa[] = [];
  loading = false;
  feedback: { type: 'success' | 'error' | 'warn'; text: string } | null = null;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      empresa_id: ['', Validators.required],
      usuario: ['', [Validators.required, Validators.minLength(3)]],
      senha: ['', Validators.required]
    });
  }

  ngOnInit() {
    this.carregarEmpresas();
  }

  carregarEmpresas() {
    this.authService.listarEmpresas().subscribe({
      next: (empresas) => {
        this.empresas = empresas;
      },
      error: (error) => {
        console.error('Erro ao carregar empresas:', error);
        this.setFeedback('error', 'Não foi possível carregar a lista de empresas');
      }
    });
  }

  onSubmit() {
    if (this.loginForm.valid) {
      this.loading = true;
      const credentials = this.loginForm.value;

      this.authService.login(credentials).subscribe({
        next: (response) => {
          this.setFeedback('success', `Bem-vindo, ${response.username}!`);
          this.router.navigate(['/dashboard']);
        },
        error: (error) => {
          console.error('Erro no login:', error);
          this.setFeedback('error', 'Usuário, senha ou empresa incorretos');
          this.loading = false;
        },
        complete: () => {
          this.loading = false;
        }
      });
    } else {
      this.setFeedback('warn', 'Preencha todos os campos obrigatórios');
    }
  }

  private setFeedback(type: 'success' | 'error' | 'warn', text: string) {
    this.feedback = { type, text };
    setTimeout(() => {
      if (this.feedback?.text === text) {
        this.feedback = null;
      }
    }, 4000);
  }
}
