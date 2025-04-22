import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatListModule } from '@angular/material/list';
import {TorneioService} from '../../../services/torneio.service';
import {Startup} from '../../../model/frontend.model';
import {Router} from '@angular/router';
import {MatButton} from '@angular/material/button';
import {
  MatCell,
  MatCellDef,
  MatColumnDef,
  MatHeaderCell,
  MatHeaderCellDef,
  MatHeaderRow, MatHeaderRowDef, MatRow, MatRowDef,
  MatTable
} from '@angular/material/table';

@Component({
  selector: 'app-relatorio',
  standalone: true,
  templateUrl: './relatorio.component.html',
  styleUrls: ['./relatorio.component.css'],
  imports: [CommonModule, MatCardModule, MatListModule, MatButton, MatTable, MatCell, MatHeaderCell, MatColumnDef, MatHeaderCellDef, MatCellDef, MatHeaderRow, MatRow, MatHeaderRowDef, MatRowDef]
})
export class RelatorioComponent implements OnInit {
  startups: Startup[] = [];
  campea: Startup | null = null;

  constructor(private torneioService: TorneioService, private router: Router) {}

  displayedColumns = ['nome', 'pontuacao', 'pitch', 'bugs', 'tracao', 'investidor', 'fake'];

  ngOnInit(): void {
    this.torneioService.getStatus().subscribe(status => {
      this.campea = status.campea;
    });

    this.torneioService.getRanking().subscribe(startups => {
      this.startups = startups;
    });
  }

  isEliminada(startup: Startup): boolean {
    return this.campea ? startup.id !== this.campea.id : true;
  }

  reiniciarTorneio() {
    this.torneioService.resetarTorneio().subscribe(() => {
      this.router.navigate(['/startup']);
    });
  }

}
