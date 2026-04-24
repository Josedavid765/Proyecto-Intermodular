import { Routes } from '@angular/router';
import { RegistroComponent } from './auth/components/registro/registro';
import { LoginComponent } from './auth/components/login/login';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'registro', component: RegistroComponent },
  
  { 
    path: 'comercios', 
    loadChildren: () => import('./comercios/comercios-module').then(m => m.ComerciosModule) 
  },
  { 
    path: 'pedidos', 
    loadChildren: () => import('./pedidos/pedidos-module').then(m => m.PedidosModule) 
  },

  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: '**', redirectTo: '/login' }
];