package com.ecodrop.backend.Service;

import com.ecodrop.backend.DTO.RepartidorDTO;
import com.ecodrop.backend.Model.Entities.Repartidor;
import com.ecodrop.backend.Repository.RepartidorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public RepartidorDTO guardar(RepartidorDTO dto) {
        Repartidor repartidor = mapToEntity(dto);
        Repartidor guardado = repartidorRepository.save(repartidor);
        return mapToDTO(guardado);
    }

    private RepartidorDTO mapToDTO(Repartidor r) {
        RepartidorDTO dto = new RepartidorDTO();
        dto.setIdRepartidor(r.getIdRepartidor());
        dto.setNombre(r.getNombre());
        dto.setTelefono(r.getTelefono());
        dto.setVehiculo(r.getVehiculo());
        dto.setEstadoDisponibilidad(r.getEstadodisponibilidad());
        return dto;
    }

    private Repartidor mapToEntity(RepartidorDTO dto) {
        Repartidor r = new Repartidor();
        r.setNombre(dto.getNombre());
        r.setTelefono(dto.getTelefono());
        r.setVehiculo(dto.getVehiculo());
        r.setEstadodisponibilidad(dto.getEstadoDisponibilidad());
        return r;
    }
}
