package com.ecodrop.backend.Model.Entities;

import com.ecodrop.backend.Model.Enum.EstadoRepartidor;
import com.ecodrop.backend.Model.Enum.Vehiculo;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.persistence.CascadeType;
import java.util.List;

@Entity
@Table(name = "repartidor")
public class Repartidor {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRepartidor;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellidos;

    @NotBlank(message = "El telefono es obligatorio")
    @Pattern(regexp = "^[0-9]{9}$", message = "El telefono debe tener 9 digitos")
    private String telefono;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Vehiculo vehiculo;

    private Boolean disponibilidad;

    @Enumerated(EnumType.STRING)
    @NotNull
    private EstadoRepartidor estado;

    @OneToMany(mappedBy = "repartidor", cascade = CascadeType.ALL)
    private List<Pedido> pedidos;

    @OneToOne
    private Usuario usuario;

    public Repartidor() {}

    public Repartidor(Long idRepartidor, String nombre, String apellidos, String telefono, Vehiculo vehiculo, Boolean disponibilidad, EstadoRepartidor estado, List<Pedido> pedidos, Usuario usuario) {
        this.idRepartidor = idRepartidor;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.vehiculo = vehiculo;
        this.disponibilidad = disponibilidad;
        this.estado = estado;
        this.pedidos = pedidos;
        this.usuario = usuario;
    }

    public Long getIdRepartidor() { return idRepartidor; }
    public void setIdRepartidor(Long idRepartidor) { this.idRepartidor = idRepartidor; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public Vehiculo getVehiculo() { return vehiculo; }
    public void setVehiculo(Vehiculo vehiculo) { this.vehiculo = vehiculo; }
    public Boolean getDisponibilidad() { return disponibilidad; }
    public void setDisponibilidad(Boolean disponibilidad) { this.disponibilidad = disponibilidad; }
    public EstadoRepartidor getEstado() { return estado; }
    public void setEstado(EstadoRepartidor estado) { this.estado = estado; }
    public List<Pedido> getPedidos() { return pedidos; }
    public void setPedidos(List<Pedido> pedidos) { this.pedidos = pedidos; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}
