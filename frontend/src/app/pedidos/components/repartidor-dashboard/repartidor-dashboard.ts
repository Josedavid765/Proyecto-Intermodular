import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { Pedido } from '../../../models/pedido.model';
import { PedidoService } from '../../../services/pedido';
import { RepartidorService, Repartidor } from '../../../services/repartidor';

@Component({
  selector: 'app-repartidor-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './repartidor-dashboard.html',
  styleUrl: './repartidor-dashboard.css'
})
export class RepartidorDashboardComponent implements OnInit {
  tabActivo: 'disponibles' | 'mios' | 'entregados' = 'disponibles';
  repartidor: Repartidor | null = null;
  disponibles: Pedido[] = [];
  misPedidos: Pedido[] = [];
  error: string | null = null;
  mensajeExito: string | null = null;

  pedidoValorando: number | null = null;
  mostrandoConfirmacionRechazar: number | null = null;
  puntuacionValoracion = 5;
  cargando = false;

  constructor(
    private repartidorService: RepartidorService,
    private pedidoService: PedidoService
  ) {}

  ngOnInit(): void {
    this.cargarRepartidor();
  }

  private cargarRepartidor(): void {
    this.repartidorService.getMiPerfil().subscribe({
      next: (r) => {
        this.repartidor = r;
        this.cargarPedidos();
      },
      error: (err) => {
        this.error = 'Error al cargar perfil: ' + err.message;
      }
    });
  }

  get misPedidosActivos(): Pedido[] {
    return this.misPedidos.filter(p => p.estado !== 'ENTREGADO');
  }

  get misPedidosEntregados(): Pedido[] {
    return this.misPedidos.filter(p => p.estado === 'ENTREGADO');
  }

  cargarPedidos(): void {
  if (!this.repartidor?.idRepartidor) {
    this.error = 'No se pudo obtener el ID del repartidor';
    return;
  }
  this.cargando = true;
  const id = this.repartidor.idRepartidor;

  this.pedidoService.getPedidosDisponibles().subscribe({
    next: (data) => { this.disponibles = data; },
    error: (err) => { this.error = 'Error al cargar pedidos disponibles: ' + err.message; }
  });

  this.pedidoService.getPedidosRepartidor(id).subscribe({
    next: (data) => {
      this.misPedidos = data;
      this.cargando = false;
    },
    error: (err) => {
      this.cargando = false;
      this.error = 'Error al cargar mis pedidos: ' + err.message;
    }
  });
}

  aceptarReparto(idPedido: number): void {
    this.error = null;
    const idRepartidor = this.repartidor!.idRepartidor!;
    this.pedidoService.asignarRepartidor(idPedido, idRepartidor).subscribe({
      next: () => {
        this.mensajeExito = 'Reparto asignado correctamente';
        this.pedidoService.getPedidosDisponibles().subscribe({
          next: (data) => { this.disponibles = data; }
        });
        this.pedidoService.getPedidosRepartidor(idRepartidor).subscribe({
          next: (data) => {
            this.misPedidos = data;
            this.tabActivo = 'mios';
          }
        });
      },
      error: (err) => { this.error = 'Error al asignar reparto: ' + err.message; }
    });
  }

  marcarEntregado(id: number): void {
    this.error = null;
    this.pedidoService.actualizarEstado(id, 'ENTREGADO').subscribe({
      next: () => {
        this.mensajeExito = 'Pedido marcado como entregado';
        this.cargarPedidos();
      },
      error: (err) => { this.error = 'Error al entregar: ' + err.message; }
    });
  }

  iniciarValoracion(idPedido: number): void {
    this.pedidoValorando = idPedido;
    this.puntuacionValoracion = 5;
  }

  valorarComercio(idPedido: number): void {
    this.pedidoService.valorar(idPedido, 'COMERCIO', this.puntuacionValoracion).subscribe({
      next: () => {
        this.pedidoValorando = null;
        this.mensajeExito = 'Valoración enviada';
        this.cargarPedidos();
      },
      error: (err) => { this.error = 'Error al valorar: ' + err.message; }
    });
  }

  confirmarRechazar(): void {
    const id = this.mostrandoConfirmacionRechazar;
    if (!id) return;
    this.mostrandoConfirmacionRechazar = null;
    this.error = null;
    this.pedidoService.rechazarPedido(id).subscribe({
      next: () => {
        this.mensajeExito = 'Pedido rechazado correctamente';
        this.cargarPedidos();
      },
      error: (err) => { this.error = 'Error al rechazar: ' + err.message; }
    });
  }

  cancelarValoracion(): void {
    this.pedidoValorando = null;
  }

  getEstadoClass(estado: string | undefined): string {
    switch (estado) {
      case 'PENDIENTE': return 'pendiente';
      case 'EN_TRANSITO': return 'transito';
      case 'ENTREGADO': return 'entregado';
      default: return '';
    }
  }

  getEstadoTexto(estado: string | undefined): string {
    switch (estado) {
      case 'PENDIENTE': return 'PENDIENTE';
      case 'EN_TRANSITO': return 'EN REPARTO';
      case 'ENTREGADO': return 'ENTREGADO';
      default: return estado || '';
    }
  }

  limpiarMensajes(): void {
    this.error = null;
    this.mensajeExito = null;
  }
}
