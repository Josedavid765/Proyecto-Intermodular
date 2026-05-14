package com.ecodrop.backend.Service;

import com.ecodrop.backend.DTO.ComercioLocalDTO;
import com.ecodrop.backend.Exceptions.RecursoNoEncontrado;
import com.ecodrop.backend.Model.Entities.ComercioLocal;
import com.ecodrop.backend.Repository.ComercioLocalRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("null")
@Service
public class ComercioLocalService {
    
    private final ComercioLocalRepository comercioRepository;

    public ComercioLocalService(ComercioLocalRepository comercioRepository) {
        this.comercioRepository = comercioRepository;
    }

    public List<ComercioLocalDTO> listarTodos() {
        return comercioRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public ComercioLocalDTO buscarPorId(Long id) {
        ComercioLocal comercio = comercioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontrado("Comercio no encontrado con ID: " + id));
        return mapToDTO(comercio);
    }

    public ComercioLocalDTO obtenerPorEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("El email no puede ser nulo o vacío");
        }
        ComercioLocal comercio = comercioRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNoEncontrado("Comercio no encontrado para el email: " + email));
        return mapToDTO(comercio);
    }

    public ComercioLocalDTO guardar(ComercioLocalDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("El DTO no puede ser nulo");
        }
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        ComercioLocal comercio = comercioRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNoEncontrado("Comercio no encontrado con email: " + email));
        comercio.setNombreComercio(dto.getNombreComercio());
        comercio.setCategoria(dto.getCategoria());
        comercio.setDireccionComercio(dto.getDireccionComercio());
        comercio.setLogo(dto.getLogo());
        comercio.setTelefono(dto.getTelefono());
        comercio.setHorarioApertura(dto.getHorarioApertura());
        ComercioLocal guardado = comercioRepository.save(comercio);
        return mapToDTO(guardado);
    }

    private ComercioLocalDTO mapToDTO(ComercioLocal c) {
        ComercioLocalDTO dto = new ComercioLocalDTO();
        dto.setIdComercio(c.getIdcomercio());
        dto.setNombreComercio(c.getNombreComercio());
        dto.setCategoria(c.getCategoria());
        dto.setDireccionComercio(c.getDireccionComercio());
        dto.setLogo(c.getLogo());
        dto.setTelefono(c.getTelefono());
        dto.setHorarioApertura(c.getHorarioApertura());
        dto.setEmail(c.getEmail());
        return dto;
    }
}
