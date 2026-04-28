package com.ecodrop.backend.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecodrop.backend.Model.Entities.LineaPedido;

@Repository
public interface LineaPedidoRepository extends JpaRepository<LineaPedido, Long> {
    List<LineaPedido> findByPedidoIdPedido(Long idPedido);
}
