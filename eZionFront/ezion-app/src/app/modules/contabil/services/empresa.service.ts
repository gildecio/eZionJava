import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, catchError, of } from 'rxjs';
import { API_CONFIG } from '../../../config/api.config';

export interface Empresa {
  id?: number;
  razaoSocial: string;
  nomeFantasia: string;
  cnpj: string;
  inscricaoEstadual?: string;
  inscricaoMunicipal?: string;
  email?: string;
  telefone?: string;
  logradouro?: string;
  numero?: string;
  complemento?: string;
  bairro?: string;
  cidade?: string;
  estado?: string;
  cep?: string;
  regimeEscal?: string;
  tipoContribuinte?: string;
  aliquotaPIS?: string;
  aliquotaCOFINS?: string;
  aliquotaIRRF?: string;
  aliquotaINSS?: string;
  faturamentoAnual?: number;
  responsavelNome?: string;
  responsavelCPF?: string;
  responsavelEmail?: string;
  responsavelTelefone?: string;
  dataCriacao?: string;
  dataAtualizacao?: string;
  ativa?: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class EmpresaService {
  private apiUrl = `${API_CONFIG.baseUrl}/empresas`;

  constructor(private http: HttpClient) {}

  listar(): Observable<Empresa[]> {
    return this.http.get<Empresa[]>(this.apiUrl).pipe(
      catchError((error) => {
        console.error('Erro ao listar empresas:', error);
        return of([]);
      })
    );
  }

  obter(id: number): Observable<Empresa | null> {
    return this.http.get<Empresa>(`${this.apiUrl}/${id}`).pipe(
      catchError((error) => {
        console.error('Erro ao obter empresa:', error);
        return of(null);
      })
    );
  }

  criar(empresa: Empresa): Observable<Empresa | null> {
    return this.http.post<Empresa>(this.apiUrl, empresa).pipe(
      catchError((error) => {
        console.error('Erro ao criar empresa:', error);
        return of(null);
      })
    );
  }

  atualizar(id: number, empresa: Empresa): Observable<Empresa | null> {
    return this.http.put<Empresa>(`${this.apiUrl}/${id}`, empresa).pipe(
      catchError((error) => {
        console.error('Erro ao atualizar empresa:', error);
        return of(null);
      })
    );
  }

  deletar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`).pipe(
      catchError((error) => {
        console.error('Erro ao deletar empresa:', error);
        return of(void 0);
      })
    );
  }

  listarAtivas(): Observable<Empresa[]> {
    return this.http.get<Empresa[]>(`${this.apiUrl}/ativas`).pipe(
      catchError((error) => {
        console.error('Erro ao listar empresas ativas:', error);
        return of([]);
      })
    );
  }

  listarInativas(): Observable<Empresa[]> {
    return this.http.get<Empresa[]>(`${this.apiUrl}/inativas`).pipe(
      catchError((error) => {
        console.error('Erro ao listar empresas inativas:', error);
        return of([]);
      })
    );
  }

  obterPorCnpj(cnpj: string): Observable<Empresa | null> {
    return this.http.get<Empresa>(`${this.apiUrl}/cnpj/${cnpj}`).pipe(
      catchError((error) => {
        console.error('Erro ao obter empresa por CNPJ:', error);
        return of(null);
      })
    );
  }
}
