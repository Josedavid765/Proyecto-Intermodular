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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
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
}
