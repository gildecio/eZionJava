import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ConfirmDeleteService } from '../../services/confirm-delete.service';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

@Component({
  selector: 'app-confirm-delete-modal',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="modal-overlay" *ngIf="isOpen" (click)="onBackdropClick()">
      <div class="modal-content" (click)="$event.stopPropagation()">
        <div class="modal-header">
          <div class="icon-danger">🗑️</div>
          <h2>Excluir Empresa</h2>
        </div>

        <div class="modal-body">
          <p class="warning-text">
            Tem certeza que deseja excluir
            <strong>{{ item?.nomeFantasia || item?.razaoSocial || 'este item' }}</strong>?
          </p>
          <p class="info-text">
            Esta ação não pode ser desfeita.
          </p>
        </div>

        <div class="modal-footer">
          <button type="button" class="btn-cancel" (click)="onCancel()">
            Cancelar
          </button>
          <button type="button" class="btn-delete" (click)="onConfirm()">
            Excluir
          </button>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .modal-overlay {
      position: fixed;
      inset: 0;
      background: rgba(0, 0, 0, 0.3);
      display: grid;
      place-items: center;
      padding: 1rem;
      z-index: 3000;
      animation: fadeIn 0.2s ease;
    }

    @keyframes fadeIn {
      from {
        opacity: 0;
      }
      to {
        opacity: 1;
      }
    }

    .modal-content {
      width: min(420px, 100%);
      background: linear-gradient(135deg, #ffffff 0%, #f0f5ff 100%);
      border: 1px solid rgba(79, 139, 245, 0.2);
      border-radius: var(--radius);
      padding: 2rem;
      box-shadow: 0 20px 60px rgba(79, 139, 245, 0.15);
      animation: slideUp 0.3s ease;
      backdrop-filter: blur(10px);
    }

    @keyframes slideUp {
      from {
        transform: translateY(30px);
        opacity: 0;
      }
      to {
        transform: translateY(0);
        opacity: 1;
      }
    }

    .modal-header {
      display: flex;
      flex-direction: column;
      align-items: center;
      gap: 1rem;
      margin-bottom: 1.5rem;
    }

    .icon-danger {
      width: 60px;
      height: 60px;
      display: flex;
      align-items: center;
      justify-content: center;
      background: linear-gradient(135deg, rgba(244, 102, 102, 0.1) 0%, rgba(255, 140, 140, 0.1) 100%);
      border: 2px solid rgba(255, 102, 102, 0.3);
      border-radius: 50%;
      font-size: 1.8rem;
    }

    .modal-header h2 {
      margin: 0;
      color: #2c3e50;
      font-size: 1.5rem;
      text-align: center;
      font-weight: 600;
    }

    .modal-body {
      margin-bottom: 2rem;
    }

    .warning-text {
      color: #2c3e50;
      font-size: 1rem;
      margin: 0 0 0.75rem 0;
      line-height: 1.5;
    }

    .warning-text strong {
      color: #e74c3c;
      font-weight: 700;
    }

    .info-text {
      color: #7f8c8d;
      font-size: 0.875rem;
      margin: 0;
      line-height: 1.4;
    }

    .modal-footer {
      display: flex;
      gap: 1rem;
      justify-content: flex-end;
    }

    .btn-cancel,
    .btn-delete {
      padding: 0.75rem 1.5rem;
      border-radius: 8px;
      font-weight: 500;
      cursor: pointer;
      transition: all 0.2s ease;
      border: none;
      font-size: 0.95rem;
    }

    .btn-cancel {
      background: #f8f9fb;
      color: #2c3e50;
      border: 1.5px solid rgba(79, 139, 245, 0.2);
    }

    .btn-cancel:hover {
      background: #ecf0f8;
      border-color: #4f8bf5;
    }

    .btn-delete {
      background: linear-gradient(135deg, #ff6b6b 0%, #ff5252 100%);
      color: #fff;
      border: 1px solid #ff5252;
    }

    .btn-delete:hover {
      background: linear-gradient(135deg, #ff5252 0%, #ff3333 100%);
      transform: translateY(-2px);
      box-shadow: 0 8px 16px rgba(255, 82, 82, 0.25);
    }

    .btn-delete:active {
      transform: translateY(0);
    }

    @media (max-width: 500px) {
      .modal-content {
        padding: 1.5rem;
      }

      .icon-danger {
        width: 50px;
        height: 50px;
        font-size: 1.5rem;
      }

      .modal-header h2 {
        font-size: 1.25rem;
      }

      .modal-footer {
        flex-direction: column;
      }

      .btn-cancel,
      .btn-delete {
        width: 100%;
      }
    }
  `]
})
export class ConfirmDeleteModalComponent implements OnInit, OnDestroy {
  isOpen = false;
  item: any = null;

  private destroy$ = new Subject<void>();

  constructor(private confirmDeleteService: ConfirmDeleteService) {}

  ngOnInit(): void {
    this.confirmDeleteService.state
      .pipe(takeUntil(this.destroy$))
      .subscribe(state => {
        this.isOpen = state.isOpen;
        this.item = state.item;
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  onConfirm(): void {
    console.log('Modal onConfirm clicado, item:', this.item);
    this.confirmDeleteService.confirm();
  }

  onCancel(): void {
    console.log('Modal onCancel clicado');
    this.confirmDeleteService.cancel();
  }

  onBackdropClick(): void {
    console.log('Backdrop clicado');
    this.confirmDeleteService.cancel();
  }
}
