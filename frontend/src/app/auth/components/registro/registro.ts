import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { Auth } from '../../../services/auth';

@Component({
  selector: 'app-registro',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './registro.html', 
  styleUrl: './registro.css'     
})
export class RegistroComponent {
  public registroData: any = {
    nombre: '',
    apellido: '',
    email: '',
    password: '',
    telefono: '',
    rol: 'COMERCIO',
    nombreComercio: '',
    categoria: '',
    direccionComercio: '',
    horarioApertura: '',
    vehiculo: 'BICICLETA'
  };
  public error: string | null = null;
  public successMessage: string | null = null;

  constructor(
    private authService: Auth,
    private router: Router
  ) {}

  onRegistro(): void {
    this.error = null;
    this.successMessage = null;

    const esComercio = this.registroData.rol === 'COMERCIO';

    const datos = esComercio
      ? {
          nombreComercio: this.registroData.nombreComercio,
          categoria: this.registroData.categoria,
          direccionComercio: this.registroData.direccionComercio,
          horarioApertura: this.registroData.horarioApertura,
          telefono: this.registroData.telefono,
          email: this.registroData.email,
          password: this.registroData.password
        }
      : {
          nombre: this.registroData.nombre,
          apellidos: this.registroData.apellido,
          telefono: this.registroData.telefono,
          vehiculo: this.registroData.vehiculo,
          email: this.registroData.email,
          password: this.registroData.password
        };

    const request$ = esComercio
      ? this.authService.registrarComercio(datos)
      : this.authService.registrarRepartidor(datos);

    request$.subscribe({
      next: () => {
        this.successMessage = 'Cuenta creada con éxito. Ahora puedes iniciar sesión.';
        setTimeout(() => this.router.navigate(['/login']), 2000);
      },
      error: (error) => {
        console.error('Fallo al registrar:', error);
        this.error = error.error?.error || 'Error en el registro. Inténtalo de nuevo.';
      }
    });
  }
}
