import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { API_CONFIG } from '../config/api.config';

interface LoginRequest {
  usuario: string;
  senha: string;
}

interface LoginResponse {
  token: string;
  usuario: any;
}

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiUrl = `${API_CONFIG.baseUrl}${API_CONFIG.endpoints.auth}`;
  private currentUserSubject = new BehaviorSubject<any>(null);
  public currentUser$ = this.currentUserSubject.asObservable();
  private tokenKey = 'auth_token';

  constructor(private http: HttpClient) {
    // Inicializar com dados do localStorage se existirem
    const user = this.getUserFromStorage();
    if (user) {
      this.currentUserSubject.next(user);
    }
  }

  login(usuario: string, senha: string): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, { usuario, senha })
      .pipe(
        map(response => {
          if (response && response.token) {
            localStorage.setItem(this.tokenKey, response.token);
            localStorage.setItem('usuario', JSON.stringify(response.usuario));
            this.currentUserSubject.next(response.usuario);
          }
          return response;
        })
      );
  }

  register(userData: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/register`, userData);
  }

  logout(): void {
    localStorage.removeItem(this.tokenKey);
    localStorage.removeItem('usuario');
    this.currentUserSubject.next(null);
  }

  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  getUsuario(): string {
    const usuario = localStorage.getItem('usuario');
    if (usuario) {
      try {
        const usuarioObj = JSON.parse(usuario);
        return usuarioObj.username || usuarioObj.usuario || 'Usuário';
      } catch (e) {
        console.warn('Erro ao fazer parse do usuário:', e);
        return 'Usuário';
      }
    }
    return 'Usuário';
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }

  private getUserFromStorage(): any {
    try {
      const usuario = localStorage.getItem('usuario');
      if (usuario) {
        return JSON.parse(usuario);
      }
    } catch (e) {
      console.warn('Erro ao carregar usuário do storage:', e);
    }
    return null;
  }
}
