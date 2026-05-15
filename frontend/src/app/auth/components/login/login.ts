import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { Auth } from '../../../services/auth';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class LoginComponent {
  public credentials = {
    email: '',
    password: ''
  };
  public error: string | null = null;

  constructor(
    private authService: Auth,
    private router: Router
  ) {}

  onLogin() {
    this.error = null;
    this.authService.login(this.credentials).subscribe({
      next: (res) => {
        const rol = res.rol;
        if (rol === 'COMERCIO') {
          this.router.navigate(['/comercio/dashboard']);
        } else if (rol === 'REPARTIDOR') {
          this.router.navigate(['/repartidor/dashboard']);
        } else {
          this.error = 'Rol de usuario no reconocido';
        }
      },
      error: (err) => {
        console.error('Error en login:', err);
        this.error = err.error?.error || 'Email o contraseña incorrectos';
      }
    });
  }
}
