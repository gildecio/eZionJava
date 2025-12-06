/**
 * Exemplo de uso do validador de CNPJ
 * 
 * Este arquivo demonstra como usar o validador de CNPJ em diferentes cenários
 */

// ============================================================
// 1. USO COM REACTIVE FORMS (Recomendado)
// ============================================================

/*
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { cnpjValidator, isValidCNPJ } from '../validators';

@Component({
  selector: 'app-example-cnpj-validation',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <form [formGroup]="form">
      <div>
        <label for="cnpj">CNPJ</label>
        <input
          id="cnpj"
          type="text"
          formControlName="cnpj"
          placeholder="00.000.000/0000-00"
        />
        <div *ngIf="form.get('cnpj')?.hasError('cnpjInvalid')" class="error">
          CNPJ inválido
        </div>
      </div>
      <button type="submit" [disabled]="!form.valid">Enviar</button>
    </form>
  `
})
export class ExampleCNPJValidationComponent {
  form: FormGroup;

  constructor(private fb: FormBuilder) {
    this.form = this.fb.group({
      cnpj: ['', [Validators.required, cnpjValidator()]]
    });
  }
}
*/

// ============================================================
// 2. USO COM NGMODEL - Validação via função
// ============================================================

/*
import { Component } from '@angular/core';
import { CommonModule, NgFor } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { isValidCNPJ } from '../validators';

@Component({
  selector: 'app-example-template-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <form (ngSubmit)="onSubmit()">
      <input
        [(ngModel)]="cnpj"
        name="cnpj"
        placeholder="00.000.000/0000-00"
      />
      <button (click)="validarCNPJ()" type="button">Validar</button>
      
      <div *ngIf="cnpjInvalido" class="error">
        CNPJ inválido. Verifique o valor informado.
      </div>
    </form>
  `
})
export class ExampleTemplateCNPJComponent {
  cnpj = '';
  cnpjInvalido = false;

  validarCNPJ() {
    this.cnpjInvalido = !isValidCNPJ(this.cnpj);
  }

  onSubmit() {
    this.validarCNPJ();
    if (!this.cnpjInvalido) {
      console.log('CNPJ válido:', this.cnpj);
    }
  }
}
*/

// ============================================================
// 3. USO DIRETO EM SERVIÇO
// ============================================================

/*
import { Injectable } from '@angular/core';
import { isValidCNPJ } from '../validators';

@Injectable({
  providedIn: 'root'
})
export class EmpresaValidationService {
  validarCNPJ(cnpj: string): boolean {
    return isValidCNPJ(cnpj);
  }

  obterErrosCNPJ(cnpj: string): string[] {
    const erros: string[] = [];

    if (!cnpj) {
      erros.push('CNPJ é obrigatório');
      return erros;
    }

    const cnpjLimpo = cnpj.replace(/[^\d]/g, '');

    if (cnpjLimpo.length !== 14) {
      erros.push('CNPJ deve conter 14 dígitos');
    }

    if (!isValidCNPJ(cnpj)) {
      erros.push('CNPJ inválido');
    }

    return erros;
  }
}
*/

// ============================================================
// EXEMPLOS DE CNPJ VÁLIDOS
// ============================================================

const cnpjValidos = [
  '11.222.333/0001-81', // Com formatação
  '11222333000181',      // Sem formatação
  '34.028.316/0001-57',  // Outro exemplo
];

// ============================================================
// COMO USAR NO SEU COMPONENT
// ============================================================

/*
PARA REACTIVE FORMS:
=====================

import { cnpjValidator } from '@shared/validators';
import { FormBuilder, Validators } from '@angular/forms';

export class MeuComponent {
  empresaForm = this.fb.group({
    razaoSocial: ['', Validators.required],
    cnpj: ['', [Validators.required, cnpjValidator()]],
    email: ['', [Validators.required, Validators.email]]
  });

  constructor(private fb: FormBuilder) {}
}

PARA VALIDAÇÃO SIMPLES (Template Forms):
==========================================

import { isValidCNPJ } from '@shared/validators';

export class MeuComponent {
  cnpj = '';
  
  validar() {
    if (isValidCNPJ(this.cnpj)) {
      console.log('CNPJ válido');
    } else {
      console.log('CNPJ inválido');
    }
  }
}
*/
