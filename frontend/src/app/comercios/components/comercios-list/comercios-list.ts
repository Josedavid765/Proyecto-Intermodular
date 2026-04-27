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
      next: (data) => {
        this.comercios = data;
        this.cargando = false;
      },
      error: (err) => {
        // 1. Extraemos el código de estado (Ej: 403, 500, 404)
        const status = err.status; 
        
        // 2. Extraemos el mensaje de texto (Ej: Forbidden, Not Found)
        const statusText = err.statusText; 
        
        // 3. Extraemos el mensaje específico que haya mandado tu Java (si lo hay)
        const backendMessage = err.error ? JSON.stringify(err.error) : 'El backend no ha dado más detalles.';

        // 4. Lo montamos todo en nuestra variable de error
        this.error = `ERROR TÉCNICO -> Código: ${status} | Tipo: ${statusText} | Detalle: ${backendMessage}`;
        
        this.cargando = false;
        
        // 5. Lo imprimimos también en la consola por si acaso
        console.error('🛑 DETALLE DEL ERROR COMPLETO:', err);
      }
    });
  }
}