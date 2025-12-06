import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EmpresaListagemComponent } from './empresa-listagem.component';
import { EmpresaFormComponent } from './empresa-form.component';
import { Empresa } from '../../services/empresa.service';

@Component({
  selector: 'app-empresas',
  standalone: true,
  imports: [CommonModule, EmpresaListagemComponent, EmpresaFormComponent],
  templateUrl: './empresas.html',
  styleUrl: './empresas.css'
})
export class EmpresasComponent {
  tela: 'listagem' | 'formulario' = 'listagem';
  empresaParaEditar?: Empresa;
  listagem: EmpresaListagemComponent | null = null;

  irParaFormulario(empresa?: Empresa) {
    this.empresaParaEditar = empresa;
    this.tela = 'formulario';
  }

  voltarParaListagem() {
    this.tela = 'listagem';
    this.empresaParaEditar = undefined;
    if (this.listagem) {
      this.listagem.recarregar();
    }
  }

  salvoFormulario() {
    this.voltarParaListagem();
  }

  canceladoFormulario() {
    this.voltarParaListagem();
  }
}
