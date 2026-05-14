import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { Pedido } from '../../../models/pedido.model';
import { Comercio } from '../../../models/comercio.model';
import { ComercioService } from '../../../services/comercio';
import { PedidoService } from '../../../services/pedido';

@Component({
  selector: 'app-comercio-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './comercio-dashboard.html',
  styleUrl: './comercio-dashboard.css'
})
export class ComercioDashboardComponent implements OnInit {
  comercio: Comercio | null = null;
  pedidos: Pedido[] = [];
  error: string | null = null;
  mensajeExito: string | null = null;

  mostrarFormulario = false;
  nuevoPedido = { nombre: '', direccionEntrega: '', peso: null as number | null };
  creando = false;
  mostrarConfirmacion = false;

  constructor(
    private comercioService: ComercioService,
    private pedidoService: PedidoService
  ) {}

  ngOnInit(): void {
    this.cargarComercio();
  }

  private cargarComercio(): void {
    this.comercioService.getMiComercio().subscribe({
      next: (c) => {
        this.comercio = c;
        this.cargarPedidos();
      },
      error: (err) => {
        this.error = 'Error al cargar comercio: ' + err.message;
      }
    });
  }

  cargarPedidos(): void {
    console.log('📦 Cargando pedidos...');
    this.pedidoService.getPedidosComercio().subscribe({
      next: (data) => {
        console.log('📦 Pedidos recibidos:', data);
        this.pedidos = data;
      },
      error: (err) => {
        console.error('📦 Error al cargar pedidos:', err);
        this.error = 'Error al cargar pedidos: ' + err.message;
      }
    });
  }

  mostrarConfirmarPedido(): void {
    if (!this.nuevoPedido.nombre || !this.nuevoPedido.direccionEntrega || !this.nuevoPedido.peso) return;
    this.mostrarConfirmacion = true;
  }

  cancelarCreacion(): void {
    this.mostrarConfirmacion = false;
  }

  confirmarCreacion(): void {
    this.mostrarConfirmacion = false;
    this.creando = true;
    this.error = null;
    this.mensajeExito = null;

    this.pedidoService.crearPedido(this.nuevoPedido).subscribe({
      next: () => {
        this.nuevoPedido = { nombre: '', direccionEntrega: '', peso: null };
        this.creando = false;
        this.mostrarFormulario = false;
        this.mensajeExito = 'Pedido creado correctamente';
        this.cargarPedidos();
      },
      error: (err) => {
        this.error = 'Error al crear pedido: ' + err.message;
        this.creando = false;
      }
    });
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
