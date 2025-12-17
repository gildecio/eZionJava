import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

export interface ConfirmDeleteState {
  isOpen: boolean;
  item: any;
  onConfirm: (() => void) | null;
  onCancel: (() => void) | null;
}

@Injectable({
  providedIn: 'root'
})
export class ConfirmDeleteService {
  private initialState: ConfirmDeleteState = {
    isOpen: false,
    item: null,
    onConfirm: null,
    onCancel: null
  };

  private state$ = new BehaviorSubject<ConfirmDeleteState>(this.initialState);

  public state: Observable<ConfirmDeleteState> = this.state$.asObservable();

  constructor() { }

  /**
   * Abre o modal de confirmação de exclusão
   * @param item - Objeto a ser deletado (será exibido no modal)
   * @param onConfirm - Callback executado quando confirmado
   * @param onCancel - Callback executado quando cancelado
   */
  openConfirmDelete(item: any, onConfirm: () => void, onCancel?: () => void): void {
    this.state$.next({
      isOpen: true,
      item,
      onConfirm,
      onCancel: onCancel || null
    });
  }

  /**
   * Fecha o modal
   */
  closeConfirmDelete(): void {
    this.state$.next(this.initialState);
  }

  /**
   * Executa o callback de confirmação e fecha o modal
   */
  confirm(): void {
    console.log('ConfirmDeleteService.confirm() chamado');
    const currentState = this.state$.value;
    console.log('Estado atual:', currentState);
    if (currentState.onConfirm) {
      console.log('Executando callback onConfirm...');
      currentState.onConfirm();
    } else {
      console.error('onConfirm é null!');
    }
    this.closeConfirmDelete();
  } 

  /**
   * Executa o callback de cancelamento e fecha o modal
   */
  cancel(): void {
    const currentState = this.state$.value;
    if (currentState.onCancel) {
      currentState.onCancel();
    }
    this.closeConfirmDelete();
  }
}
