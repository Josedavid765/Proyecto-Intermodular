import { Component } from '@angular/core';
import { Auth } from '../../../services/auth';

@Component({
  selector: 'app-header',
  standalone: false,
  templateUrl: './header.html',
  styleUrl: './header.css'
})
export class HeaderComponent {
  constructor(public authService: Auth) {}

  logout() {
    this.authService.logout();
  }
}