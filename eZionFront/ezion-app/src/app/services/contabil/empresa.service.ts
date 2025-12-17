import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

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
  regimeEscal?: 'LUCRO_REAL' | 'LUCRO_PRESUMIDO' | 'SIMPLES_NACIONAL' | 'MEI';
  tipoContribuinte?: 'CONTRIBUINTE_ICMS' | 'CONTRIBUINTE_ISENTO' | 'NAO_CONTRIBUINTE';
  faturamentoAnual?: number;
  responsavelNome?: string;
  responsavelCPF?: string;
  responsavelEmail?: string;
  responsavelTelefone?: string;
  ativa?: boolean;
}

@Injectable({ providedIn: 'root' })
export class EmpresaService {
  private readonly API_URL = `${environment.apiUrl}/empresas`;

  constructor(private http: HttpClient) {}

  listar(): Observable<Empresa[]> {
    return this.http.get<Empresa[]>(this.API_URL);
  }

  obter(id: number): Observable<Empresa> {
    return this.http.get<Empresa>(`${this.API_URL}/${id}`);
  }

  criar(payload: Empresa): Observable<Empresa> {
    return this.http.post<Empresa>(this.API_URL, payload);
  }

  atualizar(id: number, payload: Empresa): Observable<Empresa> {
    return this.http.put<Empresa>(`${this.API_URL}/${id}`, payload);
  }

  remover(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`);
  }
}
