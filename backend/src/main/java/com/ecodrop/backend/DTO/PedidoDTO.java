package com.ecodrop.backend.DTO;

import java.time.LocalDate;
import java.util.List;

import com.ecodrop.backend.Model.Enum.EstadoPedido;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDTO {
    private Long idPedido;

    private LocalDate fechaPedido;

    private EstadoPedido estado;

    @DecimalMin(value = "0.0", message = "Los gastos de envío no pueden ser negativos")
    private Double gastosEnvio;

    private Double total;

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long idUsuario;

    @NotNull(message = "El ID del comercio es obligatorio")
    private Long idComercio;

    private Long idRepartidor;

    @NotBlank(message = "La dirección de entrega es obligatoria")
    private String direccionEntrega;

    @NotNull(message = "La lista de líneas es obligatoria")
    @Size(min = 1, message = "El pedido debe tener al menos un producto")
    private List<LineaPedidoDTO> lineas;
}
