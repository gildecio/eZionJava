import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Empresa, EmpresaService } from '../../services/contabil/empresa.service';
import { ConfirmDeleteService } from '../../services/confirm-delete.service';
import { EmpresasListComponent } from './empresas-list.component';
import { EmpresasFormComponent } from './empresas-form.component';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { ChangeDetectorRef } from '@angular/core';



@Component({
  selector: 'app-empresas',
  standalone: true,
  imports: [CommonModule, EmpresasListComponent, EmpresasFormComponent],
  templateUrl: './empresas.html',
  styleUrls: ['./empresas.css']
})
export class Empresas implements OnInit, OnDestroy {
  empresas: Empresa[] = [];
  loading = false;
  deletingId: number | null = null;
  editingEmpresa: Empresa | null = null;
  isShowingForm = false;
  feedback: { type: 'success' | 'error'; text: string } | null = null;

  private destroy$ = new Subject<void>();

  constructor(
    private empresaService: EmpresaService,
    private confirmDeleteService: ConfirmDeleteService,
    private cdRef: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  buscarEmpresas(): void {
    this.loading = true;
    this.empresaService.listar()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (empresas) => {
          this.empresas = empresas;
          this.loading = false;
          this.cdRef.detectChanges();
        },
        error: () => {
          this.setFeedback('error', 'Não foi possível carregar empresas');
          this.loading = false;
          this.cdRef.detectChanges();
        }
      });
  }

  novaEmpresa(): void {
    this.editingEmpresa = null;
    this.isShowingForm = true;
  }

  editarEmpresa(empresa: Empresa): void {
    this.editingEmpresa = empresa;
    this.isShowingForm = true;
  }

  async salvarEmpresa(formValue: Partial<Empresa>): Promise<void> {
    const payload: Empresa = { ...this.editingEmpresa, ...formValue } as Empresa;

    if (payload.id) {
      this.empresaService.atualizar(payload.id, payload)
        .pipe(takeUntil(this.destroy$))
        .subscribe({
          next: () => {
            this.setFeedback('success', 'Empresa atualizada');
            this.fecharForm();
            this.buscarEmpresas();
          },
          error: () => {
            this.setFeedback('error', 'Não foi possível salvar a empresa');
          }
        });
    } else {
      this.empresaService.criar(payload)
        .pipe(takeUntil(this.destroy$))
        .subscribe({
          next: () => {
            this.setFeedback('success', 'Empresa criada');
            this.fecharForm();
            this.buscarEmpresas();
          },
          error: () => {
            this.setFeedback('error', 'Não foi possível salvar a empresa');
          }
        });
    }
  }

  removerEmpresa(empresa: Empresa): void {
    console.log('removerEmpresa chamado para:', empresa);
    this.confirmDeleteService.openConfirmDelete(
      empresa,
      () => {
        console.log('Callback de confirmação executando...');
        this.confirmarDelecao(empresa);
      },
      () => {
        console.log('Callback de cancelamento executando...');
      }
    );
  }

  private confirmarDelecao(empresa: Empresa): void {
    console.log('confirmarDelecao chamado para:', empresa);
    if (!empresa.id) {
      console.error('ID da empresa não encontrado');
      return;
    }

    this.deletingId = empresa.id;
    console.log('Chamando remover com ID:', empresa.id);

    this.empresaService.remover(empresa.id)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: () => {
          console.log('Exclusão bem-sucedida!');
          this.setFeedback('success', 'Empresa excluída');
          this.deletingId = null;
          this.buscarEmpresas();
        },
        error: (error) => {
          console.error('Erro ao excluir empresa:', error);
          this.setFeedback('error', 'Não foi possível excluir a empresa');
          this.deletingId = null;
        }
      });
  }

  fecharForm(): void {
    this.editingEmpresa = null;
    this.isShowingForm = false;
  }

  private setFeedback(type: 'success' | 'error', text: string): void {
    this.feedback = { type, text };
    setTimeout(() => {
      if (this.feedback?.text === text) {
        this.feedback = null;
      }
    }, 3500);
  }
}
