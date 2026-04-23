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
  public nuevoUsuario: Usuario = new Usuario();

  constructor(
    private authService: Auth,
    private router: Router
  ) {}

  onSubmit() {
    this.authService.registrar(this.nuevoUsuario).subscribe({
      next: (res) => {
        alert('¡Usuario registrado con éxito!');
        this.router.navigate(['/login']);
      },
      error: (err) => {
        console.error(err);
        alert('Error al registrar: ' + (err.error?.message || 'Error desconocido'));
      }
    });
  }
}