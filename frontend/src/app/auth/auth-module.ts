import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { RegistroComponent } from './components/registro/registro';

@NgModule({
  declarations: [
    RegistroComponent
  ],
  imports: [
    CommonModule, 
    FormsModule,
    RouterModule
  ],
  exports: [
    RegistroComponent
  ]
})
export class AuthModule {}