package com.ecodrop.backend.Service;

import com.ecodrop.backend.DTO.LineaPedidoDTO;
import com.ecodrop.backend.DTO.PedidoDTO;
import com.ecodrop.backend.Exceptions.RecursoNoEncontrado;
import com.ecodrop.backend.Exceptions.StockInsuficienteException;
import com.ecodrop.backend.Model.Entities.ComercioLocal;
import com.ecodrop.backend.Model.Entities.LineaPedido;
import com.ecodrop.backend.Model.Entities.Pedido;
import com.ecodrop.backend.Model.Entities.Producto;
import com.ecodrop.backend.Model.Entities.Usuario;
import com.ecodrop.backend.Model.Enum.EstadoPedido;
import com.ecodrop.backend.Repository.ComercioLocalRepository;
import com.ecodrop.backend.Repository.LineaPedidoRepository;
import com.ecodrop.backend.Repository.PedidoRepository;
import com.ecodrop.backend.Repository.ProductoRepository;
import com.ecodrop.backend.Repository.UsuarioRepository;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@SuppressWarnings("null")
@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ComercioLocalRepository comercioRepository;
    private final ProductoRepository productoRepository;
    private final LineaPedidoRepository lineaPedidoRepository;

    public PedidoService(PedidoRepository pedidoRepository,
                         UsuarioRepository usuarioRepository,
                         ComercioLocalRepository comercioRepository,
                         ProductoRepository productoRepository,
                         LineaPedidoRepository lineaPedidoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.usuarioRepository = usuarioRepository;
        this.comercioRepository = comercioRepository;
        this.productoRepository = productoRepository;
        this.lineaPedidoRepository = lineaPedidoRepository;
    }

    public List<PedidoDTO> listarTodos() {
        return pedidoRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<PedidoDTO> listarPorRepartidor(Long idRepartidor) {
        return pedidoRepository.findByRepartidorIdRepartidor(idRepartidor).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<PedidoDTO> listarPorUsuario(@NonNull Long idUsuario) {
        return pedidoRepository.findByClienteIdUsuario(idUsuario).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<PedidoDTO> listarPorComercio(@NonNull Long idComercio) {
        return pedidoRepository.findByComercioIdcomercio(idComercio).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    @PreAuthorize("hasRole('USUARIO')")
    public PedidoDTO crearPedido(@NonNull PedidoDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("El DTO del pedido no puede ser nulo");
        }
        if (dto.getIdComercio() == null) {
            throw new IllegalArgumentException("El ID del comercio es obligatorio");
        }

        // 1. Obtener usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Usuario usuario = Objects.requireNonNull(
                usuarioRepository.findByEmail(email)
                        .orElseThrow(() -> new RecursoNoEncontrado("Usuario no encontrado"))
        );

        // 2. Obtener comercio del pedido
        ComercioLocal comercio = Objects.requireNonNull(
                comercioRepository.findById(dto.getIdComercio())
                        .orElseThrow(() -> new RecursoNoEncontrado("Comercio no encontrado"))
        );

        // 3. Crear pedido base
        Pedido pedido = new Pedido();
        pedido.setGastosEnvio(dto.getGastosEnvio() != null ? dto.getGastosEnvio() : 0.0);
        pedido.setCliente(usuario);
        pedido.setComercio(comercio);

        // Guardar pedido para obtener ID
        Pedido guardado = pedidoRepository.save(pedido);
        guardado.setDireccionEntrega(usuario.getDireccionEntrega());

        // 4. Procesar líneas de pedido
        double subtotal = 0.0;
        if (dto.getLineas() != null && !dto.getLineas().isEmpty()) {
            for (LineaPedidoDTO lineaDTO : dto.getLineas()) {
                if (lineaDTO.getIdProducto() == null) {
                    throw new IllegalArgumentException("El ID del producto es obligatorio en la línea de pedido");
                }
                if (lineaDTO.getCantidad() <= 0) {
                    throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
                }

                // Obtener producto
                Producto producto = Objects.requireNonNull(
                        productoRepository.findById(lineaDTO.getIdProducto())
                                .orElseThrow(() -> new RecursoNoEncontrado("Producto no encontrado: " + lineaDTO.getIdProducto()))
                );

                // Validar que el producto pertenece al comercio del pedido
                if (!producto.getComercio().getIdcomercio().equals(comercio.getIdcomercio())) {
                    throw new RecursoNoEncontrado("El producto " + producto.getNombre() + " no pertenece al comercio del pedido");
                }

                // Validar stock
                int cantidadSolicitada = lineaDTO.getCantidad();
                if (producto.getStock() < cantidadSolicitada) {
                    throw new StockInsuficienteException(
                            "Stock insuficiente para " + producto.getNombre() +
                            ". Disponible: " + producto.getStock() + ", Solicitado: " + cantidadSolicitada
                    );
                }

                // Decrementar stock
                producto.setStock(producto.getStock() - cantidadSolicitada);
                productoRepository.save(producto);

                // Crear línea (precio de venta se copia del producto, no del DTO)
                LineaPedido linea = new LineaPedido();
                linea.setCantidad(cantidadSolicitada);
                linea.setPrecioVenta(producto.getPrecioUnitario());
                linea.setPedido(guardado);
                linea.setProducto(producto);

                lineaPedidoRepository.save(linea);
                subtotal += linea.getCantidad() * linea.getPrecioVenta();
            }
        }

        // 5. Cálculo de total (suma de productos + gastosEnvio, sin fee extra)
        guardado.setTotal(subtotal + guardado.getGastosEnvio());

        // 6. Automatización de fecha y estado
        guardado.setFechaPedido(LocalDate.now());
        guardado.setEstado(EstadoPedido.PENDIENTE);

        return mapToDTO(guardado);
    }

    private PedidoDTO mapToDTO(Pedido p) {
        PedidoDTO dto = new PedidoDTO();
        dto.setIdPedido(p.getIdPedido());
        dto.setFechaPedido(p.getFechaPedido());
        dto.setGastosEnvio(p.getGastosEnvio());
        dto.setTotal(p.getTotal());
        dto.setEstado(p.getEstado());
        dto.setIdUsuario(p.getCliente().getIdUsuario());
        dto.setIdComercio(p.getComercio().getIdcomercio());
        
        if (p.getLineas() != null) {
            List<LineaPedidoDTO> lineasDTO = p.getLineas().stream()
                    .map(this::mapLineaToDTO)
                    .collect(Collectors.toList());
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
