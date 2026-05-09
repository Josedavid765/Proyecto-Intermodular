import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Comercio } from '../models/comercio.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class ComercioService {
  private apiUrl = `${environment.apiUrl}/comercios`;

  constructor(private http: HttpClient) {}

  getComercios(): Observable<Comercio[]> {
    return this.http.get<Comercio[]>(this.apiUrl);
  }

  getComercioPorId(id: number): Observable<Comercio> {
    return this.http.get<Comercio>(`${this.apiUrl}/${id}`);
  }
}