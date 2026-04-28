package com.ecodrop.backend.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecodrop.backend.Model.Entities.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long>{
    List<Pedido> findByUsuarioIdUsuario(Long idUsuario);
    List<Pedido> findByComercioIdComercio(Long idComercio);
    List<Pedido> findByRepartidorIdRepartidor(Long idRepartidor);
}
