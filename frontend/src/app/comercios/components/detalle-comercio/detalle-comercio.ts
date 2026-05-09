import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Comercio } from '../../../models/comercio.model';
import { Producto } from '../../../models/producto.model';
import { ComercioService } from '../../../services/comercio';
import { ProductoService } from '../../../services/producto';

@Component({
  selector: 'app-detalle-comercio',
  standalone: false,
  templateUrl: './detalle-comercio.html',
  styleUrl: './detalle-comercio.css'
})
export class DetalleComercioComponent implements OnInit {
  public comercio: Comercio | null = null;
  public productos: Producto[] = [];
  
  public cargando: boolean = true;
  public error: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private comercioService: ComercioService,
    private productoService: ProductoService
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.cargarComercio(+id);
      this.cargarProductos(+id);
    }
  }

  cargarComercio(id: number): void {
    this.comercioService.getComercioPorId(id).subscribe({
      next: (data) => { this.comercio = data; },
      error: (err) => { this.error = `ERROR: ${err.status} - ${err.message}`; }
    });
  }

  cargarProductos(idComercio: number): void {
    this.productoService.listarPorComercio(idComercio).subscribe({
      next: (data) => {
        this.productos = Array.isArray(data) ? data : [];
        this.cargando = false;
      },
      error: (err) => {
        this.error = `ERROR: ${err.status} - ${err.message}`;
        this.cargando = false;
      }
    });
  }
}
