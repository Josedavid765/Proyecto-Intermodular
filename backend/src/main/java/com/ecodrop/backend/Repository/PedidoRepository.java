package com.ecodrop.backend.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecodrop.backend.Model.Entities.Pedido;
import com.ecodrop.backend.Model.Enum.EstadoPedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long>{
    List<Pedido> findByComercioIdcomercio(Long idcomercio);
    List<Pedido> findByComercioIdcomercioAndEstado(Long idcomercio, EstadoPedido estado);
    List<Pedido> findByRepartidorIdRepartidor(Long idRepartidor);
    List<Pedido> findByRepartidorIdRepartidorAndEstado(Long idRepartidor, EstadoPedido estado);
    List<Pedido> findByEstadoAndRepartidorIsNull(EstadoPedido estado);
}
