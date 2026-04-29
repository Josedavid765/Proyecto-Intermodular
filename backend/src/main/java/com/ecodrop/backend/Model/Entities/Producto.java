package com.ecodrop.backend.Model.Entities;

import com.ecodrop.backend.Model.Enum.CategoriaProducto;
import com.ecodrop.backend.Model.Enum.UnidadMedida;

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
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProducto;

    @NotBlank(message = "El nombre del producto es obligatorio")
    private String nombre;

    private String descripcion;

    @NotNull(message = "El precio unitario es obligatorio")
    @DecimalMin(value = "0.0", message = "El precio debe ser mayor o igual a 0")
    private Double precioUnitario;

    @NotNull(message = "El stock es obligatorio")
    @Min(0)
    private Integer stock;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "La categoria es obligatoria")
    private CategoriaProducto categoriaProducto;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "La unidad de medida es obligatoria")
    private UnidadMedida unidadMedida;

    private Boolean disponibilidad;

    private String imagen;

    @ManyToOne
    @JoinColumn(name = "id_comercio", nullable = false)
    private ComercioLocal comercio;

    public Producto() {}

    public Producto(Long idProducto, String nombre, String descripcion, Double precioUnitario, Integer stock, CategoriaProducto categoriaProducto, UnidadMedida unidadMedida, Boolean disponibilidad, String imagen, ComercioLocal comercio) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioUnitario = precioUnitario;
        this.stock = stock;
        this.categoriaProducto = categoriaProducto;
        this.unidadMedida = unidadMedida;
        this.disponibilidad = disponibilidad;
        this.imagen = imagen;
        this.comercio = comercio;
    }

    public Long getIdProducto() { return idProducto; }
    public void setIdProducto(Long idProducto) { this.idProducto = idProducto; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(Double precioUnitario) { this.precioUnitario = precioUnitario; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    public CategoriaProducto getCategoriaProducto() { return categoriaProducto; }
    public void setCategoriaProducto(CategoriaProducto categoriaProducto) { this.categoriaProducto = categoriaProducto; }
    public UnidadMedida getUnidadMedida() { return unidadMedida; }
    public void setUnidadMedida(UnidadMedida unidadMedida) { this.unidadMedida = unidadMedida; }
    public Boolean getDisponibilidad() { return disponibilidad; }
    public void setDisponibilidad(Boolean disponibilidad) { this.disponibilidad = disponibilidad; }
    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }
    public ComercioLocal getComercio() { return comercio; }
    public void setComercio(ComercioLocal comercio) { this.comercio = comercio; }
}
