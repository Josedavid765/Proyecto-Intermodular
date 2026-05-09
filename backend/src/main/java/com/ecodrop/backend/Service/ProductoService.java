package com.ecodrop.backend.Service;

import com.ecodrop.backend.DTO.ProductoDTO;
import com.ecodrop.backend.Exceptions.RecursoNoEncontrado;
import com.ecodrop.backend.Model.Entities.ComercioLocal;
import com.ecodrop.backend.Model.Entities.Producto;
import com.ecodrop.backend.Repository.ComercioLocalRepository;
import com.ecodrop.backend.Repository.ProductoRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("null")
@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final ComercioLocalRepository comercioRepository;

    public ProductoService(ProductoRepository productoRepository, ComercioLocalRepository comercioRepository) {
        this.productoRepository = productoRepository;
        this.comercioRepository = comercioRepository;
    }

    public List<ProductoDTO> listarTodos() {
        return productoRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<ProductoDTO> listarPorComercio(@NonNull Long idComercio) {
        return productoRepository.findByComercioIdcomercio(idComercio).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<ProductoDTO> listarDisponibles() {
        return productoRepository.findByDisponibilidadTrue().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public ProductoDTO buscarPorId(@NonNull Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontrado("Producto no encontrado con ID: " + id));
        return mapToDTO(producto);
    }

    public ProductoDTO crearProducto(@NonNull ProductoDTO dto) {
        if (dto.getIdComercio() == null) {
            throw new IllegalArgumentException("El ID del comercio es obligatorio");
        }
        if (dto.getNombre() == null || dto.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio");
        }
        if (dto.getPrecioUnitario() == null || dto.getPrecioUnitario() <= 0) {
            throw new IllegalArgumentException("El precio unitario debe ser mayor a 0");
        }

        ComercioLocal comercio = comercioRepository.findById(dto.getIdComercio())
                .orElseThrow(() -> new RecursoNoEncontrado("Comercio no encontrado con ID: " + dto.getIdComercio()));

        Producto producto = mapToEntity(dto);
        producto.setComercio(comercio);
        Producto guardado = productoRepository.save(producto);
        return mapToDTO(guardado);
    }

    public ProductoDTO actualizarProducto(@NonNull Long id, @NonNull ProductoDTO dto) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontrado("Producto no encontrado con ID: " + id));

        if (dto.getNombre() != null) producto.setNombre(dto.getNombre());
        if (dto.getPrecioUnitario() != null) producto.setPrecioUnitario(dto.getPrecioUnitario());
        if (dto.getStock() != null) producto.setStock(dto.getStock());
        if (dto.getCategoriaProducto() != null) producto.setCategoriaProducto(dto.getCategoriaProducto());
        if (dto.getUnidadMedida() != null) producto.setUnidadMedida(dto.getUnidadMedida());
        if (dto.getDisponibilidad() != null) producto.setDisponibilidad(dto.getDisponibilidad());
        if (dto.getImagen() != null) producto.setImagen(dto.getImagen());

        Producto actualizado = productoRepository.save(producto);
        return mapToDTO(actualizado);
    }

    public void eliminarProducto(@NonNull Long id) {
        if (!productoRepository.existsById(id)) {
            throw new RecursoNoEncontrado("Producto no encontrado con ID: " + id);
        }
        productoRepository.deleteById(id);
    }

    private ProductoDTO mapToDTO(Producto p) {
        ProductoDTO dto = new ProductoDTO();
        dto.setIdProducto(p.getIdProducto());
        dto.setNombre(p.getNombre());
        dto.setPrecioUnitario(p.getPrecioUnitario());
        dto.setStock(p.getStock());
        dto.setCategoriaProducto(p.getCategoriaProducto());
        dto.setUnidadMedida(p.getUnidadMedida());
        dto.setDisponibilidad(p.getDisponibilidad());
        dto.setImagen(p.getImagen());
        dto.setIdComercio(p.getComercio().getIdcomercio());
        return dto;
    }

    private Producto mapToEntity(ProductoDTO dto) {
        Producto p = new Producto();
        p.setNombre(dto.getNombre());
        p.setPrecioUnitario(dto.getPrecioUnitario());
        p.setStock(dto.getStock());
        p.setCategoriaProducto(dto.getCategoriaProducto());
        p.setUnidadMedida(dto.getUnidadMedida());
        p.setDisponibilidad(dto.getDisponibilidad());
        p.setImagen(dto.getImagen());
        return p;
    }
}
