package com.ecodrop.backend.DTO;

import jakarta.validation.constraints.Email;
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
public class UsuarioDTO {

    private Long idUsuario;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 50)
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, max = 50)
    private String apellido;

    @Email(message = "Formato de email no válido")
    @NotBlank(message = "El email es obligatorio")
    private String email;

    @NotNull(message = "El telefono es obligatorio")
    private int telefono;

    @NotBlank(message = "La dirección de entrega es obligatoria")
    private String direccionEntrega;
}
