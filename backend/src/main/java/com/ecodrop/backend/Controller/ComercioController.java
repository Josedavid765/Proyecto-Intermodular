package com.ecodrop.backend.Controller;

import com.ecodrop.backend.DTO.ComercioLocalDTO;
import com.ecodrop.backend.Service.ComercioLocalService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        // TODO: Need to add obtenerPorEmail method to ComercioLocalService
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<ComercioLocalDTO> crear(@RequestBody ComercioLocalDTO dto) {
        return ResponseEntity.ok(comercioService.guardar(dto));
    }
}
