package com.ecodrop.backend.Service;

import com.ecodrop.backend.DTO.LineaPedidoDTO;
import com.ecodrop.backend.DTO.PedidoDTO;
import com.ecodrop.backend.Exceptions.RecursoNoEncontrado;
import com.ecodrop.backend.Model.Entities.ComercioLocal;
import com.ecodrop.backend.Model.Entities.LineaPedido;
import com.ecodrop.backend.Model.Entities.Pedido;
import com.ecodrop.backend.Model.Entities.Producto;
import com.ecodrop.backend.Model.Entities.Repartidor;
import com.ecodrop.backend.Model.Entities.Usuario;
import com.ecodrop.backend.Repository.ComercioLocalRepository;
import com.ecodrop.backend.Repository.LineaPedidoRepository;
import com.ecodrop.backend.Repository.PedidoRepository;
import com.ecodrop.backend.Repository.ProductoRepository;
import com.ecodrop.backend.Repository.RepartidorRepository;
import com.ecodrop.backend.Repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ComercioLocalRepository comercioRepository;
    private final RepartidorRepository repartidorRepository;
    private final ProductoRepository productoRepository;
    private final LineaPedidoRepository lineaPedidoRepository;

    public PedidoService(PedidoRepository pedidoRepository,
                         UsuarioRepository usuarioRepository,
                         ComercioLocalRepository comercioRepository,
                         RepartidorRepository repartidorRepository,
                         ProductoRepository productoRepository,
                         LineaPedidoRepository lineaPedidoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.usuarioRepository = usuarioRepository;
        this.comercioRepository = comercioRepository;
        this.repartidorRepository = repartidorRepository;
        this.productoRepository = productoRepository;
        this.lineaPedidoRepository = lineaPedidoRepository;
    }

    public List<PedidoDTO> listarTodos() {
        return pedidoRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<PedidoDTO> listarPorUsuario(Long idUsuario) {
        return pedidoRepository.findByUsuarioIdUsuario(idUsuario).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<PedidoDTO> listarPorComercio(Long idComercio) {
        return pedidoRepository.findByComercioIdComercio(idComercio).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public PedidoDTO crearPedido(PedidoDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new RecursoNoEncontrado("Usuario no encontrado"));
        
        ComercioLocal comercio = comercioRepository.findById(dto.getIdComercio())
                .orElseThrow(() -> new RecursoNoEncontrado("Comercio no encontrado"));

        Pedido pedido = new Pedido();
        pedido.setFechaPedido(dto.getFechaPedido());
        pedido.setGastosEnvio(dto.getGastosEnvio());
        pedido.setTotal(dto.getTotal());
        pedido.setEstado(dto.getEstado());
        pedido.setUsuario(usuario);
        pedido.setComercio(comercio);

        if (dto.getIdRepartidor() != null) {
            Repartidor repartidor = repartidorRepository.findById(dto.getIdRepartidor())
                    .orElseThrow(() -> new RecursoNoEncontrado("Repartidor no encontrado"));
            pedido.setRepartidor(repartidor);
        }

        Pedido guardado = pedidoRepository.save(pedido);

        if (dto.getLineas() != null) {
            for (LineaPedidoDTO lineaDTO : dto.getLineas()) {
                Producto producto = productoRepository.findById(lineaDTO.getIdProducto())
                        .orElseThrow(() -> new RecursoNoEncontrado("Producto no encontrado: " + lineaDTO.getIdProducto()));

                LineaPedido linea = new LineaPedido();
                linea.setCantidad(lineaDTO.getCantidad());
                linea.setPrecioVenta(lineaDTO.getPrecioVenta());
                linea.setPedido(guardado);
                linea.setProducto(producto);

                lineaPedidoRepository.save(linea);
            }
        }

        return mapToDTO(guardado);
    }

    private PedidoDTO mapToDTO(Pedido p) {
        PedidoDTO dto = new PedidoDTO();
        dto.setIdPedido(p.getIdPedido());
        dto.setFechaPedido(p.getFechaPedido());
        dto.setGastosEnvio(p.getGastosEnvio());
        dto.setTotal(p.getTotal());
        dto.setEstado(p.getEstado());
        dto.setIdUsuario(p.getUsuario().getIdUsuario());
        dto.setIdComercio(p.getComercio().getIdcomercio());
        
        if (p.getRepartidor() != null) {
            dto.setIdRepartidor(p.getRepartidor().getIdRepartidor());
        }

        if (p.getLineas() != null) {
            List<LineaPedidoDTO> lineasDTO = p.getLineas().stream().map(this::mapLineaToDTO).collect(Collectors.toList());
            dto.setLineas(lineasDTO);
        }
        
        return dto;
    }

    private LineaPedidoDTO mapLineaToDTO(LineaPedido lp) {
        LineaPedidoDTO dto = new LineaPedidoDTO();
        dto.setIdLineaPedido(lp.getIdLineaPedido());
        dto.setCantidad(lp.getCantidad());
        dto.setPrecioVenta(lp.getPrecioVenta());
        dto.setIdPedido(lp.getPedido().getIdPedido());
        dto.setIdProducto(lp.getProducto().getIdProducto());
        return dto;
    }
}