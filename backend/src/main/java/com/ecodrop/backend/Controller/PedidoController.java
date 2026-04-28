package com.ecodrop.backend.Controller;

import com.ecodrop.backend.DTO.PedidoDTO;
import com.ecodrop.backend.Exceptions.RecursoNoEncontrado;
import com.ecodrop.backend.Model.Entities.Usuario;
import com.ecodrop.backend.Model.Enum.Rol;
import com.ecodrop.backend.Repository.ComercioLocalRepository;
import com.ecodrop.backend.Repository.UsuarioRepository;
import com.ecodrop.backend.Service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;
    private final UsuarioRepository usuarioRepository;
    private final ComercioLocalRepository comercioRepository;

    public PedidoController(PedidoService pedidoService,
                           UsuarioRepository usuarioRepository,
                           ComercioLocalRepository comercioRepository) {
        this.pedidoService = pedidoService;
        this.usuarioRepository = usuarioRepository;
        this.comercioRepository = comercioRepository;
    }

    @GetMapping
    public ResponseEntity<List<PedidoDTO>> listar() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNoEncontrado("Usuario no encontrado"));

        if (usuario.getRol() == Rol.ROLE_USUARIO) {
            return ResponseEntity.ok(pedidoService.listarPorUsuario(usuario.getIdUsuario()));
        } else if (usuario.getRol() == Rol.ROLE_COMERCIO) {
            Long idComercio = comercioRepository.findByUsuarioIdUsuario(usuario.getIdUsuario())
                    .map(comercio -> comercio.getIdcomercio())
                    .orElseThrow(() -> new RecursoNoEncontrado("No se encontró comercio asociado al usuario"));
            return ResponseEntity.ok(pedidoService.listarPorComercio(idComercio));
        } else {
            return ResponseEntity.ok(pedidoService.listarTodos());
        }
    }

    @PostMapping
    public ResponseEntity<PedidoDTO> realizarPedido(@Valid @RequestBody PedidoDTO pedidoDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNoEncontrado("Usuario no encontrado"));

        pedidoDTO.setIdUsuario(usuario.getIdUsuario());
        return ResponseEntity.ok(pedidoService.crearPedido(pedidoDTO));
    }
}
