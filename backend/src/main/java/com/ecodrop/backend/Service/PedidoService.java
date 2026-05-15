package com.ecodrop.backend.Service;

import com.ecodrop.backend.DTO.PedidoDTO;
import com.ecodrop.backend.Exceptions.RecursoNoEncontrado;
import com.ecodrop.backend.Model.Entities.Repartidor;
import com.ecodrop.backend.Repository.RepartidorRepository;
import com.ecodrop.backend.Model.Entities.ComercioLocal;
import com.ecodrop.backend.Model.Entities.Pedido;
import com.ecodrop.backend.Model.Enum.EstadoPedido;
import com.ecodrop.backend.Model.Enum.EstadoRepartidor;
import com.ecodrop.backend.Repository.ComercioLocalRepository;
import com.ecodrop.backend.Repository.PedidoRepository;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("null")
@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ComercioLocalRepository comercioRepository;
    private final RepartidorRepository repartidorRepository;
    private final GeocodingService geocodingService;
    private final RoutingService routingService;

    public PedidoService(PedidoRepository pedidoRepository,
                         ComercioLocalRepository comercioRepository,
                         RepartidorRepository repartidorRepository,
                         GeocodingService geocodingService,
                         RoutingService routingService) {
        this.pedidoRepository = pedidoRepository;
        this.comercioRepository = comercioRepository;
        this.repartidorRepository = repartidorRepository;
        this.geocodingService = geocodingService;
        this.routingService = routingService;
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

    public List<PedidoDTO> listarPorRepartidor(Long idRepartidor, EstadoPedido estado) {
        return pedidoRepository.findByRepartidorIdRepartidorAndEstado(idRepartidor, estado).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<PedidoDTO> listarPorComercio(@NonNull Long idComercio) {
        return pedidoRepository.findByComercioIdcomercio(idComercio).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<PedidoDTO> listarPorComercio(@NonNull Long idComercio, EstadoPedido estado) {
        return pedidoRepository.findByComercioIdcomercioAndEstado(idComercio, estado).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<PedidoDTO> listarPedidosPorRepartidorActual() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Repartidor repartidor = repartidorRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNoEncontrado("Repartidor no encontrado"));
        return pedidoRepository.findByRepartidorIdRepartidor(repartidor.getIdRepartidor()).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<PedidoDTO> listarPedidosPorComercioActual() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        ComercioLocal comercio = comercioRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNoEncontrado("Comercio no encontrado"));
        return pedidoRepository.findByComercioIdcomercio(comercio.getIdcomercio()).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public PedidoDTO crearPedido(@NonNull PedidoDTO dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        ComercioLocal comercio = comercioRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNoEncontrado("Comercio no encontrado para este usuario"));

        Pedido pedido = new Pedido();
        pedido.setNombre(dto.getNombre());
        pedido.setPeso(dto.getPeso());
        pedido.setDireccionRecogida(comercio.getDireccionComercio());
        pedido.setDireccionEntrega(dto.getDireccionEntrega());
        pedido.setComercio(comercio);
        pedido.setFechaPedido(LocalDate.now());
        pedido.setEstado(EstadoPedido.PENDIENTE);

        pedido = pedidoRepository.save(pedido);

        try {
            Double[] coordsRecogida = geocodingService.geocodificar(pedido.getDireccionRecogida());
            Double[] coordsEntrega = geocodingService.geocodificar(pedido.getDireccionEntrega());

            if (coordsRecogida != null && coordsEntrega != null) {
                pedido.setLatitudRecogida(coordsRecogida[0]);
                pedido.setLongitudRecogida(coordsRecogida[1]);
                pedido.setLatitudEntrega(coordsEntrega[0]);
                pedido.setLongitudEntrega(coordsEntrega[1]);

                Double distancia = routingService.calcularDistancia(
                        coordsRecogida[0], coordsRecogida[1],
                        coordsEntrega[0], coordsEntrega[1]);
                pedido.setDistancia(distancia);
            }
        } catch (Exception e) {
            System.err.println("Error al geocodificar: " + e.getMessage());
        }

        pedido = pedidoRepository.save(pedido);
        return mapToDTO(pedido);
    }

    public List<PedidoDTO> listarSinRepartidor() {
        return pedidoRepository.findByEstadoAndRepartidorIsNull(EstadoPedido.PENDIENTE).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public PedidoDTO asignarRepartidor(@NonNull Long idPedido, @NonNull Long idRepartidor) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RecursoNoEncontrado("Pedido no encontrado con ID: " + idPedido));

        if (pedido.getRepartidor() != null) {
            throw new IllegalStateException("El pedido ya tiene un repartidor asignado");
        }

        Repartidor repartidor = repartidorRepository.findById(idRepartidor)
                .orElseThrow(() -> new RecursoNoEncontrado("Repartidor no encontrado con ID: " + idRepartidor));

        pedido.setRepartidor(repartidor);
        pedido.setEstado(EstadoPedido.EN_TRANSITO);
        pedido = pedidoRepository.save(pedido);

        repartidor.setEstado(EstadoRepartidor.OCUPADO);
        repartidorRepository.save(repartidor);

        if (pedido.getDistancia() == null && pedido.getLatitudRecogida() != null && pedido.getLatitudEntrega() != null) {
            try {
                Double distancia = routingService.calcularDistancia(
                        pedido.getLatitudRecogida(), pedido.getLongitudRecogida(),
                        pedido.getLatitudEntrega(), pedido.getLongitudEntrega());
                pedido.setDistancia(distancia);
                pedido = pedidoRepository.save(pedido);
            } catch (Exception e) {
                System.err.println("Error al calcular distancia: " + e.getMessage());
            }
        }

        return mapToDTO(pedido);
    }

    @Transactional
    public PedidoDTO cambiarEstado(@NonNull Long idPedido, @NonNull EstadoPedido nuevoEstado) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RecursoNoEncontrado("Pedido no encontrado con ID: " + idPedido));
        pedido.setEstado(nuevoEstado);
        pedido = pedidoRepository.save(pedido);

        if (nuevoEstado == EstadoPedido.ENTREGADO && pedido.getRepartidor() != null) {
            Repartidor repartidor = pedido.getRepartidor();
            repartidor.setEstado(EstadoRepartidor.DISPONIBLE);
            repartidorRepository.save(repartidor);
        }

        return mapToDTO(pedido);
    }

    @Transactional
    public PedidoDTO rechazarPedido(@NonNull Long idPedido) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RecursoNoEncontrado("Pedido no encontrado con ID: " + idPedido));

        if (pedido.getRepartidor() == null) {
            throw new IllegalStateException("El pedido no tiene repartidor asignado");
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        String repartidorEmail = pedido.getRepartidor().getEmail();
        if (!repartidorEmail.equals(email)) {
            throw new IllegalStateException("No puedes rechazar un pedido que no tienes asignado");
        }

        Repartidor repartidor = pedido.getRepartidor();
        repartidor.setEstado(EstadoRepartidor.DISPONIBLE);
        repartidorRepository.save(repartidor);

        pedido.setRepartidor(null);
        pedido.setEstado(EstadoPedido.PENDIENTE);
        pedido = pedidoRepository.save(pedido);

        return mapToDTO(pedido);
    }

    @Transactional
    public PedidoDTO valorar(@NonNull Long idPedido, @NonNull String tipo, @NonNull Integer puntuacion) {
        if (puntuacion < 1 || puntuacion > 5) {
            throw new IllegalArgumentException("La puntuación debe ser entre 1 y 5");
        }
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RecursoNoEncontrado("Pedido no encontrado con ID: " + idPedido));

        if ("COMERCIO".equalsIgnoreCase(tipo)) {
            pedido.setValoracionComercio(puntuacion);
        } else if ("REPARTIDOR".equalsIgnoreCase(tipo)) {
            pedido.setValoracionRepartidor(puntuacion);
        } else {
            throw new IllegalArgumentException("Tipo de valoración inválido. Use COMERCIO o REPARTIDOR");
        }

        pedido = pedidoRepository.save(pedido);
        return mapToDTO(pedido);
    }

    @Transactional
    public PedidoDTO obtenerPorId(@NonNull Long idPedido) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RecursoNoEncontrado("Pedido no encontrado con ID: " + idPedido));
        return mapToDTO(pedido);
    }

    @Transactional
    public PedidoDTO actualizarPedido(@NonNull Long idPedido, @NonNull PedidoDTO dto) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RecursoNoEncontrado("Pedido no encontrado con ID: " + idPedido));

        if (dto.getNombre() != null) pedido.setNombre(dto.getNombre());
        if (dto.getPeso() != null) pedido.setPeso(dto.getPeso());
        if (dto.getDireccionRecogida() != null) pedido.setDireccionRecogida(dto.getDireccionRecogida());
        if (dto.getDireccionEntrega() != null) pedido.setDireccionEntrega(dto.getDireccionEntrega());

        pedido = pedidoRepository.save(pedido);
        return mapToDTO(pedido);
    }

    @Transactional
    public void eliminarPedido(@NonNull Long idPedido) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RecursoNoEncontrado("Pedido no encontrado con ID: " + idPedido));
        pedidoRepository.delete(pedido);
    }

    private PedidoDTO mapToDTO(Pedido p) {
        PedidoDTO dto = new PedidoDTO();
        dto.setIdPedido(p.getIdPedido());
        dto.setFechaPedido(p.getFechaPedido());
        dto.setNombre(p.getNombre());
        dto.setPeso(p.getPeso());
        dto.setDireccionRecogida(p.getDireccionRecogida());
        dto.setDireccionEntrega(p.getDireccionEntrega());
        dto.setLatitudRecogida(p.getLatitudRecogida());
        dto.setLongitudRecogida(p.getLongitudRecogida());
        dto.setLatitudEntrega(p.getLatitudEntrega());
        dto.setLongitudEntrega(p.getLongitudEntrega());
        dto.setDistancia(p.getDistancia());
        dto.setEstado(p.getEstado());
        dto.setIdComercio(p.getComercio().getIdcomercio());
        dto.setNombreComercio(p.getComercio().getNombreComercio());

        if (p.getRepartidor() != null) {
            dto.setIdRepartidor(p.getRepartidor().getIdRepartidor());
            dto.setNombreRepartidor(p.getRepartidor().getNombre() + " " + p.getRepartidor().getApellidos());
        }

        dto.setValoracionComercio(p.getValoracionComercio());
        dto.setValoracionRepartidor(p.getValoracionRepartidor());

        return dto;
    }
}
