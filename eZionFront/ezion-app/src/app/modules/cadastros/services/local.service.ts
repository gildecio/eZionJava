import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, catchError, of, map } from 'rxjs';
import { API_CONFIG } from '../../../config/api.config';

export interface Local {
  id?: number;
  nome: string;
  ativo?: boolean;
  dataCriacao?: string;
  dataAtualizacao?: string;
}

@Injectable({
  providedIn: 'root'
})
export class LocalService {
  private apiUrl = `${API_CONFIG.baseUrl}/locais`;

  constructor(private http: HttpClient) {}

  listar(): Observable<Local[]> {
    return this.http.get<Local[]>(this.apiUrl).pipe(
      catchError((error) => {
        console.error('Erro ao listar locais:', error);
        return of([]);
      })
    );
  }

  listarAtivos(): Observable<Local[]> {
    return this.http.get<Local[]>(`${this.apiUrl}/ativos`).pipe(
      catchError((error) => {
        console.error('Erro ao listar locais ativos:', error);
        return of([]);
      })
    );
  }

  buscarPorId(id: number): Observable<Local | null> {
    return this.http.get<Local>(`${this.apiUrl}/${id}`).pipe(
      catchError((error) => {
        console.error('Erro ao buscar local por ID:', error);
        return of(null);
      })
    );
  }

  buscarPorNome(nome: string): Observable<Local | null> {
    return this.http.get<Local>(`${this.apiUrl}/nome/${nome}`).pipe(
      catchError((error) => {
        console.error('Erro ao buscar local por nome:', error);
        return of(null);
      })
    );
  }

  buscarPorNomeParcial(nome: string): Observable<Local[]> {
    return this.http.get<Local[]>(`${this.apiUrl}/buscar?nome=${encodeURIComponent(nome)}`).pipe(
      catchError((error) => {
        console.error('Erro ao buscar locais por nome parcial:', error);
        return of([]);
      })
    );
  }

  criar(local: Local): Observable<Local | null> {
    return this.http.post<Local>(this.apiUrl, local).pipe(
      catchError((error) => {
        console.error('Erro ao criar local:', error);
        return of(null);
      })
    );
  }

  atualizar(id: number, local: Local): Observable<Local | null> {
    return this.http.put<Local>(`${this.apiUrl}/${id}`, local).pipe(
      catchError((error) => {
        console.error('Erro ao atualizar local:', error);
        return of(null);
      })
    );
  }

  ativar(id: number): Observable<Local | null> {
    return this.http.put<Local>(`${this.apiUrl}/${id}/ativar`, {}).pipe(
      catchError((error) => {
        console.error('Erro ao ativar local:', error);
        return of(null);
      })
    );
  }

  desativar(id: number): Observable<Local | null> {
    return this.http.put<Local>(`${this.apiUrl}/${id}/desativar`, {}).pipe(
      catchError((error) => {
        console.error('Erro ao desativar local:', error);
        return of(null);
      })
    );
  }

  excluir(id: number): Observable<boolean> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`).pipe(
      catchError((error) => {
        console.error('Erro ao excluir local:', error);
        return of(false);
      }),
      // Converte void para boolean
      map(() => true)
    );
  }
}