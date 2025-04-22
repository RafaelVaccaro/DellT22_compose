import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {Startup} from '../model/frontend.model';

@Injectable({
  providedIn: 'root'
})
export class StartupService {
  private api = 'http://localhost:8080/startup';

  constructor(private http: HttpClient) {}

  getStartups(): Observable<Startup[]> {
    return this.http.get<Startup[]>(this.api);
  }

  createStartup(data: Partial<Startup>): Observable<void> {
    return this.http.post<void>(this.api, data);
  }

  usarInvestidorSecreto(id: number): Observable<void> {
    return this.http.patch<void>(`${this.api}/${id}/investidor-secreto`, {});
  }
}
