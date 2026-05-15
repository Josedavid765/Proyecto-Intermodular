import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { Router } from '@angular/router';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class Auth {
  private apiUrl = `${environment.apiUrl}/auth`; 
  isLoggedIn$ = new BehaviorSubject<boolean>(!!localStorage.getItem('eco_token'));
  rol$ = new BehaviorSubject<string | null>(localStorage.getItem('eco_rol'));

  constructor(
    private http: HttpClient,
    private router: Router
  ) {}

  registrarComercio(datos: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/registrar/comercio`, datos);
  }

  registrarRepartidor(datos: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/registrar/repartidor`, datos);
  }

  login(credenciales: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/login`, credenciales).pipe(
      tap(res => {
        if (res.token) {
          localStorage.setItem('eco_token', res.token);
          this.isLoggedIn$.next(true);
        }
        if (res.rol) {
          localStorage.setItem('eco_rol', res.rol);
          this.rol$.next(res.rol);
        }
        if (res.email) {
          localStorage.setItem('eco_email', res.email);
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

  getRol(): string | null {
    return localStorage.getItem('eco_rol');
  }

  getEmail(): string | null {
    return localStorage.getItem('eco_email');
  }

  logout() {
    this.isLoggedIn$.next(false);
    localStorage.removeItem('eco_token');
    this.rol$.next(null);
    localStorage.removeItem('eco_rol');
    localStorage.removeItem('eco_email');
    this.router.navigate(['/login']);
  }
}
