package com.ecodrop.backend.Controller;

import com.ecodrop.backend.DTO.PedidoDTO;
import com.ecodrop.backend.Model.Enum.EstadoPedido;
import com.ecodrop.backend.Service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@SuppressWarnings("null")
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping("/comercio/{idComercio}")
    @PreAuthorize("hasRole('COMERCIO')")
    public ResponseEntity<List<PedidoDTO>> listarPorComercio(
            @PathVariable Long idComercio,
            @RequestParam(required = false) EstadoPedido estado) {
        if (estado != null) {
            return ResponseEntity.ok(pedidoService.listarPorComercio(idComercio, estado));
        }
        return ResponseEntity.ok(pedidoService.listarPorComercio(idComercio));
    }

    @GetMapping("/comercio/me")
    @PreAuthorize("hasRole('COMERCIO')")
    public ResponseEntity<List<PedidoDTO>> listarMisPedidosComercio() {
        return ResponseEntity.ok(pedidoService.listarPedidosPorComercioActual());
    }

    @GetMapping("/disponibles")
    @PreAuthorize("hasRole('REPARTIDOR')")
    public ResponseEntity<List<PedidoDTO>> listarDisponibles() {
        return ResponseEntity.ok(pedidoService.listarSinRepartidor());
    }

    @GetMapping("/repartidor/{idRepartidor}")
    @PreAuthorize("hasRole('REPARTIDOR')")
    public ResponseEntity<List<PedidoDTO>> listarPorRepartidor(
            @PathVariable Long idRepartidor,
            @RequestParam(required = false) EstadoPedido estado) {
        if (estado != null) {
            return ResponseEntity.ok(pedidoService.listarPorRepartidor(idRepartidor, estado));
        }
        return ResponseEntity.ok(pedidoService.listarPorRepartidor(idRepartidor));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('COMERCIO') or hasRole('REPARTIDOR')")
    public ResponseEntity<PedidoDTO> obtenerPedido(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('COMERCIO')")
    public ResponseEntity<PedidoDTO> actualizarPedido(@PathVariable Long id, @Valid @RequestBody PedidoDTO dto) {
        return ResponseEntity.ok(pedidoService.actualizarPedido(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('COMERCIO')")
    public ResponseEntity<Void> eliminarPedido(@PathVariable Long id) {
        pedidoService.eliminarPedido(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    @PreAuthorize("hasRole('COMERCIO')")
    public ResponseEntity<PedidoDTO> crearPedido(@Valid @RequestBody PedidoDTO dto) {
        return ResponseEntity.ok(pedidoService.crearPedido(dto));
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasRole('COMERCIO') or hasRole('REPARTIDOR')")
    public ResponseEntity<PedidoDTO> actualizarEstado(@PathVariable Long id, @RequestBody Map<String, String> body) {
        EstadoPedido nuevoEstado = EstadoPedido.valueOf(body.get("estado"));
        return ResponseEntity.ok(pedidoService.cambiarEstado(id, nuevoEstado));
    }

    @PutMapping("/{id}/repartidor/{idRepartidor}")
    @PreAuthorize("hasRole('REPARTIDOR')")
    public ResponseEntity<PedidoDTO> asignarRepartidor(@PathVariable Long id, @PathVariable Long idRepartidor) {
        return ResponseEntity.ok(pedidoService.asignarRepartidor(id, idRepartidor));
    }

    @PutMapping("/{id}/valorar")
    @PreAuthorize("hasRole('COMERCIO') or hasRole('REPARTIDOR')")
    public ResponseEntity<PedidoDTO> valorar(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        String tipo = (String) body.get("tipo");
        Integer puntuacion = (Integer) body.get("puntuacion");
        return ResponseEntity.ok(pedidoService.valorar(id, tipo, puntuacion));
    }
}
