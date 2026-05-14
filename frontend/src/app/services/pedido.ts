import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Pedido } from '../models/pedido.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class PedidoService {

  private apiUrl = `${environment.apiUrl}/pedidos`;

  constructor(private http: HttpClient) {}

  getPedidosComercio(): Observable<Pedido[]> {
    return this.http.get<Pedido[]>(`${this.apiUrl}/comercio/me`);
  }

  getPedidosDisponibles(): Observable<Pedido[]> {
    return this.http.get<Pedido[]>(`${this.apiUrl}/disponibles`);
  }

  getPedidosRepartidor(idRepartidor: number, estado?: string): Observable<Pedido[]> {
    let url = `${this.apiUrl}/repartidor/${idRepartidor}`;
    if (estado) url += `?estado=${estado}`;
    return this.http.get<Pedido[]>(url);
  }

  crearPedido(datos: any): Observable<Pedido> {
    return this.http.post<Pedido>(this.apiUrl, datos);
  }

  asignarRepartidor(idPedido: number, idRepartidor: number): Observable<Pedido> {
    return this.http.put<Pedido>(`${this.apiUrl}/${idPedido}/repartidor/${idRepartidor}`, {});
  }

  actualizarEstado(id: number, estado: string): Observable<Pedido> {
    return this.http.patch<Pedido>(`${this.apiUrl}/${id}/estado`, { estado });
  }

  getPedido(id: number): Observable<Pedido> {
    return this.http.get<Pedido>(`${this.apiUrl}/${id}`);
  }

  modificarPedido(id: number, datos: any): Observable<Pedido> {
    return this.http.put<Pedido>(`${this.apiUrl}/${id}`, datos);
  }

  eliminarPedido(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  valorar(id: number, tipo: string, puntuacion: number): Observable<Pedido> {
    return this.http.put<Pedido>(`${this.apiUrl}/${id}/valorar`, { tipo, puntuacion });
  }

  rechazarPedido(id: number): Observable<Pedido> {
    return this.http.delete<Pedido>(`${this.apiUrl}/${id}/repartidor`);
  }
}
