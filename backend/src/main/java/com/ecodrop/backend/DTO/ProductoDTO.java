package com.ecodrop.backend.DTO;

import com.ecodrop.backend.Model.Enum.CategoriaProducto;
import com.ecodrop.backend.Model.Enum.UnidadMedida;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ProductoDTO {
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

    @NotNull(message = "La categoria es obligatoria")
    private CategoriaProducto categoriaProducto;

    @NotNull(message = "La unidad de medida es obligatoria")
    private UnidadMedida unidadMedida;

    private Boolean disponibilidad;
    private String imagen;

    @NotNull(message = "El ID del comercio es obligatorio")
    private Long idComercio;

    public ProductoDTO() {}

    public ProductoDTO(Long idProducto, String nombre, String descripcion, Double precioUnitario, Integer stock, CategoriaProducto categoriaProducto, UnidadMedida unidadMedida, Boolean disponibilidad, String imagen, Long idComercio) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioUnitario = precioUnitario;
        this.stock = stock;
        this.categoriaProducto = categoriaProducto;
        this.unidadMedida = unidadMedida;
        this.disponibilidad = disponibilidad;
        this.imagen = imagen;
        this.idComercio = idComercio;
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
    public Long getIdComercio() { return idComercio; }
    public void setIdComercio(Long idComercio) { this.idComercio = idComercio; }
}
