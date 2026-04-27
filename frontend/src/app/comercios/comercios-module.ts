import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';

import { ComerciosListComponent } from './components/comercios-list/comercios-list';

const routes: Routes = [
  { 
    path: '',
    component: ComerciosListComponent 
  }
];

@NgModule({
  declarations: [
    ComerciosListComponent
  ],
  imports: [
    CommonModule,
    RouterModule.forChild(routes) 
  ]
})
export class ComerciosModule { }