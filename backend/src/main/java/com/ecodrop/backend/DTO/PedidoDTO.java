package com.ecodrop.backend.DTO;

import java.time.LocalDate;

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

    @NotNull(message = "El monto total es obligatorio")
    @DecimalMin(value = "0.0", message = "El monto no puede ser negativo")
    private Double montoTotal;

    @NotNull(message = "El estado del pedido es obligatorio")
    private EstadoPedido estado;

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long idUsuario;

    @NotNull(message = "El ID del comercio es obligatorio")
    private Long idComercio;

    private Long idRepartidor;
}
