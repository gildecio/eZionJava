import { Component, OnInit, Output, EventEmitter, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EmpresaStateService } from '../../services/empresa-state.service';
import { Empresa } from '../../services/empresa.service';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { ConfirmDialogComponent } from '../../../../shared/components/confirm-dialog/confirm-dialog.component';
import { ButtonModule } from 'primeng/button';
import { TooltipModule } from 'primeng/tooltip';
import { CnpjMaskPipe } from '../../../../shared/pipes/cnpj-mask.pipe';

@Component({
  selector: 'app-empresa-listagem',
  standalone: true,
  imports: [CommonModule, ConfirmDialogComponent, ButtonModule, TooltipModule, CnpjMaskPipe],
  templateUrl: './empresa-listagem.html',
  styleUrl: './empresas.css'
})
export class EmpresaListagemComponent implements OnInit, OnDestroy {
  private destroy$ = new Subject<void>();

  // Estado reativo usando o serviço global - inicializado no ngOnInit
  empresas: any;
  empresasFiltradas: any;
  carregando: any;
  filtro: any;
  temDados: any;
  quantidadeEmpresas: any;
  error: any;

  // Estado do diálogo de confirmação
  confirmDialogVisible = false;
  confirmDialogTitle = '';
  confirmDialogMessage = '';
  confirmDialogLabel = '';
  private confirmCallback: (() => void) | null = null;

  @Output() novaEmpresa = new EventEmitter<void>();
  @Output() editarEmpresa = new EventEmitter<Empresa>();
  @Output() visualizarEmpresa = new EventEmitter<Empresa>();

  constructor(private empresaStateService: EmpresaStateService) {}

  ngOnInit() {
    // Inicializar signals após a injeção de dependência
    this.empresas = this.empresaStateService.empresas;
    this.empresasFiltradas = this.empresaStateService.empresasFiltradas;
    this.carregando = this.empresaStateService.carregando;
    this.filtro = this.empresaStateService.filtro;
    this.temDados = this.empresaStateService.temDados;
    this.quantidadeEmpresas = this.empresaStateService.quantidadeEmpresas;
    this.error = this.empresaStateService.error;
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

  // Getters para compatibilidade com código existente
  get ultimoCarregamento() {
    return this.empresaStateService.ultimoCarregamento();
  }

  // Actions delegadas para o serviço de estado
  async carregarEmpresas() {
    await this.empresaStateService.carregarEmpresas(this.filtro());
  }

  alterarFiltro(filtro: 'todas' | 'ativas' | 'inativas') {
    this.empresaStateService.setFiltro(filtro);
  }

  async recarregar() {
    await this.empresaStateService.recarregar();
  }

  // Método de compatibilidade - agora usa o serviço de estado
  inserirOuAtualizarEmpresa(empresa: Empresa) {
    if (empresa.id) {
      this.empresaStateService.atualizarEmpresa(empresa);
    } else {
      this.empresaStateService.adicionarEmpresa(empresa);
    }
  }

  // Event handlers
  onNovaEmpresa() {
    this.novaEmpresa.emit();
  }

  onEditarEmpresa(empresa: Empresa) {
    this.editarEmpresa.emit(empresa);
  }

  onVisualizarEmpresa(empresa: Empresa) {
    this.visualizarEmpresa.emit(empresa);
  }

  async deletarEmpresa(id: number) {
    this.confirmDialogTitle = 'Confirmar Exclusão';
    this.confirmDialogMessage = 'Tem certeza que deseja deletar esta empresa? Esta ação não pode ser desfeita.';
    this.confirmDialogLabel = 'Excluir';
    this.confirmCallback = async () => {
      try {
        await this.empresaStateService.excluirEmpresa(id);
      } catch (error) {
        console.error('Erro ao deletar empresa:', error);
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
