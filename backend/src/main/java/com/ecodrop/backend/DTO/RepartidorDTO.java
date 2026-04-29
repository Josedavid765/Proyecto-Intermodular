package com.ecodrop.backend.DTO;

import com.ecodrop.backend.Model.Enum.EstadoRepartidor;
import com.ecodrop.backend.Model.Enum.Vehiculo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
public class RepartidorDTO {
    private Long idRepartidor;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellidos;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^[0-9]{9}$", message = "El teléfono debe tener 9 dígitos")
    private String telefono;

    @NotNull(message = "El tipo de vehículo es obligatorio")
    private Vehiculo vehiculo;

    @NotNull(message = "El estado de disponibilidad es obligatorio")
    private EstadoRepartidor estado;
}
