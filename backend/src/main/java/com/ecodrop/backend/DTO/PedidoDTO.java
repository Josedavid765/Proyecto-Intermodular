package com.ecodrop.backend.DTO;

import java.time.LocalDate;
import java.util.List;
import com.ecodrop.backend.Model.Enum.EstadoPedido;

public class PedidoDTO {
    private Long idPedido;
    private LocalDate fechaPedido;
    private Double gastosEnvio;
    private Double total;
    private EstadoPedido estado;
    private Long idUsuario;
    private Long idComercio;
    private Long idRepartidor;
    private List<LineaPedidoDTO> lineas;

    public PedidoDTO() {}

    public PedidoDTO(Long idPedido, LocalDate fechaPedido, Double gastosEnvio, Double total, EstadoPedido estado, Long idUsuario, Long idComercio, Long idRepartidor, List<LineaPedidoDTO> lineas) {
        this.idPedido = idPedido;
        this.fechaPedido = fechaPedido;
        this.gastosEnvio = gastosEnvio;
        this.total = total;
        this.estado = estado;
        this.idUsuario = idUsuario;
        this.idComercio = idComercio;
        this.idRepartidor = idRepartidor;
        this.lineas = lineas;
    }

    public Long getIdPedido() { return idPedido; }
    public void setIdPedido(Long idPedido) { this.idPedido = idPedido; }
    public LocalDate getFechaPedido() { return fechaPedido; }
    public void setFechaPedido(LocalDate fechaPedido) { this.fechaPedido = fechaPedido; }
    public Double getGastosEnvio() { return gastosEnvio; }
    public void setGastosEnvio(Double gastosEnvio) { this.gastosEnvio = gastosEnvio; }
    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }
    public EstadoPedido getEstado() { return estado; }
    public void setEstado(EstadoPedido estado) { this.estado = estado; }
    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }
    public Long getIdComercio() { return idComercio; }
    public void setIdComercio(Long idComercio) { this.idComercio = idComercio; }
    public Long getIdRepartidor() { return idRepartidor; }
    public void setIdRepartidor(Long idRepartidor) { this.idRepartidor = idRepartidor; }
    public List<LineaPedidoDTO> getLineas() { return lineas; }
    public void setLineas(List<LineaPedidoDTO> lineas) { this.lineas = lineas; }
}
