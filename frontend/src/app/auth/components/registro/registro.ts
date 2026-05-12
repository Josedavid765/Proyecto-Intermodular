import { Component } from '@angular/core';
import { Auth } from '../../../services/auth';
import { Router } from '@angular/router';

@Component({
  selector: 'app-registro',
  standalone: false,
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
    direccion: '',
    rol: 'USUARIO',
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
    
    this.authService.registrar(this.registroData).subscribe({
      next: (response) => {
        console.log('Registro exitoso', response);
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
