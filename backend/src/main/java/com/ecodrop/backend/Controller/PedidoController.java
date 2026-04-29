package com.ecodrop.backend.Controller;

import com.ecodrop.backend.DTO.PedidoDTO;
import com.ecodrop.backend.Service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SuppressWarnings("null")
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping("/todos")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PedidoDTO>> listarTodos() {
        return ResponseEntity.ok(pedidoService.listarTodos());
    }

    @GetMapping("/usuario/{idUsuario}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USUARIO')")
    public ResponseEntity<List<PedidoDTO>> listarPorUsuario(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(pedidoService.listarPorUsuario(idUsuario));
    }

    @GetMapping("/comercio/{idComercio}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('COMERCIO')")
    public ResponseEntity<List<PedidoDTO>> listarPorComercio(@PathVariable Long idComercio) {
        return ResponseEntity.ok(pedidoService.listarPorComercio(idComercio));
    }

    @GetMapping("/repartidor/{idRepartidor}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('REPARTIDOR')")
    public ResponseEntity<List<PedidoDTO>> listarPorRepartidor(@PathVariable Long idRepartidor) {
        return ResponseEntity.ok(pedidoService.listarPorRepartidor(idRepartidor));
    }

    @PostMapping
    @PreAuthorize("hasRole('USUARIO')")
    public ResponseEntity<PedidoDTO> crearPedido(@Valid @RequestBody PedidoDTO dto) {
        return ResponseEntity.ok(pedidoService.crearPedido(dto));
    }
}
