import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface Repartidor {
  idRepartidor?: number;
  nombre?: string;
  apellidos?: string;
  telefono?: string;
  vehiculo?: string;
  estado?: string;
  email?: string;
}

@Injectable({
  providedIn: 'root',
})
export class RepartidorService {
  private apiUrl = `${environment.apiUrl}/repartidores`;

  constructor(private http: HttpClient) {}

  getMiPerfil(): Observable<Repartidor> {
    return this.http.get<Repartidor>(`${this.apiUrl}/me`);
  }
}
