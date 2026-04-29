package com.ecodrop.backend.Service;

import com.ecodrop.backend.DTO.ComercioLocalDTO;
import com.ecodrop.backend.Exceptions.RecursoNoEncontrado;
import com.ecodrop.backend.Model.Entities.ComercioLocal;
import com.ecodrop.backend.Repository.ComercioLocalRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public ComercioLocalDTO buscarPorId(@NonNull Long id) {
        ComercioLocal comercio = comercioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontrado("Comercio no encontrado con ID: " + id));
        return mapToDTO(comercio);
    }

    public ComercioLocalDTO guardar(@NonNull ComercioLocalDTO dto) {
        ComercioLocal comercio = mapToEntity(dto);
        ComercioLocal guardado = comercioRepository.save(comercio);
        return mapToDTO(guardado);
    }

    private ComercioLocalDTO mapToDTO(ComercioLocal c) {
        ComercioLocalDTO dto = new ComercioLocalDTO();
        dto.setIdComercio(c.getIdcomercio());
        dto.setNombreComercio(c.getNombreComercio());
        dto.setCategoria(c.getCategoria());
        dto.setDireccionComercio(c.getDireccionComercio());
        dto.setTelefono(c.getTelefono());
        dto.setHorarioApertura(c.getHorarioApertura());
        return dto;
    }

    private ComercioLocal mapToEntity(ComercioLocalDTO dto) {
        ComercioLocal c = new ComercioLocal();
        c.setNombreComercio(dto.getNombreComercio());
        c.setCategoria(dto.getCategoria());
        c.setDireccionComercio(dto.getDireccionComercio());
        c.setTelefono(dto.getTelefono());
        c.setHorarioApertura(dto.getHorarioApertura());
        return c;
    }
}
