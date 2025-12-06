import { Component, OnInit, Output, EventEmitter, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { EmpresaService, Empresa } from '../../services/empresa.service';

@Component({
  selector: 'app-empresa-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './empresa-form.html',
  styleUrl: './empresas.css'
})
export class EmpresaFormComponent implements OnInit {
  @Input() empresa?: Empresa;
  @Output() salvo = new EventEmitter<void>();
  @Output() cancelado = new EventEmitter<void>();

  enviando = false;
  editandoId: number | null = null;
  nextId = 1;
  abaAtiva: string = 'dados-basicos';

  regimes = ['LUCRO_REAL', 'LUCRO_PRESUMIDO', 'SIMPLES_NACIONAL', 'MEI'];
  tiposContribuinte = ['CONTRIBUINTE_ICMS', 'CONTRIBUINTE_ISENTO', 'NAO_CONTRIBUINTE'];
  estados = ['AC', 'AL', 'AP', 'AM', 'BA', 'CE', 'DF', 'ES', 'GO', 'MA', 'MT', 'MS', 'MG', 'PA', 'PB', 'PR', 'PE', 'PI', 'RJ', 'RN', 'RS', 'RO', 'RR', 'SC', 'SP', 'SE', 'TO'];

  empresaForm: Empresa = {
    razaoSocial: '',
    nomeFantasia: '',
    cnpj: '',
    inscricaoEstadual: '',
    inscricaoMunicipal: '',
    email: '',
    telefone: '',
    logradouro: '',
    numero: '',
    complemento: '',
    bairro: '',
    cidade: '',
    estado: '',
    cep: '',
    regimeEscal: 'LUCRO_REAL',
    tipoContribuinte: 'CONTRIBUINTE_ICMS',
    ativa: true
  };

  constructor(private empresaService: EmpresaService) {
    this.carregarNextId();
  }

  ngOnInit() {
    if (this.empresa) {
      this.editandoId = this.empresa.id || null;
      this.empresaForm = { ...this.empresa };
    } else {
      this.resetForm();
    }
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

  resetForm() {
    this.empresaForm = {
      razaoSocial: '',
      nomeFantasia: '',
      cnpj: '',
      inscricaoEstadual: '',
      inscricaoMunicipal: '',
      email: '',
      telefone: '',
      logradouro: '',
      numero: '',
      complemento: '',
      bairro: '',
      cidade: '',
      estado: '',
      cep: '',
      regimeEscal: 'LUCRO_REAL',
      tipoContribuinte: 'CONTRIBUINTE_ICMS',
      ativa: true
    };
  }

  salvar() {
    this.enviando = true;

    if (this.editandoId) {
      // Editar empresa existente
      this.empresaService.atualizar(this.editandoId, this.empresaForm).subscribe({
        next: () => console.log('Empresa atualizada na API'),
        error: (erro) => console.warn('Erro ao atualizar na API:', erro),
        complete: () => {
          this.atualizarLocalStorage();
          this.enviando = false;
          this.salvo.emit();
        }
      });
    } else {
      // Criar nova empresa
      const novaEmpresa: Empresa = {
        ...this.empresaForm,
        id: this.nextId++,
        dataCriacao: new Date().toISOString()
      };

      this.empresaService.criar(this.empresaForm).subscribe({
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
          this.atualizarLocalStorage();
          this.enviando = false;
          this.salvo.emit();
        }
      });
    }
  }

  atualizarLocalStorage() {
    const empresasLocal = localStorage.getItem('empresas') || '[]';
    let empresas = JSON.parse(empresasLocal);

    if (this.editandoId) {
      const index = empresas.findIndex((e: Empresa) => e.id === this.editandoId);
      if (index !== -1) {
        empresas[index] = { ...this.empresaForm, id: this.editandoId };
      }
    } else {
      const novaEmpresa: Empresa = {
        ...this.empresaForm,
        id: this.nextId - 1,
        dataCriacao: new Date().toISOString()
      };
      empresas.push(novaEmpresa);
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
}
