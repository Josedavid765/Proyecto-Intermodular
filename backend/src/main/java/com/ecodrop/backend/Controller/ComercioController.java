package com.ecodrop.backend.Controller;

import com.ecodrop.backend.DTO.ComercioLocalDTO;
import com.ecodrop.backend.Service.ComercioLocalService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SuppressWarnings("null")
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/comercios")
public class ComercioController {

    private final ComercioLocalService comercioService;

    public ComercioController(ComercioLocalService comercioService) {
        this.comercioService = comercioService;
    }

    @GetMapping
    public ResponseEntity<List<ComercioLocalDTO>> listarTodos() {
        return ResponseEntity.ok(comercioService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComercioLocalDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(comercioService.buscarPorId(id));
    }

    @GetMapping("/me")
    public ResponseEntity<ComercioLocalDTO> obtenerPerfil() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(comercioService.obtenerPorEmail(email));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('COMERCIO')")
    public ResponseEntity<ComercioLocalDTO> crear(@Valid @RequestBody ComercioLocalDTO dto) {
        return ResponseEntity.ok(comercioService.guardar(dto));
    }
}
