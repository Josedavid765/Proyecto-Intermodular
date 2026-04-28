import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';

import { ComerciosListComponent } from './components/comercios-list/comercios-list';
import { DetalleComercioComponent } from './components/detalle-comercio/detalle-comercio';

const routes: Routes = [
  { 
    path: '',
    component: ComerciosListComponent 
  },
  {
    path: ':id',
    component: DetalleComercioComponent
  }
];

@NgModule({
  declarations: [
    ComerciosListComponent,
    DetalleComercioComponent
  ],
  imports: [
    CommonModule,
    RouterModule.forChild(routes) 
  ]
})
export class ComerciosModule { }