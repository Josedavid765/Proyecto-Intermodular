import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { Auth } from '../../../services/auth';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './header.html',
  styleUrl: './header.css'
})
export class HeaderComponent {
  constructor(public authService: Auth, private router: Router) {}

  get rol(): string | null {
    return this.authService.getRol();
  }

  get isLanding(): boolean {
    return this.router.url === '/';
  }

  logout() {
    this.authService.logout();
  }
}
