import { Injectable, signal, computed } from '@angular/core';
import { UnidadeService, Unidade } from './unidade.service';

export interface UnidadeState {
  unidades: Unidade[];
  carregando: boolean;
  filtro: 'todas' | 'ativas' | 'inativas';
  ultimoCarregamento: 'listarAtivas' | 'listarInativas' | 'listar' | null;
  error: string | null;
}

@Injectable({
  providedIn: 'root'
})
export class UnidadeStateService {
  // Estado usando Signals (Angular moderno)
  private state = signal<UnidadeState>({
    unidades: [],
    carregando: false,
    filtro: 'todas',
    ultimoCarregamento: null,
    error: null
  });

  // Signals públicos (readonly)
  readonly unidades = computed(() => this.state().unidades);
  readonly carregando = computed(() => this.state().carregando);
  readonly filtro = computed(() => this.state().filtro);
  readonly ultimoCarregamento = computed(() => this.state().ultimoCarregamento);
  readonly error = computed(() => this.state().error);

  // Computed signals para dados derivados
  readonly unidadesFiltradas = computed(() => {
    const { unidades, filtro } = this.state();
    switch (filtro) {
      case 'ativas':
        return unidades.filter(unid => unid.ativo !== false);
      case 'inativas':
        return unidades.filter(unid => unid.ativo === false);
      default:
        return unidades;
    }
  });

  readonly temDados = computed(() => this.state().unidades.length > 0);
  readonly quantidadeUnidades = computed(() => this.state().unidades.length);

  constructor(private unidadeService: UnidadeService) {}

  // Métodos para atualizar o estado
  private updateState(updates: Partial<UnidadeState>) {
    this.state.update(current => ({ ...current, ...updates }));
  }

  // Métodos públicos para manipulação do estado
  setFiltro(filtro: 'todas' | 'ativas' | 'inativas') {
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
      const unidades = await this.unidadeService.listar().toPromise() || [];
      this.updateState({
        unidades,
        carregando: false,
        ultimoCarregamento: 'listar'
      });
    } catch (error) {
      this.setError('Erro ao carregar unidades');
      this.setCarregando(false);
    }
  }

  async listarAtivas() {
    this.setCarregando(true);
    this.setError(null);

    try {
      const unidades = await this.unidadeService.listarAtivas().toPromise() || [];
      this.updateState({
        unidades,
        carregando: false,
        ultimoCarregamento: 'listarAtivas'
      });
    } catch (error) {
      this.setError('Erro ao carregar unidades ativas');
      this.setCarregando(false);
    }
  }

  async listarInativas() {
    this.setCarregando(true);
    this.setError(null);

    try {
      // Como não temos endpoint específico para inativas, vamos listar todas e filtrar
      const unidades = await this.unidadeService.listar().toPromise() || [];
      const unidadesInativas = unidades.filter(u => u.ativo === false);
      this.updateState({
        unidades: unidadesInativas,
        carregando: false,
        ultimoCarregamento: 'listarInativas'
      });
    } catch (error) {
      this.setError('Erro ao carregar unidades inativas');
      this.setCarregando(false);
    }
  }

  // Método para recarregar baseado no último carregamento
  async recarregar() {
    const ultimo = this.state().ultimoCarregamento;
    switch (ultimo) {
      case 'listarAtivas':
        await this.listarAtivas();
        break;
      case 'listarInativas':
        await this.listarInativas();
        break;
      default:
        await this.listar();
    }
  }

  // Método para adicionar unidade ao estado local (otimista)
  adicionarUnidade(unidade: Unidade) {
    this.state.update(current => ({
      ...current,
      unidades: [...current.unidades, unidade]
    }));
  }

  // Método para atualizar unidade no estado local
  atualizarUnidade(unidade: Unidade) {
    this.state.update(current => ({
      ...current,
      unidades: current.unidades.map(u => u.id === unidade.id ? unidade : u)
    }));
  }

  // Método para remover unidade do estado local
  removerUnidade(id: number) {
    this.state.update(current => ({
      ...current,
      unidades: current.unidades.filter(u => u.id !== id)
    }));
  }

  // Método para ativar/desativar unidade no estado local
  toggleAtivo(id: number) {
    this.state.update(current => ({
      ...current,
      unidades: current.unidades.map(u =>
        u.id === id ? { ...u, ativo: !u.ativo } : u
      )
    }));
  }

  // Métodos públicos para operações diretas
  async ativarUnidade(id: number): Promise<Unidade | null> {
    try {
      const unidade = await this.unidadeService.ativar(id).toPromise();
      if (unidade) {
        this.atualizarUnidade(unidade);
      }
      return unidade || null;
    } catch (error) {
      console.error('Erro ao ativar unidade:', error);
      return null;
    }
  }

  async desativarUnidade(id: number): Promise<Unidade | null> {
    try {
      const unidade = await this.unidadeService.desativar(id).toPromise();
      if (unidade) {
        this.atualizarUnidade(unidade);
      }
      return unidade || null;
    } catch (error) {
      console.error('Erro ao desativar unidade:', error);
      return null;
    }
  }

  async excluirUnidade(id: number): Promise<boolean> {
    try {
      const sucesso = await this.unidadeService.excluir(id).toPromise();
      if (sucesso !== false) {
        this.removerUnidade(id);
        return true;
      }
      return false;
    } catch (error) {
      console.error('Erro ao excluir unidade:', error);
      return false;
    }
  }
}