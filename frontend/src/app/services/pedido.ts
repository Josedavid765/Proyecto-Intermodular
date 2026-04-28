import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { Pedido } from '../models/pedido';

@Injectable({
  providedIn: 'root'
})
export class PedidoService {

  private apiUrl = 'http://localhost:8080/api/pedidos';

  constructor(private http: HttpClient) {}

  getPedidos(): Observable<Pedido[]> {
    return this.http.get<any>(this.apiUrl).pipe(
      map(response => this.unwrapResponse(response))
    );
  }

  crearPedido(datosPedido: any): Observable<Pedido> {
    return this.http.post<any>(this.apiUrl, datosPedido).pipe(
      map(response => this.unwrapResponse(response))
    );
  }

  private unwrapResponse(response: any): any {
    if (Array.isArray(response)) {
      return response;
    }
    if (response && response.data) {
      if (Array.isArray(response.data)) {
        return response.data;
      }
      if (response.data.content && Array.isArray(response.data.content)) {
        return response.data.content;
      }
    }
    return response;
  }
}