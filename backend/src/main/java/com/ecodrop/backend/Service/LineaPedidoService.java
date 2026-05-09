package com.ecodrop.backend.Service;

import com.ecodrop.backend.DTO.LineaPedidoDTO;
import com.ecodrop.backend.Exceptions.RecursoNoEncontrado;
import com.ecodrop.backend.Model.Entities.LineaPedido;
import com.ecodrop.backend.Model.Entities.Pedido;
import com.ecodrop.backend.Model.Entities.Producto;
import com.ecodrop.backend.Repository.LineaPedidoRepository;
import com.ecodrop.backend.Repository.PedidoRepository;
import com.ecodrop.backend.Repository.ProductoRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("null")
@Service
public class LineaPedidoService {

    private final LineaPedidoRepository lineaPedidoRepository;
    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;

    public LineaPedidoService(LineaPedidoRepository lineaPedidoRepository,
                               PedidoRepository pedidoRepository,
                               ProductoRepository productoRepository) {
        this.lineaPedidoRepository = lineaPedidoRepository;
        this.pedidoRepository = pedidoRepository;
        this.productoRepository = productoRepository;
    }

    public List<LineaPedidoDTO> listarPorPedido(@NonNull Long idPedido) {
        return lineaPedidoRepository.findByPedidoIdPedido(idPedido).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public LineaPedidoDTO crearLinea(@NonNull LineaPedidoDTO dto) {
        Pedido pedido = pedidoRepository.findById(dto.getIdPedido())
                .orElseThrow(() -> new RecursoNoEncontrado("Pedido no encontrado con ID: " + dto.getIdPedido()));

        Producto producto = productoRepository.findById(dto.getIdProducto())
                .orElseThrow(() -> new RecursoNoEncontrado("Producto no encontrado con ID: " + dto.getIdProducto()));

        LineaPedido linea = mapToEntity(dto);
        linea.setPedido(pedido);
        linea.setProducto(producto);

        LineaPedido guardada = lineaPedidoRepository.save(linea);
        return mapToDTO(guardada);
    }

    public void eliminarLinea(@NonNull Long id) {
        if (!lineaPedidoRepository.existsById(id)) {
            throw new RecursoNoEncontrado("LineaPedido no encontrada con ID: " + id);
        }
        lineaPedidoRepository.deleteById(id);
    }

    private LineaPedidoDTO mapToDTO(LineaPedido lp) {
        LineaPedidoDTO dto = new LineaPedidoDTO();
        dto.setIdLineaPedido(lp.getIdLineaPedido());
        dto.setCantidad(lp.getCantidad());
        dto.setPrecioVenta(lp.getPrecioVenta());
        dto.setIdPedido(lp.getPedido().getIdPedido());
        dto.setIdProducto(lp.getProducto().getIdProducto());
        return dto;
    }

    private LineaPedido mapToEntity(LineaPedidoDTO dto) {
        LineaPedido lp = new LineaPedido();
        lp.setCantidad(dto.getCantidad());
        lp.setPrecioVenta(dto.getPrecioVenta());
        return lp;
    }
}
