import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EmpresaService, Empresa } from '../../services/empresa.service';

@Component({
  selector: 'app-empresa-listagem',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './empresa-listagem.html',
  styleUrl: './empresas.css'
})
export class EmpresaListagemComponent implements OnInit {
  empresas: Empresa[] = [];
  carregando = true;
  filtroAtivas: string = 'todas';

  @Output() novaEmpresa = new EventEmitter<void>();
  @Output() editarEmpresa = new EventEmitter<Empresa>();

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

  onNovaEmpresa() {
    this.novaEmpresa.emit();
  }

  onEditarEmpresa(empresa: Empresa) {
    this.editarEmpresa.emit(empresa);
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

  recarregar() {
    this.carregarEmpresas();
  }
}
