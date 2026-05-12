package com.ecodrop.backend.Controller;

import com.ecodrop.backend.DTO.RepartidorDTO;
import com.ecodrop.backend.Service.RepartidorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SuppressWarnings("null")
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/repartidores")
public class RepartidorController {

    private final RepartidorService repartidorService;

    public RepartidorController(RepartidorService repartidorService) {
        this.repartidorService = repartidorService;
    }

    @GetMapping
    public ResponseEntity<List<RepartidorDTO>> listarTodos() {
        return ResponseEntity.ok(repartidorService.listarTodos());
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<RepartidorDTO>> listarDisponibles() {
        return ResponseEntity.ok(repartidorService.listarDisponibles());
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('REPARTIDOR')")
    public ResponseEntity<RepartidorDTO> obtenerPerfil() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(repartidorService.obtenerPorEmail(email));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RepartidorDTO> crear(@Valid @RequestBody RepartidorDTO dto) {
        return ResponseEntity.ok(repartidorService.crear(dto));
    }

    @PutMapping("/estado")
    @PreAuthorize("hasRole('REPARTIDOR')")
    public ResponseEntity<RepartidorDTO> actualizarEstado(@Valid @RequestBody RepartidorDTO dto) {
        return ResponseEntity.ok(repartidorService.guardar(dto));
    }
}
