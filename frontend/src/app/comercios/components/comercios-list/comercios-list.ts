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
      // Ponemos 'any' temporalmente para poder inspeccionar la caja
      next: (data: any) => { 
        console.log('📦 Datos crudos desde Java:', data);

        // --- LA MAGIA DEL DESEMPAQUETADO ---
        if (Array.isArray(data)) {
          // Caso A: Java manda la lista directamente
          this.comercios = data; 
        } else if (data && data.content) {
          // Caso B: Java lo manda envuelto en "content" (Muy típico en Spring Boot)
          this.comercios = data.content; 
        } else if (data && data.data) {
          // Caso C: Java lo manda envuelto en "data"
          this.comercios = data.data; 
        } else {
          // Fallback de seguridad
          this.comercios = []; 
          console.warn('⚠️ Formato de datos desconocido:', data);
        }

        // Apagamos el mensaje de "Cargando..."
        this.cargando = false; 
        
        console.log('✅ Comercios listos para pintar en HTML:', this.comercios);
      },
      error: (err) => {
        // Tu código de error se queda exactamente igual
        const status = err.status; 
        const statusText = err.statusText; 
        const backendMessage = err.error ? JSON.stringify(err.error) : 'Sin detalles.';
        this.error = `ERROR -> Código: ${status} | Detalle: ${backendMessage}`;
        this.cargando = false;
        console.error('🛑 ERROR:', err);
      }
    });
  }
}