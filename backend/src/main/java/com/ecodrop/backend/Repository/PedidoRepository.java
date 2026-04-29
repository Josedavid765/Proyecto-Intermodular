package com.ecodrop.backend.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecodrop.backend.Model.Entities.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long>{
    List<Pedido> findByClienteIdUsuario(Long idUsuario);
    List<Pedido> findByComercioIdcomercio(Long idcomercio);
    List<Pedido> findByRepartidorIdRepartidor(Long idRepartidor);
}
