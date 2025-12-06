import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ButtonModule } from 'primeng/button';

@Component({
  selector: 'app-confirm-dialog',
  standalone: true,
  imports: [CommonModule, ButtonModule],
  template: `
    <div class="confirm-overlay" *ngIf="visible" (click)="onCancel()">
      <div class="confirm-dialog" (click)="$event.stopPropagation()">
        <div class="confirm-header">
          <h3 class="confirm-title">{{ title }}</h3>
          <button class="close-btn" (click)="onCancel()" type="button">
            <span>&times;</span>
          </button>
        </div>
        <div class="confirm-body">
          <i class="pi pi-exclamation-triangle confirm-icon"></i>
          <p class="confirm-message">{{ message }}</p>
        </div>
        <div class="confirm-footer">
          <button pButton type="button" label="Cancelar" class="p-button-secondary" (click)="onCancel()"></button>
          <button pButton type="button" [label]="confirmLabel" class="p-button-danger" (click)="onConfirm()"></button>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .confirm-overlay {
      position: fixed;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      background: rgba(0, 0, 0, 0.5);
      display: flex;
      align-items: center;
      justify-content: center;
      z-index: 1000;
      animation: fadeIn 0.2s ease-in;
    }

    @keyframes fadeIn {
      from {
        opacity: 0;
      }
      to {
        opacity: 1;
      }
    }

    .confirm-dialog {
      background: white;
      border-radius: 8px;
      box-shadow: 0 10px 40px rgba(0, 0, 0, 0.15);
      max-width: 400px;
      width: 90%;
      animation: slideUp 0.3s ease-out;
    }

    @keyframes slideUp {
      from {
        transform: translateY(20px);
        opacity: 0;
      }
      to {
        transform: translateY(0);
        opacity: 1;
      }
    }

    .confirm-header {
      padding: 1.5rem;
      border-bottom: 1px solid #e9ecef;
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .confirm-title {
      margin: 0;
      font-size: 1.1rem;
      font-weight: 600;
      color: #2c3e50;
    }

    .close-btn {
      background: none;
      border: none;
      font-size: 1.5rem;
      color: #6c757d;
      cursor: pointer;
      padding: 0;
      width: 30px;
      height: 30px;
      display: flex;
      align-items: center;
      justify-content: center;
      border-radius: 4px;
      transition: all 0.2s;
    }

    .close-btn:hover {
      background: #f8f9fa;
      color: #2c3e50;
    }

    .confirm-body {
      padding: 1.5rem;
      display: flex;
      gap: 1rem;
      align-items: flex-start;
    }

    .confirm-icon {
      font-size: 1.5rem;
      color: #f39c12;
      flex-shrink: 0;
      margin-top: 2px;
    }

    .confirm-message {
      margin: 0;
      color: #495057;
      line-height: 1.5;
      font-size: 0.95rem;
    }

    .confirm-footer {
      padding: 1rem 1.5rem;
      border-top: 1px solid #e9ecef;
      display: flex;
      gap: 0.75rem;
      justify-content: flex-end;
      background: #f8f9fa;
      border-radius: 0 0 8px 8px;
    }

    ::ng-deep .p-button {
      padding: 0.5rem 1rem;
      font-weight: 500;
    }
  `]
})
export class ConfirmDialogComponent {
  @Input() visible = false;
  @Input() title = 'Confirmação';
  @Input() message = '';
  @Input() confirmLabel = 'Confirmar';
  
  @Output() confirmed = new EventEmitter<void>();
  @Output() cancelled = new EventEmitter<void>();

  onConfirm() {
    this.confirmed.emit();
  }

  onCancel() {
    this.cancelled.emit();
  }
}
