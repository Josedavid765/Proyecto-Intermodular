package com.ecodrop.backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecodrop.backend.Model.Entities.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long>{

}
