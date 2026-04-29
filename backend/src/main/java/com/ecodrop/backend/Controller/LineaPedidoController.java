package com.ecodrop.backend.Controller;

import com.ecodrop.backend.DTO.LineaPedidoDTO;
import com.ecodrop.backend.Service.LineaPedidoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SuppressWarnings("null")
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/lineas-pedido")
public class LineaPedidoController {

    private final LineaPedidoService lineaPedidoService;

    public LineaPedidoController(LineaPedidoService lineaPedidoService) {
        this.lineaPedidoService = lineaPedidoService;
    }

    @GetMapping("/pedido/{idPedido}")
    public ResponseEntity<List<LineaPedidoDTO>> listarPorPedido(@PathVariable Long idPedido) {
        return ResponseEntity.ok(lineaPedidoService.listarPorPedido(idPedido));
    }

    @PostMapping
    public ResponseEntity<LineaPedidoDTO> crear(@Valid @RequestBody LineaPedidoDTO dto) {
        return ResponseEntity.ok(lineaPedidoService.crearLinea(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        lineaPedidoService.eliminarLinea(id);
        return ResponseEntity.noContent().build();
    }
}
