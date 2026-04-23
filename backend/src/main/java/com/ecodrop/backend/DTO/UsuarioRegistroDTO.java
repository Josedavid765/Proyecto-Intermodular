package com.ecodrop.backend.DTO;

import com.ecodrop.backend.Model.Enum.Rol;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRegistroDTO {
    private String nombre;
    private String apellido;
    private String email;
    private String password;
    private String telefono;
    private String direccionEntrega;
    private Rol rol; 
}