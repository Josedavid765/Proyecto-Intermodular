package com.ecodrop.backend.Controller;

import com.ecodrop.backend.DTO.RepartidorDTO;
import com.ecodrop.backend.Service.RepartidorService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        return ResponseEntity.ok(repartidorService.listarTodos());
    }

    @PutMapping("/estado")
    @PreAuthorize("hasRole('REPARTIDOR')")
    public ResponseEntity<RepartidorDTO> actualizarEstado(@RequestBody RepartidorDTO dto) {
        return ResponseEntity.ok(repartidorService.guardar(dto));
    }
}
