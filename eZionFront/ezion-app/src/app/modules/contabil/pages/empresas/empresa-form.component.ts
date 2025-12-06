import { Component, OnInit, Output, EventEmitter, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { EmpresaService, Empresa } from '../../services/empresa.service';
import { cnpjValidator } from '../../../../shared/validators';

@Component({
  selector: 'app-empresa-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './empresa-form.html',
  styleUrl: './empresas.css'
})
export class EmpresaFormComponent implements OnInit {
  @Input() empresa?: Empresa;
  @Output() salvo = new EventEmitter<void>();
  @Output() cancelado = new EventEmitter<void>();

  form: FormGroup;
  enviando = false;
  editandoId: number | null = null;
  nextId = 1;
  abaAtiva: string = 'dados-basicos';

  regimes = ['LUCRO_REAL', 'LUCRO_PRESUMIDO', 'SIMPLES_NACIONAL', 'MEI'];
  tiposContribuinte = ['CONTRIBUINTE_ICMS', 'CONTRIBUINTE_ISENTO', 'NAO_CONTRIBUINTE'];
  estados = ['AC', 'AL', 'AP', 'AM', 'BA', 'CE', 'DF', 'ES', 'GO', 'MA', 'MT', 'MS', 'MG', 'PA', 'PB', 'PR', 'PE', 'PI', 'RJ', 'RN', 'RS', 'RO', 'RR', 'SC', 'SP', 'SE', 'TO'];

  constructor(
    private fb: FormBuilder,
    private empresaService: EmpresaService
  ) {
    this.carregarNextId();
    this.form = this.criarForm();
  }

  ngOnInit() {
    if (this.empresa) {
      this.editandoId = this.empresa.id || null;
      this.form.patchValue(this.empresa);
    }
  }

  criarForm(): FormGroup {
    return this.fb.group({
      razaoSocial: ['', [Validators.required, Validators.minLength(3)]],
      nomeFantasia: ['', [Validators.required, Validators.minLength(3)]],
      cnpj: ['', [Validators.required, cnpjValidator()]],
      inscricaoEstadual: [''],
      inscricaoMunicipal: [''],
      email: ['', [Validators.email]],
      telefone: [''],
      logradouro: [''],
      numero: [''],
      complemento: [''],
      bairro: [''],
      cidade: [''],
      estado: [''],
      cep: [''],
      regimeEscal: ['LUCRO_REAL'],
      tipoContribuinte: ['CONTRIBUINTE_ICMS'],
      faturamentoAnual: [0],
      responsavelNome: [''],
      responsavelCPF: [''],
      responsavelEmail: [''],
      responsavelTelefone: [''],
      ativa: [true]
    });
  }

  carregarNextId() {
    const empresasLocal = localStorage.getItem('empresas');
    if (empresasLocal) {
      try {
        const empresas = JSON.parse(empresasLocal);
        const ids = empresas.map((e: Empresa) => e.id || 0);
        this.nextId = Math.max(...ids, 0) + 1;
      } catch (e) {
        this.nextId = 1;
      }
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

    if (control.hasError('minlength')) {
      const minLength = control.errors['minlength'].requiredLength;
      return `${this.obterLabelCampo(campo)} deve ter no mínimo ${minLength} caracteres`;
    }

    if (control.hasError('email')) {
      return 'Email inválido';
    }

    if (control.hasError('cnpjInvalid')) {
      return 'CNPJ inválido';
    }

    return 'Campo inválido';
  }

  private obterLabelCampo(campo: string): string {
    const labels: { [key: string]: string } = {
      razaoSocial: 'Razão Social',
      nomeFantasia: 'Nome Fantasia',
      cnpj: 'CNPJ',
      email: 'Email'
    };
    return labels[campo] || campo;
  }

  salvar() {
    if (!this.form.valid) {
      // Marcar todos os campos como touched para mostrar erros
      Object.keys(this.form.controls).forEach(key => {
        this.form.get(key)?.markAsTouched();
      });
      return;
    }

    this.enviando = true;
    const formData = this.form.getRawValue();

    if (this.editandoId) {
      // Editar empresa existente
      this.empresaService.atualizar(this.editandoId, formData).subscribe({
        next: () => console.log('Empresa atualizada na API'),
        error: (erro) => console.warn('Erro ao atualizar na API:', erro),
        complete: () => {
          this.atualizarLocalStorage(formData);
          this.enviando = false;
          this.salvo.emit();
        }
      });
    } else {
      // Criar nova empresa
      const novaEmpresa: Empresa = {
        ...formData,
        id: this.nextId++,
        dataCriacao: new Date().toISOString()
      };

      this.empresaService.criar(formData).subscribe({
        next: (empresa) => {
          if (empresa && empresa.id) {
            this.nextId = (empresa.id || 0) + 1;
          }
          console.log('Empresa criada na API');
        },
        error: (erro) => {
          console.warn('Erro ao criar na API:', erro);
        },
        complete: () => {
          this.atualizarLocalStorage(novaEmpresa);
          this.enviando = false;
          this.salvo.emit();
        }
      });
    }
  }

  atualizarLocalStorage(empresa: Empresa) {
    const empresasLocal = localStorage.getItem('empresas') || '[]';
    let empresas = JSON.parse(empresasLocal);

    if (this.editandoId) {
      const index = empresas.findIndex((e: Empresa) => e.id === this.editandoId);
      if (index !== -1) {
        empresas[index] = { ...empresa, id: this.editandoId };
      }
    } else {
      empresas.push(empresa);
    }

    localStorage.setItem('empresas', JSON.stringify(empresas));
  }

  cancelar() {
    this.cancelado.emit();
  }

  mudarAba(abaName: string) {
    this.abaAtiva = abaName;
    setTimeout(() => {
      const tabBtns = document.querySelectorAll('.tab-btn');
      const tabContents = document.querySelectorAll('.tab-content');
      
      tabBtns.forEach((btn) => {
        btn.classList.remove('active');
      });
      tabContents.forEach((content) => {
        content.classList.remove('active');
      });

      const activeBtn = document.querySelector(`.tab-btn[data-tab="${abaName}"]`);
      const activeContent = document.querySelector(`.tab-content[data-tab="${abaName}"]`);
      
      if (activeBtn) activeBtn.classList.add('active');
      if (activeContent) activeContent.classList.add('active');
    }, 0);
  }

  temErro(campo: string): boolean {
    const control = this.form.get(campo);
    return !!(control && control.invalid && control.touched);
  }
}
