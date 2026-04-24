import { Component, OnInit } from '@angular/core';
import { Comercio } from '../../../../models/comercio';
import { ComercioService } from '../../../../services/comercio';

@Component({
  selector: 'app-comercios-list',
  standalone: false,
  templateUrl: './comercios-list.html',
  styleUrl: './comercios-list.css'
})
export class ComerciosListComponent implements OnInit {
  public comercios: Comercio[] = [];
  
  public cargando: boolean = true;
  public error: string | null = null;

  constructor(private comercioService: ComercioService) {}

  ngOnInit(): void {
    this.cargarComercios();
  }

  cargarComercios(): void {
    this.comercioService.getComercios().subscribe({
      next: (data) => {
        this.comercios = data;
        this.cargando = false;
        console.log('Comercios cargados con éxito:', this.comercios);
      },
      error: (err) => {
        this.error = 'No hemos podido cargar los locales. Revisa tu conexión o el servidor.';
        this.cargando = false;
        console.error('Error del Backend:', err);
      }
    });
  }
}