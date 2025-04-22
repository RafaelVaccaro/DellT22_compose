import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import {StartupService} from '../../../services/startup.service';
import {Startup} from '../../../model/frontend.model';
import {MatSnackBar} from '@angular/material/snack-bar';
import {TorneioService} from '../../../services/torneio.service';
import {catchError, of} from 'rxjs';

@Component({
  selector: 'app-startups',
  standalone: true,
  templateUrl: './startups.component.html',
  styleUrls: ['./startups.component.css'],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatCardModule,
    MatButtonModule
  ]
})
export class StartupsComponent implements OnInit {
  form!: FormGroup;
  startups: Startup[] = [];

  constructor(
    private fb: FormBuilder,
    private startupService: StartupService,
    private torneioService: TorneioService,
    private router: Router,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      nome: ['', Validators.required],
      slogan: ['', Validators.required],
      anoFundacao: ['', Validators.required]
    });

    this.carregarStartups();
  }

  carregarStartups() {
    this.startupService.getStartups().subscribe(data => {
      this.startups = data;
    });
  }

  cadastrar() {
    if (this.form.invalid) return;

    this.startupService.createStartup(this.form.value).subscribe({
      next: () => {
        this.form.reset();
        this.carregarStartups();
      },
      error: (err) => {
        const mensagem = err?.error?.mensagem || 'Erro ao cadastrar startup.';
        this.snackBar.open(mensagem, 'Fechar', {
          duration: 3000,
          panelClass: ['snackbar-error']
        });
      }
    });
  }

  iniciarTorneio() {
    this.torneioService.iniciarTorneio()
      .pipe(
        catchError(err => {
          const mensagem = err?.error?.mensagem || 'Erro ao iniciar torneio.';
          this.snackBar.open(mensagem, 'Fechar', {
            duration: 3000,
            panelClass: ['snackbar-error']
          });
          return of();
        })
      )
      .subscribe(() => {
        this.router.navigate(['/batalha']);
      });
  }

  podeIniciar(): boolean {
    return this.startups.length === 4 || this.startups.length === 8;
  }

  usarInvestidorSecreto(startup: Startup) {
    this.startupService.usarInvestidorSecreto(startup.id).subscribe({
      next: () => {
        this.snackBar.open(`${startup.nome} recebeu +5 pontos!`, 'Fechar', { duration: 3000 });
        this.carregarStartups();
      },
      error: () => {
        this.snackBar.open(`Investidor Secreto já foi usado.`, 'Fechar', { duration: 3000 });
      }
    });
  }

}
