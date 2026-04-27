import { Component, OnInit } from '@angular/core';
import { Comercio } from '../../../models/comercio';
import { ComercioService } from '../../../services/comercio';

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
      next: (data: any) => { 
        try {
          if (Array.isArray(data)) {
            this.comercios = data; 
          } else if (data && data.content) {
            this.comercios = data.content; 
          } else if (data && data.data) {
            this.comercios = data.data; 
          } else {
            this.comercios = [data]; 
          }
        } catch (e) {
          this.error = "Fallo interno al leer los datos.";
        } finally {
          this.cargando = false; 
        }
      },
      error: (err) => {
        this.error = `ERROR: ${err.status} - ${err.message}`;
        this.cargando = false;
      }
    });
  }
}