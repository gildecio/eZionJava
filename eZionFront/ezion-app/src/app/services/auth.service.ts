import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { environment } from '../../environments/environment';

export interface Empresa {
  id: number;
  nomeFantasia?: string;
  razaoSocial?: string;
  cnpj: string;
}

export interface LoginRequest {
  empresa_id: number;
  usuario: string;
  senha: string;
}

export interface LoginResponse {
  token: string;
  refreshToken: string;
  type: string;
  id: number;
  username: string;
  email: string;
  nomeCompleto: string;
  roles: string[];
  permissoes: string[];
  empresa: Empresa;
}

export interface AuthState {
  isAuthenticated: boolean;
  token: string | null;
  usuario: string | null;
  empresa: Empresa | null;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly API_URL = environment.apiUrl;

  private authState = signal<AuthState>({
    isAuthenticated: false,
    token: null,
    usuario: null,
    empresa: null
  });

  public isAuthenticated$ = new BehaviorSubject<boolean>(false);

  constructor(
    private http: HttpClient,
    private router: Router
  ) {
    this.checkAuthState();
  }

  private checkAuthState() {
    const token = localStorage.getItem('token');
    const usuario = localStorage.getItem('usuario');
    const empresa = localStorage.getItem('empresa');

    if (token && usuario && empresa) {
      this.authState.set({
        isAuthenticated: true,
        token,
        usuario,
        empresa: JSON.parse(empresa)
      });
      this.isAuthenticated$.next(true);
    }
  }

  listarEmpresas(): Observable<Empresa[]> {
    return this.http.get<Empresa[]>(`${this.API_URL}/empresas`);
  }

  login(credentials: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.API_URL}/auth/login`, credentials)
      .pipe(
        tap(response => {
          localStorage.setItem('token', response.token);
          localStorage.setItem('usuario', response.username);
          localStorage.setItem('empresa', JSON.stringify(response.empresa));

          this.authState.set({
            isAuthenticated: true,
            token: response.token,
            usuario: response.username,
            empresa: response.empresa
          });

          this.isAuthenticated$.next(true);
        })
      );
  }

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('usuario');
    localStorage.removeItem('empresa');

    this.authState.set({
      isAuthenticated: false,
      token: null,
      usuario: null,
      empresa: null
    });

    this.isAuthenticated$.next(false);
    this.router.navigate(['/login']);
  }

  getToken(): string | null {
    return this.authState().token;
  }

  getUsuario(): string | null {
    return this.authState().usuario;
  }

  getEmpresa(): Empresa | null {
    return this.authState().empresa;
  }

  isAuthenticated(): boolean {
    return this.authState().isAuthenticated;
  }
}