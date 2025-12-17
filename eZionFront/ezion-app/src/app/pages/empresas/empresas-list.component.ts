import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Empresa } from '../../services/contabil/empresa.service';

@Component({
  selector: 'app-empresas-list',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="table-card surface">
      <div *ngIf="loading" class="loading-row">Carregando empresas...</div>
      <table class="table" *ngIf="!loading && empresas.length">
        <thead>
          <tr>
            <th>Nome fantasia</th>
            <th>Razão social</th>
            <th>CNPJ</th>
            <th>Regime</th>
            <th>Contribuinte</th>
            <th>Status</th>
            <th class="actions-col">Ações</th>
          </tr>
        </thead>
        <tbody>
          <tr *ngFor="let empresa of empresas">
            <td>{{ empresa.nomeFantasia || '-' }}</td>
            <td>{{ empresa.razaoSocial || '-' }}</td>
            <td>{{ empresa.cnpj || '-' }}</td>
            <td>{{ formatarEnum(empresa.regimeEscal) }}</td>
            <td>{{ formatarEnum(empresa.tipoContribuinte) }}</td>
            <td>
              <span class="status" [class.inactive]="empresa.ativa === false">
                {{ empresa.ativa === false ? 'Inativa' : 'Ativa' }}
              </span>
            </td>
            <td class="actions">
              <button type="button" class="ghost-btn" (click)="editar.emit(empresa)" title="Editar">✏️</button>
              <button type="button" class="ghost-btn danger" (click)="removerClick.emit(empresa)" [disabled]="deletingId === empresa.id" title="Excluir">
                <span *ngIf="deletingId === empresa.id">⏳</span>
                <span *ngIf="deletingId !== empresa.id">🗑️</span>
              </button>
            </td>
          </tr>
        </tbody>
      </table>
      <div *ngIf="!loading && !empresas.length" class="empty">Nenhuma empresa cadastrada.</div>
    </div>
  `,
  styles: [`
    .table-card {
      padding: 1rem;
      border-radius: var(--radius);
      border: 1px solid var(--border);
    }

    .loading-row,
    .empty {
      padding: 1rem;
      color: var(--text-300);
    }

    .table {
      width: 100%;
      border-collapse: collapse;
      color: var(--text-100);
    }

    .table thead th {
      text-align: left;
      padding: 0.75rem;
      background: rgba(255, 255, 255, 0.04);
      color: var(--text-200);
      font-weight: 600;
      border-bottom: 1px solid var(--border);
    }

    .table tbody td {
      padding: 0.75rem;
      border-bottom: 1px solid var(--border);
    }

    .table tbody tr:hover {
      background: rgba(255, 255, 255, 0.02);
    }

    .actions-col {
      width: 160px;
      text-align: center;
    }

    .status {
      display: inline-flex;
      align-items: center;
      gap: 0.4rem;
      padding: 0.35rem 0.75rem;
      border-radius: 999px;
      background: rgba(76, 209, 55, 0.15);
      color: #9af59f;
      border: 1px solid rgba(76, 209, 55, 0.3);
      font-weight: 600;
    }

    .status.inactive {
      background: rgba(255, 107, 107, 0.12);
      color: #ffc1c1;
      border-color: rgba(255, 107, 107, 0.28);
    }

    .actions {
      display: flex;
      gap: 0.5rem;
      justify-content: center;
    }

    .ghost-btn {
      background: transparent;
      border: 1px solid var(--border);
      color: var(--text-100);
      padding: 0.35rem 0.55rem;
      border-radius: 10px;
      cursor: pointer;
      transition: border-color 0.2s ease, background 0.2s ease;
    }

    .ghost-btn:hover {
      border-color: var(--brand-500);
      background: rgba(255, 255, 255, 0.06);
    }

    .ghost-btn.danger:hover {
      border-color: #ff7b7b;
      background: rgba(255, 107, 107, 0.1);
    }

    .ghost-btn:disabled {
      opacity: 0.6;
      cursor: not-allowed;
    }

    .ghost-btn:disabled:hover {
      border-color: var(--border);
      background: transparent;
    }
  `]
})
export class EmpresasListComponent {
  @Input() empresas: Empresa[] = [];
  @Input() loading = false;
  @Input() deletingId: number | null = null;
  @Output() editar = new EventEmitter<Empresa>();
  @Output() removerClick = new EventEmitter<Empresa>();

  formatarEnum(valor?: string): string {
    if (!valor) return '-';
    return valor.replace(/_/g, ' ');
  }
}
