import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PedidosListComponent } from './components/pedidos-list/pedidos-list';
import { RouterModule } from '@angular/router';
import { routes } from '../app.routes';

@NgModule({
  declarations: [PedidosListComponent],
  imports: [CommonModule, RouterModule.forChild(routes)],
})
export class PedidosModule {}
