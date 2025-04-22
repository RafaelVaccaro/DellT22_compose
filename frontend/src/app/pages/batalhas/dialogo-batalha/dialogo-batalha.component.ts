import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

import { MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { CommonModule } from '@angular/common';
import {Batalha, TipoEvento} from '../../../model/frontend.model';
import {BatalhaService} from '../../../services/batalha.service';

@Component({
  selector: 'app-dialogo-batalha',
  standalone: true,
  templateUrl: './dialogo-batalha.component.html',
  styleUrls: ['./dialogo-batalha.component.css'],
  imports: [CommonModule, MatDialogModule, MatButtonModule]
})
export class DialogoBatalhaComponent {

  eventosAplicadosA: TipoEvento[] = [];
  eventosAplicadosB: TipoEvento[] = [];

  eventos: { nome: string; tipo: TipoEvento }[] = [
    { nome: 'Pitch Convincente', tipo: 'PITCH_CONVINCENTE' },
    { nome: 'Bug no Sistema', tipo: 'PRODUTO_COM_BUGS' },
    { nome: 'Aumento de Tração', tipo: 'BOA_TRACAO_USUARIOS' },
    { nome: 'Investidor Irritado', tipo: 'INVESTIDOR_IRRITADO' },
    { nome: 'Fake News', tipo: 'FAKE_NEWS' }
  ];

  constructor(
    @Inject(MAT_DIALOG_DATA) public batalha: Batalha,
    private dialogRef: MatDialogRef<DialogoBatalhaComponent>,
    private batalhaService: BatalhaService
  ) {}

  aplicarEvento(idStartup: number, tipo: TipoEvento) {
    this.batalhaService.aplicarEvento(this.batalha.id, {
      idStartup,
      tipoEvento: tipo
    }).subscribe(() => {
      if (idStartup === this.batalha.startupA.id) {
        this.eventosAplicadosA.push(tipo);
      } else {
        this.eventosAplicadosB.push(tipo);
      }
    });
  }

  finalizar() {
    this.batalhaService.finalizarBatalha(this.batalha.id).subscribe(() => {
      this.dialogRef.close();
    });
  }
}
