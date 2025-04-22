import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import {BatalhaService} from '../../../services/batalha.service';
import {DialogoBatalhaComponent} from '../dialogo-batalha/dialogo-batalha.component';
import {Batalha, Startup} from '../../../model/frontend.model';
import {TorneioService} from '../../../services/torneio.service';
import {Router} from '@angular/router';


@Component({
  selector: 'app-batalhas',
  standalone: true,
  templateUrl: './batalhas.component.html',
  styleUrls: ['./batalhas.component.css'],
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatDialogModule
  ]
})
export class BatalhasComponent implements OnInit {
  batalhas: Batalha[] = [];

  campea: Startup | null = null;

  constructor(
    private batalhaService: BatalhaService,
    private torneioService: TorneioService,
    private dialog: MatDialog,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.carregarBatalhas();
    this.carregarStatus();
  }

  carregarStatus() {
    this.torneioService.getStatus().subscribe(status => {
      this.campea = status.campea;
    });
  }

  carregarBatalhas() {
    this.batalhaService.getBatalhas().subscribe(b => {
      this.batalhas = b;

      const todasFinalizadas = b.length > 0 && b.every(b => b.finalizada);

      if (todasFinalizadas && !this.campea) {
        this.batalhaService.avancarRodada().subscribe(() => {
          this.carregarBatalhas();
          this.carregarStatus();
        });
      }
    });
  }

  abrirDialog(batalha: Batalha) {
    this.dialog.open(DialogoBatalhaComponent, {
      data: batalha,
      width: '600px',
      disableClose: true
    }).afterClosed().subscribe(() => this.carregarBatalhas());
  }


  todasFinalizadas(): boolean {
    return this.batalhas.length > 0 && this.batalhas.every(b => b.finalizada);
  }

  avancarRodada() {
    this.batalhaService.avancarRodada().subscribe(() => {
      this.carregarBatalhas();
      this.carregarStatus();
    });
  }

  irParaRelatorio() {
    this.router.navigate(['/relatorio']);
  }

}
