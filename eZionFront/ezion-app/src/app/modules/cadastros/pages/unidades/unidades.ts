import { Component, ViewChild, AfterViewInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UnidadeListagemComponent } from './unidade-listagem.component';
import { UnidadeFormComponent } from './unidade-form.component';
import { UnidadeStateService } from '../../services/unidade-state.service';
import { Unidade } from '../../services/unidade.service';

@Component({
  selector: 'app-unidades',
  standalone: true,
  imports: [CommonModule, UnidadeListagemComponent, UnidadeFormComponent],
  templateUrl: './unidades.html',
  styleUrl: './unidades.css'
})
export class UnidadesComponent implements AfterViewInit {
  @ViewChild('listagemRef') listagemRef?: UnidadeListagemComponent;

  tela: 'listagem' | 'formulario' = 'listagem';
  unidadeParaEditar?: Unidade;
  somenteLeitura: boolean = false;
  listagem: UnidadeListagemComponent | null = null;

  constructor(private unidadeStateService: UnidadeStateService) {}

  ngAfterViewInit() {
    this.listagem = this.listagemRef || null;
  }

  irParaFormulario(unidade?: Unidade, somenteLeitura: boolean = false) {
    this.unidadeParaEditar = unidade;
    this.somenteLeitura = somenteLeitura;
    this.tela = 'formulario';
  }

  voltarParaListagem() {
    this.tela = 'listagem';
    this.unidadeParaEditar = undefined;

    // Recarrega apenas se já teve dados carregados anteriormente
    if (this.listagem && this.listagem.ultimoCarregamento) {
      this.listagem.recarregar();
    }
  }

  salvoFormulario(unidade: Unidade) {
    this.voltarParaListagem();
  }

  canceladoFormulario() {
    this.voltarParaListagem();
  }
}