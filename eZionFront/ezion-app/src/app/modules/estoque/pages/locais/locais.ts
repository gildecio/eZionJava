import { Component, ViewChild, AfterViewInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LocalListagemComponent } from './local-listagem.component';
import { LocalFormComponent } from './local-form.component';
import { LocalStateService } from '../../services/local-state.service';
import { Local } from '../../services/local.service';

@Component({
  selector: 'app-locais',
  standalone: true,
  imports: [CommonModule, LocalListagemComponent, LocalFormComponent],
  templateUrl: './locais.html',
  styleUrl: './locais.css'
})
export class LocaisComponent implements AfterViewInit {
  @ViewChild('listagemRef') listagemRef?: LocalListagemComponent;

  tela: 'listagem' | 'formulario' = 'listagem';
  localParaEditar?: Local;
  somenteLeitura: boolean = false;
  listagem: LocalListagemComponent | null = null;

  constructor(private localStateService: LocalStateService) {}

  ngAfterViewInit() {
    this.listagem = this.listagemRef || null;
  }

  irParaFormulario(local?: Local, somenteLeitura: boolean = false) {
    this.localParaEditar = local;
    this.somenteLeitura = somenteLeitura;
    this.tela = 'formulario';
  }

  voltarParaListagem() {
    this.tela = 'listagem';
    this.localParaEditar = undefined;

    // Recarrega apenas se já teve dados carregados anteriormente
    if (this.listagem && this.listagem.ultimoCarregamento) {
      this.listagem.recarregar();
    }
  }

  salvoFormulario(local: Local) {
    this.voltarParaListagem();
  }

  canceladoFormulario() {
    this.voltarParaListagem();
  }
}