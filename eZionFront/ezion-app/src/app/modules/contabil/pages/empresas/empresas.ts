import { Component, ViewChild, AfterViewInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EmpresaListagemComponent } from './empresa-listagem.component';
import { EmpresaFormComponent } from './empresa-form.component';
import { EmpresaStateService } from '../../services/empresa-state.service';
import { Empresa } from '../../services/empresa.service';

@Component({
  selector: 'app-empresas',
  standalone: true,
  imports: [CommonModule, EmpresaListagemComponent, EmpresaFormComponent],
  templateUrl: './empresas.html',
  styleUrl: './empresas.css'
})
export class EmpresasComponent implements AfterViewInit {
  @ViewChild('listagemRef') listagemRef?: EmpresaListagemComponent;

  tela: 'listagem' | 'formulario' = 'listagem';
  empresaParaEditar?: Empresa;
  somenteLeitura: boolean = false;
  listagem: EmpresaListagemComponent | null = null;

  constructor(private empresaStateService: EmpresaStateService) {}

  ngAfterViewInit() {
    this.listagem = this.listagemRef || null;
  }

  irParaFormulario(empresa?: Empresa, somenteLeitura: boolean = false) {
    this.empresaParaEditar = empresa;
    this.somenteLeitura = somenteLeitura;
    this.tela = 'formulario';
  }

  voltarParaListagem() {
    this.tela = 'listagem';
    this.empresaParaEditar = undefined;

    // Recarrega apenas se já teve dados carregados anteriormente
    if (this.listagem && this.listagem.ultimoCarregamento) {
      this.listagem.recarregar();
    }
  }

  salvoFormulario(empresa: Empresa) {
    this.voltarParaListagem();
  }

  canceladoFormulario() {
    this.voltarParaListagem();
  }
}
