import { Component, OnInit, Output, EventEmitter, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UnidadeStateService } from '../../services/unidade-state.service';
import { Unidade } from '../../services/unidade.service';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { ConfirmDialogComponent } from '../../../../shared/components/confirm-dialog/confirm-dialog.component';
import { ButtonModule } from 'primeng/button';
import { TooltipModule } from 'primeng/tooltip';

@Component({
  selector: 'app-unidade-listagem',
  standalone: true,
  imports: [CommonModule, ConfirmDialogComponent, ButtonModule, TooltipModule],
  templateUrl: './unidade-listagem.html',
  styleUrl: './unidades.css'
})
export class UnidadeListagemComponent implements OnInit, OnDestroy {
  private destroy$ = new Subject<void>();

  // Estado reativo usando o serviço global - inicializado no ngOnInit
  unidades: any;
  unidadesFiltradas: any;
  carregando: any;
  filtro: any;
  temDados: any;
  quantidadeUnidades: any;
  error: any;

  // Estado do diálogo de confirmação
  confirmDialogVisible = false;
  confirmDialogTitle = '';
  confirmDialogMessage = '';
  confirmDialogLabel = '';
  private confirmCallback: (() => void) | null = null;

  @Output() novaUnidade = new EventEmitter<void>();
  @Output() editarUnidade = new EventEmitter<Unidade>();
  @Output() visualizarUnidade = new EventEmitter<Unidade>();

  constructor(private unidadeStateService: UnidadeStateService) {}

  ngOnInit() {
    // Inicializar signals após a injeção de dependência
    this.unidades = this.unidadeStateService.unidades;
    this.unidadesFiltradas = this.unidadeStateService.unidadesFiltradas;
    this.carregando = this.unidadeStateService.carregando;
    this.filtro = this.unidadeStateService.filtro;
    this.temDados = this.unidadeStateService.temDados;
    this.quantidadeUnidades = this.unidadeStateService.quantidadeUnidades;
    this.error = this.unidadeStateService.error;
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

  // Getters para compatibilidade com código existente
  get ultimoCarregamento() {
    return this.unidadeStateService.ultimoCarregamento();
  }

  // Actions delegadas para o serviço de estado
  async carregarUnidades() {
    await this.unidadeStateService.listar();
  }

  alterarFiltro(filtro: 'todas' | 'ativas' | 'inativas') {
    this.unidadeStateService.setFiltro(filtro);
  }

  async recarregar() {
    await this.unidadeStateService.recarregar();
  }

  // Método de compatibilidade - agora usa o serviço de estado
  inserirOuAtualizarUnidade(unidade: Unidade) {
    if (unidade.id) {
      this.unidadeStateService.atualizarUnidade(unidade);
    } else {
      this.unidadeStateService.adicionarUnidade(unidade);
    }
  }

  // Event handlers
  onNovaUnidade() {
    this.novaUnidade.emit();
  }

  onEditarUnidade(unidade: Unidade) {
    this.editarUnidade.emit(unidade);
  }

  onVisualizarUnidade(unidade: Unidade) {
    this.visualizarUnidade.emit(unidade);
  }

  async deletarUnidade(id: number) {
    this.confirmDialogTitle = 'Confirmar Exclusão';
    this.confirmDialogMessage = 'Tem certeza que deseja deletar esta unidade? Esta ação não pode ser desfeita.';
    this.confirmDialogLabel = 'Excluir';
    this.confirmCallback = async () => {
      try {
        await this.unidadeStateService.excluirUnidade(id);
      } catch (error) {
        console.error('Erro ao deletar unidade:', error);
      }
    };
    this.confirmDialogVisible = true;
  }

  onConfirmDialogConfirmed() {
    this.confirmDialogVisible = false;
    if (this.confirmCallback) {
      this.confirmCallback();
      this.confirmCallback = null;
    }
  }

  onConfirmDialogCancelled() {
    this.confirmDialogVisible = false;
    this.confirmCallback = null;
  }
}