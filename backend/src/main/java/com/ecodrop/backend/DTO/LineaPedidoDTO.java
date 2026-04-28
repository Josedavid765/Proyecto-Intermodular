package com.ecodrop.backend.DTO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class LineaPedidoDTO {

    private Long idLineaPedido;

    @NotNull(message = "La cantidad es obligatoria")
    private Integer cantidad;

    @NotNull(message = "El precio de venta es obligatorio")
    @DecimalMin(value = "0.0", message = "El precio debe ser mayor o igual a 0")
    private Double precioVenta;

    @NotNull(message = "El ID del pedido es obligatorio")
    private Long idPedido;

    @NotNull(message = "El ID del producto es obligatorio")
    private Long idProducto;
}
