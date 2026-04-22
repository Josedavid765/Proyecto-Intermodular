package com.ecodrop.backend.Model.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comercio_local")
public class ComercioLocal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idcomercio;
    @NotBlank(message = "El nombre del comercio es obligatorios")
    @Size(max = 100)
    String nombreComercio;
    @NotBlank(message = "La categoria es obligatoria")
    String categoria;
    @NotBlank(message = "La direccion es obligatoria")
    String direccionComercio;
    @NotBlank(message = "El telefono es obligatorio")
    @Pattern(regexp = "^[0-9]{9}$", message = "El telefono debe tener 9 digitos")
    String telefono;
    String horarioApertura;
}
