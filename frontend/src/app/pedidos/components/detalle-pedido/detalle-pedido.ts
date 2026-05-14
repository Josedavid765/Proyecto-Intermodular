import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { Pedido } from '../../../models/pedido.model';
import { PedidoService } from '../../../services/pedido';

@Component({
  selector: 'app-detalle-pedido',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './detalle-pedido.html',
  styleUrl: './detalle-pedido.css'
})
export class DetallePedidoComponent implements OnInit {
  pedido: Pedido | null = null;
  error: string | null = null;
  mensajeExito: string | null = null;

  editando = false;
  editData: any = {};
  guardando = false;
  mostrandoConfirmacionEliminar = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private pedidoService: PedidoService
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.cargarPedido(+id);
    } else {
      this.error = 'ID de pedido no válido';
    }
  }

  cargarPedido(id: number): void {
    console.log('📄 Cargando pedido ID:', id);
    this.pedidoService.getPedido(id).subscribe({
      next: (data) => {
        console.log('📄 Pedido recibido:', data);
        this.pedido = data;
      },
      error: (err) => {
        console.error('📄 Error al cargar pedido:', err);
        this.error = 'Error al cargar el pedido. Es posible que no exista o no tengas permisos.';
      }
    });
  }

  iniciarEdicion(): void {
    this.editData = {
      nombre: this.pedido?.nombre,
      peso: this.pedido?.peso,
      direccionRecogida: this.pedido?.direccionRecogida,
      direccionEntrega: this.pedido?.direccionEntrega
    };
    this.editando = true;
  }

  cancelarEdicion(): void {
    this.editando = false;
    this.error = null;
  }

  guardarCambios(): void {
    this.guardando = true;
    this.error = null;
    this.mensajeExito = null;
    this.pedidoService.modificarPedido(this.pedido!.idPedido!, this.editData).subscribe({
      next: (data) => {
        this.pedido = data;
        this.editando = false;
        this.guardando = false;
        this.mensajeExito = 'Pedido actualizado correctamente';
      },
      error: () => {
        this.error = 'Error al actualizar el pedido';
        this.guardando = false;
      }
    });
  }

  eliminarPedido(): void {
    this.pedidoService.eliminarPedido(this.pedido!.idPedido!).subscribe({
      next: () => {
        this.router.navigate(['/comercio/dashboard']);
      },
      error: () => {
        this.error = 'Error al eliminar el pedido';
        this.mostrandoConfirmacionEliminar = false;
      }
    });
  }

  volver(): void {
    const rol = localStorage.getItem('eco_rol');
    if (rol === 'COMERCIO') {
      this.router.navigate(['/comercio/dashboard']);
    } else {
      this.router.navigate(['/repartidor/dashboard']);
    }
  }
}
