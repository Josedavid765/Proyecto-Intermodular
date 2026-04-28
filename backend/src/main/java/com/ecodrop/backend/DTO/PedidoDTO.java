package com.ecodrop.backend.DTO;

import java.time.LocalDate;
import java.util.List;

import com.ecodrop.backend.Model.Enum.EstadoPedido;

import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.NotNull;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDTO {
    private Long idPedido;

    @NotNull(message = "La fecha del pedido es obligatoria")
    private LocalDate fechaPedido;

    @NotNull(message = "El estado del pedido es obligatorio")
    private EstadoPedido estado;

    @NotNull(message = "Los gastos de envío son obligatorios")
    @DecimalMin(value = "0.0", message = "Los gastos de envío no pueden ser negativos")
    private Double gastosEnvio;

    @NotNull(message = "El total es obligatorio")
    @DecimalMin(value = "0.0", message = "El total no puede ser negativo")
    private Double total;

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long idUsuario;

    @NotNull(message = "El ID del comercio es obligatorio")
    private Long idComercio;

    private Long idRepartidor;

    private List<LineaPedidoDTO> lineas;
}
