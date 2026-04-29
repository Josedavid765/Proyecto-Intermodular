package com.ecodrop.backend.Model.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.persistence.JoinColumn;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.persistence.CascadeType;
import java.util.List;

@Entity
@Table(name = "comercio_local")
public class ComercioLocal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idcomercio;

    @NotBlank(message = "El nombre del comercio es obligatorios")
    @Size(max = 100)
    private String nombreComercio;

    @NotBlank(message = "La categoria es obligatoria")
    private String categoria;

    @NotBlank(message = "La direccion es obligatoria")
    private String direccionComercio;

    private String logo;

    @NotBlank(message = "El telefono es obligatorio")
    @Pattern(regexp = "^[0-9]{9}$", message = "El telefono debe tener 9 digitos")
    private String telefono;

    @NotBlank(message = "El horario de apertura es obligatorio")
    private String horarioApertura;

    @OneToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "comercio", cascade = CascadeType.ALL)
    private List<Pedido> pedidos;

    @OneToMany(mappedBy = "comercio", cascade = CascadeType.ALL)
    private List<Producto> productos;

    public ComercioLocal() {}

    public ComercioLocal(Long idcomercio, String nombreComercio, String categoria, String direccionComercio, String logo, String telefono, String horarioApertura, Usuario usuario, List<Pedido> pedidos, List<Producto> productos) {
        this.idcomercio = idcomercio;
        this.nombreComercio = nombreComercio;
        this.categoria = categoria;
        this.direccionComercio = direccionComercio;
        this.logo = logo;
        this.telefono = telefono;
        this.horarioApertura = horarioApertura;
        this.usuario = usuario;
        this.pedidos = pedidos;
        this.productos = productos;
    }

    public Long getIdcomercio() { return idcomercio; }
    public void setIdcomercio(Long idcomercio) { this.idcomercio = idcomercio; }
    public String getNombreComercio() { return nombreComercio; }
    public void setNombreComercio(String nombreComercio) { this.nombreComercio = nombreComercio; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public String getDireccionComercio() { return direccionComercio; }
    public void setDireccionComercio(String direccionComercio) { this.direccionComercio = direccionComercio; }
    public String getLogo() { return logo; }
    public void setLogo(String logo) { this.logo = logo; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getHorarioApertura() { return horarioApertura; }
    public void setHorarioApertura(String horarioApertura) { this.horarioApertura = horarioApertura; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public List<Pedido> getPedidos() { return pedidos; }
    public void setPedidos(List<Pedido> pedidos) { this.pedidos = pedidos; }
    public List<Producto> getProductos() { return productos; }
    public void setProductos(List<Producto> productos) { this.productos = productos; }
}
