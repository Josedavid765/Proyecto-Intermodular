import { Component } from '@angular/core';
import { Usuario } from '../../../models/usuario';
import { Auth } from '../../../services/auth';
import { Router } from '@angular/router';

@Component({
  selector: 'app-registro',
  standalone: false,
  templateUrl: './registro.html', 
  styleUrl: './registro.css'     
})
export class RegistroComponent {
  public usuario: Usuario = new Usuario();

  constructor(
    private authService: Auth,
    private router: Router
  ) {}

  onRegistro(): void {
    console.log('Datos a enviar al backend:', this.usuario);
    
    this.authService.registrar(this.usuario).subscribe({
      next: (response) => {
        console.log('Registro exitoso', response);
        alert('Cuenta creada con éxito. Ahora puedes iniciar sesión.');
        this.router.navigate(['/login']);
      },
      error: (error) => {
        console.error('Fallo al registrar:', error);
        alert('Error en el registro. Abre la consola (F12) para más detalles.');
      }
    });
  }
}