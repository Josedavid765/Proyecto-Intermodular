package com.ecodrop.backend.Controller;

import com.ecodrop.backend.DTO.ComercioLocalDTO;
import com.ecodrop.backend.Service.ComercioLocalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping
    public ResponseEntity<ComercioLocalDTO> crear(@RequestBody ComercioLocalDTO dto) {
        return ResponseEntity.ok(comercioService.guardar(dto));
    }
}
