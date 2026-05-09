import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { Usuario } from '../models/usuario.model';
import { Router } from '@angular/router';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class Auth {
  private apiUrl = `${environment.apiUrl}/auth`; 

  constructor(
    private http: HttpClient,
    private router: Router
  ) {}

  registrar(usuario: Usuario): Observable<Usuario> {
    return this.http.post<Usuario>(`${this.apiUrl}/registrar`, usuario);
  }

  login(credenciales: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/login`, credenciales).pipe(
      tap(res => {
        if (res.token) {
          localStorage.setItem('eco_token', res.token);
        }
      })
    );
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('eco_token');
  }

  getToken(): string | null {
    return localStorage.getItem('eco_token');
  }

  logout() {
    localStorage.removeItem('eco_token');
    this.router.navigate(['/login']);
  }
}