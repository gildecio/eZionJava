import { Component, Input, Output, EventEmitter, OnInit, OnChanges, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Empresa } from '../../services/contabil/empresa.service';

@Component({
  selector: 'app-empresas-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <div class="form-card surface">
      <div class="dialog-title">
        <div class="logo-mark small">EZ</div>
        <div>
          <p class="eyebrow">Cadastro</p>
          <h3>{{ isEdicao ? 'Editar empresa' : 'Nova empresa' }}</h3>
        </div>
      </div>

      <form [formGroup]="form" class="form-grid">
        <div class="field">
          <label>Razão social *</label>
          <input class="form-input" formControlName="razaoSocial" placeholder="Razão social" />
        </div>
        <div class="field">
          <label>Nome fantasia *</label>
          <input class="form-input" formControlName="nomeFantasia" placeholder="Nome fantasia" />
        </div>
        <div class="field">
          <label>CNPJ *</label>
          <input class="form-input" formControlName="cnpj" placeholder="00.000.000/0000-00" />
        </div>
        <div class="field">
          <label>Inscrição Estadual</label>
          <input class="form-input" formControlName="inscricaoEstadual" placeholder="Informe se houver" />
        </div>
        <div class="field">
          <label>Email</label>
          <input class="form-input" formControlName="email" placeholder="contato@empresa.com" />
        </div>
        <div class="field">
          <label>Telefone</label>
          <input class="form-input" formControlName="telefone" placeholder="(00) 00000-0000" />
        </div>
        <div class="field">
          <label>Regime *</label>
          <select formControlName="regimeEscal">
            <option value="" disabled>Selecione</option>
            <option *ngFor="let r of regimes" [value]="r.value">{{ r.label }}</option>
          </select>
        </div>
        <div class="field">
          <label>Tipo contribuinte *</label>
          <select formControlName="tipoContribuinte">
            <option value="" disabled>Selecione</option>
            <option *ngFor="let c of contribuintes" [value]="c.value">{{ c.label }}</option>
          </select>
        </div>
        <div class="field inline">
          <label>Ativa</label>
          <input type="checkbox" formControlName="ativa" />
        </div>
      </form>

      <div class="dialog-footer">
        <button type="button" class="ghost-btn" (click)="cancelar.emit()">Cancelar</button>
        <button 
          type="button" 
          class="btn-primary" 
          (click)="salvarClick.emit(form.value)"
          [disabled]="form.invalid">
          {{ isEdicao ? 'Salvar alterações' : 'Criar empresa' }}
        </button>
      </div>
    </div>
  `,
  styles: [`
    .form-card {
      padding: 1.25rem;
      border-radius: var(--radius);
      border: 1px solid var(--border);
      margin-top: 1rem;
    }

    .eyebrow {
      margin: 0;
      text-transform: uppercase;
      letter-spacing: 0.08em;
      font-size: 0.75rem;
      color: var(--text-300);
    }

    .dialog-title {
      display: flex;
      align-items: center;
      gap: 0.75rem;
      margin-bottom: 1rem;
    }

    .dialog-title h3 {
      margin: 0;
    }

    .logo-mark.small {
      width: 38px;
      height: 38px;
      font-size: 0.85rem;
      display: flex;
      align-items: center;
      justify-content: center;
      background: rgba(77, 163, 255, 0.2);
      border-radius: 8px;
      flex-shrink: 0;
    }

    .form-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
      gap: 0.75rem 1rem;
      margin-bottom: 1rem;
    }

    .field {
      display: flex;
      flex-direction: column;
      gap: 0.35rem;
    }

    .field label {
      color: var(--text-100);
      font-weight: 600;
    }

    .field.inline {
      flex-direction: row;
      align-items: center;
      justify-content: space-between;
    }

    .form-input,
    select,
    input[type='text'] {
      background: var(--muted);
      color: var(--text-100);
      border: 1px solid var(--border);
      padding: 0.85rem 0.9rem;
      border-radius: 12px;
    }

    select:focus,
    .form-input:focus,
    input[type='text']:focus {
      outline: none;
      border-color: var(--brand-500);
      box-shadow: 0 0 0 3px rgba(77, 163, 255, 0.18);
    }

    .dialog-footer {
      display: flex;
      justify-content: flex-end;
      gap: 0.5rem;
    }

    .ghost-btn {
      background: transparent;
      border: 1px solid var(--border);
      color: var(--text-100);
      padding: 0.5rem 1rem;
      border-radius: 8px;
      cursor: pointer;
      transition: border-color 0.2s ease, background 0.2s ease;
    }

    .ghost-btn:hover {
      border-color: var(--brand-500);
      background: rgba(255, 255, 255, 0.06);
    }
  `]
})
export class EmpresasFormComponent implements OnInit, OnChanges {
  @Input() empresa: Empresa | null = null;
  @Output() salvarClick = new EventEmitter<Partial<Empresa>>();
  @Output() cancelar = new EventEmitter<void>();

  form: FormGroup;
  isEdicao = false;

  regimes = [
    { label: 'Lucro Real', value: 'LUCRO_REAL' },
    { label: 'Lucro Presumido', value: 'LUCRO_PRESUMIDO' },
    { label: 'Simples Nacional', value: 'SIMPLES_NACIONAL' },
    { label: 'MEI', value: 'MEI' }
  ];

  contribuintes = [
    { label: 'Contribuinte ICMS', value: 'CONTRIBUINTE_ICMS' },
    { label: 'Contribuinte Isento', value: 'CONTRIBUINTE_ISENTO' },
    { label: 'Não contribuinte', value: 'NAO_CONTRIBUINTE' }
  ];

  constructor(private fb: FormBuilder) {
    this.form = this.criarForm();
  }

  ngOnInit(): void {
    // Inicialização do componente se necessário
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['empresa'] && this.empresa) {
      this.isEdicao = true;
      this.form.patchValue(this.empresa);
    } else if (changes['empresa'] && !this.empresa) {
      this.isEdicao = false;
      this.form.reset({ ativa: true });
    }
  }

  private criarForm(): FormGroup {
    return this.fb.group({
      id: [null],
      razaoSocial: ['', [Validators.required]],
      nomeFantasia: ['', [Validators.required]],
      cnpj: ['', [Validators.required]],
      inscricaoEstadual: [''],
      email: ['', [Validators.email]],
      telefone: [''],
      regimeEscal: [null, [Validators.required]],
      tipoContribuinte: [null, [Validators.required]],
      ativa: [true]
    });
  }
}
