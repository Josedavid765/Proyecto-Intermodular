import { inject } from '@angular/core';
import { Router, CanActivateFn } from '@angular/router';
import { Auth } from '../services/auth';

export function roleGuard(rolesPermitidos: string[]): CanActivateFn {
  return (route, state) => {
    const authService = inject(Auth);
    const router = inject(Router);
    const rol = authService.getRol();
    if (rol && rolesPermitidos.includes(rol)) {
      return true;
    }
    router.navigate(['/login']);
    return false;
  };
}
