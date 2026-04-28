import { Component, OnInit } from '@angular/core';
import { Pedido } from '../../../models/pedido';
import { PedidoService } from '../../../services/pedido';

@Component({
  selector: 'app-pedidos-list',
  standalone: false,
  templateUrl: './pedidos-list.html',
  styleUrls: ['./pedidos-list.css']
})
export class PedidosListComponent implements OnInit {
  public pedidos: Pedido[] = [];
  public cargando: boolean = true;
  public error: string | null = null;

  constructor(private pedidoService: PedidoService) {}

  ngOnInit(): void {
    this.cargarPedidos();
  }

  cargarPedidos(): void {
    this.pedidoService.getPedidos().subscribe({
      next: (data: any) => { 
        try {
          if (Array.isArray(data)) {
            this.pedidos = data; 
          } else if (data && data.content) {
            this.pedidos = data.content; 
          } else if (data && data.data) {
            this.pedidos = data.data; 
          } else {
            this.pedidos = [data]; 
          }
        } catch (e) {
          this.error = "Fallo interno al procesar los pedidos.";
        } finally {
          this.cargando = false; 
        }
      },
      error: (err) => {
        this.error = `ERROR: No se pudieron cargar los pedidos. Verifica tu conexión.`;
        this.cargando = false;
        console.error('Error cargando pedidos:', err);
      }
    });
  }

  getClassPorEstado(estado: string | undefined): string {
    if (!estado) return 'estado-default';
    
    switch(estado.toUpperCase()) {
      case 'PENDIENTE': return 'estado-pendiente';
      case 'EN_CAMINO': return 'estado-camino';
      case 'ENTREGADO': return 'estado-entregado';
      case 'CANCELADO': return 'estado-cancelado';
      default: return 'estado-default';
    }
  }
}