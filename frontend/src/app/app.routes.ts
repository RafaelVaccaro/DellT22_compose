import { Routes } from '@angular/router';
import {StartupsComponent} from './pages/startups/startups/startups.component';
import {BatalhasComponent} from './pages/batalhas/batalhas/batalhas.component';
import {RelatorioComponent} from './pages/relatorio/relatorio/relatorio.component';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'startup',
    pathMatch: 'full'
  },
  {
    path: 'startup',
    component: StartupsComponent
  },
  {
    path: 'batalha',
    component: BatalhasComponent
  },
  {
    path: 'relatorio',
    component: RelatorioComponent
  },
  {
    path: '**',
    redirectTo: 'startup'
  }
];
