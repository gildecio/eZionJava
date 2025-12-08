import { Component, OnInit, Output, EventEmitter, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { LocalService, Local } from '../../services/local.service';

@Component({
  selector: 'app-local-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './local-form.html',
  styleUrl: './locais.css'
})
export class LocalFormComponent implements OnInit {
  @Input() local?: Local;
  @Input() somenteLeitura: boolean = false;
  @Output() salvo = new EventEmitter<Local>();
  @Output() cancelado = new EventEmitter<void>();

  form: FormGroup;
  enviando = false;
  editandoId: number | null = null;

  constructor(
    private fb: FormBuilder,
    private localService: LocalService
  ) {
    this.form = this.criarForm();
  }

  ngOnInit() {
    if (this.local) {
      this.editandoId = this.local.id || null;
      this.form.patchValue(this.local);
    }

    // Se for somente leitura, desabilita todos os campos
    if (this.somenteLeitura) {
      this.form.disable();
    }
  }

  criarForm(): FormGroup {
    return this.fb.group({
      nome: ['', [Validators.required, Validators.maxLength(100)]]
    });
  }

  obterMensagemErro(campo: string): string {
    const control = this.form.get(campo);
    if (!control || !control.errors || !control.touched) {
      return '';
    }

    if (control.hasError('required')) {
      return `${this.obterLabelCampo(campo)} é obrigatório`;
    }

    if (control.hasError('maxlength')) {
      return `${this.obterLabelCampo(campo)} deve ter no máximo ${control.errors['maxlength'].requiredLength} caracteres`;
    }

    return 'Campo inválido';
  }

  obterLabelCampo(campo: string): string {
    const labels: { [key: string]: string } = {
      nome: 'Nome'
    };
    return labels[campo] || campo;
  }

  async salvar() {
    if (this.form.invalid) {
      this.marcarCamposInvalidos();
      return;
    }

    this.enviando = true;

    try {
      const formValue = this.form.value;
      const local: Local = {
        ...formValue
      };

      let resultado: Local | null | undefined;

      if (this.editandoId) {
        resultado = await this.localService.atualizar(this.editandoId, local).toPromise();
      } else {
        resultado = await this.localService.criar(local).toPromise();
      }

      if (resultado) {
        this.salvo.emit(resultado);
      }
    } catch (error) {
      console.error('Erro ao salvar local:', error);
    } finally {
      this.enviando = false;
    }
  }

  cancelar() {
    this.cancelado.emit();
  }

  private marcarCamposInvalidos() {
    Object.keys(this.form.controls).forEach(key => {
      const control = this.form.get(key);
      if (control?.invalid) {
        control.markAsTouched();
      }
    });
  }
}