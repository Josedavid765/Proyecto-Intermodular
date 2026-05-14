package com.ecodrop.backend.Repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ecodrop.backend.Model.Entities.Repartidor;
import com.ecodrop.backend.Model.Enum.EstadoRepartidor;

public interface RepartidorRepository extends JpaRepository<Repartidor, Long>{
    Optional<Repartidor> findByEmail(String email);
    boolean existsByEmail(String email);
    List<Repartidor> findByEstado(EstadoRepartidor estado);
}
