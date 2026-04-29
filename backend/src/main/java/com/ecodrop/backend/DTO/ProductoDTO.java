package com.ecodrop.backend.DTO;

import com.ecodrop.backend.Model.Enum.CategoriaProducto;
import com.ecodrop.backend.Model.Enum.UnidadMedida;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDTO {

    private Long idProducto;

    @NotBlank(message = "El nombre del producto es obligatorio")
    private String nombre;

    private String descripcion;

    @NotNull(message = "El precio unitario es obligatorio")
    @DecimalMin(value = "0.0", message = "El precio debe ser mayor o igual a 0")
    private Double precioUnitario;

    private Integer stock;

    private CategoriaProducto categoriaProducto;

    private UnidadMedida unidadMedida;

    private Boolean disponibilidad;

    private String imagen;

    @NotNull(message = "El ID del comercio es obligatorio")
    private Long idComercio;
}
