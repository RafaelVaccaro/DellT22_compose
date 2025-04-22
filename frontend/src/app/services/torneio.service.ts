import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Startup, TorneioStatusDTO} from '../model/frontend.model';

@Injectable({
  providedIn: 'root'
})
export class TorneioService {

  private api = 'http://localhost:8080/torneio';

  constructor(private http: HttpClient) {}

  iniciarTorneio(): Observable<void> {
    return this.http.post<void>(`${this.api}/iniciar`, {});
  }

  getStatus(): Observable<TorneioStatusDTO> {
    return this.http.get<TorneioStatusDTO>(`${this.api}/status`);
  }

  getRanking(): Observable<Startup[]> {
    return this.http.get<Startup[]>(`${this.api}/ranking`);
  }

  resetarTorneio(): Observable<void> {
    return this.http.delete<void>(`${this.api}/resetar`);
  }


}
