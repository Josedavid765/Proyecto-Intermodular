import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Comercio } from '../models/comercio';

@Injectable({
  providedIn: 'root'
})
export class ComercioService {
  private apiUrl = 'http://localhost:8080/api/comercios';

  constructor(private http: HttpClient) { }

  getComercios(): Observable<Comercio[]> {
    return this.http.get<Comercio[]>(this.apiUrl);
  }

  getComercioById(id: number): Observable<Comercio> {
    return this.http.get<Comercio>(`${this.apiUrl}/${id}`);
  }
}