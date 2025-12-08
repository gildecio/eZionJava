import { Component, OnInit, Output, EventEmitter, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LocalStateService } from '../../services/local-state.service';
import { Local } from '../../services/local.service';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { ConfirmDialogComponent } from '../../../../shared/components/confirm-dialog/confirm-dialog.component';
import { ButtonModule } from 'primeng/button';
import { TooltipModule } from 'primeng/tooltip';

@Component({
  selector: 'app-local-listagem',
  standalone: true,
  imports: [CommonModule, ConfirmDialogComponent, ButtonModule, TooltipModule],
  templateUrl: './local-listagem.html',
  styleUrl: './locais.css'
})
export class LocalListagemComponent implements OnInit, OnDestroy {
  private destroy$ = new Subject<void>();

  // Estado reativo usando o serviço global - inicializado no ngOnInit
  locais: any;
  locaisFiltrados: any;
  carregando: any;
  filtro: any;
  temDados: any;
  quantidadeLocais: any;
  error: any;

  // Estado do diálogo de confirmação
  confirmDialogVisible = false;
  confirmDialogTitle = '';
  confirmDialogMessage = '';
  confirmDialogLabel = '';
  private confirmCallback: (() => void) | null = null;

  @Output() novoLocal = new EventEmitter<void>();
  @Output() editarLocal = new EventEmitter<Local>();
  @Output() visualizarLocal = new EventEmitter<Local>();

  constructor(private localStateService: LocalStateService) {}

  ngOnInit() {
    // Inicializar signals após a injeção de dependência
    this.locais = this.localStateService.locais;
    this.locaisFiltrados = this.localStateService.locaisFiltrados;
    this.carregando = this.localStateService.carregando;
    this.filtro = this.localStateService.filtro;
    this.temDados = this.localStateService.temDados;
    this.quantidadeLocais = this.localStateService.quantidadeLocais;
    this.error = this.localStateService.error;
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

  // Getters para compatibilidade com código existente
  get ultimoCarregamento() {
    return this.localStateService.ultimoCarregamento();
  }

  // Actions delegadas para o serviço de estado
  async carregarLocais() {
    await this.localStateService.listar();
  }

  alterarFiltro(filtro: 'todas' | 'ativos' | 'inativos') {
    this.localStateService.setFiltro(filtro);
  }

  async recarregar() {
    await this.localStateService.recarregar();
  }

  // Método de compatibilidade - agora usa o serviço de estado
  inserirOuAtualizarLocal(local: Local) {
    if (local.id) {
      this.localStateService.atualizarLocal(local);
    } else {
      this.localStateService.adicionarLocal(local);
    }
  }

  // Event handlers
  onNovoLocal() {
    this.novoLocal.emit();
  }

  onEditarLocal(local: Local) {
    this.editarLocal.emit(local);
  }

  onVisualizarLocal(local: Local) {
    this.visualizarLocal.emit(local);
  }

  async deletarLocal(id: number) {
    this.confirmDialogTitle = 'Confirmar Exclusão';
    this.confirmDialogMessage = 'Tem certeza que deseja deletar este local? Esta ação não pode ser desfeita.';
    this.confirmDialogLabel = 'Excluir';
    this.confirmCallback = async () => {
      try {
        await this.localStateService.excluirLocal(id);
      } catch (error) {
        console.error('Erro ao deletar local:', error);
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