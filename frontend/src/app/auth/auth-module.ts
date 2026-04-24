import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { RegistroComponent } from './components/registro/registro';
import { LoginComponent } from './components/login/login';

@NgModule({
  declarations: [
    RegistroComponent,
    LoginComponent
  ],
  imports: [
    CommonModule, 
    FormsModule,
    RouterModule
  ],
  exports: [
    RegistroComponent,
    LoginComponent
  ]
})
export class AuthModule {}