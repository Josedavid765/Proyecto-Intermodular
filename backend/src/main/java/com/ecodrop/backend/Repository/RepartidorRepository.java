package com.ecodrop.backend.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecodrop.backend.Model.Entities.Repartidor;

public interface RepartidorRepository extends JpaRepository<Repartidor, Long>{
    Optional<Repartidor> findByUsuarioEmail(String email);
}
