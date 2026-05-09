package com.ecodrop.backend.Service;

import com.ecodrop.backend.DTO.ComercioLocalDTO;
import com.ecodrop.backend.Exceptions.RecursoNoEncontrado;
import com.ecodrop.backend.Model.Entities.ComercioLocal;
import com.ecodrop.backend.Model.Entities.Usuario;
import com.ecodrop.backend.Repository.ComercioLocalRepository;
import com.ecodrop.backend.Repository.UsuarioRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("null")
@Service
public class ComercioLocalService {
    
    private final ComercioLocalRepository comercioRepository;
    private final UsuarioRepository usuarioRepository;

    public ComercioLocalService(ComercioLocalRepository comercioRepository, UsuarioRepository usuarioRepository) {
        this.comercioRepository = comercioRepository;
        this.usuarioRepository = usuarioRepository;
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
        ComercioLocal comercio = comercioRepository.findByUsuarioEmail(email)
                .orElseThrow(() -> new RecursoNoEncontrado("Comercio no encontrado para el email: " + email));
        return mapToDTO(comercio);
    }

    public ComercioLocalDTO guardar(ComercioLocalDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("El DTO no puede ser nulo");
        }
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNoEncontrado("Usuario no encontrado con email: " + email));
        ComercioLocal comercio = mapToEntity(dto);
        comercio.setUsuario(usuario);
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
        return dto;
    }

    private ComercioLocal mapToEntity(ComercioLocalDTO dto) {
        ComercioLocal c = new ComercioLocal();
        c.setNombreComercio(dto.getNombreComercio());
        c.setCategoria(dto.getCategoria());
        c.setDireccionComercio(dto.getDireccionComercio());
        c.setLogo(dto.getLogo());
        c.setTelefono(dto.getTelefono());
        return c;
    }
}
