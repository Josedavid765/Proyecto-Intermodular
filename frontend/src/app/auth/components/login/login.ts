import { Component } from '@angular/core';
import { Auth } from '../../../services/auth';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: false,
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
        this.router.navigate(['/comercios']); 
      },
      error: (err) => {
        console.error('Error en login:', err);
        this.error = err.error?.error || 'Email o contraseña incorrectos';
      }
    });
  }
}