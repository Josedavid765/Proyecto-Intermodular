import { Routes } from '@angular/router';
import { LandingComponent } from './pages/landing/landing';
import { RegistroComponent } from './auth/components/registro/registro';
import { LoginComponent } from './auth/components/login/login';
import { authGuard } from './guards/auth';
import { roleGuard } from './guards/role';

export const routes: Routes = [
  { path: '', component: LandingComponent },
  { path: 'login', component: LoginComponent },
  { path: 'registro', component: RegistroComponent },

  {
    path: 'comercio/dashboard',
    loadComponent: () => import('./comercios/components/comercio-dashboard/comercio-dashboard').then(m => m.ComercioDashboardComponent),
    canActivate: [authGuard, roleGuard(['COMERCIO'])]
  },
  {
    path: 'repartidor/dashboard',
    loadComponent: () => import('./pedidos/components/repartidor-dashboard/repartidor-dashboard').then(m => m.RepartidorDashboardComponent),
    canActivate: [authGuard, roleGuard(['REPARTIDOR'])]
  },
  {
    path: 'pedido/:id',
    loadComponent: () => import('./pedidos/components/detalle-pedido/detalle-pedido').then(m => m.DetallePedidoComponent),
    canActivate: [authGuard]
  },

  { path: '**', redirectTo: '', pathMatch: 'full' }
];
