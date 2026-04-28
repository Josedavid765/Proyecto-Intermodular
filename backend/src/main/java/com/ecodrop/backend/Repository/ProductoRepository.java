package com.ecodrop.backend.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecodrop.backend.Model.Entities.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByComercioIdComercio(Long idComercio);
    List<Producto> findByDisponibilidadTrue();
}
