import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { Auth } from '../../../services/auth';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './header.html',
  styleUrl: './header.css'
})
export class HeaderComponent {
  constructor(public authService: Auth) {}

  get rol(): string | null {
    return this.authService.getRol();
  }

  get isLanding(): boolean {
    return window.location.pathname === '/' || window.location.pathname === '';
  }

  logout() {
    this.authService.logout();
  }
}
