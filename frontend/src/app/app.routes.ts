import { Routes } from '@angular/router';
import { RegistroComponent } from './auth/components/registro/registro';
import { LoginComponent } from './auth/components/login/login';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'registro', component: RegistroComponent },
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'comercios', loadChildren: () => import('./comercios/comercios-module').then(m => m.ComerciosModule) }
];