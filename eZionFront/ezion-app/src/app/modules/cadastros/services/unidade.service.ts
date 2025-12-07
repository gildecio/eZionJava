import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, catchError, of, map } from 'rxjs';
import { API_CONFIG } from '../../../config/api.config';

export interface Unidade {
  id?: number;
  sigla: string;
  descricao: string;
  fator?: number;
  unidadePai?: Unidade;
  ativo?: boolean;
  dataCriacao?: string;
  dataAtualizacao?: string;
}

@Injectable({
  providedIn: 'root'
})
export class UnidadeService {
  private apiUrl = `${API_CONFIG.baseUrl}/unidades`;

  constructor(private http: HttpClient) {}

  listar(): Observable<Unidade[]> {
    return this.http.get<Unidade[]>(this.apiUrl).pipe(
      catchError((error) => {
        console.error('Erro ao listar unidades:', error);
        return of([]);
      })
    );
  }

  listarAtivas(): Observable<Unidade[]> {
    return this.http.get<Unidade[]>(`${this.apiUrl}/ativas`).pipe(
      catchError((error) => {
        console.error('Erro ao listar unidades ativas:', error);
        return of([]);
      })
    );
  }

  buscarPorId(id: number): Observable<Unidade | null> {
    return this.http.get<Unidade>(`${this.apiUrl}/${id}`).pipe(
      catchError((error) => {
        console.error('Erro ao buscar unidade por ID:', error);
        return of(null);
      })
    );
  }

  buscarPorSigla(sigla: string): Observable<Unidade | null> {
    return this.http.get<Unidade>(`${this.apiUrl}/sigla/${sigla}`).pipe(
      catchError((error) => {
        console.error('Erro ao buscar unidade por sigla:', error);
        return of(null);
      })
    );
  }

  criar(unidade: Unidade): Observable<Unidade | null> {
    return this.http.post<Unidade>(this.apiUrl, unidade).pipe(
      catchError((error) => {
        console.error('Erro ao criar unidade:', error);
        return of(null);
      })
    );
  }

  atualizar(id: number, unidade: Unidade): Observable<Unidade | null> {
    return this.http.put<Unidade>(`${this.apiUrl}/${id}`, unidade).pipe(
      catchError((error) => {
        console.error('Erro ao atualizar unidade:', error);
        return of(null);
      })
    );
  }

  ativar(id: number): Observable<Unidade | null> {
    return this.http.put<Unidade>(`${this.apiUrl}/${id}/ativar`, {}).pipe(
      catchError((error) => {
        console.error('Erro ao ativar unidade:', error);
        return of(null);
      })
    );
  }

  desativar(id: number): Observable<Unidade | null> {
    return this.http.put<Unidade>(`${this.apiUrl}/${id}/desativar`, {}).pipe(
      catchError((error) => {
        console.error('Erro ao desativar unidade:', error);
        return of(null);
      })
    );
  }

  excluir(id: number): Observable<boolean> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`).pipe(
      catchError((error) => {
        console.error('Erro ao excluir unidade:', error);
        return of(false);
      }),
      // Converte void para boolean
      map(() => true)
    );
  }

  listarUnidadesBase(): Observable<Unidade[]> {
    return this.http.get<Unidade[]>(`${this.apiUrl}/base`).pipe(
      catchError((error) => {
        console.error('Erro ao listar unidades base:', error);
        return of([]);
      })
    );
  }

  listarUnidadesDerivadas(unidadePaiId: number): Observable<Unidade[]> {
    return this.http.get<Unidade[]>(`${this.apiUrl}/derivadas/${unidadePaiId}`).pipe(
      catchError((error) => {
        console.error('Erro ao listar unidades derivadas:', error);
        return of([]);
      })
    );
  }

  calcularFatorTotal(id: number): Observable<number | null> {
    return this.http.get<number>(`${this.apiUrl}/${id}/fator-total`).pipe(
      catchError((error) => {
        console.error('Erro ao calcular fator total:', error);
        return of(null);
      })
    );
  }
}