import { Routes } from '@angular/router';
import { RegistroComponent } from './auth/components/registro/registro';
import { LoginComponent } from './auth/components/login/login';
import { authGuard } from './guards/auth';

export const routes: Routes = [
  // Rutas públicas
  { path: 'login', component: LoginComponent },
  { path: 'registro', component: RegistroComponent },
  
  { 
    path: 'comercios', 
    loadChildren: () => import('./comercios/comercios-module').then(m => m.ComerciosModule),
    canActivate: [authGuard]
  },
  { 
    path: 'pedidos', 
    loadChildren: () => import('./pedidos/pedidos-module').then(m => m.PedidosModule),
    canActivate: [authGuard] 
  },

  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: '**', redirectTo: '/login' }
];