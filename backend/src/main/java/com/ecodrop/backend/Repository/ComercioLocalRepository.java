package com.ecodrop.backend.Repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ecodrop.backend.Model.Entities.ComercioLocal;

@Repository
public interface ComercioLocalRepository extends JpaRepository<ComercioLocal, Long>{
    Optional<ComercioLocal> findByEmail(String email);
    boolean existsByEmail(String email);
}
