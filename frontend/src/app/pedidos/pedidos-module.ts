import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PedidosListComponent } from './components/pedidos-list/pedidos-list';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  { path: '', component: PedidosListComponent }
];

@NgModule({
  declarations: [PedidosListComponent],
  imports: [CommonModule, RouterModule.forChild(routes)],
})
export class PedidosModule {}
