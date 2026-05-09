package com.ecodrop.backend.Service;

import com.ecodrop.backend.DTO.RepartidorDTO;
import com.ecodrop.backend.Exceptions.RecursoNoEncontrado;
import com.ecodrop.backend.Model.Entities.Repartidor;
import com.ecodrop.backend.Model.Enum.EstadoRepartidor;
import com.ecodrop.backend.Repository.RepartidorRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("null")
@Service
public class RepartidorService {
    
    private final RepartidorRepository repartidorRepository;

    public RepartidorService(RepartidorRepository repartidorRepository) {
        this.repartidorRepository = repartidorRepository;
    }

    public List<RepartidorDTO> listarTodos() {
        return repartidorRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<RepartidorDTO> listarDisponibles() {
        return repartidorRepository.findByEstado(EstadoRepartidor.DISPONIBLE).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public RepartidorDTO obtenerPorEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("El email no puede ser nulo o vacío");
        }
        Repartidor repartidor = repartidorRepository.findByUsuarioEmail(email)
                .orElseThrow(() -> new RecursoNoEncontrado("Repartidor no encontrado para el email: " + email));
        return mapToDTO(repartidor);
    }

    public RepartidorDTO guardar(@NonNull RepartidorDTO dto) {
        Repartidor repartidor = mapToEntity(dto);
        Repartidor guardado = repartidorRepository.save(repartidor);
        return mapToDTO(guardado);
    }

    private RepartidorDTO mapToDTO(Repartidor r) {
        RepartidorDTO dto = new RepartidorDTO();
        dto.setIdRepartidor(r.getIdRepartidor());
        dto.setNombre(r.getNombre());
        dto.setApellidos(r.getApellidos());
        dto.setTelefono(r.getTelefono());
        dto.setVehiculo(r.getVehiculo());
        dto.setEstado(r.getEstado());
        return dto;
    }

    private Repartidor mapToEntity(RepartidorDTO dto) {
        Repartidor r = new Repartidor();
        r.setNombre(dto.getNombre());
        r.setApellidos(dto.getApellidos());
        r.setTelefono(dto.getTelefono());
        r.setVehiculo(dto.getVehiculo());
        r.setEstado(dto.getEstado());
        return r;
    }
}
