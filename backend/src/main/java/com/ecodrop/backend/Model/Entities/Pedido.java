package com.ecodrop.backend.Model.Entities;

import java.time.LocalDate;

import com.ecodrop.backend.Model.Enum.EstadoPedido;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "pedido")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPedido;

    @NotNull
    private LocalDate fechaPedido;

    @NotBlank
    private String nombre;

    @NotNull
    @DecimalMin(value = "0.0")
    private Double peso;

    @NotBlank
    private String direccionRecogida;

    @NotBlank
    private String direccionEntrega;

    private Double latitudRecogida;
    private Double longitudRecogida;
    private Double latitudEntrega;
    private Double longitudEntrega;

    private Double distancia;

    @Enumerated(EnumType.STRING)
    @NotNull
    private EstadoPedido estado;

    @ManyToOne
    @JoinColumn(name = "id_comercio", nullable = false)
    private ComercioLocal comercio;

    @ManyToOne
    @JoinColumn(name = "id_repartidor", nullable = true)
    private Repartidor repartidor;

    private Integer valoracionComercio;
    private Integer valoracionRepartidor;

    public Pedido() {}

    public Pedido(Long idPedido, LocalDate fechaPedido, String nombre, Double peso, String direccionRecogida, String direccionEntrega, Double latitudRecogida, Double longitudRecogida, Double latitudEntrega, Double longitudEntrega, Double distancia, EstadoPedido estado, ComercioLocal comercio, Repartidor repartidor, Integer valoracionComercio, Integer valoracionRepartidor) {
        this.idPedido = idPedido;
        this.fechaPedido = fechaPedido;
        this.nombre = nombre;
        this.peso = peso;
        this.direccionRecogida = direccionRecogida;
        this.direccionEntrega = direccionEntrega;
        this.latitudRecogida = latitudRecogida;
        this.longitudRecogida = longitudRecogida;
        this.latitudEntrega = latitudEntrega;
        this.longitudEntrega = longitudEntrega;
        this.distancia = distancia;
        this.estado = estado;
        this.comercio = comercio;
        this.repartidor = repartidor;
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
    public ComercioLocal getComercio() { return comercio; }
    public void setComercio(ComercioLocal comercio) { this.comercio = comercio; }
    public Repartidor getRepartidor() { return repartidor; }
    public void setRepartidor(Repartidor repartidor) { this.repartidor = repartidor; }
    public Integer getValoracionComercio() { return valoracionComercio; }
    public void setValoracionComercio(Integer valoracionComercio) { this.valoracionComercio = valoracionComercio; }
    public Integer getValoracionRepartidor() { return valoracionRepartidor; }
    public void setValoracionRepartidor(Integer valoracionRepartidor) { this.valoracionRepartidor = valoracionRepartidor; }
}
