package com.ecodrop.backend.Service;

import com.ecodrop.backend.DTO.PedidoDTO;
import com.ecodrop.backend.Model.Entities.ComercioLocal;
import com.ecodrop.backend.Model.Entities.Pedido;
import com.ecodrop.backend.Model.Entities.Repartidor;
import com.ecodrop.backend.Model.Entities.Usuario;
import com.ecodrop.backend.Repository.ComercioLocalRepository;
import com.ecodrop.backend.Repository.PedidoRepository;
import com.ecodrop.backend.Repository.RepartidorRepository;
import com.ecodrop.backend.Repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ComercioLocalRepository comercioRepository;
    private final RepartidorRepository repartidorRepository;

    public PedidoService(PedidoRepository pedidoRepository, 
                         UsuarioRepository usuarioRepository, 
                         ComercioLocalRepository comercioRepository, 
                         RepartidorRepository repartidorRepository) {
        this.pedidoRepository = pedidoRepository;
        this.usuarioRepository = usuarioRepository;
        this.comercioRepository = comercioRepository;
        this.repartidorRepository = repartidorRepository;
    }

    public List<PedidoDTO> listarTodos() {
        return pedidoRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public PedidoDTO crearPedido(PedidoDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        ComercioLocal comercio = comercioRepository.findById(dto.getIdComercio())
                .orElseThrow(() -> new RuntimeException("Comercio no encontrado"));

        Pedido pedido = new Pedido();
        pedido.setFechaPedido(dto.getFechaPedido());
        pedido.setMontoTotal(dto.getMontoTotal());
        pedido.setEstado(dto.getEstado());
        pedido.setUsuario(usuario);
        pedido.setComercio(comercio);

        if (dto.getIdRepartidor() != null) {
            Repartidor repartidor = repartidorRepository.findById(dto.getIdRepartidor())
                    .orElseThrow(() -> new RuntimeException("Repartidor no encontrado"));
            pedido.setRepartidor(repartidor);
        }

        Pedido guardado = pedidoRepository.save(pedido);
        return mapToDTO(guardado);
    }

    private PedidoDTO mapToDTO(Pedido p) {
        PedidoDTO dto = new PedidoDTO();
        dto.setIdPedido(p.getIdPedido());
        dto.setFechaPedido(p.getFechaPedido());
        dto.setMontoTotal(p.getMontoTotal());
        dto.setEstado(p.getEstado());
        dto.setIdUsuario(p.getUsuario().getIdUsuario());
        dto.setIdComercio(p.getComercio().getIdcomercio());
        
        if (p.getRepartidor() != null) {
            dto.setIdRepartidor(p.getRepartidor().getIdRepartidor());
        }
        
        return dto;
    }
}