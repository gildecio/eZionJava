import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { EmpresaService, Empresa } from '../../services/empresa.service';

@Component({
  selector: 'app-empresas',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './empresas.html',
  styleUrl: './empresas.css'
})
export class EmpresasComponent implements OnInit {
  empresas: Empresa[] = [];
  carregando = true;
  enviando = false;
  modalAberto = false;
  editandoId: number | null = null;
  nextId = 1;
  filtroAtivas: string = 'todas'; // todas, ativas, inativas
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
    this.carregarEmpresasDoLocalStorage();
  }

  ngOnInit() {
    this.carregarEmpresas();
  }

  carregarEmpresas() {
    this.carregando = true;
    
    const request = this.filtroAtivas === 'ativas' 
      ? this.empresaService.listarAtivas()
      : this.filtroAtivas === 'inativas'
      ? this.empresaService.listarInativas()
      : this.empresaService.listar();

    request.subscribe({
      next: (dados) => {
        this.empresas = dados || [];
        this.carregando = false;
        this.salvarEmpresasNoLocalStorage();
      },
      error: (erro) => {
        console.warn('API de empresas não disponível, usando dados locais:', erro);
        this.carregarEmpresasDoLocalStorage();
        this.carregando = false;
      }
    });
  }

  carregarEmpresasDoLocalStorage() {
    const empresasLocal = localStorage.getItem('empresas');
    if (empresasLocal) {
      try {
        const todas = JSON.parse(empresasLocal);
        
        if (this.filtroAtivas === 'ativas') {
          this.empresas = todas.filter((e: Empresa) => e.ativa);
        } else if (this.filtroAtivas === 'inativas') {
          this.empresas = todas.filter((e: Empresa) => !e.ativa);
        } else {
          this.empresas = todas;
        }
        
        const ids = this.empresas.map(e => e.id || 0);
        this.nextId = Math.max(...ids, 0) + 1;
      } catch (e) {
        console.error('Erro ao carregar empresas do localStorage:', e);
        this.empresas = [];
      }
    } else {
      this.empresas = [];
    }
  }

  salvarEmpresasNoLocalStorage() {
    localStorage.setItem('empresas', JSON.stringify(this.empresas));
  }

  alterarFiltro(filtro: string) {
    this.filtroAtivas = filtro;
    this.carregarEmpresas();
  }

  abrirModalCriar() {
    this.editandoId = null;
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
    this.modalAberto = true;
  }

  abrirModalEditar(empresa: Empresa) {
    this.editandoId = empresa.id || null;
    this.empresaForm = { ...empresa };
    this.modalAberto = true;
  }

  fecharModal() {
    this.modalAberto = false;
    this.editandoId = null;
  }

  salvar() {
    this.enviando = true;

    if (this.editandoId) {
      // Editar empresa existente
      const index = this.empresas.findIndex(e => e.id === this.editandoId);
      if (index !== -1) {
        this.empresas[index] = { ...this.empresaForm, id: this.editandoId };
        
        this.empresaService.atualizar(this.editandoId, this.empresaForm).subscribe({
          next: () => console.log('Empresa atualizada na API'),
          error: (erro) => console.warn('Erro ao atualizar na API:', erro),
          complete: () => {
            this.salvarEmpresasNoLocalStorage();
            this.fecharModal();
            this.enviando = false;
          }
        });
      } else {
        this.enviando = false;
      }
    } else {
      // Criar nova empresa
      const novaEmpresa: Empresa = {
        ...this.empresaForm,
        id: this.nextId++,
        dataCriacao: new Date().toISOString()
      };

      this.empresaService.criar(this.empresaForm).subscribe({
        next: (empresa) => {
          if (empresa) {
            this.empresas.push(empresa);
          } else {
            this.empresas.push(novaEmpresa);
          }
          console.log('Empresa criada na API');
        },
        error: (erro) => {
          console.warn('Erro ao criar na API:', erro);
          this.empresas.push(novaEmpresa);
        },
        complete: () => {
          this.salvarEmpresasNoLocalStorage();
          this.fecharModal();
          this.enviando = false;
        }
      });
    }
  }

  ativarEmpresa(id: number) {
    this.empresaService.ativar(id).subscribe({
      next: () => {
        const empresa = this.empresas.find(e => e.id === id);
        if (empresa) empresa.ativa = true;
        this.salvarEmpresasNoLocalStorage();
        this.carregarEmpresas();
      },
      error: (erro) => console.error('Erro ao ativar:', erro)
    });
  }

  desativarEmpresa(id: number) {
    if (confirm('Tem certeza que deseja desativar esta empresa?')) {
      this.empresaService.desativar(id).subscribe({
        next: () => {
          const empresa = this.empresas.find(e => e.id === id);
          if (empresa) empresa.ativa = false;
          this.salvarEmpresasNoLocalStorage();
          this.carregarEmpresas();
        },
        error: (erro) => console.error('Erro ao desativar:', erro)
      });
    }
  }

  deletarEmpresa(id: number) {
    if (confirm('Tem certeza que deseja deletar esta empresa?')) {
      this.empresas = this.empresas.filter(e => e.id !== id);

      this.empresaService.deletar(id).subscribe({
        next: () => console.log('Empresa deletada na API'),
        error: (erro) => console.warn('Erro ao deletar na API:', erro),
        complete: () => this.salvarEmpresasNoLocalStorage()
      });
    }
  }

  mudarAba(abaName: string) {
    this.abaAtiva = abaName;
    // Atualizar as abas visualmente
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
