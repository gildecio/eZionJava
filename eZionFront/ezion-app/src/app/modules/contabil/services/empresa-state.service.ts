import { Injectable, signal, computed } from '@angular/core';
import { EmpresaService, Empresa } from './empresa.service';
import { BehaviorSubject, Observable } from 'rxjs';

export interface EmpresaState {
  empresas: Empresa[];
  carregando: boolean;
  filtro: 'todas' | 'ativas' | 'inativas';
  ultimoCarregamento: 'listarAtivas' | 'listarInativas' | 'listar' | null;
  error: string | null;
}

@Injectable({
  providedIn: 'root'
})
export class EmpresaStateService {
  // Estado usando Signals (Angular moderno)
  private state = signal<EmpresaState>({
    empresas: [],
    carregando: false,
    filtro: 'todas',
    ultimoCarregamento: null,
    error: null
  });

  // Signals públicos (readonly)
  readonly empresas = computed(() => this.state().empresas);
  readonly carregando = computed(() => this.state().carregando);
  readonly filtro = computed(() => this.state().filtro);
  readonly ultimoCarregamento = computed(() => this.state().ultimoCarregamento);
  readonly error = computed(() => this.state().error);

  // Computed signals para dados derivados
  readonly empresasFiltradas = computed(() => {
    const { empresas, filtro } = this.state();
    switch (filtro) {
      case 'ativas':
        return empresas.filter(emp => emp.ativa !== false);
      case 'inativas':
        return empresas.filter(emp => emp.ativa === false);
      default:
        return empresas;
    }
  });

  readonly temDados = computed(() => this.state().empresas.length > 0);
  readonly quantidadeEmpresas = computed(() => this.state().empresas.length);

  constructor(private empresaService: EmpresaService) {}

  // Actions para modificar o estado
  setCarregando(carregando: boolean) {
    this.state.update(state => ({ ...state, carregando, error: null }));
  }

  setFiltro(filtro: 'todas' | 'ativas' | 'inativas') {
    this.state.update(state => ({ ...state, filtro }));
  }

  setEmpresas(empresas: Empresa[], tipoCarregamento: 'listarAtivas' | 'listarInativas' | 'listar') {
    this.state.update(state => ({
      ...state,
      empresas,
      carregando: false,
      ultimoCarregamento: tipoCarregamento,
      error: null
    }));
  }

  setError(error: string) {
    this.state.update(state => ({
      ...state,
      carregando: false,
      error
    }));
  }

  // Actions para manipular empresas
  adicionarEmpresa(empresa: Empresa) {
    this.state.update(state => ({
      ...state,
      empresas: [...state.empresas, empresa]
    }));
  }

  atualizarEmpresa(empresa: Empresa) {
    this.state.update(state => ({
      ...state,
      empresas: state.empresas.map(emp =>
        emp.id === empresa.id ? empresa : emp
      )
    }));
  }

  removerEmpresa(id: number) {
    this.state.update(state => ({
      ...state,
      empresas: state.empresas.filter(emp => emp.id !== id)
    }));
  }

  // Actions assíncronas
  async carregarEmpresas(filtro: 'todas' | 'ativas' | 'inativas' = 'todas') {
    try {
      this.setCarregando(true);
      this.setFiltro(filtro);

      let request: Observable<Empresa[]>;
      let tipoCarregamento: 'listarAtivas' | 'listarInativas' | 'listar';

      switch (filtro) {
        case 'ativas':
          request = this.empresaService.listarAtivas();
          tipoCarregamento = 'listarAtivas';
          break;
        case 'inativas':
          request = this.empresaService.listarInativas();
          tipoCarregamento = 'listarInativas';
          break;
        default:
          request = this.empresaService.listar();
          tipoCarregamento = 'listar';
          break;
      }

      const empresas = await request.toPromise() || [];
      this.setEmpresas(empresas, tipoCarregamento);

    } catch (error) {
      console.error('Erro ao carregar empresas:', error);
      this.setError('Erro ao carregar empresas');
    }
  }

  async recarregar() {
    const ultimoTipo = this.state().ultimoCarregamento;
    
    // Se nunca foi carregado ou não há histórico, carrega todas
    if (!ultimoTipo) {
      await this.carregarEmpresas('todas');
      return;
    }

    const filtroMap = {
      'listarAtivas': 'ativas' as const,
      'listarInativas': 'inativas' as const,
      'listar': 'todas' as const
    };

    await this.carregarEmpresas(filtroMap[ultimoTipo]);
  }

  async salvarEmpresa(empresa: Empresa): Promise<Empresa> {
    try {
      let empresaSalva: Empresa | null | undefined;

      if (empresa.id) {
        empresaSalva = await this.empresaService.atualizar(empresa.id, empresa).toPromise();
      } else {
        empresaSalva = await this.empresaService.criar(empresa).toPromise();
      }

      if (!empresaSalva) {
        throw new Error('Falha ao salvar empresa');
      }

      if (empresa.id) {
        this.atualizarEmpresa(empresaSalva);
      } else {
        this.adicionarEmpresa(empresaSalva);
      }

      return empresaSalva;
    } catch (error) {
      console.error('Erro ao salvar empresa:', error);
      this.setError('Erro ao salvar empresa');
      throw error;
    }
  }

  async excluirEmpresa(id: number): Promise<void> {
    try {
      await this.empresaService.deletar(id).toPromise();
      this.removerEmpresa(id);
    } catch (error) {
      console.error('Erro ao excluir empresa:', error);
      this.setError('Erro ao excluir empresa');
      throw error;
    }
  }

  // Método para compatibilidade com código existente
  getEmpresasObservable(): Observable<Empresa[]> {
    return new BehaviorSubject(this.empresas()).asObservable();
  }

  getCarregandoObservable(): Observable<boolean> {
    return new BehaviorSubject(this.carregando()).asObservable();
  }
}