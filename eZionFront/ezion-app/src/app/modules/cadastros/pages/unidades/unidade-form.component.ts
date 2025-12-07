import { Component, OnInit, Output, EventEmitter, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { UnidadeService, Unidade } from '../../services/unidade.service';

@Component({
  selector: 'app-unidade-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './unidade-form.html',
  styleUrl: './unidades.css'
})
export class UnidadeFormComponent implements OnInit {
  @Input() unidade?: Unidade;
  @Input() somenteLeitura: boolean = false;
  @Output() salvo = new EventEmitter<Unidade>();
  @Output() cancelado = new EventEmitter<void>();

  form: FormGroup;
  enviando = false;
  editandoId: number | null = null;
  unidadesBase: Unidade[] = [];

  constructor(
    private fb: FormBuilder,
    private unidadeService: UnidadeService
  ) {
    this.form = this.criarForm();
  }

  ngOnInit() {
    if (this.unidade) {
      this.editandoId = this.unidade.id || null;
      this.form.patchValue({
        ...this.unidade,
        unidadePaiId: this.unidade.unidadePai?.id
      });
    }

    // Se for somente leitura, desabilita todos os campos
    if (this.somenteLeitura) {
      this.form.disable();
    }

    this.carregarUnidadesBase();
  }

  criarForm(): FormGroup {
    return this.fb.group({
      sigla: ['', [Validators.required, Validators.maxLength(10)]],
      descricao: ['', [Validators.required, Validators.maxLength(100)]],
      fator: [null, [Validators.min(0.0001)]],
      unidadePaiId: [null]
    });
  }

  async carregarUnidadesBase() {
    try {
      this.unidadesBase = await this.unidadeService.listarUnidadesBase().toPromise() || [];
    } catch (error) {
      console.error('Erro ao carregar unidades base:', error);
    }
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

    if (control.hasError('min')) {
      return `${this.obterLabelCampo(campo)} deve ser maior que zero`;
    }

    return 'Campo inválido';
  }

  obterLabelCampo(campo: string): string {
    const labels: { [key: string]: string } = {
      sigla: 'Sigla',
      descricao: 'Descrição',
      fator: 'Fator',
      unidadePaiId: 'Unidade Pai'
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
      const unidade: Unidade = {
        ...formValue,
        unidadePai: formValue.unidadePaiId ?
          this.unidadesBase.find(u => u.id === formValue.unidadePaiId) :
          undefined
      };

      let resultado: Unidade | null | undefined;

      if (this.editandoId) {
        resultado = await this.unidadeService.atualizar(this.editandoId, unidade).toPromise();
      } else {
        resultado = await this.unidadeService.criar(unidade).toPromise();
      }

      if (resultado) {
        this.salvo.emit(resultado);
      }
    } catch (error) {
      console.error('Erro ao salvar unidade:', error);
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