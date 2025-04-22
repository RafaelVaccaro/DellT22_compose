import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {Batalha, EventoRequestDTO} from '../model/frontend.model';

@Injectable({
  providedIn: 'root'
})
export class BatalhaService {
  private api = 'http://localhost:8080/batalha';
  private torneioApi = 'http://localhost:8080/torneio';

  constructor(private http: HttpClient) {}

  getBatalhas(): Observable<Batalha[]> {
    return this.http.get<Batalha[]>(this.api);
  }

  avancarRodada(): Observable<void> {
    return this.http.post<void>(`${this.torneioApi}/avancar`, {});
  }

  aplicarEvento(idBatalha: number, dto: EventoRequestDTO): Observable<void> {
    return this.http.post<void>(`${this.api}/${idBatalha}/eventos`, dto);
  }

  finalizarBatalha(idBatalha: number): Observable<void> {
    return this.http.post<void>(`${this.api}/${idBatalha}/finalizar`, {});
  }
}
