package com.ecodrop.backend.DTO;

import java.time.LocalDate;
import com.ecodrop.backend.Model.Enum.EstadoPedido;

public class PedidoDTO {
    private Long idPedido;
    private LocalDate fechaPedido;
    private String nombre;
    private Double peso;
    private String direccionRecogida;
    private String direccionEntrega;
    private Double latitudRecogida;
    private Double longitudRecogida;
    private Double latitudEntrega;
    private Double longitudEntrega;
    private Double distancia;
    private EstadoPedido estado;
    private Long idComercio;
    private String nombreComercio;
    private Long idRepartidor;
    private String nombreRepartidor;
    private Integer valoracionComercio;
    private Integer valoracionRepartidor;

    public PedidoDTO() {}

    public PedidoDTO(Long idPedido, LocalDate fechaPedido, String nombre, Double peso, String direccionRecogida, String direccionEntrega, Double distancia, EstadoPedido estado, Long idComercio, Long idRepartidor, Integer valoracionComercio, Integer valoracionRepartidor) {
        this.idPedido = idPedido;
        this.fechaPedido = fechaPedido;
        this.nombre = nombre;
        this.peso = peso;
        this.direccionRecogida = direccionRecogida;
        this.direccionEntrega = direccionEntrega;
        this.distancia = distancia;
        this.estado = estado;
        this.idComercio = idComercio;
        this.idRepartidor = idRepartidor;
        this.valoracionComercio = valoracionComercio;
        this.valoracionRepartidor = valoracionRepartidor;
    }

    public Long getIdPedido() { return idPedido; }
    public void setIdPedido(Long idPedido) { this.idPedido = idPedido; }
    public LocalDate getFechaPedido() { return fechaPedido; }
    public void setFechaPedido(LocalDate fechaPedido) { this.fechaPedido = fechaPedido; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Double getPeso() { return peso; }
    public void setPeso(Double peso) { this.peso = peso; }
    public String getDireccionRecogida() { return direccionRecogida; }
    public void setDireccionRecogida(String direccionRecogida) { this.direccionRecogida = direccionRecogida; }
    public String getDireccionEntrega() { return direccionEntrega; }
    public void setDireccionEntrega(String direccionEntrega) { this.direccionEntrega = direccionEntrega; }
    public Double getLatitudRecogida() { return latitudRecogida; }
    public void setLatitudRecogida(Double latitudRecogida) { this.latitudRecogida = latitudRecogida; }
    public Double getLongitudRecogida() { return longitudRecogida; }
    public void setLongitudRecogida(Double longitudRecogida) { this.longitudRecogida = longitudRecogida; }
    public Double getLatitudEntrega() { return latitudEntrega; }
    public void setLatitudEntrega(Double latitudEntrega) { this.latitudEntrega = latitudEntrega; }
    public Double getLongitudEntrega() { return longitudEntrega; }
    public void setLongitudEntrega(Double longitudEntrega) { this.longitudEntrega = longitudEntrega; }
    public Double getDistancia() { return distancia; }
    public void setDistancia(Double distancia) { this.distancia = distancia; }
    public EstadoPedido getEstado() { return estado; }
    public void setEstado(EstadoPedido estado) { this.estado = estado; }
    public Long getIdComercio() { return idComercio; }
    public void setIdComercio(Long idComercio) { this.idComercio = idComercio; }
    public String getNombreComercio() { return nombreComercio; }
    public void setNombreComercio(String nombreComercio) { this.nombreComercio = nombreComercio; }
    public Long getIdRepartidor() { return idRepartidor; }
    public void setIdRepartidor(Long idRepartidor) { this.idRepartidor = idRepartidor; }
    public String getNombreRepartidor() { return nombreRepartidor; }
    public void setNombreRepartidor(String nombreRepartidor) { this.nombreRepartidor = nombreRepartidor; }
    public Integer getValoracionComercio() { return valoracionComercio; }
    public void setValoracionComercio(Integer valoracionComercio) { this.valoracionComercio = valoracionComercio; }
    public Integer getValoracionRepartidor() { return valoracionRepartidor; }
    public void setValoracionRepartidor(Integer valoracionRepartidor) { this.valoracionRepartidor = valoracionRepartidor; }
}
