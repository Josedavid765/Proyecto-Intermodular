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

  constructor(
    private authService: Auth,
    private router: Router
  ) {}

  onLogin() {
    this.authService.login(this.credentials).subscribe({
      next: (res) => {
        this.router.navigate(['/comercios']); 
      },
      error: (err) => {
        console.error('Error en login:', err);
        alert('Email o contraseña incorrectos');
      }
    });
  }
}