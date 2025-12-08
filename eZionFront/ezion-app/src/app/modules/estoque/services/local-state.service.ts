import { Injectable, signal, computed } from '@angular/core';
import { LocalService, Local } from './local.service';

export interface LocalState {
  locais: Local[];
  carregando: boolean;
  filtro: 'todas' | 'ativos' | 'inativos';
  ultimoCarregamento: 'listarAtivos' | 'listarInativos' | 'listar' | null;
  error: string | null;
}

@Injectable({
  providedIn: 'root'
})
export class LocalStateService {
  // Estado usando Signals (Angular moderno)
  private state = signal<LocalState>({
    locais: [],
    carregando: false,
    filtro: 'todas',
    ultimoCarregamento: null,
    error: null
  });

  // Signals públicos (readonly)
  readonly locais = computed(() => this.state().locais);
  readonly carregando = computed(() => this.state().carregando);
  readonly filtro = computed(() => this.state().filtro);
  readonly ultimoCarregamento = computed(() => this.state().ultimoCarregamento);
  readonly error = computed(() => this.state().error);

  // Computed signals para dados derivados
  readonly locaisFiltrados = computed(() => {
    const { locais, filtro } = this.state();
    switch (filtro) {
      case 'ativos':
        return locais.filter(local => local.ativo !== false);
      case 'inativos':
        return locais.filter(local => local.ativo === false);
      default:
        return locais;
    }
  });

  readonly temDados = computed(() => this.state().locais.length > 0);
  readonly quantidadeLocais = computed(() => this.state().locais.length);

  constructor(private localService: LocalService) {}

  // Métodos para atualizar o estado
  private updateState(updates: Partial<LocalState>) {
    this.state.update(current => ({ ...current, ...updates }));
  }

  // Métodos públicos para manipulação do estado
  setFiltro(filtro: 'todas' | 'ativos' | 'inativos') {
    this.updateState({ filtro });
  }

  setCarregando(carregando: boolean) {
    this.updateState({ carregando });
  }

  setError(error: string | null) {
    this.updateState({ error });
  }

  // Métodos para carregar dados
  async listar() {
    this.setCarregando(true);
    this.setError(null);

    try {
      const locais = await this.localService.listar().toPromise() || [];
      this.updateState({
        locais,
        carregando: false,
        ultimoCarregamento: 'listar'
      });
    } catch (error) {
      this.setError('Erro ao carregar locais');
      this.setCarregando(false);
    }
  }

  async listarAtivos() {
    this.setCarregando(true);
    this.setError(null);

    try {
      const locais = await this.localService.listarAtivos().toPromise() || [];
      this.updateState({
        locais,
        carregando: false,
        ultimoCarregamento: 'listarAtivos'
      });
    } catch (error) {
      this.setError('Erro ao carregar locais ativos');
      this.setCarregando(false);
    }
  }

  async listarInativos() {
    this.setCarregando(true);
    this.setError(null);

    try {
      // Como não temos endpoint específico para inativos, vamos listar todas e filtrar
      const locais = await this.localService.listar().toPromise() || [];
      const locaisInativos = locais.filter(l => l.ativo === false);
      this.updateState({
        locais: locaisInativos,
        carregando: false,
        ultimoCarregamento: 'listarInativos'
      });
    } catch (error) {
      this.setError('Erro ao carregar locais inativos');
      this.setCarregando(false);
    }
  }

  // Método para recarregar baseado no último carregamento
  async recarregar() {
    const ultimo = this.state().ultimoCarregamento;
    switch (ultimo) {
      case 'listarAtivos':
        await this.listarAtivos();
        break;
      case 'listarInativos':
        await this.listarInativos();
        break;
      default:
        await this.listar();
    }
  }

  // Método para adicionar local ao estado local (otimista)
  adicionarLocal(local: Local) {
    this.state.update(current => ({
      ...current,
      locais: [...current.locais, local]
    }));
  }

  // Método para atualizar local no estado local
  atualizarLocal(local: Local) {
    this.state.update(current => ({
      ...current,
      locais: current.locais.map(l => l.id === local.id ? local : l)
    }));
  }

  // Método para remover local do estado local
  removerLocal(id: number) {
    this.state.update(current => ({
      ...current,
      locais: current.locais.filter(l => l.id !== id)
    }));
  }

  // Método para ativar/desativar local no estado local
  toggleAtivo(id: number) {
    this.state.update(current => ({
      ...current,
      locais: current.locais.map(l =>
        l.id === id ? { ...l, ativo: !l.ativo } : l
      )
    }));
  }

  // Métodos públicos para operações diretas
  async ativarLocal(id: number): Promise<Local | null> {
    try {
      const local = await this.localService.ativar(id).toPromise();
      if (local) {
        this.atualizarLocal(local);
      }
      return local || null;
    } catch (error) {
      console.error('Erro ao ativar local:', error);
      return null;
    }
  }

  async desativarLocal(id: number): Promise<Local | null> {
    try {
      const local = await this.localService.desativar(id).toPromise();
      if (local) {
        this.atualizarLocal(local);
      }
      return local || null;
    } catch (error) {
      console.error('Erro ao desativar local:', error);
      return null;
    }
  }

  async excluirLocal(id: number): Promise<boolean> {
    try {
      const sucesso = await this.localService.excluir(id).toPromise();
      if (sucesso !== false) {
        this.removerLocal(id);
        return true;
      }
      return false;
    } catch (error) {
      console.error('Erro ao excluir local:', error);
      return false;
    }
  }
}