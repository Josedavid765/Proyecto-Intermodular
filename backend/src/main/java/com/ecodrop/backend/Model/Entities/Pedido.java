package com.ecodrop.backend.Model.Entities;

import java.time.LocalDate;

import com.ecodrop.backend.Model.Enum.EstadoPedido;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "pedido")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPedido;

    @NotNull
    private LocalDate fechaPedido;

    @NotNull
    @DecimalMin(value = "0.0")
    private Double total;

    @NotNull
    @DecimalMin(value = "0.0")
    private Double gastosEnvio;

    @Enumerated(EnumType.STRING)
    @NotNull
    private EstadoPedido estado;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario cliente;

    @ManyToOne
    @JoinColumn(name = "id_comercio", nullable = false)
    private ComercioLocal comercio;

    @NotBlank(message = "La dirección de entrega es obligatoria")
    private String direccionEntrega;

    @ManyToOne
    @JoinColumn(name = "id_repartidor", nullable = true)
    private Repartidor repartidor;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<LineaPedido> lineas;

    public Pedido() {}

    public Pedido(Long idPedido, LocalDate fechaPedido, Double total, Double gastosEnvio, EstadoPedido estado, Usuario cliente, ComercioLocal comercio, String direccionEntrega, Repartidor repartidor, List<LineaPedido> lineas) {
        this.idPedido = idPedido;
        this.fechaPedido = fechaPedido;
        this.total = total;
        this.gastosEnvio = gastosEnvio;
        this.estado = estado;
        this.cliente = cliente;
        this.comercio = comercio;
        this.direccionEntrega = direccionEntrega;
        this.repartidor = repartidor;
        this.lineas = lineas;
    }

    public Long getIdPedido() { return idPedido; }
    public void setIdPedido(Long idPedido) { this.idPedido = idPedido; }
    public LocalDate getFechaPedido() { return fechaPedido; }
    public void setFechaPedido(LocalDate fechaPedido) { this.fechaPedido = fechaPedido; }
    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }
    public Double getGastosEnvio() { return gastosEnvio; }
    public void setGastosEnvio(Double gastosEnvio) { this.gastosEnvio = gastosEnvio; }
    public EstadoPedido getEstado() { return estado; }
    public void setEstado(EstadoPedido estado) { this.estado = estado; }
    public Usuario getCliente() { return cliente; }
    public void setCliente(Usuario cliente) { this.cliente = cliente; }
    public ComercioLocal getComercio() { return comercio; }
    public void setComercio(ComercioLocal comercio) { this.comercio = comercio; }
    public String getDireccionEntrega() { return direccionEntrega; }
    public void setDireccionEntrega(String direccionEntrega) { this.direccionEntrega = direccionEntrega; }
    public Repartidor getRepartidor() { return repartidor; }
    public void setRepartidor(Repartidor repartidor) { this.repartidor = repartidor; }
    public List<LineaPedido> getLineas() { return lineas; }
    public void setLineas(List<LineaPedido> lineas) { this.lineas = lineas; }
}
